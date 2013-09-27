package fi.om.municipalityinitiative.conf;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaMailSenderFake implements JavaMailSender {

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    private AtomicInteger sentMessages = new AtomicInteger(0);

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        sentMessages.addAndGet(1);

    }

    public int getSentMessages() {
        return sentMessages.get();
    }

    public void clearSentMessages() {
        sentMessages.set(0);
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

}
