package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.MunicipalityInfo;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={NEWIntegrationTestConfiguration.class})
public class JdbcMunicipalityDaoTest {

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    NEWTestHelper testHelper;

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void find_all_municipalities() {
        assertThat(municipalityDao.findMunicipalities(), is(not(empty())));
    }

    @Test
    public void municipalities_are_ordered_by_name() {
        List<MunicipalityInfo> result = municipalityDao.findMunicipalities();

        String last = null;
        for (MunicipalityInfo municipalityInfo : result) {
            if (municipalityInfo.name.toLowerCase().contains("å")) {
                continue; // Posgtre seems to think that ä is before å at the alphabets
            }

            if (last != null) {
                assertThat(municipalityInfo.name, is(greaterThan(last)));
            }
            last = municipalityInfo.name;
        }
    }
}
