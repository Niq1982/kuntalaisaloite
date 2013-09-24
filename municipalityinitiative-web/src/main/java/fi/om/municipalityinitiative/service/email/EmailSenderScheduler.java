package fi.om.municipalityinitiative.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmailSenderScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderScheduler.class);

    public static final int DELAY_BETWEEN_EMAILS_MILLIS = 300;

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
                boolean hadEmailForSending = false;
                do {
                    if (hadEmailForSending) {
                        Thread.sleep(DELAY_BETWEEN_EMAILS_MILLIS);
                    }
                    hadEmailForSending = emailSender.sendNextEmail();
                }
                while (hadEmailForSending);

            } catch (Throwable e) {
                // emailSender.sendNextEmail() is responsible for checking errors and saving the
                // send-status of email to database. If if throws any exception in any case,
                // it was not able to save the sent-state to database.
                // Stop executing this thread immediately to avoid email-spamming.
                stop();
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
