package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.stub;


public class PublicInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase{

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

        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);

        assertThat(initiative.getManagementHash().get(), is(RandomHashGenerator.getPrevious()));
    }

    @Test
    public void preparing_initiative_sets_participant_information() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        assertThat(initiativeDao.getByIdWithOriginalAuthor(initiativeId).getParticipantCount(), is(1));

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
        contactInfo.setShowName(false); // As far as default is true ...
        editDto.setContactInfo(contactInfo);
        editDto.setName("updated initiative name");
        editDto.setProposal("updated proposal");
        editDto.setExtraInfo("updated extrainfo");

        service.editInitiativeDraft(initiativeId, authorLoginUserHolder, editDto);

        InitiativeDraftUIEditDto updated = service.getInitiativeDraftForEdit(initiativeId);

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getContactInfo().isShowName(), is(editDto.getContactInfo().isShowName()));
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

//        service.sendReview(initiativeId, authorLoginUserHolder, false, Locales.LOCALE_FI);
        service.sendReviewOnlyForAcceptance(initiativeId, authorLoginUserHolder, null);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.UNDEFINED));
    }

    @Test
    public void send_initiative_as_review_sets_state_as_review_and_type_as_single_if_single() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());
//        service.sendReview(initiativeId, authorLoginUserHolder, true, Locales.LOCALE_FI);
        service.sendReviewAndStraightToMunicipality(initiativeId, authorLoginUserHolder, null, null);

        Initiative updated = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.SINGLE));
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_review_and_to_municipality_fails_if_initiative_accepted() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
//        service.sendReview(accepted, authorLoginUserHolder, true, Locales.LOCALE_FI);
        service.sendReviewAndStraightToMunicipality(accepted, authorLoginUserHolder, null, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_review_not_single_fails_if_initiative_accepted() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.sendReviewOnlyForAcceptance(accepted, authorLoginUserHolder, null);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_single_to_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.sendReviewAndStraightToMunicipality(accepted, unknownLoginUserHolder, null, null);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_to_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.sendReviewOnlyForAcceptance(accepted, unknownLoginUserHolder, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void publish_initiative_fails_if_not_accepted() {
        Long review = testHelper.createCollectableReview(testMunicipality.getId());
//        service.publishAcceptedInitiative(review, false, authorLoginUserHolder, null);
        service.publishAndStartCollecting(review, authorLoginUserHolder, null);
    }

    @Test
    public void publish_initiative_and_start_collecting_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

//        service.publishAcceptedInitiative(accepted, true, authorLoginUserHolder, null);
        service.publishAndStartCollecting(accepted, authorLoginUserHolder, null);

        Initiative collecting = initiativeDao.getByIdWithOriginalAuthor(accepted);
        assertThat(collecting.getState(), is(InitiativeState.PUBLISHED));
        assertThat(collecting.getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(collecting.getSentTime().isPresent(), is(false));
    }

    @Test(expected = AccessDeniedException.class)
    public void publish_inititive_and_send_to_municipality_fails_of_not_author() {
       Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
//        service.publishAcceptedInitiative(accepted, true, unknownLoginUserHolder, null);
        service.publishAndSendToMunicipality(accepted, unknownLoginUserHolder, "", null);
    }

    @Test
    public void publish_initiative_and_send_to_municipality_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

//        service.publishAcceptedInitiative(accepted, false, authorLoginUserHolder, null);
        service.publishAndSendToMunicipality(accepted, authorLoginUserHolder, "some sent comment", null);

        Initiative sent = initiativeDao.getByIdWithOriginalAuthor(accepted);
        assertThat(sent.getState(), is(InitiativeState.PUBLISHED));
        assertThat(sent.getType(), is(InitiativeType.SINGLE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("some sent comment"));
    }

    @Test(expected = OperationNotAllowedException.class)
    public void sending_collaborative_to_municipality_fails_if_already_sent() {
        Long collaborativeSent = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withSent(DateTime.now()));

        service.sendCollaborativeToMunicipality(collaborativeSent, authorLoginUserHolder, "", null);
    }

    @Test(expected = AccessDeniedException.class)
    public void sending_collaborative_to_municipality_fails_if_no_rights_to_initiative() {
        Long collectableAccepted = testHelper.createCollectableAccepted(testMunicipality.getId());

        service.sendCollaborativeToMunicipality(collectableAccepted, unknownLoginUserHolder, "", null);
    }

    @Test
    public void sending_collobarative_to_municipality_sets_sent_time_and_sent_comment() {
        Long collaborative = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        service.sendCollaborativeToMunicipality(collaborative, authorLoginUserHolder, "my sent comment", null);

        Initiative sent = initiativeDao.getByIdWithOriginalAuthor(collaborative);
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("my sent comment"));
    }

    @Test
    public void sendToMunicipality_marks_initiative_as_sigle_if_not_marked_as_collaboratibe() {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        service.sendToMunicipality(initiativeId, authorLoginUserHolder, "comment for municipality", null);
        Initiative sent = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        assertThat(sent.getType(), is(InitiativeType.SINGLE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("comment for municipality"));
    }

    @Test
    public void sendToMunicipality_marks_initiative_as_sent_if_marked_as_collaborative() {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        service.sendToMunicipality(initiativeId, authorLoginUserHolder, "comment for municipality", null);
        Initiative sent = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        assertThat(sent.getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("comment for municipality"));
    }

    @Test
    public void update_initiative_updates_given_fields() {

        Long initiativeId = testHelper.createCollectableAccepted(testMunicipality.getId());

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        ContactInfo contactInfo = new ContactInfo();
        updateDto.setContactInfo(contactInfo);

        updateDto.setExtraInfo("Modified extra info");
        contactInfo.setName("Modified Name");
        contactInfo.setAddress("Modified Address");
        contactInfo.setPhone("Modified Phone");
        contactInfo.setEmail("Modified Email");
        contactInfo.setShowName(false);
        updateDto.setContactInfo(contactInfo);
        service.updateInitiative(initiativeId, authorLoginUserHolder, updateDto);

        Initiative updated = initiativeDao.getById(initiativeId, TestHelper.TEST_MANAGEMENT_HASH);
        Assert.assertThat(updated.getAuthor().getContactInfo().isShowName(), is(false));

        Author author = service.getAuthorInformation(initiativeId, authorLoginUserHolder);
        ReflectionTestUtils.assertReflectionEquals(author.getContactInfo(), contactInfo);

        // TODO: Assert extraInfo

    }

    private static ParticipantUICreateDto participantUICreateDto() {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());
        participant.setMunicipality(testMunicipality.getId());
        return participant;
    }

    private static PrepareInitiativeUICreateDto prepareDto() {
        PrepareInitiativeUICreateDto prepareInitiativeUICreateDto = new PrepareInitiativeUICreateDto();
        prepareInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeUICreateDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeUICreateDto.setParticipantEmail("authorEmail@example.com");
        return prepareInitiativeUICreateDto;
    }


}

