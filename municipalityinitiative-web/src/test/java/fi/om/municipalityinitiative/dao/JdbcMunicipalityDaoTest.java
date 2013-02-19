package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcMunicipalityDaoTest {

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    TestHelper testHelper;

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void find_all_municipalities() {
        testHelper.createTestMunicipality("some test municipality");
        List<MunicipalityInfo> result = municipalityDao.findMunicipalities();
        assertThat(result, is(not(empty())));
        assertThat(result.get(0).getName(), is("some test municipality"));
    }

    @Test
    public void municipalities_are_ordered_by_name() {
        List<MunicipalityInfo> result = municipalityDao.findMunicipalities();

        String last = null;
        for (MunicipalityInfo municipalityInfo : result) {
            if (municipalityInfo.getName().toLowerCase().contains("å")) {
                continue; // Posgtre seems to think that ä is before å at the alphabets
            }

            if (last != null) {
                assertThat(municipalityInfo.getName(), is(greaterThan(last)));
            }
            last = municipalityInfo.getName();
        }
    }

    @Test
    public void get_municipality_email() {
        Long municipalityId = testHelper.createTestMunicipality("tuusula");

        String email = municipalityDao.getMunicipalityEmail(municipalityId);
        assertThat(email, is("tuusula@example.com"));
    }
}
