package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;

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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

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

    private static Municipality testMunicipality;

    private static Municipality participantMunicipality;

    private static LoginUserHolder authorLoginUserHolder;

    private static LoginUserHolder unknownLoginUserHolder;

    static {
        authorLoginUserHolder = mock(LoginUserHolder.class);
        unknownLoginUserHolder = mock(LoginUserHolder.class);
        doThrow(new AccessDeniedException("Access denied")).when(unknownLoginUserHolder).assertManagementRightsForInitiative(anyLong());
    }

    @Before
    public void setup() {
        testHelper.dbCleanup();

        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName);

        municipalityName = "Participant municipality";
        participantMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName);

    }


    @Test
    public void all_fields_are_set_when_getting_municipalityInitiativeInfo() throws Exception {
        Long initiativeId = testHelper.createCollectableAccepted(testMunicipality.getId());
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);
        assertThat(initiative.getState(), is(InitiativeState.ACCEPTED));
        assertThat(initiative.getAuthorName(), is(TestHelper.DEFAULT_AUTHOR_NAME));
        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.isShowName(), is(true));
        assertThat(initiative.isCollectable(), is(true));
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void participating_allowance_is_checked() {
        Long initiative = testHelper.createCollectableReview(testMunicipality.getId());

        ParticipantUICreateDto participant = participantUICreateDto();
        service.createParticipant(participant, initiative, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void accepting_participation_allowance_is_checked() {
        testHelper.createSingleSent(testMunicipality.getId());
        service.confirmParticipation(testHelper.getLastParticipantId(), null);
    }

    @Test
    public void adding_participant_does_not_increase_denormalized_participantCount_but_accepting_does() {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        long originalParticipantCount = getSingleInitiativeInfo().getParticipantCount();

        Long participantId = service.createParticipant(participantUICreateDto(), initiativeId, null);
        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(originalParticipantCount));

        service.confirmParticipation(participantId, RandomHashGenerator.getPrevious());
        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(originalParticipantCount +1));
    }

    @Test
    public void sets_participant_count_to_one_when_adding_new_collaborative_initiative() {
        PrepareInitiativeUICreateDto prepareInitiativeUICreateDto = new PrepareInitiativeUICreateDto();
        prepareInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeUICreateDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeUICreateDto.setParticipantEmail("authorEmail@example.com");
        Long initiativeId = service.prepareInitiative(prepareInitiativeUICreateDto, Locales.LOCALE_FI);
        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.state, InitiativeState.PUBLISHED);

        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(1L));
    }

    private InitiativeListInfo getSingleInitiativeInfo() {
        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        precondition(initiatives, hasSize(1));
        return initiatives.get(0);
    }

    @Test
    public void preparing_initiative_sets_municipality() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        InitiativeViewInfo municipalityInitiative = service.getMunicipalityInitiative(initiativeId);

        assertThat(municipalityInitiative.getMunicipality().getId(), is(testMunicipality.getId()));
    }

    @Test
    public void preparing_initiative_creates_hash() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        stubAuthorLoginUserHolderWith(initiativeId);

        InitiativeViewInfo municipalityInitiative = service.getMunicipalityInitiative(initiativeId, authorLoginUserHolder);

        assertThat(municipalityInitiative.getManagementHash().get(), is(RandomHashGenerator.getPrevious()));
    }

    @Test
    public void preparing_initiative_sets_participant_information() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.state, InitiativeState.PUBLISHED); // XXX: Hard coded state change

        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(1L));

        Participant participant = participantDao.findAllParticipants(initiativeId).get(0);
        assertThat(participant.getHomeMunicipality().getId(), is(participantMunicipality.getId()));
        assertThat(participant.getParticipateDate(), is(LocalDate.now()));
    }

    @Test
    public void preparing_initiative_saves_email() {
        PrepareInitiativeUICreateDto createDto = prepareDto();
        createDto.setParticipantEmail("any@example.com");
        Long initiativeId = service.prepareInitiative(createDto, Locales.LOCALE_FI);

        // TODO: Change to getContactInfo etc.
        assertThat(service.getInitiativeDraftForEdit(initiativeId).getContactInfo().getEmail(), is(createDto.getParticipantEmail()));
    }

    @Test(expected = AccessDeniedException.class)
    public void editing_initiative_throws_exception_if_wrong_author() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(ReflectionTestUtils.modifyAllFields(new Initiative()));

        service.editInitiativeDraft(initiativeId, unknownLoginUserHolder, editDto);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void edit_initiative_fails_if_initiative_accepted() {
        Long collectableAccepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.editInitiativeDraft(collectableAccepted, authorLoginUserHolder, new InitiativeDraftUIEditDto());
    }

    @Test
    public void editing_initiative_updates_all_required_fields() {

        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(ReflectionTestUtils.modifyAllFields(new Initiative()));

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

        service.editInitiativeDraft(initiativeId, authorLoginUserHolder, editDto);

        InitiativeDraftUIEditDto updated = service.getInitiativeDraftForEdit(initiativeId);

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getShowName(), is(editDto.getShowName()));
        assertThat(updated.getExtraInfo(), is(editDto.getExtraInfo()));
        ReflectionTestUtils.assertNoNullFields(updated);

    }

    @Test(expected = OperationNotAllowedException.class)
    public void update_initiative_fails_if_initiative_sent() {
        Long sent = testHelper.createSingleSent(testMunicipality.getId());
        service.updateInitiative(sent, authorLoginUserHolder, new InitiativeUIUpdateDto());
    }

    @Test
    public void get_initiative_for_update_sets_all_required_information() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality.getId());
        stubAuthorLoginUserHolderWith(initiativeId);
        InitiativeUIUpdateDto initiativeForUpdate = service.getInitiativeForUpdate(initiativeId, authorLoginUserHolder);
        ReflectionTestUtils.assertNoNullFields(initiativeForUpdate);
    }

    private void stubAuthorLoginUserHolderWith(Long initiativeId) {
        stub(authorLoginUserHolder.getInitiative()).toReturn(Maybe.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId)));
    }

    @Test(expected = AccessDeniedException.class)
    public void get_initiative_for_update_fails_if_not_allowed() {
        Long initiativeId = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.getInitiativeForUpdate(initiativeId, unknownLoginUserHolder);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void get_initiative_for_update_fails_if_sent() {
        Long sent = testHelper.createSingleSent(testMunicipality.getId());
        service.getInitiativeForUpdate(sent, authorLoginUserHolder);
    }

    @Test
    public void preparing_initiative_sets_email_and_municipality() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        InitiativeDraftUIEditDto initiativeForEdit = service.getInitiativeDraftForEdit(initiativeId);
        assertThat(initiativeForEdit.getMunicipality().getId(), is(testMunicipality.getId())); // XXX: Remove?
        assertThat(initiativeForEdit.getState(), is(InitiativeState.DRAFT)); // XXX: Remove
        assertThat(initiativeForEdit.getContactInfo().getEmail(), is("authorEmail@example.com"));

        // Note that all fields are not set when preparing
    }

    @Test(expected = OperationNotAllowedException.class)
    public void get_initiative_for_edit_fails_if_initiative_accepted() {
        Long collectableAccepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.getInitiativeDraftForEdit(collectableAccepted);
    }

    @Test
    public void send_initiative_as_review_sents_state_as_review_and_leaves_type_as_null_if_not_single() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        service.sendReview(initiativeId, authorLoginUserHolder, false, Locales.LOCALE_FI);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.UNDEFINED));
    }

    @Test
    public void send_initiative_as_review_sents_state_as_review_and_type_as_single_if_single() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());
        service.sendReview(initiativeId, authorLoginUserHolder, true, Locales.LOCALE_FI);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.SINGLE));
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_review_fails_if_initiative_accepted() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.sendReview(accepted, authorLoginUserHolder, true, Locales.LOCALE_FI);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.sendReview(accepted, unknownLoginUserHolder, true, Locales.LOCALE_FI);

    }

    @Test(expected = OperationNotAllowedException.class)
    public void publish_initiative_fails_if_not_accepted() {
        Long review = testHelper.createCollectableReview(testMunicipality.getId());
        service.publishInitiative(review, false, authorLoginUserHolder, null);
    }

    @Test
    public void publish_initiative_and_start_collecting_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

        service.publishInitiative(accepted, true, authorLoginUserHolder, null);

        Initiative collecting = initiativeDao.getByIdWithOriginalAuthor(accepted);
        assertThat(collecting.getState(), is(InitiativeState.PUBLISHED));
        assertThat(collecting.getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(collecting.getSentTime().isPresent(), is(false));
    }

    @Test(expected = AccessDeniedException.class)
    public void publish_initiative_fails_if_not_author() {
       Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        service.publishInitiative(accepted, true, unknownLoginUserHolder, null);
    }

    @Test
    public void publish_initiative_and_send_to_municipality_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

        service.publishInitiative(accepted, false, authorLoginUserHolder, null);

        Initiative sent = initiativeDao.getByIdWithOriginalAuthor(accepted);
        assertThat(sent.getState(), is(InitiativeState.PUBLISHED));
        assertThat(sent.getType(), is(InitiativeType.SINGLE));
        assertThat(sent.getSentTime().isPresent(), is(true));
    }

    private static ParticipantUICreateDto participantUICreateDto() {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());
        return participant;
    }

    private static PrepareInitiativeUICreateDto prepareDto() {
        PrepareInitiativeUICreateDto prepareInitiativeUICreateDto = new PrepareInitiativeUICreateDto();
        prepareInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeUICreateDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeUICreateDto.setParticipantEmail("authorEmail@example.com");
        return prepareInitiativeUICreateDto;
    }


    private static String randomString() {
        return String.valueOf(new Random().nextInt());
    }

}
