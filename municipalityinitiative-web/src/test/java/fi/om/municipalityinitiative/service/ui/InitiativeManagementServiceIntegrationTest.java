package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailSubjectPropertyKeys;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class InitiativeManagementServiceIntegrationTest extends ServiceIntegrationTestBase {

    @Resource
    InitiativeManagementService service;

    @Resource
    TestHelper testHelper;

    private static Municipality testMunicipality;
    private String testMunicipalityEmail;

    @Override
    public void childSetup() {
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
        testMunicipalityEmail = TestHelper.toEmail(municipalityName);
    }

    @Test
    public void all_functions_require_om_rights() throws InvocationTargetException, IllegalAccessException {

        for (Method method : InitiativeManagementService.class.getDeclaredMethods()) {
            if (method.getModifiers() != 1) {
                continue;
            }
            System.out.println("Checking that method requires om rights: " + method.getName());
            Object[] parameters = new Object[method.getParameterTypes().length];
            parameters[1] = TestHelper.unknownLoginUserHolder;
            try {
                method.invoke(service, parameters);
                fail("Should have checked om-rights for user: " + method.getName());
            } catch (InvocationTargetException e) {
                Assert.assertThat(e.getCause(), instanceOf(AccessDeniedException.class));
            }
        }
    }

    @Test(expected = OperationNotAllowedException.class)
    public void get_initiative_for_edit_fails_if_initiative_accepted() {
        Long collaborativeAccepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.getInitiativeDraftForEdit(collaborativeAccepted, TestHelper.authorLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void editing_initiative_throws_exception_if_wrong_author() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(ReflectionTestUtils.modifyAllFields(new Initiative()), new ContactInfo());

        service.editInitiativeDraft(initiativeId, TestHelper.unknownLoginUserHolder, editDto);
    }

    @Test(expected = AccessDeniedException.class)
    public void getting_initiativeDraft_for_edit_throws_exception_if_not_allowed() {
        service.getInitiativeDraftForEdit(null, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void edit_initiative_fails_if_initiative_accepted() {
        Long collaborativeAccepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.editInitiativeDraft(collaborativeAccepted, TestHelper.authorLoginUserHolder, new InitiativeDraftUIEditDto());
    }

    @Test
    public void get_initiative_for_update_sets_all_required_information() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality.getId());
        InitiativeUIUpdateDto initiativeForUpdate = service.getInitiativeForUpdate(initiativeId, TestHelper.authorLoginUserHolder);
        ReflectionTestUtils.assertNoNullFields(initiativeForUpdate);
    }

    @Test(expected = AccessDeniedException.class)
    public void get_initiative_for_update_fails_if_not_allowed() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.getInitiativeForUpdate(initiativeId, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void get_initiative_for_update_fails_if_sent() {
        Long sent = testHelper.createSingleSent(testMunicipality.getId());
        service.getInitiativeForUpdate(sent, TestHelper.authorLoginUserHolder);
    }

    @Test
    public void editing_initiative_updates_all_required_fields() {

        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(
                ReflectionTestUtils.modifyAllFields(new Initiative()),
                ReflectionTestUtils.modifyAllFields(new ContactInfo())
        );

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("updated email");
        contactInfo.setAddress("updated address");
        contactInfo.setPhone("updated phone");
        contactInfo.setName("updated author name");
        contactInfo.setShowName(!contactInfo.isShowName()); // As far as default is true ...
        editDto.setContactInfo(contactInfo);
        editDto.setName("updated initiative name");
        editDto.setProposal("updated proposal");
        editDto.setExtraInfo("updated extrainfo");

        service.editInitiativeDraft(initiativeId, TestHelper.authorLoginUserHolder, editDto);

        InitiativeDraftUIEditDto updated = service.getInitiativeDraftForEdit(initiativeId, TestHelper.authorLoginUserHolder);

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getContactInfo().isShowName(), is(editDto.getContactInfo().isShowName()));
        assertThat(updated.getExtraInfo(), is(editDto.getExtraInfo()));
        ReflectionTestUtils.assertNoNullFields(updated);

    }

    @Test
    public void send_initiative_as_review_sents_state_as_review_and_leaves_type_as_null_if_not_single() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        service.sendReviewOnlyForAcceptance(initiativeId, TestHelper.authorLoginUserHolder);

        Initiative updated = testHelper.getInitiative(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.UNDEFINED));
    }

    @Test
    public void send_initiative_as_review_sends_emails_to_author_and_moderator() throws MessagingException {
        service.sendReviewOnlyForAcceptance(testHelper.createDraft(testMunicipality.getId()), TestHelper.authorLoginUserHolder);

        assertFirstSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX + EmailMessageType.SENT_TO_REVIEW.name()+".subject");
        assertSecondSentEmail(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM, EmailSubjectPropertyKeys.EMAIL_NOTIFICATION_TO_MODERATOR_SUBJECT);

    }


    @Test
    public void send_initiative_as_review_sets_state_as_review_and_type_as_single_if_single() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());
        service.sendReviewAndStraightToMunicipality(initiativeId, TestHelper.authorLoginUserHolder, null);

        Initiative updated = testHelper.getInitiative(initiativeId);

        assertThat(updated.getState(), is(InitiativeState.REVIEW));
        assertThat(updated.getType(), is(InitiativeType.SINGLE));
    }

    @Test
    public void send_initiative_as_review_and_straight_to_municipality_sends_emails_to_author_and_moderator() throws MessagingException {
        service.sendReviewAndStraightToMunicipality(testHelper.createDraft(testMunicipality.getId()), TestHelper.authorLoginUserHolder, null);
        assertFirstSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX + EmailMessageType.SENT_TO_REVIEW.name()+".subject");
        assertSecondSentEmail(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM, EmailSubjectPropertyKeys.EMAIL_NOTIFICATION_TO_MODERATOR_SUBJECT);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_review_and_to_municipality_fails_if_initiative_accepted() {
        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.sendReviewAndStraightToMunicipality(accepted, TestHelper.authorLoginUserHolder, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_review_not_single_fails_if_initiative_accepted() {
        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.sendReviewOnlyForAcceptance(accepted, TestHelper.authorLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_single_to_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.sendReviewAndStraightToMunicipality(accepted, TestHelper.unknownLoginUserHolder, null);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_to_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.sendReviewOnlyForAcceptance(accepted, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void send_fix_to_review_fails_if_no_right_to_initiative() {
        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        service.sendFixToReview(accepted, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void send_fix_to_review_fails_if_initiative_sent() {
        Long sent = testHelper.createSingleSent(testMunicipality.getId());
        service.sendFixToReview(sent, TestHelper.authorLoginUserHolder);
    }

    @Test
    public void send_fix_to_review_sets_fixState_as_review() {
        Long accepted = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.FIX)
                .applyAuthor()
                .toInitiativeDraft());

        precondition(testHelper.getInitiative(accepted).getFixState(), is(FixState.FIX));

        service.sendFixToReview(accepted, TestHelper.authorLoginUserHolder);

        assertThat(testHelper.getInitiative(accepted).getFixState(), is(FixState.REVIEW));
    }

    @Test
    public void send_fix_to_review_sends_emails_to_author_and_moderator() throws MessagingException {
        Long accepted = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.FIX)
                .applyAuthor()
                .toInitiativeDraft());

        service.sendFixToReview(accepted, TestHelper.authorLoginUserHolder);

        assertFirstSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX + EmailMessageType.SENT_FIX_TO_REVIEW.name()+".subject");
        assertSecondSentEmail(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM, EmailSubjectPropertyKeys.EMAIL_NOTIFICATION_TO_MODERATOR_SUBJECT);

    }

    @Test(expected = OperationNotAllowedException.class)
    public void publish_initiative_fails_if_not_accepted() {
        Long review = testHelper.createCollaborativeReview(testMunicipality.getId());
//        service.publishAcceptedInitiative(review, false, authorLoginUserHolder, null);
        service.publishAndStartCollecting(review, TestHelper.authorLoginUserHolder);
    }

    @Test
    public void publish_initiative_and_start_collecting_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

        service.publishAndStartCollecting(accepted, TestHelper.authorLoginUserHolder);

        Initiative collecting = testHelper.getInitiative(accepted);
        assertThat(collecting.getState(), is(InitiativeState.PUBLISHED));
        assertThat(collecting.getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(collecting.getSentTime().isPresent(), is(false));
    }

    @Test
    public void publish_initiative_and_start_collecting_sends_status_email_to_author() throws MessagingException, InterruptedException {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

        service.publishAndStartCollecting(accepted, TestHelper.authorLoginUserHolder);
        assertUniqueSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX+EmailMessageType.PUBLISHED_COLLECTING.name()+".subject");
    }

    @Test(expected = AccessDeniedException.class)
    public void publish_inititive_and_send_to_municipality_fails_of_not_author() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        service.sendToMunicipality(accepted, TestHelper.unknownLoginUserHolder, "", null);
    }

    @Test
    public void publish_initiative_and_send_to_municipality_sets_all_data() {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);

