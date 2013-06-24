package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.MimeMessage;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

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

        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on hyväksytty / Ditt invånarinitiativ har godkänts"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(MODERATOR_COMMENT));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(managementHash())));
    }

    @Test
    public void om_accept_initiative_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on hyväksytty ja lähetetty kuntaan / Ditt invånarinitiativ har godkänts och skickats till kommunen"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.view(initiativeId())));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString("Kuntalaisaloitteesi on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(managementHash())));
        // TODO: assertThat(getMessageContent().html, containsString("SV Kuntalaisaloitteesi on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan"));
    }

    @Test
    public void om_reject_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.REJECTED_BY_OM);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on palautettu / Ditt invånarinitiativ har returnerats"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(MODERATOR_COMMENT));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(managementHash())));
    }

    @Test
    public void author_publish_and_start_collecting_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.PUBLISHED_COLLECTING);
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Aloitteesi on julkaistu ja siihen kerätään osallistujia Kuntalaisaloite.fi-palvelussa / Ditt initiativ har publicerats och namninsamling pågår i webbtjänsten Invånarinitiativ.fi"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        //assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.view(initiativeId())));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(managementHash())));
    }

    @Test
    public void author_publish_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(initiativeId(), EmailMessageType.SENT_TO_MUNICIPALITY);
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Aloitteesi on lähetetty kuntaan / Ditt initiativ har skickats till kommunen"));
    }

    @Test
    public void unsent_initiative_contains_managementLink() throws Exception {
        Long initiativeId = testHelper.createCollaborativeAccepted(getMunicipalityId());
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(managementHash())));
    }

    @Test
    public void sent_initiative_contains_no_managementLink() throws Exception {
        Long initiativeId = testHelper.createSingleSent(getMunicipalityId());
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
        assertThat(javaMailSenderFake.getMessageContent().html, not(containsString(urls.loginAuthor(managementHash()))));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.view(initiativeId)));
    }

    @Test
    public void status_emails_sent_to_authors_contains_separate_management_links() throws Exception {
        Long initiativeId = testHelper.createInitiative(new TestHelper.InitiativeDraft(getMunicipalityId())
                .applyAuthor()
                .withParticipantEmail("aa@example.com")
                .toInitiativeDraft());
        String firstManagementHash = testHelper.getPreviousTestManagementHash();

        testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiativeId, getMunicipalityId()).withParticipantEmail("bb@example.com"));
        String secondManagementHash = testHelper.getPreviousTestManagementHash();

        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);

        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages(2);
        assertThat(JavaMailSenderFake.getMessageContent(sentMessages.get(0)).html, containsString(urls.loginAuthor(firstManagementHash)));
        assertThat(JavaMailSenderFake.getMessageContent(sentMessages.get(1)).html, containsString(urls.loginAuthor(secondManagementHash)));

    }


}
