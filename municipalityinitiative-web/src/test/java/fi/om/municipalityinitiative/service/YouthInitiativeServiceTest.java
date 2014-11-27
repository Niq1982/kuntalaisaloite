package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class YouthInitiativeServiceTest {

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private YouthInitiativeService youthInitiativeService;

    @Resource
    private TestHelper testHelper;
    private Long unactiveMunicipality;
    private Long activeMunicipality;

    @Before
    public void setup() {
        unactiveMunicipality = testHelper.createTestMunicipality(randomAlphabetic(10), false);
        activeMunicipality = testHelper.createTestMunicipality(randomAlphabetic(10), true);

    }

    @Test(expected = AccessDeniedException.class)
    public void rejectsIfMunicipalityNotActive() {

        YouthInitiativeCreateDto editDto = new YouthInitiativeCreateDto();
        editDto.setMunicipality(unactiveMunicipality);
        youthInitiativeService.prepareYouthInitiative(editDto);
    }

    @Test
    public void youthInitiativeIsCreated() {
        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto);

        Initiative createdInitiative = testHelper.getInitiative(initiativeId);

        assertThat(createdInitiative.getName(), is(editDto.getName()));
        assertThat(createdInitiative.getProposal(), is(editDto.getProposal()));
        assertThat(createdInitiative.getExtraInfo(), is(editDto.getExtraInfo()));
        assertThat(createdInitiative.getMunicipality().getId(), is(editDto.getMunicipality()));
        assertThat(createdInitiative.getYouthInitiativeId(), isPresent());
        assertThat(createdInitiative.getYouthInitiativeId().get(), is(editDto.getYouthInitiativeId()));
    }

    @Transactional
    @Test
    public void participantIsAddedWhenCreated() {
        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto);

        Initiative createdInitiative = testHelper.getInitiative(initiativeId);

        assertThat(createdInitiative.getParticipantCount(), is(1));
        assertThat(createdInitiative.getParticipantCountPublic(), is(1));

        List<NormalParticipant> normalAllParticipants = participantDao.findNormalAllParticipants(createdInitiative.getId(), 0, 10);
        assertThat(normalAllParticipants, hasSize(1));

        assertThat(normalAllParticipants.get(0).getEmail(), is(editDto.getContactInfo().getEmail()));
    }


    // TODO: Test that author is created

    // TODO: Test that generated has is returned

    // TODO: Test that email with admin-link is sent to author

    private YouthInitiativeCreateDto youthInitiativeCreateDto() {
        YouthInitiativeCreateDto editDto = new YouthInitiativeCreateDto();

        editDto.setMunicipality(activeMunicipality);

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
        return editDto;
    }


}