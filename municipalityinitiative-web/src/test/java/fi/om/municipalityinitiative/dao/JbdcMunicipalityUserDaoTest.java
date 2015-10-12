package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JbdcMunicipalityUserDaoTest {

    public static final String MANAGEMENT_HASH = "hashash";
    @Resource
    TestHelper testHelper;

    @Resource
    MunicipalityUserDao municipalityUserDao;

    private Long initiativeId;
    private Long municipalityId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        municipalityId = testHelper.createTestMunicipality("mun");
        initiativeId = createInitiative();
    }

    @Test
    public void add_municipality_user(){

        Long id = municipalityUserDao.getInitiativeId(MANAGEMENT_HASH);
        assertThat(initiativeId.equals(id), is(true));

    }

    private Long createInitiative() {
        return testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(municipalityId).applyAuthor().toInitiativeDraft());
    }
}
