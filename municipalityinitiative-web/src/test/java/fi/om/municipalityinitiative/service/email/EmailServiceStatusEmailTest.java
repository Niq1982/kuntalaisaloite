package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Transactional
public class EmailServiceStatusEmailTest extends MailSendingEmailServiceTestBase {

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    @Test
    public void om_accept_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.ACCEPTED_BY_OM);

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Palvelun ylläpito on tarkastanut aloitteesi / Ditt invånarinitiativ har godkänts"));
        assertThat(email.getBodyHtml(), containsString(MODERATOR_COMMENT));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(managementHash())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void om_accept_initiative_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.ACCEPTED_BY_OM_AND_SENT);

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Kuntalaisaloitteesi on hyväksytty ja lähetetty kuntaan / Ditt invånarinitiativ har godkänts och skickats till kommunen"));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getBodyHtml(), containsString("Kuntalaisaloitteesi on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan"));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void om_reject_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.REJECTED_BY_OM);

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Kuntalaisaloitteesi on palautettu / Ditt invånarinitiativ har returnerats"));
        assertThat(email.getBodyHtml(), containsString(MODERATOR_COMMENT));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(managementHash())));
        assertThat(email.getBodyHtml(), containsString(urls.alt().loginAuthor(managementHash())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void author_publish_and_start_collecting_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.PUBLISHED_COLLECTING);

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Aloitteesi on julkaistu ja siihen kerätään osallistujia Kuntalaisaloite.fi-palvelussa / Ditt initiativ har publicerats och namninsamling pågår i webbtjänsten Invånarinitiativ.fi"));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        //assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(managementHash())));
        assertThat(email.getBodyHtml(), containsString(urls.alt().loginAuthor(managementHash())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void author_publish_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.SENT_TO_MUNICIPALITY);

        EmailDto email = testHelper.getSingleQueuedEmail();
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getSubject(), is("Aloitteesi on lähetetty kuntaan / Ditt initiativ har skickats till kommunen"));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void unsent_initiative_contains_managementLink() throws Exception {
        Long initiativeId = testHelper.createCollaborativeAccepted(getMunicipalityId());
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
        EmailDto email = testHelper.getSingleQueuedEmail();
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(managementHash())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }
    
    @Test
    public void status_emails_sent_to_authors_contains_separate_management_links() throws Exception {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(getMunicipalityId())
                .applyAuthor()
                .withParticipantEmail("aa@example.com")
                .toInitiativeDraft());
        String firstManagementHash = testHelper.getPreviousTestManagementHash();

        testHelper.createDefaultAuthorAndParticipant(new TestHelper.AuthorDraft(initiativeId, getMunicipalityId()).withParticipantEmail("bb@example.com"));
        String secondManagementHash = testHelper.getPreviousTestManagementHash();

        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);

        List<EmailDto> sentMessages = testHelper.findQueuedEmails();
        assertThat(sentMessages, hasSize(2));

        // TODO: Assert management-hash links - order may differ;

    }


}
