package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.List;

public class JavaMailSenderFake implements JavaMailSender {

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    private List<MimeMessage> sentMessages = Lists.newArrayList();

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        sentMessages.add(mimeMessage);
    }

    public List<MimeMessage> getSentMessages() {
        for (int i = 0; i < 50; ++i) {
            if (sentMessages.size() == 0)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            else
                return sentMessages;
        }
        throw new RuntimeException("Email was not sent in time");
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
}
