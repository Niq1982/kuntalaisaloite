package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
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
public class InitiativeServiceIntegrationTest {

    @Resource
    private InitiativeService service;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    @Resource
    TestHelper testHelper;

    private static MunicipalityInfo testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));
    }


    @Test
    public void createNotCollectable() {
        service.createMunicipalityInitiative(createDto(false), null);
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }
    
    @Test
    public void createCollectable() {
        service.createMunicipalityInitiative(createDto(true), null);
        assertThat(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative), is(1L));
    }    

    @Test
    public void all_fields_are_set_when_getting_municipalityInitiativeInfo() throws Exception {
        Long initiativeId = service.createMunicipalityInitiative(createDto(true), null);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void create_and_get() {
        InitiativeUICreateDto createDto = createDto(false);
        Long initiativeId = service.createMunicipalityInitiative(createDto, null);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);

        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getAuthorName(), is(createDto.getContactInfo().getName()));
        assertThat(initiative.getName(), is(createDto.getName()));
        assertThat(initiative.getProposal(), is(createDto.getProposal()));
        assertThat(initiative.isShowName(), is(createDto.getShowName()));
        assertThat(initiative.getMunicipality().getName(), is(testMunicipality.getName()));

        assertThat(initiative.getCreateTime(), is(notNullValue()));
        assertThat(initiative.getManagementHash().isPresent(), is(false));
        assertThat(initiative.isCollectable(), is(false));
        assertThat(initiative.getSentTime().isPresent(), is(true));

        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void creating_collectable_initiative_adds_hash() {
        InitiativeUICreateDto createDto = createDto(true);
        Long initiativeId = service.createMunicipalityInitiative(createDto, null);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);

        assertThat(initiative.getManagementHash().get(), is(notNullValue()));
        assertThat(initiative.getManagementHash().get(), is(RandomHashGenerator.getPrevious()));
        assertThat(initiative.isCollectable(), is(true));
    }

    @Test
    public void creating_collectable_initiative_leaves_sent_time_null() {
        InitiativeUICreateDto createDto = createDto(true);
        Long initiativeId = service.createMunicipalityInitiative(createDto, null);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);

        assertThat(initiative.getSentTime().isPresent(), is(false));
    }

    @Test
    public void creating_not_collectable_initiative_sets_sent_time() {
        InitiativeUICreateDto createDto = createDto(false);
        Long initiativeId = service.createMunicipalityInitiative(createDto, null);
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI);

        assertThat(initiative.getSentTime().isPresent(), is(true));
    }

    @Test
    public void participating_to_non_collectable_initiative_is_forbidden() {
        Long initiative = testHelper.createTestInitiative(testMunicipality.getId());
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());
        try {
            service.createParticipant(participant, initiative);
            fail("Expected ParticipatingUnallowedException");
        } catch(ParticipatingUnallowedException e) {
            assertThat(e.getMessage(), containsString("Initiative not collectable"));
        }
    }

    @Test
    public void participating_to_sent_but_collectable_initiative_is_forbidden() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "initiative title", false, true);
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());

        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.sent, new DateTime());

        try {
            service.createParticipant(participant, initiativeId);
            fail("Expected ParticipatingUnallowedException");
        } catch(ParticipatingUnallowedException e) {
            assertThat(e.getMessage(), containsString("Initiative already sent"));
        }
    }

    @Test
    public void sending_collectable_initiative_to_municipality_saves_new_contact_information_to_initiative_and_marks_it_as_sended() {

        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Initiative name", true, true);

        SendToMunicipalityDto sendToMunicipalityDto = new SendToMunicipalityDto();
        sendToMunicipalityDto.setContactInfo(new ContactInfo());

        sendToMunicipalityDto.getContactInfo().setAddress("New Address");
        sendToMunicipalityDto.getContactInfo().setName("New Name");
        sendToMunicipalityDto.getContactInfo().setEmail("new_email@example.com");
        sendToMunicipalityDto.getContactInfo().setPhone("555");
        sendToMunicipalityDto.setManagementHash(TestHelper.TEST_MANAGEMENT_HASH);

        service.sendToMunicipality(initiativeId,sendToMunicipalityDto, null);

        ContactInfo newContactInfo = service.getContactInfo(initiativeId);
        assertThat(newContactInfo.getEmail(), is("new_email@example.com"));
        assertThat(newContactInfo.getName(), is("New Name"));
        assertThat(newContactInfo.getAddress(), is("New Address"));
        assertThat(newContactInfo.getPhone(), is("555"));
        // TODO: DO not use getInitiative either, implement needed functionality to testHelper.
        assertThat(service.getMunicipalityInitiative(initiativeId, Locales.LOCALE_FI).getSentTime().isPresent(), is(true));

    }

    public void succeeds_in_sending_to_municipality() {
        // TODO: Implement some test that ensures that emailservice is really used. (verifying by mock is ok)
    }

    @Test
    public void sets_participant_count_to_one_when_adding_new_collectable_initiative() {
        service.createMunicipalityInitiative(createDto(true), null);
        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(1L));
    }

    @Test
    public void leaves_participant_count_to_zero_when_adding_new_non_collectable_initiative() {
        service.createMunicipalityInitiative(createDto(false), null);
        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(0L));
    }

    @Test
    public void increases_participant_count_when_participating_to_collectable_initiative() {
        Long municipalityInitiative = service.createMunicipalityInitiative(createDto(true), null);

        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("name");
        participant.setHomeMunicipality(testMunicipality.getId());
        participant.setFranchise(true);
        participant.setShowName(true);
        service.createParticipant(participant, municipalityInitiative);

        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getParticipantCount(), is(2L));
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

        assertThat(service.findMunicipalityInitiatives(new InitiativeSearch()).get(0).getParticipantCount(), is(1L));

        Participant participant = participantDao.findAllParticipants(initiativeId).get(0);
        assertThat(participant.getHomeMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(participant.isFranchise(), is(true));
        assertThat(participant.getParticipateDate(), is(LocalDate.now()));
    }

    @Test
    public void preparing_initiative_saves_email() {
        PrepareInitiativeDto createDto = initiativePrepareDtoWithFranchise();
        createDto.setAuthorEmail("any@example.com");
        Long initiativeId = service.prepareInitiative(createDto, Locales.LOCALE_FI);

        // TODO: Change to getContactInfo etc.
        assertThat(service.getInitiativeForEdit(initiativeId, RandomHashGenerator.getPrevious()).getContactInfo().getEmail(), is(createDto.getAuthorEmail()));
    }

    @Test(expected = AccessDeniedException.class)
    public void get_initiative_for_edit_throws_exception_if_wrong_management_hash() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);
        service.getInitiativeForEdit(initiativeId, "some invalid management hash");
    }

    @Test(expected = AccessDeniedException.class)
    public void editing_initiative_throws_exception_if_wrong_management_hash() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        InitiativeUIEditDto editDto = new InitiativeUIEditDto(new Municipality(testMunicipality.getId(), testMunicipality.getName(), testMunicipality.getName()));
        editDto.setManagementHash("invalid management hash");

        service.updateInitiativeDraft(initiativeId, editDto);
    }

    @Test
    public void editing_initiative_updates_all_required_fields() {

        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        InitiativeUIEditDto editDto = new InitiativeUIEditDto(new Municipality(testMunicipality.getId(), testMunicipality.getName(), testMunicipality.getName()));
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

        service.updateInitiativeDraft(initiativeId, editDto);

        InitiativeUIEditDto updated = service.getInitiativeForEdit(initiativeId, RandomHashGenerator.getPrevious());

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getShowName(), is(editDto.getShowName()));

    }

    @Test
    public void get_initiative_for_edit_has_all_information() {
        Long initiativeId = service.prepareInitiative(initiativePrepareDtoWithFranchise(), Locales.LOCALE_FI);

        String managementHash = RandomHashGenerator.getPrevious();

        InitiativeUIEditDto initiativeForEdit = service.getInitiativeForEdit(initiativeId, managementHash);
        assertThat(initiativeForEdit.getManagementHash(), is(managementHash));
        assertThat(initiativeForEdit.getMunicipality().getId(), is(testMunicipality.getId()));
    }

    private static PrepareInitiativeDto initiativePrepareDtoWithFranchise() {
        PrepareInitiativeDto prepareInitiativeDto = new PrepareInitiativeDto();
        prepareInitiativeDto.setCollectable(true);
        prepareInitiativeDto.setFranchise(true);
        prepareInitiativeDto.setHomeMunicipality(testMunicipality.getId());
        prepareInitiativeDto.setMunicipality(testMunicipality.getId());
        return prepareInitiativeDto;
    }

    private InitiativeUICreateDto createDto(boolean collectable) {
        InitiativeUICreateDto createDto = new InitiativeUICreateDto();
        createDto.setProposal("Proposal " + randomString());
        createDto.setName("Name " + randomString());
        createDto.setFranchise(true);
        createDto.setShowName(true);
        createDto.setMunicipality(testMunicipality.getId());
        createDto.setHomeMunicipality(testMunicipality.getId());
        createDto.setMunicipalMembership(true);
        createDto.setCollectable(collectable);

        createDto.setContactInfo(new ContactInfo());
        createDto.getContactInfo().setAddress("contact address " + randomString());
        createDto.getContactInfo().setPhone("contact phone " + randomString());
        createDto.getContactInfo().setName("contact name " + randomString());
        createDto.getContactInfo().setEmail(randomString()+"@example.com");

        ReflectionTestUtils.assertNoNullFields(createDto);
        return createDto;
    }


    private static String randomString() {
        return String.valueOf(new Random().nextInt());
    }

}
