package fi.om.municipalityinitiative.service.ui;

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
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.Collections;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class VerifiedInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    VerifiedInitiativeService service;

    private Municipality testMunicipality;

    public static LoginUserHolder<VerifiedUser> verifiedLoginUserHolder;

    private static final String EMAIL = "email@example.com";
    private static final String ADDRESS = "Some address whatsoever";

    public static final String PHONE = "32434";

    public static final String VERIFIED_AUTHOR_NAME = "Verified Author Name";

    public static final String HASH = "hash";

    static {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(EMAIL);
        contactInfo.setAddress(ADDRESS);
        contactInfo.setPhone(PHONE);
        contactInfo.setName(VERIFIED_AUTHOR_NAME);

        verifiedLoginUserHolder = new LoginUserHolder<>(
                User.verifiedUser(HASH, contactInfo, Collections.<Long>emptySet())
        );

    }

    @Override
    protected void childSetup() {
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
    }

    @Test
    public void preparing_sets_participant_count_to_one_when_adding_new_safe_initiative() {
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }

    @Test
    public void preparing_creates_user_if_not_yet_created() {
        precondition(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(0L));
        service.prepareSafeInitiative(verifiedLoginUserHolder, prepareUICreateDto());
        assertThat(testHelper.countAll(QVerifiedAuthor.verifiedAuthor), is(1L));
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
    @Ignore
    // TODO: Implement
    public void preparing_initiative_sets_initiative_type_and_municipality() {
        PrepareSafeInitiativeUICreateDto prepareSafeInitiativeUICreateDto = new PrepareSafeInitiativeUICreateDto();
        prepareSafeInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareSafeInitiativeUICreateDto.setInitiativeType(InitiativeType.COLLABORATIVE_COUNCIL);
        long initiativeId = service.prepareSafeInitiative(verifiedLoginUserHolder, prepareSafeInitiativeUICreateDto);

        Initiative initiative = testHelper.getInitiative(initiativeId);

        assertThat(initiative.getType(), is(InitiativeType.COLLABORATIVE_COUNCIL));
        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
    }

    @Test
    @Ignore
    public void preparing_safe_initiative_sends_email() {
        // TODO: Implement
    }

    @Test
    @Ignore
    public void preparing_safe_initiative_sets_participant_information() {
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
        createDto.setVerifiedAuthorEmail("email@example.com");
        return createDto;
    }
}
