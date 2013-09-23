package fi.om.municipalityinitiative.dto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmailSenderScheduler {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderScheduler.class);

    public static final int DELAY_BETWEEN_EMAILS_MILLIS = 300;

    @Resource
    private EmailSender emailSender;

    private AtomicBoolean interrupted = new AtomicBoolean(false);

    @Scheduled(fixedDelay = 5000)
    public void sendEmails() {
            if (interrupted.get()) {
                return;
            }
            try {
                boolean hadEmailForSending;
                do {
                    hadEmailForSending = emailSender.sendNextEmail();
                }
                while (hadEmailForSending);

            } catch (Throwable e) {
                stop();
                log.error("Unknown error while sending emails, task stopped", e);
            }
    }

    private void stop() {
        this.interrupted.set(true);
    }


}
