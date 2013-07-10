package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Collections;

import static fi.om.municipalityinitiative.util.ReflectionTestUtils.assertReflectionEquals;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class VerifiedInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase{

    public static final String CONFIRMATION_CODE = "confirmationCode";
    @Resource
    private VerifiedInitiativeService service;

    // Test also some functionality of userDao. Ugly, but lets leave it that for now.
    @Resource
    private UserDao userDao;

    private Municipality testMunicipality;

    public static LoginUserHolder<VerifiedUser> verifiedLoginUserHolder;

    private static final String EMAIL = "email@example.com";
    private static final String ADDRESS = "Some address whatsoever";

    public static final String PHONE = "32434";

    public static final String VERIFIED_AUTHOR_NAME = "Verified Author Name";

    public static final String HASH = "hash";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    protected void childSetup() {
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

        ContactInfo contactInfo = contactInfo();

        verifiedLoginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo, Collections.<Long>emptySet(), Maybe.<Municipality>of(testMunicipality))
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
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    @Transactional // Some tests use userDao for receiving the results for assertion
    public void preparing_creates_user_with_given_information_if_not_yet_created() {
        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));

        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        VerifiedUser created = userDao.getVerifiedUser(HASH).get();
        assertThat(created.getContactInfo().getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(created.getContactInfo().getAddress(), is(ADDRESS));
        assertThat(created.getHash(), is(HASH));

        // These are little disturbing, because we actually don't receive these from vetuma...
        assertThat(created.getContactInfo().getPhone(), is(PHONE));
        assertThat(created.getContactInfo().getEmail(), is(EMAIL));

        assertThat(created.getInitiatives(), hasSize(1));

    }

    @Test
    public void prepare_does_not_create_user_if_already_created() {
        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));
        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
    }

    @Test
    public void prepare_creates_author_and_participant() {
        precondition(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(0L));
        precondition(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(0L));
        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(1L));
    }

    @Test
    public void trying_to_prepare_safe_initiative_to_non_active_municipality_is_forbidden() {
        Long unactiveMunicipality = testHelper.createTestMunicipality("Some Unactive Municipality", false);
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(unactiveMunicipality);

        thrown.expect(AccessDeniedException.class);
        thrown.expectMessage(containsString("Municipality is not active"));

        LoginUserHolder<VerifiedUser> verifiedLoginUserHolder = verifiedUserHolderWithMunicipalityId(Maybe.of(unactiveMunicipality));
        service.prepareSafeInitiative(verifiedLoginUserHolder, createDto);
    }

    @Test
    public void preparing_safe_initiative_sets_initiative_type_and_municipality() {
        PrepareSafeInitiativeUICreateDto createDto = prepareUICreateDto();
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, createDto);

        Initiative initiative = testHelper.getInitiative(initiativeId);

        assertThat(initiative.getType(), is(createDto.getInitiativeType()));
        assertThat(initiative.getMunicipality().getId(), is(createDto.getMunicipality()));
    }

    @Test
    public void preparing_safe_initiative_throws_exception_if_wrong_municipality_from_vetuma() {
        PrepareSafeInitiativeUICreateDto createDto = prepareUICreateDto();

        createDto.setMunicipality(testHelper.createTestMunicipality("Other municipality"));

        thrown.expect(InvalidHomeMunicipalityException.class);

        service.prepareSafeInitiative(verifiedLoginUserHolder, createDto);
    }

    @Test
    public void preparing_safe_initiative_throws_exception_if_wrong_homeMunicipality_given_by_user_when_vetuma_gives_null_municipality() {
        PrepareSafeInitiativeUICreateDto createDto = prepareUICreateDto();

        createDto.setMunicipality(testHelper.createTestMunicipality("Other municipality"));
        createDto.setUserGivenHomeMunicipality(testMunicipality.getId());

        thrown.expect(InvalidHomeMunicipalityException.class);

        service.prepareSafeInitiative(verifiedUserHolderWithMunicipalityId(Maybe.<Long>absent()), createDto);

    }

    @Test
    @Ignore
    public void preparing_safe_initiative_sends_email() {
        // TODO: Implement
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
                .withType(InitiativeType.COLLABORATIVE)
                .withState(InitiativeState.ACCEPTED));
    }

    @Test
    @Ignore("Implement after participation")
    public void accepting_invitation_if_already_participated_adds_only_author_and_combines_to_participation() {
        // TODO: Implement after participation
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

        precondition(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, authorInvitationConfirmDto(), Locales.LOCALE_FI);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(2));
    }

    @Test
    @Transactional
    public void accepting_invitation_creates_user_with_given_information_if_user_does_not_exist() {
        Long initiativeId = createVerifiedCollaborative();
        testHelper.addAuthorInvitation(authorInvitation(initiativeId), false);

        AuthorInvitationUIConfirmDto confirmDto = authorInvitationConfirmDto();

        service.confirmVerifiedAuthorInvitation(verifiedUserHolderForInitiative(initiativeId), initiativeId, confirmDto, Locales.LOCALE_FI);

        Maybe<VerifiedUser> verifiedUser = userDao.getVerifiedUser(HASH);
        assertThat(verifiedUser.isPresent(), is(true));
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

        service.createParticipant(verifiedLoginUserHolder, firstInitiative, participantCreateDto());
        service.createParticipant(verifiedLoginUserHolder, secondInitiative, participantCreateDto());

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        assertThat(testHelper.countAll(QVerifiedParticipant.verifiedParticipant), is(2L));
    }

    @Test
    public void participating_to_safe_initiative_throws_exception_if_wrong_municipality_from_vetuma() {
    }

    @Test
    public void participating_not_allowed_if_initiative_in_incorrect_state() {
    }

    @Test
    public void participating_increases_participant_count() {
    }

    @Test
    public void participating_to_safe_initiative_throws_exception_if_wrong_homeMunicipality_given_by_user_when_vetuma_gives_null_municipality() {
    }

    @Test
    public void participating_updates_user_with_given_information_if_user_already_exists() {
    }

    @Test
    public void participating_creates_user_with_given_information_if_user_does_not_exist()  {
    }

    @Test
    public void participating_if_already_participated_throws_exception() {
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
        createDto.assignMunicipality(testMunicipality.getId());
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
        return new LoginUserHolder<>(User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo(), Collections.<Long>emptySet(), municipality));
    }

    private static LoginUserHolder<VerifiedUser> verifiedUserHolderForInitiative(Long initiativeId) {
        return new LoginUserHolder<>(User.verifiedUser(new VerifiedUserId(-1L), HASH, contactInfo(), Collections.singleton(initiativeId), Maybe.<Municipality>absent()));
    }

    private PrepareSafeInitiativeUICreateDto prepareUICreateDto() {
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(testMunicipality.getId());
        createDto.setInitiativeType(InitiativeType.COLLABORATIVE_CITIZEN);
        return createDto;
    }
}