//        service.publishAcceptedInitiative(accepted, false, authorLoginUserHolder, null);
        service.sendToMunicipality(accepted, TestHelper.authorLoginUserHolder, "some sent comment", null);

        Initiative sent = testHelper.getInitiative(accepted);
        assertThat(sent.getState(), is(InitiativeState.PUBLISHED));
        assertThat(sent.getType(), is(InitiativeType.SINGLE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("some sent comment"));
    }

    @Test
    public void publish_initiative_and_send_to_municipality_sends_emails_to_municipality_and_author() throws MessagingException {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        service.sendToMunicipality(accepted, TestHelper.authorLoginUserHolder, "some sent comment", null);

        assertFirstSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX+EmailMessageType.SENT_TO_MUNICIPALITY.name()+".subject");
        assertSecondSentEmail(testMunicipalityEmail, EmailSubjectPropertyKeys.EMAIL_NOT_COLLABORATIVE_MUNICIPALITY_SUBJECT, TestHelper.DEFAULT_INITIATIVE_NAME);
    }

    @Test
    public void send_published_collaborative_to_municipality_sends_emails_to_municipality_and_author() throws MessagingException {
        Long accepted = testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        service.sendToMunicipality(accepted, TestHelper.authorLoginUserHolder, "some sent comment", null);

        assertFirstSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_COLLABORATIVE_AUTHOR_SUBJECT);
        assertSecondSentEmail(testMunicipalityEmail, EmailSubjectPropertyKeys.EMAIL_COLLABORATIVE_MUNICIPALITY_SUBJECT, TestHelper.DEFAULT_INITIATIVE_NAME);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void sending_collaborative_to_municipality_fails_if_already_sent() {
        Long collaborativeSent = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withSent(DateTime.now())
                .applyAuthor().toInitiativeDraft());

        service.sendToMunicipality(collaborativeSent, TestHelper.authorLoginUserHolder, "", null);
    }

    @Test(expected = AccessDeniedException.class)
    public void sending_collaborative_to_municipality_fails_if_no_rights_to_initiative() {
        Long collaborativeAccepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        service.sendToMunicipality(collaborativeAccepted, TestHelper.unknownLoginUserHolder, "", null);
    }

    @Test
    public void sending_collobarative_to_municipality_sets_sent_time_and_sent_comment() {
        Long collaborative = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());

        service.sendToMunicipality(collaborative, TestHelper.authorLoginUserHolder, "my sent comment", null);

        Initiative sent = testHelper.getInitiative(collaborative);
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("my sent comment"));
    }

    @Test
    public void sendToMunicipality_marks_initiative_as_sigle_if_not_marked_as_collaboratibe() {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        service.sendToMunicipality(initiativeId, TestHelper.authorLoginUserHolder, "comment for municipality", null);
        Initiative sent = testHelper.getInitiative(initiativeId);
        assertThat(sent.getType(), is(InitiativeType.SINGLE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("comment for municipality"));
    }

    @Test
    public void sendToMunicipality_marks_initiative_as_sent_if_marked_as_collaborative() {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        service.sendToMunicipality(initiativeId, TestHelper.authorLoginUserHolder, "comment for municipality", null);
        Initiative sent = testHelper.getInitiative(initiativeId);
        assertThat(sent.getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(sent.getSentTime().isPresent(), is(true));
        assertThat(sent.getSentComment(), is("comment for municipality"));
    }

    @Test
    public void update_initiative_updates_given_fields() {

        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality.getId());

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
        service.updateInitiative(initiativeId, TestHelper.authorLoginUserHolder, updateDto);

        assertThat(testHelper.getInitiative(initiativeId).getExtraInfo(), is(updateDto.getExtraInfo()));
        ContactInfo updatedContactInfo = service.getInitiativeForUpdate(initiativeId, TestHelper.authorLoginUserHolder).getContactInfo();
        ReflectionTestUtils.assertReflectionEquals(updatedContactInfo, contactInfo);

    }

    @Test(expected = OperationNotAllowedException.class)
    public void update_initiative_fails_if_initiative_sent() {
        Long sent = testHelper.createSingleSent(testMunicipality.getId());
        service.updateInitiative(sent, TestHelper.authorLoginUserHolder, new InitiativeUIUpdateDto());
    }
}
