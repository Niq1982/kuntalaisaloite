package fi.om.municipalityinitiative.dao;


import com.mysema.query.QueryException;
import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional(readOnly = false)
public class JdbcFollowInitiativeDaoTest {

    public static final String TESTEMAIL = "test@test.fi";
    @Resource
    TestHelper testHelper;

    @Resource
    FollowInitiativeDao followInitiativeDao;
    private Long initiativeId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        Long testMunicipality = testHelper.createTestMunicipality("Some municipality");
        initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality).withState(InitiativeState.PUBLISHED));
    }

    @Test
    public void follow_initiative() {
        Map<String, String> followers = followInitiativeDao.listFollowers(initiativeId);
        int before = followers.size();
        String hash = RandomHashGenerator.longHash();
        followInitiativeDao.addFollow(initiativeId, TESTEMAIL, hash);

        followers = followInitiativeDao.listFollowers(initiativeId);

        assertThat(followers.values(), hasSize(before +1));

    }

    @Test
    public void cant_follow_initiative_twice() {
        Map<String, String> followers = followInitiativeDao.listFollowers(initiativeId);
        int before = followers.size();

        followInitiativeDao.addFollow(initiativeId, TESTEMAIL, RandomHashGenerator.longHash());
        followers = followInitiativeDao.listFollowers(initiativeId);

        try{

            followInitiativeDao.addFollow(initiativeId, TESTEMAIL, RandomHashGenerator.longHash());
            followers = followInitiativeDao.listFollowers(initiativeId);
        } catch (QueryException e) {
            e.printStackTrace();
        }finally {
            assertThat(followers.values(), hasSize(before +1));
        }



    }

    @Test
    public void remove_follow() {
        Map<String, String> followers = followInitiativeDao.listFollowers(initiativeId);
        int before = followers.size();

        followInitiativeDao.addFollow(initiativeId, TESTEMAIL, RandomHashGenerator.longHash());

        followers = followInitiativeDao.listFollowers(initiativeId);

        assertThat(followers.values(), hasSize(before +1));

        followInitiativeDao.removeFollow(followers.get(TESTEMAIL));

        followers = followInitiativeDao.listFollowers(initiativeId);

        assertThat(followers.values(), hasSize(before));
        assertThat(followers.values(), not(contains(TESTEMAIL)));
    }
}
