package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.QAuthorInvitation;
import fi.om.municipalityinitiative.sql.QVerifiedAuthor;
import fi.om.municipalityinitiative.sql.QVerifiedParticipant;
import fi.om.municipalityinitiative.sql.QVerifiedUser;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static fi.om.municipalityinitiative.util.ReflectionTestUtils.assertReflectionEquals;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VerifiedInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase{

    public static final String CONFIRMATION_CODE = "confirmationCode";
    @Resource
    private VerifiedInitiativeService service;

    // Test also some functionality of userDao and participantDao. Ugly, but lets leave it that for now.
    @Resource
    private UserDao userDao;
    @Resource
    private ParticipantDao participantDao;


    private Municipality testMunicipality;

    public static LoginUserHolder<VerifiedUser> verifiedLoginUserHolder;

    private static final String EMAIL = "email@example.com";
    private static final String ADDRESS = "Some address whatsoever";

    public static final String PHONE = "32434";

    public static final String VERIFIED_AUTHOR_NAME = "Verified Author Name";

    public static final String HASH = "hash";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Municipality anotherMunicipality;
    private LoginUserHolder<VerifiedUser> anotherVerifiedLoginUserHolder;

    @Override
    protected void childSetup() {
        String municipalityName = "Test municipalitya";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
        String municipalityName2 = "Test MUN 2";
        anotherMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName2), municipalityName2, municipalityName2, false);

        ContactInfo contactInfo = contactInfo();

        verifiedLoginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo, Collections.<Long>emptySet(), Collections.<Long>emptySet(), Maybe.of(testMunicipality))
        );

        anotherVerifiedLoginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(new VerifiedUserId(-1L), HASH + "2", contactInfo, Collections.<Long>emptySet(), Collections.<Long>emptySet(), Maybe.of(anotherMunicipality))
        );


    }

    private static ContactInfo contactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(EMAIL);
        contactInfo.setAddress(ADDRESS);
        contactInfo.setPhone(PHONE);
        contactInfo.setName(VERIFIED_AUTHOR_NAME);
        return contactInfo;
    }

    @Test
    public void preparing_sets_participant_count_to_one_when_adding_new_safe_initiative() {
        long initiativeId = service.prepareVerifiedInitiative(verifiedLoginUserHolder, prepareSafeUICreateDto());
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    public void preparing_sets_participant_count_to_one_when_adding_normal_initiative_with_authentication() {
        long initiativeId = service.prepareNormalInitiative(verifiedLoginUserHolder, prepareNormalUiCreateDto());
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }


    @Test
    @Transactional // Some tests use userDao for receiving the results for assertion
    public void preparing_creates_user_with_given_information_if_not_yet_created() {
        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));

        service.prepareVerifiedInitiative(verifiedLoginUserHolder, prepareSafeUICreateDto());

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        VerifiedUser created = userDao.getVerifiedUser(HASH).get();
        assertThat(created.getContactInfo().getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(created.getContactInfo().getAddress(), is(ADDRESS));
        assertThat(created.getHash(), is(HASH));

        // These are little disturbing, because we actually don't receive these from vetuma...
        assertThat(created.getContactInfo().getPhone(), is(PHONE));
        assertThat(created.getContactInfo().getEmail(), is(EMAIL));

        assertThat(created.getInitiativesWithManagementRight(), hasSize(1));
        assertThat(created.getInitiativesWithParticipation(), hasSize(1));

    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void get_verified_user_gets_its_initiatives() {

        Long initiative = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());
        VerifiedUser verifiedUser = userDao.getVerifiedUser(testHelper.getPreviousUserSsnHash()).get();

        assertThat(verifiedUser.getInitiativesWithParticipation(), hasSize(1));

        assertThat(verifiedUser.getInitiativesWithManagementRight(), hasSize(1));
        assertThat(verifiedUser.getInitiativesWithManagementRight(), is(Collections.singleton(initiative)));

    }

    @Test
    @Transactional
    public void get_verified_user_gets_its_participations() {
        Long initiative = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiative, testMunicipality.getId()));

        VerifiedUser verifiedUser = userDao.getVerifiedUser(testHelper.getPreviousUserSsnHash()).get();

        assertThat(verifiedUser.getInitiativesWithManagementRight(), hasSize(0));

        assertThat(verifiedUser.getInitiativesWithParticipation(), hasSize(1));
        assertThat(verifiedUser.getInitiativesWithParticipation(), is(Collections.singleton(initiative)));

    }

    @Test
    public void prepare_does_not_create_user_if_already_created() {
        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));
        service.prepareVerifiedInitiative(verifiedLoginUserHolder, prepareSafeUICreateDto());
        service.prepareVerifiedInitiative(verifiedLoginUserHolder, prepareSafeUICreateDto());
        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
    }

    @Test
    @Transactional(readOnly = false)
    public void prepare_creates_author_and_participant() {
        precondition(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(0L));
        precondition(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(0L));
        long id = service.prepareVerifiedInitiative(verifiedLoginUserHolder, prepareSafeUICreateDto());
        assertThat(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(1L));

        assertThat(participantDao.findNormalAllParticipants(id, 0, 100), hasSize(0));
        List<VerifiedParticipant> verifiedParticipants = participantDao.findVerifiedAllParticipants(id, 0, 100);
        assertThat(verifiedParticipants, hasSize(1));


        assertThat(verifiedParticipants.get(0).getEmail(), is(EMAIL));
        assertThat(verifiedParticipants.get(0).getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(verifiedParticipants.get(0).getHomeMunicipality().get().getId(), is(testMunicipality.getId()));
        assertThat(verifiedParticipants.get(0).getMembership(), is(Membership.none));
    }

    @Test
    @Transactional(readOnly = false)
    public void prepare_normal_initiative_with_verified_user_creates_author_and_participant() {
        precondition(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(0L));
        precondition(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(0L));
        Long id = service.prepareNormalInitiative(verifiedLoginUserHolder, prepareNormalUiCreateDto());
        assertThat(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(1L));

        List<VerifiedParticipant> verifiedParticipants = participantDao.findVerifiedAllParticipants(id, 0, 100);
        assertThat(verifiedParticipants, hasSize(1));


        assertThat(verifiedParticipants.get(0).getEmail(), is(EMAIL));
        assertThat(verifiedParticipants.get(0).getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(verifiedParticipants.get(0).getHomeMunicipality().get().getId(), is(testMunicipality.getId()));
        assertThat(verifiedParticipants.get(0).getMembership(), is(Membership.none));

    }

    @Test
    @Transactional(readOnly = false)
    public void participating_to_normal_initiative_as_verified_user_creates_participant() {

        Long id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());

        ParticipantUICreateDto createDto = participantCreateDto();
        createDto.setMunicipalMembership(Membership.community);

        service.createParticipant(createDto, id, anotherVerifiedLoginUserHolder);

        List<VerifiedParticipant> verifiedParticipants = participantDao.findVerifiedAllParticipants(id, 0, 100);
        assertThat(verifiedParticipants, hasSize(1));

        assertThat(verifiedParticipants.get(0).getEmail(), is(EMAIL));
        assertThat(verifiedParticipants.get(0).getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(verifiedParticipants.get(0).getHomeMunicipality().get().getId(), is(anotherMunicipality.getId()));
        assertThat(verifiedParticipants.get(0).getMembership(), is(Membership.community));

    }

    @Test
    public void trying_to_prepare_safe_initiative_to_non_active_municipality_is_forbidden() {
        Long unactiveMunicipality = testHelper.createTestMunicipality("Some Unactive Municipality", false);
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(unactiveMunicipality);

        thrown.expect(AccessDeniedException.class);
        thrown.expectMessage(containsString("Municipality is not active"));

        LoginUserHolder<VerifiedUser> verifiedLoginUserHolder = verifiedUserHolderWithMunicipalityId(Maybe.of(unactiveMunicipality));
        service.prepareVerifiedInitiative(verifiedLoginUserHolder, createDto);
    }

    @Test
    @Transactional
    public void preparing_initiative_with_secure_identity_leaves_participants_municipality_null() {

        // Creating initiative to municipality, homeMunicipality not verified from vetuma
        Long municipalityId = testHelper.createTestMunicipality("Municipality", true);
        PrepareSafeInitiativeUICreateDto createDto = prepareSafeUICreateDto();
        createDto.setMunicipality(municipalityId);
        createDto.setUserGivenHomeMunicipality(municipalityId);

        LoginUserHolder<VerifiedUser> verifiedUserLoginUserHolder = verifiedUserHolderWithMunicipalityId(Maybe.<Long>absent());
        long initiativeId = service.prepareVerifiedInitiative(verifiedUserLoginUserHolder, createDto);

        // Update users information with municipality
        userDao.updateUserInformation(
                verifiedUserLoginUserHolder.getVerifiedUser().getHash(),
                "New Name from vetuma",
                Maybe.of(new Municipality(municipalityId, "a", "b", true))
        );

        // Assert that participants municipality is still absent
        assertThat(participantDao.findVerifiedAllParticipants(initiativeId, 0, fi.om.municipalityinitiative.web.Urls.MAX_PARTICIPANT_LIST_LIMIT).get(0).isMunicipalityVerified(), is(false));

    }

    @Test
    public void preparing_safe_initiative_sets_initiative_type_and_municipality() {
        PrepareSafeInitiativeUICreateDto createDto = prepareSafeUICreateDto();
        long initiativeId = service.prepareVerifiedInitiative(verifiedLoginUserHolder, createDto);

        Initiative initiative = testHelper.getInitiative(initiativeId);

        assertThat(initiative.getType(), is(createDto.getInitiativeType()));
        assertThat(initiative.getMunicipality().getId(), is(createDto.getMunicipality()));
    }

    @Test
    public void preparing_safe_initiative_throws_exception_if_wrong_municipality_from_vetuma() {
        PrepareSafeInitiativeUICreateDto createDto = prepareSafeUICreateDto();

        createDto.setMunicipality(testHelper.createTestMunicipality("Other municipality"));

        thrown.expect(InvalidHomeMunicipalityException.class);

        service.prepareVerifiedInitiative(verifiedLoginUserHolder, createDto);
    }

    @Test
    public void preparing_safe_initiative_throws_exception_if_wrong_homeMunicipality_given_by_user_when_vetuma_gives_null_municipality() {
        PrepareSafeInitiativeUICreateDto createDto = prepareSafeUICreateDto();

        createDto.setMunicipality(testHelper.createTestMunicipality("Other municipality"));
        createDto.setUserGivenHomeMunicipality(testMunicipality.getId());

        thrown.expect(InvalidHomeMunicipalityException.class);

        service.prepareVerifiedInitiative(verifiedUserHolderWithMunicipalityId(Maybe.<Long>absent()), createDto);

    }

    @Test
    public void accepting_invitation_to_safe_initiative_throws_exception_if_wrong_municipality_from_vetuma() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());

        AuthorInvitationUIConfirmDto createDto = authorInvitationConfirmDto();
        createDto.assignInitiativeMunicipality(testHelper.createTestMunicipality("other municipality"));
        thrown.expect(InvalidHomeMunicipalityException.class);

        service.confirmVerifiedAuthorInvitation(verifiedLoginUserHolder, initiativeId, createDto, Locales.LOCALE_FI);
    }

    @Test
    public void accepting_invitation_to_safe_initiative_throws_exception_if_wrong_homeMunicipality_given_by_user_when_vetuma_gives_null_municipality() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());

        AuthorInvitationUIConfirmDto createDto = authorInvitationConfirmDto();
        createDto.assignInitiativeMunicipality(testHelper.createTestMunicipality("other municipality"));
        createDto.setHomeMunicipality(createDto.getMunicipality()+1);
        thrown.expect(InvalidHomeMunicipalityException.class);

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderWithMunicipalityId(Maybe.<Long>absent()), initiativeId, createDto, Locales.LOCALE_FI);
    }

    @Test
    public void accepting_invitation_creates_user_only_if_not_already_created_and_creates_author_and_participant() {
        Long firstInitiative = createVerifiedCollaborative();
        Long secondInitiative = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(firstInitiative), false);
        testHelper.addAuthorInvitation(authorInvitation(secondInitiative), false);

        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));
        precondition(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(0L));
        precondition(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(0L));

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();
        confirmDto.setHomeMunicipality(testMunicipality.getId());

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(firstInitiative), firstInitiative, confirmDto, Locales.LOCALE_FI);
        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(secondInitiative), secondInitiative, confirmDto, Locales.LOCALE_FI);

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(2L));
        assertThat(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(2L));

    }

    private Long createVerifiedCollaborative() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED));
    }

    @Test
    public void accepting_invitation_if_already_participated_adds_only_author_and_combines_to_participation() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.ACCEPTED).applyAuthor().toInitiativeDraft());
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()));

        LoginUserHolder<VerifiedUser> loginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(new VerifiedUserId(testHelper.getLastVerifiedUserId()), testHelper.getPreviousUserSsnHash(), new ContactInfo(), Collections.<Long>emptySet(), Collections.<Long>emptySet(), Maybe.<Municipality>absent())
        );

        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        service.confirmVerifiedAuthorInvitation(loginUserHolder, initiativeId, authorInvitationConfirmDto(), Locales.LOCALE_FI);
    }

    @Test
    public void accepting_invitation_if_already_author_throws_exception() {
        Long initiativeId = createVerifiedCollaborative();

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();

        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);
        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);

        thrown.expect(DuplicateKeyException.class);

        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);
        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);
    }

    @Test
    public void accepting_invitation_with_invalid_confirmation_code_throws_exception() {

        Long initiativeId = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();
        confirmDto.setConfirmCode("wrong confirmation code");

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(containsString(AuthorInvitation.class.getName()));

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);
    }

    @Test
    public void accepting_invitation_removes_invitation() {
        Long initiativeId = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();

        precondition(testHelper.countAll(QAuthorInvitation.authorInvitation), is(1L));

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);

        assertThat(testHelper.countAll(QAuthorInvitation.authorInvitation), is(0L));
    }

    @Test
    public void accepting_author_invitation_increases_denormalized_participantCount() {
        Long initiativeId = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        precondition(testHelper.getInitiative(initiativeId).getParticipantCount(), is(0));

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, authorInvitationConfirmDto(), Locales.LOCALE_FI);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    @Transactional
    public void accepting_invitation_creates_user_with_given_information_if_user_does_not_exist() {
        Long initiativeId = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);

        Maybe<VerifiedUser> verifiedUser = userDao.getVerifiedUser(HASH);
        assertThat(verifiedUser, isPresent());
        assertReflectionEquals(verifiedUser.get().getContactInfo(), contactInfo());
    }

    @Test
    @Transactional
    public void accepting_invitation_updates_user_with_given_information_if_user_already_exists() {

        Long firstInitiative = createVerifiedCollaborative();
        Long secondInitiative = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(firstInitiative), false);
        testHelper.addAuthorInvitation(authorInvitation(secondInitiative), false);

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(firstInitiative), firstInitiative, confirmDto, Locales.LOCALE_FI);

        confirmDto.setContactInfo(new ContactInfo());
        confirmDto.getContactInfo().setPhone("new user phone");
        confirmDto.getContactInfo().setName("new user name");
        confirmDto.getContactInfo().setEmail("new user email");
        confirmDto.getContactInfo().setAddress("new user address");

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(secondInitiative), secondInitiative, confirmDto, Locales.LOCALE_FI);

        ContactInfo updatedContactInfo = userDao.getVerifiedUser(HASH).get().getContactInfo();
        assertThat(updatedContactInfo.getName(), is(VERIFIED_AUTHOR_NAME)); // Name is not updated
        assertThat(updatedContactInfo.getPhone(), is(confirmDto.getContactInfo().getPhone()));
        assertThat(updatedContactInfo.getAddress(), is(confirmDto.getContactInfo().getAddress()));
        assertThat(updatedContactInfo.getEmail(), is(confirmDto.getContactInfo().getEmail()));
    }

    @Test
    public void accepting_invitation_not_allowed_if_initiative_in_incorrect_state() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.DRAFT));
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        thrown.expect(OperationNotAllowedException.class);
        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, authorInvitationConfirmDto(), Locales.LOCALE_FI);
    }


    @Test
    public void participating_creates_user_only_if_not_already_created_and_creates_author_and_participant() {
        Long firstInitiative = createVerifiedCollaborative();
        Long secondInitiative = createVerifiedCollaborative();

        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));
        precondition(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(0L));

        service.createParticipant(participantCreateDto(), firstInitiative, verifiedLoginUserHolder);
        service.createParticipant(participantCreateDto(), secondInitiative, verifiedLoginUserHolder);

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(2L));
    }

    @Test
    public void participating_to_safe_initiative_throws_exception_if_wrong_municipality_from_vetuma() {

        Long initiativeId = createVerifiedCollaborative();

        thrown.expect(InvalidHomeMunicipalityException.class);

        LoginUserHolder<VerifiedUser> loginUserHolder = verifiedUserHolderWithMunicipalityId(Maybe.of(testHelper.createTestMunicipality("Other Municipality")));
        service.createParticipant(participantCreateDto(), initiativeId, loginUserHolder);
    }

    @Test
    public void participating_to_safe_initiative_throws_exception_if_wrong_homeMunicipality_given_by_user_when_vetuma_gives_null_municipality() {
        Long initiativeId = createVerifiedCollaborative();

        thrown.expect(InvalidHomeMunicipalityException.class);

        ParticipantUICreateDto createDto = participantCreateDto();
        createDto.setHomeMunicipality(testHelper.createTestMunicipality("Some other municipality"));

        service.createParticipant(createDto, initiativeId, verifiedUserHolderWithMunicipalityId(Maybe.<Long>absent()));
    }

    @Test
    public void participating_not_allowed_if_initiative_in_incorrect_state() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.DRAFT)
                .applyAuthor()
                .toInitiativeDraft());

        thrown.expect(OperationNotAllowedException.class);
        service.createParticipant(participantCreateDto(), initiativeId, verifiedLoginUserHolder);
    }

    @Test
    public void participating_increases_participant_count() {

        Long initiativeId = createVerifiedCollaborative();
        precondition(testHelper.getInitiative(initiativeId).getParticipantCount(), is(0));

        service.createParticipant(participantCreateDto(), initiativeId, verifiedLoginUserHolder);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    public void participating_if_already_participated_throws_exception() {
        Long initiativeId = createVerifiedCollaborative();
        service.createParticipant(participantCreateDto(), initiativeId, verifiedLoginUserHolder);

        thrown.expect(DuplicateKeyException.class);
        service.createParticipant(participantCreateDto(), initiativeId, verifiedLoginUserHolder);
    }

    @Test
    public void confirm_verified_author_invitation_increases_public_names_count() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.ACCEPTED));

        AuthorInvitation invitation = authorInvitation(initiativeId);
        testHelper.addAuthorInvitation(invitation, false);

        AuthorInvitationUIConfirmDto confirmDto = ReflectionTestUtils.modifyAllFields(new AuthorInvitationUIConfirmDto());
        confirmDto.getContactInfo().setShowName(true);
        confirmDto.assignInitiativeMunicipality(testMunicipality.getId());
        confirmDto.setHomeMunicipality(testMunicipality.getId());
        confirmDto.setConfirmCode(invitation.getConfirmationCode());

        int originalPublicParticipantCount = testHelper.getInitiative(initiativeId).getParticipantCountPublic();
        service.confirmVerifiedAuthorInvitation(verifiedUserHolderWithMunicipalityId(Maybe.of(testMunicipality.getId())), initiativeId, confirmDto, Locales.LOCALE_FI);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCountPublic(), is(originalPublicParticipantCount + 1));

    }

    private static AuthorInvitation authorInvitation(Long initiativeId) {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setConfirmationCode(CONFIRMATION_CODE);
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setEmail("some@example.com");
        authorInvitation.setName("any name");
        authorInvitation.setInvitationTime(DateTime.now());
        return authorInvitation;
    }

    private AuthorInvitationUIConfirmDto authorInvitationConfirmDto() {
        AuthorInvitationUIConfirmDto dto = new AuthorInvitationUIConfirmDto();
        dto.setContactInfo(contactInfo());
        dto.setConfirmCode(CONFIRMATION_CODE);
        dto.assignInitiativeMunicipality(testMunicipality.getId());
        dto.setHomeMunicipality(testMunicipality.getId());
        return dto;

    }

    private ParticipantUICreateDto participantCreateDto() {
        ParticipantUICreateDto createDto = new ParticipantUICreateDto();

        createDto.setHomeMunicipality(testMunicipality.getId());
        createDto.assignInitiativeMunicipality(testMunicipality.getId());
        createDto.setShowName(true);
        return createDto;
    }

    private static LoginUserHolder<VerifiedUser> verifiedUserHolderWithMunicipalityId(Maybe<Long> maybeMunicipality) {
        Maybe<Municipality> municipality;
        if (maybeMunicipality.isPresent()) {
            municipality = Maybe.of(new Municipality(maybeMunicipality.get(), "", "", false));
        }
        else {
            municipality = Maybe.absent();
        }
        return new LoginUserHolder<>(User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo(), Collections.<Long>emptySet(), Collections.<Long>emptySet(), municipality));
    }

    private static LoginUserHolder<VerifiedUser> verifiedUserHolderForInitiative(Long initiativeId) {
        return new LoginUserHolder<>(User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo(), Collections.singleton(initiativeId), Collections.singleton(initiativeId), Maybe.<Municipality>absent()));
    }

    private PrepareSafeInitiativeUICreateDto prepareSafeUICreateDto() {
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(testMunicipality.getId());
        createDto.setInitiativeType(InitiativeType.COLLABORATIVE_CITIZEN);
        return createDto;
    }


    private PrepareInitiativeUICreateDto prepareNormalUiCreateDto() {
        PrepareInitiativeUICreateDto prepareNormal = new PrepareInitiativeUICreateDto();
        prepareNormal.setInitiativeType(InitiativeType.UNDEFINED);
        prepareNormal.setMunicipality(testMunicipality.getId());
        return prepareNormal;
    }

}
