package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;

public class JavaMailSenderFake implements JavaMailSender {

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    private List<MimeMessage> sentMessages = Lists.newArrayList();

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        sentMessages.add(mimeMessage);
    }

    public List<MimeMessage> getSentMessages() {
        return getSentMessages(1);
    }

    public List<MimeMessage> getSentMessages(int amountOfEmailsThatShouldHaveBeenSent) {
        for (int i = 0; i < 50; ++i) {

            if (sentMessages.size() > amountOfEmailsThatShouldHaveBeenSent) {
                throw new RuntimeException("Too many emails was sent. Expected: " + amountOfEmailsThatShouldHaveBeenSent + ", sent: " + sentMessages.size());
            }
            if (sentMessages.size() < amountOfEmailsThatShouldHaveBeenSent)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            else
                return sentMessages;
        }
        throw new RuntimeException("Email(s) was not sent in time");
    }

    // Not used:

    @Override
    public MimeMessage createMimeMessage() {
        return javaMailSender.createMimeMessage();
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void send(MimeMessage[] mimeMessages) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        throw new RuntimeException("Not implemented");
    }

    public void clearSentMessages() {
        sentMessages.clear();
    }

    public final MimeMessage getSingleSentMessage() throws InterruptedException {
        List<MimeMessage> sentMessages = getSentMessages();
        assertThat(sentMessages, hasSize(1));
        return sentMessages.get(0);
    }

    public final String getSingleRecipient() throws MessagingException, InterruptedException {
        return getSingleRecipient(getSingleSentMessage());
    }

    public static String getSingleRecipient(MimeMessage mimeMessage) throws MessagingException {
        Address[] allRecipients = mimeMessage.getAllRecipients();
        assertThat(allRecipients, arrayWithSize(1));
        return allRecipients[0].toString();
    }

    // TODO: Examine this whole MimeMultipart - why is the data stored that deep in the MimeMultipart.
    public final MessageContent getMessageContent() throws Exception {
        return getMessageContent(getSingleSentMessage());

    }

    public static MessageContent getMessageContent(MimeMessage mimeMessage) throws Exception {
        MimeMultipart singleSentMessage = (MimeMultipart) mimeMessage.getContent();
        while (!(singleSentMessage.getBodyPart(0).getContent() instanceof String)) {
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
}
