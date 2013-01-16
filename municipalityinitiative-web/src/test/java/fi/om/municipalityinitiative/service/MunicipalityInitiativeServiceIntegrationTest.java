package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.NEWTestHelper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={NEWIntegrationTestConfiguration.class})
public class MunicipalityInitiativeServiceIntegrationTest {

    @Resource
    private MunicipalityInitiativeService service;

    @Resource
    NEWTestHelper testHelper;

    public void createInitiative() {

    }

}
