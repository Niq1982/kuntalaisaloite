package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class MailSendingEmailServiceTest {

    public static final String INITIATIVE_NAME = "Some name whatever";
    public static final String INITIATIVE_PROPOSAL = "Some proposal whatever";
    public static final String INITIATIVE_MUNICIPALITY = "Some municipality";
    public static final String CONTACT_PHONE = "Phone number";
    public static final String CONTACT_EMAIL = "sender.email@example.com";
    public static final String CONTACT_NAME = "Sender Name";
    public static final String CONTACT_ADDRESS = "Sender address";
    public static final String INITIATIVE_URL = "http://www.some.example.url.to.initiative";
    @Resource
    private EmailService emailService;

    // This replaces the JavaMailSender used by EmailService.
    // May be used for asserting "sent" emails.
    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    @Before
    public void setup() {
        javaMailSenderFake.getSentMessages().clear();

    }

    @Test
    public void send_straight_to_municipality_assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, "some_test_address@example.com");
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is("some_test_address@example.com"));
    }

    @Test
    public void send_straight_to_municipality_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, "some_test_address@example.com");
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
    }

    @Test
    public void send_straight_to_municipality_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, "some_test_address@example.com");

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void send_straight_to_municipality_uses_localizations_at_content() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiativeEmailInfo, "some@example.com");

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    @Ignore("Un-comment implementation")
    public void send_straight_to_municipality_assigns_senders_email_to_repllyTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, "some_test_address@example.com");
        assertThat(getSingleSentMessage().getReplyTo()[0].toString(), is(CONTACT_EMAIL));

    }

    @Test
    public void send_straight_to_author_assign_author_email_to_sendTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative);
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is(CONTACT_EMAIL));
    }

    @Test
    public void send_straight_to_author_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative);
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }

    @Test
    public void send_straight_to_author_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative);

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void send_straight_to_author_uses_localizations_at_content() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiativeEmailInfo);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    private void assertEmailHasInitiativeDetailsAndContactInfo() throws Exception {
        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString(INITIATIVE_NAME));
        assertThat(messageContent.html, containsString(INITIATIVE_NAME));
        assertThat(messageContent.text, containsString(INITIATIVE_PROPOSAL));
        assertThat(messageContent.text, containsString(INITIATIVE_PROPOSAL));
        assertThat(messageContent.html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(messageContent.text, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(messageContent.html, containsString(INITIATIVE_URL));
        assertThat(messageContent.text, containsString(INITIATIVE_URL));

        assertThat(messageContent.html, containsString(CONTACT_PHONE));
        assertThat(messageContent.text, containsString(CONTACT_PHONE));
        assertThat(messageContent.html, containsString(CONTACT_EMAIL));
        assertThat(messageContent.text, containsString(CONTACT_EMAIL));
        assertThat(messageContent.html, containsString(CONTACT_ADDRESS));
        assertThat(messageContent.text, containsString(CONTACT_ADDRESS));
        assertThat(messageContent.html, containsString(CONTACT_NAME));
        assertThat(messageContent.text, containsString(CONTACT_NAME));
    }

    private static InitiativeEmailInfo createEmailInfo() {
        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setName(INITIATIVE_NAME);
        initiativeViewInfo.setProposal(INITIATIVE_PROPOSAL);
        initiativeViewInfo.setMunicipalityName(INITIATIVE_MUNICIPALITY);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhone(CONTACT_PHONE);
        contactInfo.setEmail(CONTACT_EMAIL);
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setAddress(CONTACT_ADDRESS);

        return InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, INITIATIVE_URL);
    }

    // TODO: Examine this whole MimeMultipart - why is the data stored that deep in the MimeMultipart.
    private MessageContent getMessageContent() throws Exception {
        MimeMultipart singleSentMessage = (MimeMultipart) getSingleSentMessage().getContent();
        while (!(singleSentMessage.getBodyPart(0).getContent() instanceof String)) {
            assertThat(singleSentMessage.getCount(), is(1)); // This is not necessary, I'm just not clear how this content stuff works and why. Would like to know if count greater than 1 and why.
            singleSentMessage = (MimeMultipart) singleSentMessage.getBodyPart(0).getContent();
        }
        return new MessageContent(singleSentMessage);
    }

    private class MessageContent {
        public final String text;
        public final String html;

        private MessageContent(MimeMultipart mimeMultipart) throws Exception {
            this.text = mimeMultipart.getBodyPart(0).getContent().toString();
            this.html = mimeMultipart.getBodyPart(1).getContent().toString();
        }
    }

    private MimeMessage getSingleSentMessage() throws InterruptedException {
        waitUntilEmailSent();
        assertThat(javaMailSenderFake.getSentMessages(), hasSize(1));
        return javaMailSenderFake.getSentMessages().get(0);
    }

    private void waitUntilEmailSent() throws InterruptedException {
        for (int i = 0; i < 50; ++i) {
            if (javaMailSenderFake.getSentMessages().size() == 0)
                Thread.sleep(100);
            else
                return;
        }
        throw new RuntimeException("Email was not sent in time");
    }

}
