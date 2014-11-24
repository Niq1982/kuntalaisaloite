package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
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

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("testinimi");
        contactInfo.setAddress("testiosoite");
        contactInfo.setEmail("testiemail");
        contactInfo.setPhone("1234567");
        contactInfo.setShowName(true);

        editDto.setContactInfo(contactInfo);
        editDto.setYouthInitiativeId(-1L);
        editDto.setName("testialoite");
        editDto.setProposal("sisältö");
        editDto.setExtraInfo("lisätiedot");

        assertThat(youthInitiativeService.prepareYouthInitiative(editDto), is("ok"));
    }

}