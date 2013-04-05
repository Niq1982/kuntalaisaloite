package fi.om.municipalityinitiative.service;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.Task;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Task
public class MailSendingEmailService implements EmailService {

    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collectable";
    private static final String COLLECTABLE_TEMPLATE = "municipality-collectable";
    private static final String STATUS_INFO_TEMPLATE = "status-info-to-author";

    @Resource
    private MessageSource messageSource;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    @Override
    public void sendNotCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .withSendTo(municipalityEmail)
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .send();
    }
    
    @Override
    public void sendStatusEmail(Initiative initiative, String sendTo, EmailMessageType emailMessageType, Locale locale) {
        
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("emailMessageType", emailMessageType);
        
        emailMessageConstructor
            .fromTemplate(STATUS_INFO_TEMPLATE)
            .withSendTo(sendTo)
            .withSubject(messageSource.getMessage("email.status.info."+emailMessageType.name(), new String[]{}, locale))
            .withDataMap(dataMap)
            .send();
        
    }

    @Override
    public void sendNotCollectableToAuthor(InitiativeEmailInfo emailInfo, Locale locale) {

        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .send();
    }

    @Override
    public void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        emailMessageConstructor
                .fromTemplate(COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .withAttachment(emailInfo)
                .send();
    }

    @Override
    public void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale) {

        emailMessageConstructor
                .fromTemplate(COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .send();
    }

    private <T> HashMap<String, Object> toDataMap(T emailInfo, Locale locale) {
        HashMap<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("emailInfo", emailInfo);
        dataMap.put("localizations", new EmailLocalizationProvider(messageSource, locale));
        addEnum(EmailMessageType.class, dataMap);
        return dataMap;
    }

    private static <T extends Enum<?>> void addEnum(Class<T> enumType, Map<String, Object> dataMap) {
        Map<String, T> values = Maps.newHashMap();
        for (T value : enumType.getEnumConstants()) {
            values.put(value.name(), value);
        }
        dataMap.put(enumType.getSimpleName(), values);
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
