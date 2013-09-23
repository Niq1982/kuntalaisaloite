package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.conf.EnvironmentSettings;
import fi.om.municipalityinitiative.dao.EmailDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.pdf.ParticipantToPdfExporter;
import fi.om.municipalityinitiative.service.id.Id;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

public class EmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);


    @Resource
    private EmailDao emailDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private EnvironmentSettings environmentSettings;

    @Transactional(readOnly = false)
    public boolean sendNextEmail() throws InterruptedException {
        Maybe<EmailDto> maybeEmailDto = emailDao.getUntriedEmailForUpdate();
        if (maybeEmailDto.isPresent()) {
            EmailDto emailDto = maybeEmailDto.get();
            try {
                log.info("Sending email to " + emailDto.getRecipientsAsString() + " - " + emailDto.getSubject());
                constructAndSendEmail(emailDto);
                emailDao.succeed(emailDto.getEmailId());
            } catch (Throwable t) {
                log.error("Email sending failed: " + emailDto.getRecipientsAsString(), t);
                emailDao.failed(emailDto.getEmailId()); // If this fails, will throw an exception and the scheduled task is halted
            }
            return true;
        }
        return false;
    }

    private void constructAndSendEmail(EmailDto emailDto) throws MessagingException {

        if (environmentSettings.isTestConsoleOutput()) {
            System.out.println("----------------------------------------------------------");
            System.out.println("To: " + emailDto.getRecipientsAsString());
            System.out.println("Reply-to: " + environmentSettings.getDefaultReplyTo());
            System.out.println("Subject: " + emailDto.getSubject());
            System.out.println("---");
            System.out.println(emailDto.getBodyText());
            System.out.println("----------------------------------------------------------");
        }
        else {

            if (environmentSettings.getTestSendTo().isPresent()) {
                emailDto.setRecipients(Collections.singletonList(environmentSettings.getTestSendTo().get()));
            }

            MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true, "UTF-8");
            for (String to : emailDto.getRecipientsList()) {
                helper.addTo(to);
            }
            try {
                helper.setFrom(emailDto.getReplyTo(), emailDto.getSender());
            } catch (UnsupportedEncodingException e) {
                helper.setFrom(emailDto.getReplyTo());
            }
            helper.setReplyTo(emailDto.getReplyTo());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getBodyText(), emailDto.getBodyHtml());

            if (emailDto.getAttachmentType() == EmailAttachmentType.PARTICIPANTS) {
                Initiative initiative = initiativeDao.get(emailDto.getInitiativeId());
                List<? extends Participant<? extends Id>> participants = initiative.getType().isVerifiable() ?
                        participantDao.findVerifiedAllParticipants(initiative.getId(), 0, Integer.MAX_VALUE)
                        : participantDao.findNormalAllParticipants(initiative.getId(), 0, Integer.MAX_VALUE);

                addAttachment(helper, initiative, participants);
            }

            javaMailSender.send(helper.getMimeMessage());
        }

    }

    private static final String FILE_NAME = "Kuntalaisaloite_{0}_{1}_osallistujat.pdf";

    private static void addAttachment(MimeMessageHelper multipart, Initiative initiative, List<? extends Participant> participants) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            new ParticipantToPdfExporter(initiative, participants).createPdf(outputStream);

            byte[] bytes = outputStream.toByteArray();
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

            String fileName = MessageFormat.format(FILE_NAME, new LocalDate().toString("yyyy-MM-dd"), initiative.getId());

            multipart.addAttachment(fileName, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Package protected for test-usage
    void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
