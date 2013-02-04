package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.junit.Before;
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
public class MailSenderEmailServiceTest {

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
    public void assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeViewInfo initiative = createInitiativeInfo();
        emailService.sendToMunicipality(initiative, "some_test_address@example.com");
        assertThat(getSingleSentMessage().getAllRecipients().length, is(1));
        assertThat(getSingleSentMessage().getAllRecipients()[0].toString(), is("some_test_address@example.com"));
    }

    @Test
    public void reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeViewInfo initiative = createInitiativeInfo();
        emailService.sendToMunicipality(initiative, "some_test_address@example.com");
        assertThat(getSingleSentMessage().getSubject(), is("Uusi aloite"));
    }

    @Test
    public void adds_initiative_name_and_proposal_to_email_message() throws Exception {
        InitiativeViewInfo initiative = createInitiativeInfo();
        initiative.setName("Initiative name");
        initiative.setProposal("Initiative proposal");
        emailService.sendToMunicipality(initiative, "some_test_address@example.com");

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Initiative name"));
        assertThat(messageContent.html, containsString("Initiative name"));
        assertThat(messageContent.text, containsString("Initiative proposal"));
        assertThat(messageContent.text, containsString("Initiative proposal"));

    }

    private InitiativeViewInfo createInitiativeInfo() {
        InitiativeViewInfo initiativeViewInfo = new InitiativeViewInfo();
        initiativeViewInfo.setName("Some name whatever");
        initiativeViewInfo.setProposal("Some proposal whatever");
        return initiativeViewInfo;
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
