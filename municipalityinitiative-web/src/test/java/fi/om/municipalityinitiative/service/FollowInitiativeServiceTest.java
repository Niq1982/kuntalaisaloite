package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.FollowInitiativeDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.service.ui.InitiativeManagementService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FollowInitiativeServiceTest extends ServiceIntegrationTestBase {


    public static final String FOLLOWER_EMAIL = "test@test.fi";
    private static final String VERIFIED_INITIATIVE_AURHOR_SSN = "000000-0000" ;
    private Long testMunicipalityId;

    @Resource
    FollowInitiativeService followInitiativeService;

    @Resource
    FollowInitiativeDao followInitiativeDao;

    @Resource
    InitiativeManagementService initiativeManagementService;

    @Resource
    MunicipalityDecisionService municipalityDecisionService;

    private final LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    @Transactional
    public void follow_initiative_sends_confirm_email(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor().toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));
        EmailDto sentEmail = testHelper.getSingleQueuedEmail();

        Map<String, String> followers = followInitiativeDao.listFollowers(id);

        assertThat(sentEmail.getRecipientsAsString(), is(FOLLOWER_EMAIL));
        assertThat(sentEmail.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).unsubscribe(id, followers.get(FOLLOWER_EMAIL))));

    }

    @Test
    @Transactional
    public void send_email_to_follower_when_sending_to_municipality(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor(VERIFIED_INITIATIVE_AURHOR_SSN).toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));
        EmailDto sentEmail = testHelper.getSingleQueuedEmail();

        Map<String, String> followers = followInitiativeDao.listFollowers(id);

        assertThat(sentEmail.getRecipientsAsString(), is(FOLLOWER_EMAIL));
        assertThat(sentEmail.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).unsubscribe(id, followers.get(FOLLOWER_EMAIL))));

        initiativeManagementService.sendToMunicipality(id, TestHelper.authorLoginUserHolder, "", new Locale("fi"));

        assertThat(testHelper.findQueuedEmails(), hasSize(4));

    }
    @Test
    @Transactional
    public void send_email_to_follower_when_municipality_answers_to_initiative(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor(VERIFIED_INITIATIVE_AURHOR_SSN).toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));
        EmailDto sentEmail = testHelper.getSingleQueuedEmail();

        Map<String, String> followers = followInitiativeDao.listFollowers(id);

        assertThat(sentEmail.getRecipientsAsString(), is(FOLLOWER_EMAIL));
        assertThat(sentEmail.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).unsubscribe(id, followers.get(FOLLOWER_EMAIL))));

        try {
            municipalityDecisionService.setDecision(MunicipalityDecisionDto.build(Maybe.of("Päätös teksti")), id, new MunicipalityUserHolder(User.municipalityLoginUser(id)), new Locale("fi"));
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (InvalidAttachmentException e) {
            e.printStackTrace();
        }
        assertThat(testHelper.findQueuedEmails(), hasSize(3));

    }

    @Test
    @Transactional
    public void stop_following_initiative(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor().toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));

        Map<String, String> followers = followInitiativeDao.listFollowers(id);
        assertThat(followers.values(), hasSize(1));

        followInitiativeService.stopFollowingInitiative(followers.get(FOLLOWER_EMAIL));

        assertThat(testHelper.findQueuedEmails(), hasSize(1));
        followers = followInitiativeDao.listFollowers(id);
        assertThat(followers.values(), hasSize(0));
    }


    @Test
    @Transactional
    public void cant_follow_initiative_that_has_not_been_published(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.REVIEW).applyAuthor().toInitiativeDraft());
        try {
            followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));
        } catch (Exception e) {

        } finally {

            assertThat(testHelper.findQueuedEmails(), hasSize(0));
            Map<String, String> followers = followInitiativeDao.listFollowers(id);
            assertThat(followers.values(), hasSize(0));
        }

    }

    @Test
    @Transactional
    public void cant_follow_initiative_that_has_decision(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.REVIEW).withDecisionDate(twoDaysAgo.toDateTime(new LocalTime("12:00"))).applyAuthor().toInitiativeDraft());
        try {
            followInitiativeService.followInitiative(id, FOLLOWER_EMAIL, new Locale("fi"));
        } catch (Exception e) {

        } finally {

            assertThat(testHelper.findQueuedEmails(), hasSize(0));
            Map<String, String> followers = followInitiativeDao.listFollowers(id);
            assertThat(followers.values(), hasSize(0));
        }

    }


}
