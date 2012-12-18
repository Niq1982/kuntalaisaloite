package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.dml.SQLInsertClause;
import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

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
    public void dummyTest() {
        System.out.println("moi");
    }

    @Test
    public void testCreate() {
        MunicipalityInitiativeCreateDto dto = new MunicipalityInitiativeCreateDto();

        dto.name = "initiative name";
        dto.proposal = "initiative proposal";

        dto.contactAddress = "contact address";
        dto.contactName = "contact name";
        dto.contactEmail = "contact email";
        dto.contactPhone = "contact phone";

        municipalityInitiativeDao.create(dto);


    }
}
