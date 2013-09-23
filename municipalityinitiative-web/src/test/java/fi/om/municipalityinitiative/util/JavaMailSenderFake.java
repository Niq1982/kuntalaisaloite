package fi.om.municipalityinitiative.util;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

public class JavaMailSenderFake implements JavaMailSender {

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        // Do not send a thing.
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
