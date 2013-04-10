package fi.om.municipalityinitiative.service;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.Task;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Task
public class MailSendingEmailService implements EmailService {

    private static final String INITIATIVE_PREPARE_VERIFICATION_TEMPLATE = "initiative-create-verification";
    private static final String INITIATIVE_CREATED_TEMPLATE = "initiative-created";
    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collectable";
    private static final String COLLECTABLE_TEMPLATE = "municipality-collectable";
    private static final String STATUS_INFO_TEMPLATE = "status-info-to-author";
    private static final String NOTIFICATION_TO_MODERATOR = "notification-to-moderator";

    @Resource
    private MessageSource messageSource;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    @Override
    public void sendNotCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .withSendTo(municipalityEmail)
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", toArray(emailInfo.getName()), locale))
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
                .withSubject(messageSource.getMessage("email.status.info." + emailMessageType.name() + ".subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();
        
    }

    @Override
    public void sendPrepareCreatedEmail(Initiative initiative, String authorEmail, Locale locale) {
        emailMessageConstructor
                .fromTemplate(INITIATIVE_PREPARE_VERIFICATION_TEMPLATE)
                .withSendTo(authorEmail)
                .withSubject(messageSource.getMessage("email.prepare.create.subject", toArray(), locale))
                .withDataMap(toDataMap(initiative, locale))
                .send();
    }

    @Override
    public void sendNotCollectableToAuthor(InitiativeEmailInfo emailInfo, Locale locale) {

        String name = emailInfo.getName();
        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.author.subject", toArray(name), locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .send();
    }

    private static String[] toArray(String... name) {
        return name;
    }

    @Override
    public void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {

        emailMessageConstructor
                .fromTemplate(COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", toArray(emailInfo.getName()), locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .withAttachment(emailInfo)
                .send();
    }

    @Override
    public void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale) {

        emailMessageConstructor
                .fromTemplate(COLLECTABLE_TEMPLATE)
                .withSendTo(emailInfo.getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.author.subject", toArray(emailInfo.getName()), locale))
                .withDataMap(toDataMap(emailInfo, locale))
                .send();
    }
    
    @Override
    public void sendNotificationToModerator(Initiative initiative, Locale locale) {

        emailMessageConstructor
                .fromTemplate(NOTIFICATION_TO_MODERATOR)
                //.withSendToModerator()
                .withSendTo(initiative.getAuthor().getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.notification.to.moderator.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, locale))
                .send();
    }

    private <T> HashMap<String, Object> toDataMap(T emailModelObject, Locale locale) {
        HashMap<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("initiative", emailModelObject);
        dataMap.put("localizations", new EmailLocalizationProvider(messageSource, locale));
        dataMap.put("urls", Urls.get(locale));
        dataMap.put("locale", locale);
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
