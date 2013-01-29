package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcInitiativeDaoTest {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    TestHelper testHelper;

    private MunicipalityInfo testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));
    }

    // Create and get are tested at MunicipalityInitiativeServiceIntegrationTests

    @Test
    public void find_returns_all() {

        testHelper.createTestInitiative(testMunicipality.getId(), "First");
        testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<InitiativeListInfo> result = initiativeDao.findNewestFirst(new InitiativeSearch());
        assertThat(result.size(), is(2));
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createTestInitiative(testMunicipality.getId(), "First");
        Long second = testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<InitiativeListInfo> result = initiativeDao.findNewestFirst(new InitiativeSearch());
        assertThat(second, Matchers.is(result.get(0).getId()));
        assertThat(first, Matchers.is(result.get(1).getId()));
    }

    @Test
    public void finds_by_municipality() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long shouldBeFound = testHelper.createTestInitiative(municipalityId);

        Long shouldNotBeFound = testHelper.createTestInitiative(testMunicipality.getId());

        InitiativeSearch search = new InitiativeSearch();
        search.setMunicipality(municipalityId);

        List<InitiativeListInfo> result = initiativeDao.findNewestFirst(search);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_name() {

        testHelper.createTestInitiative(testMunicipality.getId(), "name that should not be found");
        Long shouldBeFound = testHelper.createTestInitiative(testMunicipality.getId(), "name that should be found ääöö");

        InitiativeSearch search = new InitiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<InitiativeListInfo> result = initiativeDao.findNewestFirst(search);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));

    }

    @Test
    public void marks_as_sended_if_collectable_and_not_sended() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, true);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.getSentTime().isPresent(), is(false)); // Precondition

        initiativeDao.markAsSended(initiativeId);
        initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.getSentTime().isPresent(), is(true));
    }

    @Test(expected = NotCollectableException.class)
    public void throws_exception_if_not_collectable_and_marking_as_sent() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, false);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.isCollectable(), is(false)); // Precondition

        initiativeDao.markAsSended(initiativeId);
    }

    @Test
    public void throws_exception_if_trying_double_send() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, true);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.isCollectable(), is(true)); // Precondition
        assertThat(initiative.getSentTime().isPresent(), is(false)); // Precondition

        initiativeDao.markAsSended(initiativeId);

        try {
            initiativeDao.markAsSended(initiativeId);
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {

        }
    }


}
