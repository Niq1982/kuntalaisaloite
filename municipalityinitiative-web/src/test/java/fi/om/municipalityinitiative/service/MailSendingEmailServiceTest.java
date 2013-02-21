package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class MailSendingEmailServiceTest {

    public static final String INITIATIVE_NAME = "Some name whatever";
    public static final String INITIATIVE_PROPOSAL = "Some proposal whatever";
    public static final String INITIATIVE_MUNICIPALITY = "Some municipality";
    public static final long INITIATIVE_MUNICIPALITY_ID = 1L;
    public static final String CONTACT_PHONE = "Phone number";
    public static final String CONTACT_EMAIL = "sender.email@example.com";
    public static final String CONTACT_NAME = "Sender Name";
    public static final String CONTACT_ADDRESS = "Sender address";
    public static final String INITIATIVE_URL = "http://www.some.example.url.to.initiative";
    public static final String MUNICIPALITY_EMAIL = "some_test_address@example.com";
    private static final String COMMENT = "Some state comment";

    @Resource
    private EmailService emailService;

    // This replaces the JavaMailSender used by EmailService.
    // May be used for asserting "sent" emails.
    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        Thread.sleep(1000); // This is here to make sure old email-sending-tasks have sent their emails.
    }

    @Before
    public void setup(){
        javaMailSenderFake.clearSentMessages();
    }

    // Not Collectable

    @Test
    public void notCollectable_to_municipality_assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is(MUNICIPALITY_EMAIL));
    }

    @Test
    public void notCollectable_to_municipality_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
    }

    @Test
    public void notCollectable_to_municipality_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo(false);
    }

    @Test
    public void notCollectable_to_municipality_uses_localizations_at_content() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiativeEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent(false);
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    @Ignore("Un-comment implementation")
    public void notCollectable_to_municipality_assigns_senders_email_to_repllyTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getReplyTo()[0].toString(), is(CONTACT_EMAIL));

    }

    @Test
    public void notCollectable_to_author_assign_author_email_to_sendTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is(CONTACT_EMAIL));
    }

    @Test
    public void notCollectable_to_author_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }

    @Test
    public void notCollectable_to_author_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo(false);
    }

    @Test
    public void notCollectable_to_author_uses_finnish_localization() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent(false);
        assertThat(messageContent.html, containsString("Yhteystiedot"));
    }

    @Test
    public void notCollectable_to_author_uses_sv_localization_localization() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_SV);

        MessageContent messageContent = getMessageContent(false);
        assertThat(messageContent.html, containsString("Kontaktuppgifter"));
    }
    
    // Collectable

    @Test
    public void collectable_to_municipality_assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is(MUNICIPALITY_EMAIL));
    }

    @Test
    public void collectable_to_municipality_reads_subject_to_email() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
    }

    @Test
    public void collectable_to_municipality_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo(true);
    }

    @Test
    public void collectable_to_municipality_uses_localizations_at_content() throws Exception {
        CollectableInitiativeEmailInfo initiativeEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiativeEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent(true);
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    @Ignore("Un-comment implementation")
    public void collectable_to_municipality_assigns_senders_email_to_repllyTo_field() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getReplyTo()[0].toString(), is(CONTACT_EMAIL));

    }

    @Test
    public void collectable_to_author_assign_author_email_to_sendTo_field() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is(CONTACT_EMAIL));
    }

    @Test
    public void collectable_to_author_reads_subject_to_email() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }

    @Test
    public void collectable_to_author_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo(false);
    }

    @Test
    public void collectable_to_author_uses_localizations_at_content() throws Exception {
        CollectableInitiativeEmailInfo initiativeEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent(false);
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    public void collectable_to_municipality_adds_comment_to_email() throws Exception {
        CollectableInitiativeEmailInfo collectableEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(collectableEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent(true);
        assertThat(messageContent.html, containsString(COMMENT));
        assertThat(messageContent.text, containsString(COMMENT));
    }

    private void assertEmailHasInitiativeDetailsAndContactInfo(boolean isCollectable) throws Exception {
        MessageContent messageContent = getMessageContent(isCollectable);
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
        initiativeViewInfo.setMunicipality(new Municipality(INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY_ID));
        initiativeViewInfo.setCreateTime(new LocalDate(2013, 1, 1));
        initiativeViewInfo.setSentTime(Maybe.of(new LocalDate(2013, 1, 1)));

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhone(CONTACT_PHONE);
        contactInfo.setEmail(CONTACT_EMAIL);
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setAddress(CONTACT_ADDRESS);

        return InitiativeEmailInfo.parse(contactInfo, initiativeViewInfo, INITIATIVE_URL);
    }

    private static CollectableInitiativeEmailInfo createCollectableEmailInfo() {
        InitiativeEmailInfo emailInfo = createEmailInfo();
        return CollectableInitiativeEmailInfo.parse(emailInfo, COMMENT, Lists.<Participant>newArrayList());
    }

    // TODO: Examine this whole MimeMultipart - why is the data stored that deep in the MimeMultipart.
    private MessageContent getMessageContent(boolean hasEmail) throws Exception {
        MimeMultipart singleSentMessage = (MimeMultipart) getSingleSentMessage().getContent();
        while (!(singleSentMessage.getBodyPart(0).getContent() instanceof String)) {
//            if (hasEmail) {
//                assertThat(singleSentMessage.getCount(), is(2));
//            }
//            else {
//                assertThat(singleSentMessage.getCount(), is(1));
//            }
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
        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages();
        assertThat(sentMessages, hasSize(1));
        return sentMessages.get(0);
    }

}
