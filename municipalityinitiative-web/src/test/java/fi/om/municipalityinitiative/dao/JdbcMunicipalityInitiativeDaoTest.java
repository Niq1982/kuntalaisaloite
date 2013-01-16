package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeSearch;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={NEWIntegrationTestConfiguration.class})
public class JdbcMunicipalityInitiativeDaoTest {

    @Resource
    MunicipalityInitiativeDao municipalityInitiativeDao;

    @Resource
    NEWTestHelper testHelper;

    private MunicipalityInfo testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));
    }

    @Test
    public void testCreateASd() {
        testHelper.createTestInitiative(testMunicipality.getId(), "jokunimi");
    }


    @Test
    public void create() {
        municipalityInitiativeDao.create(createDto());
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }

    @Test
    public void create_and_get() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some name");

        MunicipalityInitiativeInfo get = municipalityInitiativeDao.getById(initiativeId);
        assertThat(1, Matchers.is(2));
    }

    @Test
    public void find_returns_all() {

        testHelper.createTestInitiative(testMunicipality.getId(), "First");
        testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findNewestFirst(new MunicipalityInitiativeSearch());
        assertThat(result.size(), is(2));
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createTestInitiative(testMunicipality.getId(), "First");
        Long second = testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findNewestFirst(new MunicipalityInitiativeSearch());
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

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findNewestFirst(search);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_name() {

        testHelper.createTestInitiative(testMunicipality.getId(), "name that should not be found");
        Long shouldBeFound = testHelper.createTestInitiative(testMunicipality.getId(), "name that should be found ääöö");

        MunicipalityInitiativeSearch search = new MunicipalityInitiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findNewestFirst(search);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));

    }

    private MunicipalityInitiativeCreateDto createDto() {
        MunicipalityInitiativeCreateDto dto = new MunicipalityInitiativeCreateDto();

        dto.name = "initiativename"+randomString();
        dto.proposal = "proposal"+randomString();
        dto.municipalityId = testMunicipality.getId();

        dto.contactAddress = "address"+randomString();
        dto.contactName = "contactname"+randomString();
        dto.contactEmail = "email"+randomString();
        dto.contactPhone = "phone"+randomString();
        return dto;
    }

    private static String randomString() {
        return String.valueOf(new Random().nextLong());
    }

    private void assertCreateAndGetDtos(MunicipalityInitiativeCreateDto create, MunicipalityInitiativeInfo get) {
        assertThat(get.getProposal(), is(create.proposal));
        assertThat(get.getName(), is(create.name));
        assertThat(get.getContactName(), is(create.contactName));
        assertThat(get.getContactPhone(), is(create.contactPhone));
        assertThat(get.getContactEmail(), is(create.contactEmail));
        assertThat(get.getContactAddress(), is(create.contactAddress));
        assertThat(get.getMunicipalityName(), is(testMunicipality.getName()));
        assertThat(get.getCreateTime(), is(notNullValue()));
        assertThat(get.getId(), is(notNullValue()));
    }
}
