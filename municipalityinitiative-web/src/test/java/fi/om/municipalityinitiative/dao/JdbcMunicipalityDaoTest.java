package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
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
        Long municipalityId = testHelper.createTestMunicipality("some test municipality", true);
        List<Municipality> result = municipalityDao.findMunicipalities(true);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getNameFi(), is("some test municipality"));
        assertThat(result.get(0).getNameSv(), is("some test municipality sv"));
        assertThat(result.get(0).isActive(), is(true));
        assertThat(result.get(0).getId(), is(municipalityId));
        ReflectionTestUtils.assertNoNullFields(result.get(0));
    }

    @Test
    public void municipalities_are_ordered_by_name() {
        List<Municipality> result = municipalityDao.findMunicipalities(true);

        String last = null;
        for (Municipality municipalityInfo : result) {
            if (municipalityInfo.getNameFi().toLowerCase().contains("å")) {
                continue; // Posgtre seems to think that ä is before å at the alphabets
            }

            if (last != null) {
                assertThat(municipalityInfo.getNameFi(), is(greaterThan(last)));
            }
            last = municipalityInfo.getNameFi();
        }
    }

    @Test
    public void get_municipality_email() {
        Long municipalityId = testHelper.createTestMunicipality("tuusula");

        String email = municipalityDao.getMunicipalityEmail(municipalityId);
        assertThat(email, is("tuusula@example.com"));
    }

    @Test
    public void get_municipality() {
        Long municipalityId = testHelper.createTestMunicipality("Tuusula");

        Municipality municipality = municipalityDao.getMunicipality(municipalityId);

        assertThat(municipality.getNameFi(), is("Tuusula"));
        assertThat(municipality.getNameSv(), is("Tuusula sv"));
        assertThat(municipality.getId(), is(municipalityId));
    }

    @Test
    public void update_municipality() {
        Long municipality = testHelper.createTestMunicipality("Tuusula", false);

        String updatedEmail = "updated_email@example.com";
        municipalityDao.updateMunicipality(municipality, updatedEmail, true);

        assertThat(municipalityDao.getMunicipality(municipality).isActive(), is(true));
        assertThat(municipalityDao.getMunicipalityEmail(municipality), is(updatedEmail));
    }

    @Test
    public void find_municipalities_for_edit() {

        Long municipality = testHelper.createTestMunicipality("testMunicipality", true);

        municipalityDao.updateMunicipality(municipality, "updated@example.com", false);

        List<MunicipalityEditDto> forEdit = municipalityDao.findMunicipalitiesForEdit();

        assertThat(forEdit, hasSize(1));
        assertThat(forEdit.get(0).getEmail(), is("updated@example.com"));
        assertThat(forEdit.get(0).getNameFi(), is("testMunicipality"));
        assertThat(forEdit.get(0).isActive(), is(false));
        ReflectionTestUtils.assertNoNullFields(forEdit.get(0));



    }
}
