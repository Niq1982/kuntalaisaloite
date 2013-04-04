package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.util.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Locale;

@Task
public class MailSendingEmailService implements EmailService {

    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collectable";
    private static final String COLLECTABLE_TEMPLATE = "municipality-collectable";

    @Resource
    private MessageSource messageSource;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    private static final Logger log = LoggerFactory.getLogger(MailSendingEmailService.class);

    @Override
    public void sendNotCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        emailMessageConstructor.fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .withSendTo(municipalityEmail)
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale))
                .withDataMap(setDataMap(emailInfo, locale))
                .send();
    }

    @Override
    public void sendNotCollectableToAuthor(InitiativeEmailInfo emailInfo, Locale locale) {

        MimeMessageHelper mimeMessageHelper = emailMessageConstructor.parseBasicEmailData(
                emailInfo.getContactInfo().getEmail(),
                messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale),
                NOT_COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));
        send(mimeMessageHelper.getMimeMessage());
    }

    @Override
    public void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        MimeMessageHelper mimeMessageHelper = emailMessageConstructor.parseBasicEmailData(emailInfo.getContactInfo().getEmail(), // XXX: Temporarily is set to same as authors email.
                messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale),
                COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));

        EmailMessageConstructor.addAttachment(mimeMessageHelper, emailInfo);

        send(mimeMessageHelper.getMimeMessage());
    }

    @Override
    public void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale) {

        MimeMessageHelper mimeMessageHelper = emailMessageConstructor.parseBasicEmailData(
                emailInfo.getContactInfo().getEmail(),
                messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale),
                COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));
        send(mimeMessageHelper.getMimeMessage());
    }

    private HashMap<String, Object> setDataMap(InitiativeEmailInfo emailInfo, Locale locale) {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("emailInfo", emailInfo);
        dataMap.put("localizations", new EmailLocalizationProvider(messageSource, locale));
        return dataMap;
    }

    private void send(MimeMessage message) {
        javaMailSender.send(message);
        log.info("Email sent.");
    }

    public static class EmailLocalizationProvider {
        private final Locale locale;
        private final MessageSource messageSource;

        public EmailLocalizationProvider(MessageSource messageSource, Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        public String getMessage(String key, Object ... args) {
            return messageSource.getMessage(key, args, locale);
        }
    }
}
