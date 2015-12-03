package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.FollowInitiativeDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class FollowInitiativeServiceTest extends ServiceIntegrationTestBase {


    public static final String FOLLOWER_EMAIL = "test@test.fi";
    private Long testMunicipalityId;

    @Resource
    FollowInitiativeService followInitiativeService;

    @Resource
    FollowInitiativeDao followInitiativeDao;

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



}
