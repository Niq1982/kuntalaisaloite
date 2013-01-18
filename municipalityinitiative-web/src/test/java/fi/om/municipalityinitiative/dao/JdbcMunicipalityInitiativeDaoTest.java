package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeSearch;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcMunicipalityInitiativeDaoTest {

    @Resource
    MunicipalityInitiativeDao municipalityInitiativeDao;

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

        List<InitiativeListInfo> result = municipalityInitiativeDao.findNewestFirst(new MunicipalityInitiativeSearch());
        assertThat(result.size(), is(2));
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createTestInitiative(testMunicipality.getId(), "First");
        Long second = testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<InitiativeListInfo> result = municipalityInitiativeDao.findNewestFirst(new MunicipalityInitiativeSearch());
        assertThat(second, Matchers.is(result.get(0).getId()));
        assertThat(first, Matchers.is(result.get(1).getId()));
    }

    @Test
    public void finds_by_municipality() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long shouldBeFound = testHelper.createTestInitiative(municipalityId);

        Long shouldNotBeFound = testHelper.createTestInitiative(testMunicipality.getId());

        MunicipalityInitiativeSearch search = new MunicipalityInitiativeSearch();
        search.setMunicipality(municipalityId);

        List<InitiativeListInfo> result = municipalityInitiativeDao.findNewestFirst(search);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_name() {

        testHelper.createTestInitiative(testMunicipality.getId(), "name that should not be found");
        Long shouldBeFound = testHelper.createTestInitiative(testMunicipality.getId(), "name that should be found ääöö");

        MunicipalityInitiativeSearch search = new MunicipalityInitiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<InitiativeListInfo> result = municipalityInitiativeDao.findNewestFirst(search);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));

    }

}
