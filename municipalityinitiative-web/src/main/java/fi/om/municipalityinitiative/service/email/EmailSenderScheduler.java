package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmailSenderScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderScheduler.class);

    public static final int DELAY_BETWEEN_EMAILS_MILLIS = 100;

    public static final int FIXED_DELAY = 5000;

    @Resource
    private EmailSender emailSender;

    private AtomicBoolean interrupted = new AtomicBoolean(false);

    @Scheduled(fixedDelay = FIXED_DELAY)
    public void sendEmails() {
            if (interrupted.get()) {
                return;
            }
            try {
                Maybe<EmailDto> lastSentEmail = Maybe.absent();
                do {
                    if (lastSentEmail.isPresent()) {
                        Thread.sleep(DELAY_BETWEEN_EMAILS_MILLIS);
                    }
                    Maybe<EmailDto> currentEmail = emailSender.popUntriedEmail();

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
                // emailSender.sendNextEmail() is responsible for checking errors and saving the
                // send-status of email to database. If if throws any exception in any case,
                // it was not able to save the sent-state to database.
                // Stop executing this thread immediately to avoid email-spamming.
//                stop();
                log.error("Unknown error while sending emails, task stopped", e);
            }
    }

    public void stop() {
        interrupted.set(true);
    }

    public void start() {
        interrupted.set(false);
    }

    public boolean isRunning() {
        return !interrupted.get();
    }

}
