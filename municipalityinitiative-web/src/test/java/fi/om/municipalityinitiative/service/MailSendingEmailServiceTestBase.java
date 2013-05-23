package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class MailSendingEmailServiceTestBase {

    public static final String INITIATIVE_NAME = "Some name whatever";
    public static final String INITIATIVE_PROPOSAL = "Some proposal whatever";
    public static final String INITIATIVE_MUNICIPALITY = "Some municipality";
    public static final Long INITIATIVE_ID = 1L;
    public static final long INITIATIVE_MUNICIPALITY_ID = 2L;
    public static final long AUTHOR_ID = 3L;
    public static final String CONTACT_PHONE = "Phone number";
    public static final String CONTACT_EMAIL = "sender.email@example.com";
    public static final List<String> AUTHOR_EMAILS = Collections.singletonList(CONTACT_EMAIL);
    public static final String CONTACT_NAME = "Sender Name";
    public static final String CONTACT_ADDRESS = "Sender address";
    public static final String MUNICIPALITY_EMAIL = "some_test_address@example.com";
    public static final String EXTRA_INFO = "Some state comment";
    public static final String MODERATOR_COMMENT = "Some moderator comment";
    public static final String MANAGEMENT_HASH = "managementHash";
    public static final String SENT_COMMENT = "Some sent comment";

    @Resource
    protected EmailService emailService;

    // This replaces the JavaMailSender used by EmailService.
    // May be used for asserting "sent" emails.
    @Resource
    protected JavaMailSenderFake javaMailSenderFake;

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        Thread.sleep(1000); // This is here to make sure old email-sending-tasks have sent their emails.
    }

    protected static Initiative createDefaultInitiative() {
        Initiative initiative = new Initiative();
        initiative.setId(1L);
        initiative.setMunicipality(new Municipality(INITIATIVE_MUNICIPALITY_ID, INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY, false));

        initiative.setCreateTime(new LocalDate(2010, 1, 1));
        initiative.setProposal(INITIATIVE_PROPOSAL);
        initiative.setName(INITIATIVE_NAME);
        initiative.setExtraInfo(EXTRA_INFO);
        initiative.setModeratorComment(MODERATOR_COMMENT);
        initiative.setSentComment(SENT_COMMENT);

        return initiative;
    }

    protected static List<Author> defaultAuthors() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress(CONTACT_ADDRESS);
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setEmail(CONTACT_EMAIL);
        contactInfo.setPhone(CONTACT_PHONE);
        Author author = new Author();
        author.setContactInfo(contactInfo);
        author.setMunicipality(new Municipality(INITIATIVE_MUNICIPALITY_ID, INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY, true));
        return Collections.singletonList(author);
    }

    @Before
    public void setup(){
        javaMailSenderFake.clearSentMessages();
    }


    // TODO: Examine this whole MimeMultipart - why is the data stored that deep in the MimeMultipart.
    protected final MessageContent getMessageContent() throws Exception {
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

    public final static class MessageContent {
        public final String text;
        public final String html;

        private MessageContent(MimeMultipart mimeMultipart) throws Exception {
            this.text = mimeMultipart.getBodyPart(0).getContent().toString();
            this.html = mimeMultipart.getBodyPart(1).getContent().toString();
        }
    }

    protected final MimeMessage getSingleSentMessage() throws InterruptedException {
        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages();
        assertThat(sentMessages, hasSize(1));
        return sentMessages.get(0);
    }


    protected final String getSingleRecipient() throws MessagingException, InterruptedException {
        Address[] allRecipients = getSingleSentMessage().getAllRecipients();
        assertThat(allRecipients, arrayWithSize(1));
        return allRecipients[0].toString();
    }
}
