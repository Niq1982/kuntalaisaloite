package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class YouthInitiativeServiceTest {

    @Resource
    private YouthInitiativeService youthInitiativeService;

    @Resource
    private TestHelper testHelper;

    @Test(expected = AccessDeniedException.class)
    public void rejectsIfMunicipalityNotActive() {

        Long id = testHelper.createTestMunicipality(randomAlphabetic(10), false);
        YouthInitiativeCreateDto editDto = new YouthInitiativeCreateDto();

        editDto.setMunicipality(id);
        assertThat(youthInitiativeService.prepareYouthInitiative(editDto, -1L), is("ok"));
    }

}