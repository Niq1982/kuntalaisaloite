package fi.om.municipalityinitiative.service;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Task;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Task
public class MailSendingEmailService implements EmailService {

    private static final String INITIATIVE_PREPARE_VERIFICATION_TEMPLATE = "initiative-create-verification";
    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collectable";
    private static final String STATUS_INFO_TEMPLATE = "status-info-to-author";
    private static final String NOTIFICATION_TO_MODERATOR = "notification-to-moderator";
    private static final String PARTICIPATION_CONFIRMATION = "participant-verification";
    private static final String COLLABORATIVE_TO_MUNICIPALITY = "municipality-collectable";
    private static final String AUTHOR_INVITATION = "author-invitation";
    private static final String TEMP_INVITATION_ACCEPTANCE ="invitation-acceptance";

    @Resource
    private MessageSource messageSource;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    @Override
    public void sendAuthorConfirmedtInvitation(Initiative initiative, String email, String managementHash) {

        HashMap<String, Object> dataMap = toDataMap(initiative, Locales.LOCALE_FI);
        dataMap.put("managementHash", managementHash);

        emailMessageConstructor
                .fromTemplate(TEMP_INVITATION_ACCEPTANCE)
                .withSendTo(email)
                .withSubject(messageSource.getMessage("email.status.info." + EmailMessageType.INVITATION_ACCEPTED.name() + ".subject", toArray(), Locales.LOCALE_FI))
                .withDataMap(dataMap)
                .send();

    }

    @Override
    public void sendAuthorInvitation(Initiative initiative, AuthorInvitation authorInvitation) {
        Locale locale = Locales.LOCALE_FI;
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("authorInvitation", authorInvitation);

        emailMessageConstructor
                .fromTemplate(AUTHOR_INVITATION)
                .withSendTo(authorInvitation.getEmail())
                .withSubject(messageSource.getMessage("email.author.invitation.subject", toArray(initiative.getName()), locale))
                .withDataMap(dataMap)
                .send();
    }

    @Override
    public void sendSingleToMunicipality(Initiative initiative, String municipalityEmail, Locale locale) {
        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                //.withSendTo(municipalityEmail)
                .withSendTo(initiative.getAuthor().getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, locale))
                .send();
    }

    @Override
    public void sendCollaborativeToMunicipality(Initiative initiative, List<Participant> participants, String municipalityEmail, Locale locale) {
        emailMessageConstructor
                .fromTemplate(COLLABORATIVE_TO_MUNICIPALITY)
                // .withSendTo(municipalityEmail)
                .withSendTo(initiative.getAuthor().getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.not.collectable.municipality.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, locale))
                .withAttachment(initiative, participants)
                .send();
    }

    @Override
    public void sendStatusEmail(Initiative initiative, String sendTo, String municipalityEmail, EmailMessageType emailMessageType) {

        Locale locale = Locales.LOCALE_FI;

        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("emailMessageType", emailMessageType);
        if (municipalityEmail != null) { // XXX: Hmm. Shiiit.
            dataMap.put("municipalityEmail", municipalityEmail);
        }

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
    public void sendNotificationToModerator(Initiative initiative, Locale locale) {

        emailMessageConstructor
                .fromTemplate(NOTIFICATION_TO_MODERATOR)
                //.withSendToModerator()
                .withSendTo(initiative.getAuthor().getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.notification.to.moderator.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, locale))
                .send();
    }

    @Override
    public void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("participantId", participantId);
        dataMap.put("confirmationCode", confirmationCode);
        emailMessageConstructor
                .fromTemplate(PARTICIPATION_CONFIRMATION)
                .withSendTo(participantEmail)
                .withSubject(messageSource.getMessage("email.participation.confirmation.subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();
    }

    private static String[] toArray(String... name) {
        return name;
    }

    private <T> HashMap<String, Object> toDataMap(T emailModelObject, Locale locale) {
        HashMap<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("initiative", emailModelObject);
        dataMap.put("localizations", new EmailLocalizationProvider(messageSource, locale));
        dataMap.put("urls", Urls.get(locale));
        dataMap.put("locale", locale);
        dataMap.put("altLocale", Locales.getAltLocale(locale));
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

        public String getMessage(String key, Locale locale, Object ... args) {
            return messageSource.getMessage(key, args, locale);
        }

        public String getMessage(String key, Object ... args) {
            return messageSource.getMessage(key, args, locale);
        }
    }
}
