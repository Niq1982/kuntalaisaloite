package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
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

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void testCreateAndGet() {
        MunicipalityInitiativeCreateDto create = createDto();
        Long createId = municipalityInitiativeDao.create(create);

        MunicipalityInitiativeInfo get = municipalityInitiativeDao.getById(createId);
        assertCreateAndGetDtos(create, get);
    }

    @Test
    public void testFindReturnsAll() {

        municipalityInitiativeDao.create(createDto());
        municipalityInitiativeDao.create(createDto());

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findAllNewestFirst();
        assertThat(result.size(), is(2));
    }

    @Test
    public void findReturnsInCorrectOrder() {
        MunicipalityInitiativeCreateDto create1 = createDto();
        MunicipalityInitiativeCreateDto create2 = createDto();

        municipalityInitiativeDao.create(create1);
        municipalityInitiativeDao.create(create2);

        List<MunicipalityInitiativeInfo> result = municipalityInitiativeDao.findAllNewestFirst();
        assertCreateAndGetDtos(create2, result.get(0));
        assertCreateAndGetDtos(create1, result.get(1));

    }

    private static MunicipalityInitiativeCreateDto createDto() {
        MunicipalityInitiativeCreateDto dto = new MunicipalityInitiativeCreateDto();

        dto.name = "name"+randomString();
        dto.proposal = "proposal"+randomString();
        dto.municipalityId = 1L;

        dto.contactAddress = "address"+randomString();
        dto.contactName = "name"+randomString();
        dto.contactEmail = "email"+randomString();
        dto.contactPhone = "phone"+randomString();
        return dto;
    }

    private static String randomString() {
        return String.valueOf(new Random().nextLong());
    }

    private static void assertCreateAndGetDtos(MunicipalityInitiativeCreateDto create, MunicipalityInitiativeInfo get) {
        assertThat(get.proposal, is(create.proposal));
        assertThat(get.name, is(create.name));
        assertThat(get.contactName, is(create.contactName));
        assertThat(get.contactPhone, is(create.contactPhone));
        assertThat(get.contactEmail, is(create.contactEmail));
        assertThat(get.contactAddress, is(create.contactAddress));
//        assertThat(get.municipalityName, is("Akaa"));
    }
}
