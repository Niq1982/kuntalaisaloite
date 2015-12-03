package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.FollowInitiativeDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FollowInitiativeServiceTest extends ServiceIntegrationTestBase {


    public static final String FOLLOWER_EMAIL = "test@test.fi";
    private Long testMunicipalityId;

    @Resource
    FollowInitiativeService followInitiativeService;

    @Resource
    FollowInitiativeDao followInitiativeDao;

    private final LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    @Transactional
    public void follow_initiative_sends_confirm_email(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor().toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL);
        EmailDto sentEmail = testHelper.getSingleQueuedEmail();

        Map<String, String> followers = followInitiativeDao.listFollowers(id);

        assertThat(sentEmail.getRecipientsAsString(), is(FOLLOWER_EMAIL));
        assertThat(sentEmail.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).unsubscribe(id, followers.get(FOLLOWER_EMAIL))));

    }

    @Test
    @Transactional
    public void stop_following_initiative(){

        Long id = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).applyAuthor().toInitiativeDraft());
        followInitiativeService.followInitiative(id, FOLLOWER_EMAIL);

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
            followInitiativeService.followInitiative(id, FOLLOWER_EMAIL);
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
            followInitiativeService.followInitiative(id, FOLLOWER_EMAIL);
        } catch (Exception e) {

        } finally {

            assertThat(testHelper.findQueuedEmails(), hasSize(0));
            Map<String, String> followers = followInitiativeDao.listFollowers(id);
            assertThat(followers.values(), hasSize(0));
        }

    }


}
