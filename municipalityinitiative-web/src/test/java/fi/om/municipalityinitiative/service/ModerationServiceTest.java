package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.ui.ModerationService;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ModerationServiceTest extends ServiceIntegrationTestBase {

    @Resource
    private ModerationService moderationService;

    // XXX: Should not depend of this but fine for now.
    @Resource
    private MunicipalityUserDao municipalityUserDao;

    @Resource
    private ParticipantDao participantDao;


    private Long testMunicipalityId;

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void get_authors_for_initiative_as_om_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));
        assertThat(moderationService.findAuthors(omLoginUserHolder, initiativeId), hasSize(1));
    }

    @Test
    @Transactional
    public void update_email_for_normal_author_as_om_user() {
        Long initiativeId = createDefaultInitiativeWithAuthor();
        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));
        List<Participant> participantList = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(participantList.size(), is(1));
        Participant participant = participantList.get(0);

        String newEmail = "newemail@example.com";

        moderationService.updateEmailForNormalAuthor(omLoginUserHolder, initiativeId, participant.getId().toLong(), newEmail);
        participantList = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(participantList.size(), is(1));
        participant = participantList.get(0);
        assertThat(participant.getEmail(), is(newEmail));
    }

    @Test(expected = OperationNotAllowedException.class)
    @Transactional
    public void update_email_for_normal_author_with_wrong_initiative_id_as_om_user() {
        Long otherInitiativeId = createDefaultInitiativeWithAuthor();
        Long initiativeId = createDefaultInitiativeWithAuthor();
        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));
        List<Participant> participantList = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(participantList.size(), is(1));
        Participant participant = participantList.get(0);

        String newEmail = "newemail@example.com";

        moderationService.updateEmailForNormalAuthor(omLoginUserHolder, otherInitiativeId, participant.getId().toLong(), newEmail);
    }

    @Test
    public void get_authors_for_initiative_as_municipality_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        MunicipalityUserHolder municipalityUserHolder = new MunicipalityUserHolder(User.municipalityLoginUser( initiativeId));
        List<? extends Author> authors = new ArrayList<>();
        try {
            authors = moderationService.findAuthors(municipalityUserHolder, initiativeId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertThat(authors, hasSize(0));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @Transactional
    public void update_email_for_normal_author_as_municipality_user() {
        Long initiativeId = createDefaultInitiativeWithAuthor();
        MunicipalityUserHolder municipalityUserHolder = new MunicipalityUserHolder(User.municipalityLoginUser(initiativeId));
        List<Participant> participantList = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(participantList.size(), is(1));
        Participant participant = participantList.get(0);

        String newEmail = "newemail@example.com";
        moderationService.updateEmailForNormalAuthor(municipalityUserHolder, initiativeId, participant.getId().toLong(), newEmail);
    }

    @Test
    public void get_authors_for_initiative_as_anomous_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        LoginUserHolder anomUser = new LoginUserHolder(User.anonym());
        List<? extends Author> authors = new ArrayList<>();
        try {
            authors = moderationService.findAuthors(anomUser, initiativeId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertThat(authors, hasSize(0));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @Transactional
    public void update_email_for_normal_author_as_anonym_user() {
        Long initiativeId = createDefaultInitiativeWithAuthor();
        LoginUserHolder anomUser = new LoginUserHolder(User.anonym());
        List<Participant> participantList = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(participantList.size(), is(1));
        Participant participant = participantList.get(0);

        String newEmail = "newemail@example.com";
        moderationService.updateEmailForNormalAuthor(anomUser, initiativeId, participant.getId().toLong(), newEmail);
    }

    @Test
    @Transactional
    public void renew_municipality_management_hash() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId));

        String oldHash = RandomHashGenerator.longHash();

        municipalityUserDao.assignMunicipalityUser(initiativeId, oldHash);

        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));

        moderationService.renewMunicipalityManagementHash(omLoginUserHolder, initiativeId, new Locale("fi"));

        String newHash = municipalityUserDao.getMunicipalityUserHashAttachedToInitiative(initiativeId).managementHash;

        assertThat(oldHash, not(newHash));
    }

    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }

    private Long createDefaultInitiativeWithAuthor() {
        return testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }




}
