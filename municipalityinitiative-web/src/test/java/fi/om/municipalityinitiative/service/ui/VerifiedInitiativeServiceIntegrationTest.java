package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.sql.QVerifiedAuthor;
import fi.om.municipalityinitiative.sql.QVerifiedParticipant;
import fi.om.municipalityinitiative.sql.QVerifiedUser;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Collections;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class VerifiedInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase{

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

    @Override
    protected void childSetup() {
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(EMAIL);
        contactInfo.setAddress(ADDRESS);
        contactInfo.setPhone(PHONE);
        contactInfo.setName(VERIFIED_AUTHOR_NAME);

        verifiedLoginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(HASH, contactInfo, Collections.<Long>emptySet(), Maybe.<Municipality>of(testMunicipality))
        );

    }

    @Test
    public void preparing_sets_participant_count_to_one_when_adding_new_safe_initiative() {
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    @Transactional // Tests also userDao for receiving the information
    public void preparing_creates_user_with_given_information_if_not_yet_created() {
        precondition(testHelper.countAll(QVerifiedUser.verifiedUser), is(0L));

        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());

        assertThat(testHelper.countAll(QVerifiedUser.verifiedUser), is(1L));
        VerifiedUser created = userDao.getVerifiedUser(HASH).get();
        assertThat(created.getContactInfo().getName(), is(VERIFIED_AUTHOR_NAME));
        assertThat(created.getContactInfo().getAddress(), is(ADDRESS));
        assertThat(created.getHash(), is(HASH));

        // These are little disturbing, because we actually won't receive these from vetuma...
        assertThat(created.getContactInfo().getPhone(), is(PHONE));
        assertThat(created.getContactInfo().getEmail(), is(EMAIL));

        assertThat(created.getInitiatives(), hasSize(1));

    }

    @Test
    public void does_not_create_user_if_already_created() {
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

    @Test(expected = AccessDeniedException.class)
    public void trying_to_prepare_safe_initiative_to_non_active_municipality_is_forbidden() {
        Long unactiveMunicipality = testHelper.createTestMunicipality("Some Unactive Municipality", false);
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(unactiveMunicipality);
        service.prepareSafeInitiative(verifiedLoginUserHolder, createDto);
    }

    @Test
    public void preparing_initiative_sets_initiative_type_and_municipality() {
        PrepareSafeInitiativeUICreateDto createDto = prepareUICreateDto();
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, createDto);

        Initiative initiative = testHelper.getInitiative(initiativeId);

        assertThat(initiative.getType(), is(createDto.getInitiativeType()));
        assertThat(initiative.getMunicipality().getId(), is(createDto.getMunicipality()));
    }

    @Test
    @Ignore
    public void preparing_safe_initiative_sends_email() {
        // TODO: Implement
    }

    @Test
    @Ignore
    public void preparing_safe_initiative_saved_email_and_municipality_and_membership() {
        // TODO: Implement
    }

    private PrepareSafeInitiativeUICreateDto prepareUICreateDto() {
        PrepareSafeInitiativeUICreateDto createDto = new PrepareSafeInitiativeUICreateDto();
        createDto.setMunicipality(testMunicipality.getId());
        createDto.setInitiativeType(InitiativeType.COLLABORATIVE_CITIZEN);
        return createDto;
    }
}
