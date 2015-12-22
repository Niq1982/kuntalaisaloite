package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class YouthInitiativeServiceTest {

    @Resource
    private AuthorDao authorDao;

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
        testHelper.clearSentEmails();
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

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto).getInitiativeId();

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

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto).getInitiativeId();

        Initiative createdInitiative = testHelper.getInitiative(initiativeId);

        assertThat(createdInitiative.getParticipantCount(), is(1));
        assertThat(createdInitiative.getParticipantCountPublic(), is(1));

        List<NormalParticipant> normalAllParticipants = participantDao.findNormalAllParticipants(createdInitiative.getId(), 0, 10);
        assertThat(normalAllParticipants, hasSize(1));

        assertThat(normalAllParticipants.get(0).getEmail(), is(editDto.getContactInfo().getEmail()));
        assertThat(normalAllParticipants.get(0).getHomeMunicipality().get().getId(), is(editDto.getContactInfo().getMunicipality()));
        assertThat(normalAllParticipants.get(0).getName(), is(editDto.getContactInfo().getName()));
        assertThat(normalAllParticipants.get(0).getMembership(), is(Membership.none));
    }

    @Transactional
    @Test
    public void participant_membership_is_added() {
        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();
        editDto.getContactInfo().setMembership(Membership.community);

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto).getInitiativeId();

        Initiative createdInitiative = testHelper.getInitiative(initiativeId);

        assertThat(createdInitiative.getParticipantCount(), is(1));
        assertThat(createdInitiative.getParticipantCountPublic(), is(1));

        List<NormalParticipant> normalAllParticipants = participantDao.findNormalAllParticipants(createdInitiative.getId(), 0, 10);
        assertThat(normalAllParticipants, hasSize(1));
        assertThat(normalAllParticipants.get(0).getMembership(), is(Membership.community));
    }

    @Transactional
    @Test
    public void authorIsCreated() {
        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();

        Long initiativeId = youthInitiativeService.prepareYouthInitiative(editDto).getInitiativeId();

        Initiative createdInitiative = testHelper.getInitiative(initiativeId);

        List<NormalAuthor> normalAuthors = authorDao.findNormalAuthors(createdInitiative.getId());
        assertThat(normalAuthors, hasSize(1));
        assertThat(normalAuthors.get(0).getContactInfo().getEmail(), is(editDto.getContactInfo().getEmail()));
        assertThat(normalAuthors.get(0).getMunicipality().get().getId(), is(editDto.getContactInfo().getMunicipality()));
        assertThat(normalAuthors.get(0).getContactInfo().getName(), is(editDto.getContactInfo().getName()));
        assertThat(normalAuthors.get(0).getContactInfo().getPhone(), is(editDto.getContactInfo().getPhone()));
    }

    @Test
    public void generated_hash_is_returned_and_sent_via_email() {

        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();

        YouthInitiativeService.YouthInitiativeCreateResult result = youthInitiativeService.prepareYouthInitiative(editDto);

        assertThat(result.getManagementLink(), is(Urls.FI.loginAuthor(RandomHashGenerator.getPrevious())));

        EmailDto sentEmail = testHelper.getSingleQueuedEmail();

        assertThat(sentEmail.getSubject(), is("Olet saanut linkin kuntalaisaloitteen tekemiseen Kuntalaisaloite.fi-palvelussa"));
        assertThat(sentEmail.getRecipientsAsString(), is(editDto.getContactInfo().getEmail()));
        assertThat(sentEmail.getBodyHtml(), containsString(result.getManagementLink()));

    }

    @Test
    public void email_is_sent_in_swedish_if_swedish_locale() {

        YouthInitiativeCreateDto editDto = youthInitiativeCreateDto();
        editDto.setLocale("sv");

        YouthInitiativeService.YouthInitiativeCreateResult result = youthInitiativeService.prepareYouthInitiative(editDto);

        assertThat(result.getManagementLink(), is(Urls.SV.loginAuthor(RandomHashGenerator.getPrevious())));

        EmailDto sentEmail = testHelper.getSingleQueuedEmail();
        assertThat(sentEmail.getSubject(), is("Du har fått en länk för att skapa ett initiativ i webbtjänsten Invånarinitiativ.fi"));
    }


    private YouthInitiativeCreateDto youthInitiativeCreateDto() {
        YouthInitiativeCreateDto editDto = new YouthInitiativeCreateDto();

        editDto.setMunicipality(activeMunicipality);

        YouthInitiativeCreateDto.ContactInfo contactInfo = new YouthInitiativeCreateDto.ContactInfo();
        contactInfo.setName("testinimi");
        contactInfo.setEmail("testiemail");
        contactInfo.setPhone("1234567");
        contactInfo.setMunicipality(unactiveMunicipality);

        editDto.setContactInfo(contactInfo);
        editDto.setYouthInitiativeId(new Random().nextLong());
        editDto.setName("testialoite");
        editDto.setProposal("sisältö");
        editDto.setExtraInfo("lisätiedot");
        return editDto;
    }


}