package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.ComposerDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={NEWIntegrationTestConfiguration.class})
public class JdbcComposerDaoTest {

    @Resource
    ComposerDao composerDao;

    @Resource
    NEWTestHelper testHelper;

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void adds_new_composers() {

    }


}
