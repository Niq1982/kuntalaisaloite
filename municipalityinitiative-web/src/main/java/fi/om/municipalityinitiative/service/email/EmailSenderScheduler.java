package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Optional;

public class EmailSenderScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderScheduler.class);

    public static final int DELAY_BETWEEN_EMAILS_MILLIS = 100;

    public static final int FIXED_DELAY = 5000;

    @Resource
    private EmailSender emailSender;

    @Scheduled(fixedDelay = FIXED_DELAY)
    public void sendEmails() {
            try {
                Optional<EmailDto> lastSentEmail = Optional.empty();
                do {
                    if (lastSentEmail.isPresent()) {
                        Thread.sleep(DELAY_BETWEEN_EMAILS_MILLIS);
                    }
                    Optional<EmailDto> currentEmail = emailSender.popUntriedEmail();

                    if (currentEmail.isPresent()) {
                        try {
                            emailSender.constructAndSendEmail(currentEmail.get());
                            log.info("Sent email to " + currentEmail.get().getRecipientsAsString());
                            emailSender.succeed(currentEmail.get());
                        } catch (Throwable t) {
                            log.error("Error while sending email to " + currentEmail.get().getRecipientsAsString(), t);
                            emailSender.failed(currentEmail.get());
                        }
                    }
                    lastSentEmail = currentEmail;
                }
                while (lastSentEmail.isPresent());

            } catch (Throwable e) {
                log.error("Unknown error while popping and sending email", e);
            }
    }

}
