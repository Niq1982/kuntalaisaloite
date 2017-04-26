package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityInfoDto;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional(readOnly = false)
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

        String email = municipalityDao.getMunicipalityInfo(municipalityId).getEmail();
        assertThat(email, is("tuusula@example.com"));
    }

    @Test
    public void get_municipality_descriptions() {
        Long municipality = testHelper.createTestMunicipality("Tuusula", false, "Tuusula on hieno paikka", "Tuusula är en bra plats");
        String description = municipalityDao.getMunicipalityInfo(municipality).getDescription();
        String descriptionSv = municipalityDao.getMunicipalityInfo(municipality).getDescriptionSv();
        assertThat(description, is("Tuusula on hieno paikka"));
        assertThat(descriptionSv, is("Tuusula är en bra plats"));
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
        Long municipality = testHelper.createTestMunicipality("Tuusula", false, "Tuusula on hieno paikka", "Tuusula är en bra plats");

        String updatedEmail = "updated_email@example.com";
        String updatedDescription = "Tuusula on tyylikäs paikka";
        String updatedDescriptionSv = "Tuusula är en elegant plats";
        municipalityDao.updateMunicipality(municipality, updatedEmail, true, updatedDescription, updatedDescriptionSv);


        MunicipalityInfoDto dto = municipalityDao.getMunicipalityInfo(municipality);

        assertThat(dto.isActive(), is(true));
        assertThat(dto.getEmail(), is(updatedEmail));
        assertThat(dto.getDescription(), is(updatedDescription));
        assertThat(dto.getDescriptionSv(), is(updatedDescriptionSv));
    }

    @Test
    public void find_municipalities_for_edit() {

        Long municipality = testHelper.createTestMunicipality("testMunicipality", true);

        municipalityDao.updateMunicipality(municipality, "updated@example.com", false, "Tuusula on hieno paikka", "Tuusula är en bra plats");

        List<MunicipalityInfoDto> forEdit = municipalityDao.findMunicipalitiesForEdit();

        assertThat(forEdit, hasSize(1));
        assertThat(forEdit.get(0).getEmail(), is("updated@example.com"));
        assertThat(forEdit.get(0).getNameFi(), is("testMunicipality"));
        assertThat(forEdit.get(0).isActive(), is(false));
        assertThat(forEdit.get(0).getDescription(), is("Tuusula on hieno paikka"));
        assertThat(forEdit.get(0).getDescriptionSv(), is("Tuusula är en bra plats"));
        ReflectionTestUtils.assertNoNullFields(forEdit.get(0));
    }
}
