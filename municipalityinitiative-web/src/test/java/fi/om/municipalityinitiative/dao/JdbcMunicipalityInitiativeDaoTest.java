package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

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
        MunicipalityInitiativeCreateDto dto = createDto();
        Long createId = municipalityInitiativeDao.create(dto);

        MunicipalityInitiativeInfo result = municipalityInitiativeDao.getById(createId);

        assertThat(result.proposal, is(dto.proposal));
        assertThat(result.name, is(dto.name));
        assertThat(result.contactName, is(dto.contactName));
        assertThat(result.contactPhone, is(dto.contactPhone));
        assertThat(result.contactEmail, is(dto.contactEmail));
        assertThat(result.contactAddress, is(dto.contactAddress));
    }



    private MunicipalityInitiativeCreateDto createDto() {
        MunicipalityInitiativeCreateDto dto = new MunicipalityInitiativeCreateDto();

        dto.name = "initiative name";
        dto.proposal = "initiative proposal";
        dto.municipalityId = 1L;

        dto.contactAddress = "contact address";
        dto.contactName = "contact name";
        dto.contactEmail = "contact email";
        dto.contactPhone = "contact phone";
        return dto;
    }
}
