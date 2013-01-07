package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
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
    public void create() {
        municipalityInitiativeDao.create(createDto());
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }

    @Test
    public void create_and_get() {
        MunicipalityInitiativeCreateDto create = createDto();
        Long createId = municipalityInitiativeDao.create(create);

        MunicipalityInitiativeInfo get = municipalityInitiativeDao.getById(createId);
        assertCreateAndGetDtos(create, get);
    }

    @Test
    public void find_returns_all() {

        municipalityInitiativeDao.create(createDto());
        municipalityInitiativeDao.create(createDto());

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findAllNewestFirst();
        assertThat(result.size(), is(2));
    }

    @Test
    public void find_returns_in_correct_order() {
        MunicipalityInitiativeCreateDto create1 = createDto();
        MunicipalityInitiativeCreateDto create2 = createDto();

        municipalityInitiativeDao.create(create1);
        municipalityInitiativeDao.create(create2);

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findAllNewestFirst();
        assertCreateAndGetDtos(create2, result.get(0));
        assertCreateAndGetDtos(create1, result.get(1));
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
        assertThat(get.proposal, is(create.proposal));
        assertThat(get.name, is(create.name));
        assertThat(get.contactName, is(create.contactName));
        assertThat(get.contactPhone, is(create.contactPhone));
        assertThat(get.contactEmail, is(create.contactEmail));
        assertThat(get.contactAddress, is(create.contactAddress));
        assertThat(get.municipalityName, is(testMunicipality.getName()));
    }
}
