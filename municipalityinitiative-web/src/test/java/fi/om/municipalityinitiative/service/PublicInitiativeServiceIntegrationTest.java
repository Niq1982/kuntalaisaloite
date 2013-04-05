package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class PublicInitiativeServiceIntegrationTest {

    @Resource
    private PublicInitiativeService service;

    @Resource
    private ParticipantDao participantDao; // Do not depend on this

    @Resource
    private InitiativeDao initiativeDao; // Do not depend on this

    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    @Resource
    TestHelper testHelper;

    @Resource
    IntegrationTestConfiguration.FakeUserService fakeUserService;

    private static MunicipalityInfo testMunicipality;

    private static MunicipalityInfo participantMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));

        participantMunicipality = new MunicipalityInfo();
        participantMunicipality.setName("Participant municipality");
        participantMunicipality.setId(testHelper.createTestMunicipality(participantMunicipality.getName()));

    }


    @Test
    public void all_fields_are_set_when_getting_municipalityInitiativeInfo() throws Exception {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Initiative name");
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void get() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some name");
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void participating_to_not_accepted_initiative_is_forbidden() {
        Long initiative = testHelper.createCollectableReview(testMunicipality.getId());

        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());
        try {
            service.createParticipant(participant, initiative);
            fail("Expected ParticipatingUnallowedException");
        } catch (ParticipatingUnallowedException e) {
            assertThat(e.getMessage(), containsString("Initiative not accepted by om:"));
        }
    }

    @Test
    public void participating_to_sent_but_collectable_initiative_is_forbidden() {
        Long initiativeId = testHelper.createCollectableAccepted(testMunicipality.getId());
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());

        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.sent, new DateTime());

        try {
            service.createParticipant(participant, initiativeId);
            fail("Expected ParticipatingUnallowedException");
        } catch (ParticipatingUnallowedException e) {
            assertThat(e.getMessage(), containsString("Initiative already sent"));
        }
    }

    public void succeeds_in_sending_to_municipality() {
        // TODO: Implement some test that ensures that emailservice is really used. (verifying by mock is ok)
    }

    @Test
    public void sets_participant_count_to_one_when_adding_new_collectable_initiative() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.state, InitiativeState.ACCEPTED);

        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(1L));
    }

    @Test
    public void increases_participant_count_when_participating_to_collectable_initiative() {
        Long municipalityInitiative = testHelper.createCollectableAccepted(testMunicipality.getId());

        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("name");
        participant.setHomeMunicipality(testMunicipality.getId());
        participant.setShowName(true);
        service.createParticipant(participant, municipalityInitiative);

        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(1L));
    }

    @Test
    public void preparing_initiative_sets_municipality() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);
        InitiativeViewInfo municipalityInitiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);

        assertThat(municipalityInitiative.getMunicipality().getId(), is(testMunicipality.getId()));
    }

    @Test
    // XXX: In the future initiative should not have the hash, author should.
    public void preparing_initiative_creates_hash() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);
        InitiativeViewInfo municipalityInitiative = service.getMunicipalityInitiative(initiativeId, RandomHashGenerator.getPrevious(), Locales.LOCALE_FI);

        assertThat(municipalityInitiative.getManagementHash().get(), is(RandomHashGenerator.getPrevious()));
    }

    @Test
    public void preparing_initiative_sets_participant_information() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);
        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.state, InitiativeState.ACCEPTED);

        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(1L));

        Participant participant = participantDao.findAllParticipants(initiativeId).get(0);
        assertThat(participant.getHomeMunicipality().getId(), is(participantMunicipality.getId()));
        assertThat(participant.getParticipateDate(), is(LocalDate.now()));
    }

    @Test
    public void preparing_initiative_saves_email() {
        PrepareInitiativeDto createDto = initiativePrepareDtoWithFranchise();
        createDto.setAuthorEmail("any@example.com");
        Long initiativeId = service.prepareInitiative(createDto, Locales.LOCALE_FI);

        // TODO: Change to getContactInfo etc.
        assertThat(service.getInitiativeDraftForEdit(initiativeId, RandomHashGenerator.getPrevious()).getContactInfo().getEmail(), is(createDto.getAuthorEmail()));
    }

    @Test(expected = AccessDeniedException.class)
    public void get_initiative_for_edit_throws_exception_if_wrong_management_hash() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);
        service.getInitiativeDraftForEdit(initiativeId, "some invalid management hash");
    }

    @Test(expected = AccessDeniedException.class)
    public void editing_initiative_throws_exception_if_wrong_management_hash() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto(new Municipality(testMunicipality.getId(), testMunicipality.getName(), testMunicipality.getName()), null);
        editDto.setManagementHash("invalid management hash");

        service.editInitiativeDraft(initiativeId, editDto);
    }

    @Test
    public void editing_initiative_updates_all_required_fields() {

        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto(new Municipality(testMunicipality.getId(), testMunicipality.getName(), testMunicipality.getName()), null);
        editDto.setManagementHash(RandomHashGenerator.getPrevious());

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("updated email");
        contactInfo.setAddress("updated address");
        contactInfo.setPhone("updated phone");
        contactInfo.setName("updated author name");
        editDto.setContactInfo(contactInfo);
        editDto.setName("updated initiative name");
        editDto.setProposal("updated proposal");
        editDto.setShowName(false); // As far as default is true ...
        editDto.setExtraInfo("updated extrainfo");

        service.editInitiativeDraft(initiativeId, editDto);

        InitiativeDraftUIEditDto updated = service.getInitiativeDraftForEdit(initiativeId, RandomHashGenerator.getPrevious());

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getShowName(), is(editDto.getShowName()));
        assertThat(updated.getExtraInfo(), is(editDto.getExtraInfo()));
        ReflectionTestUtils.assertNoNullFields(updated);

    }

    @Test
    public void get_initiative_for_update_sets_all_required_information() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality.getId());
        InitiativeUIUpdateDto initiativeForUpdate = service.getInitiativeForUpdate(initiativeId, TestHelper.TEST_MANAGEMENT_HASH);
        ReflectionTestUtils.assertNoNullFields(initiativeForUpdate);
    }

    @Test
    public void get_initiative_for_edit_has_all_information() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        String managementHash = RandomHashGenerator.getPrevious();

        InitiativeDraftUIEditDto initiativeForEdit = service.getInitiativeDraftForEdit(initiativeId, managementHash);
        assertThat(initiativeForEdit.getManagementHash(), is(managementHash));
        assertThat(initiativeForEdit.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiativeForEdit.getState(), is(InitiativeState.DRAFT));


        // Note that all fields are not set when preparing
    }

    @Test
    public void send_initiative_as_review_sents_state_as_review_and_leaves_type_as_null_if_not_single() {
        Long initiativeId = testHelper.createEmptyDraft(testMunicipality.getId());

        service.sendReview(initiativeId, TestHelper.TEST_MANAGEMENT_HASH, false);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType().isPresent(), is(false));
    }

    @Test
    public void send_initiative_as_review_sents_state_as_review_and_type_as_single_if_single() {
        Long initiativeId = testHelper.createEmptyDraft(testMunicipality.getId());
        service.sendReview(initiativeId, TestHelper.TEST_MANAGEMENT_HASH, true);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType().get(), is(InitiativeType.SINGLE));
    }




    private static PrepareInitiativeDto initiativePrepareDtoWithFranchise() {
        PrepareInitiativeDto prepareInitiativeDto = new PrepareInitiativeDto();
        prepareInitiativeDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeDto.setHomeMunicipality(participantMunicipality.getId());
        return prepareInitiativeDto;
    }

    private static PrepareInitiativeDto prepareDto() {
        PrepareInitiativeDto prepareInitiativeDto = new PrepareInitiativeDto();
        prepareInitiativeDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeDto.setAuthorEmail("authorEmail@example.com");
        return prepareInitiativeDto;
    }


    private static String randomString() {
        return String.valueOf(new Random().nextInt());
    }

}
