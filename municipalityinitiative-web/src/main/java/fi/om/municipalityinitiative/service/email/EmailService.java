package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.conf.EmailSettings;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmailService {

    private static final String INITIATIVE_PREPARE_VERIFICATION_TEMPLATE = "initiative-create-verification";
    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collaborative";
    private static final String STATUS_INFO_TEMPLATE = "status-info-to-author";
    private static final String NOTIFICATION_TO_MODERATOR = "notification-to-moderator";
    private static final String PARTICIPATION_CONFIRMATION = "participant-verification";
    private static final String COLLABORATIVE_TO_MUNICIPALITY = "municipality-collaborative";
    private static final String AUTHOR_INVITATION = "author-invitation";
    private static final String INVITATION_ACCEPTANCE ="invitation-acceptance";
    private static final String AUTHOR_DELETED_TO_OTHER_AUTHORS = "author-deleted-to-other-authors";
    private static final String AUTHOR_DELETED_TO_DELETED_AUTHOR = "author-deleted-to-deleted-author";
    private static final String MANAGEMENT_HASH_RENEWED = "managementhash-renewed";
    private static final String AUTHOR_MESSAGE_CONFIRMATION = "author-message-confirmation";
    private static final String AUTHOR_MESSAGE_TO_AUTHORS = "author-message-to-authors";

    @Resource
    EmailServiceDataProvider dataProvider;
    
    @Resource
    private MessageSource messageSource;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    @Resource
    private EmailSettings emailSettings;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendAuthorConfirmedInvitation(Long initiativeId, String authorsEmail, String managementHash, Locale locale) {

        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), locale);
        dataMap.put("managementHash", managementHash);

        emailMessageConstructor
                .fromTemplate(INVITATION_ACCEPTANCE)
                .addRecipient(authorsEmail)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_INVITATION_ACCEPTED_SUBJECT, toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }

    public void sendAuthorInvitation(Long initiativeId, AuthorInvitation authorInvitation) {
        Locale locale = Locales.LOCALE_FI;
        Initiative initiative = dataProvider.get(initiativeId);
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("authorInvitation", authorInvitation);

        emailMessageConstructor
                .fromTemplate(AUTHOR_INVITATION)
                .addRecipient(authorInvitation.getEmail())
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_INVITATION_SUBJECT, toArray(initiative.getName()), locale))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendSingleToMunicipality(Long initiativeId, Locale locale) {

        Initiative initiative = dataProvider.get(initiativeId);
        List<Author> authors = dataProvider.findAuthors(initiative.getId());
        String municipalityEmail = solveMunicipalityEmail(initiative);

        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .addRecipient(municipalityEmail)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_NOT_COLLABORATIVE_MUNICIPALITY_SUBJECT, toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, authors, locale))
                .send();
    }

    
    public void sendAuthorDeletedEmailToOtherAuthors(Long initiativeId, ContactInfo removedAuthorsContactInfo) {

        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), Locales.LOCALE_FI);
        dataMap.put("contactInfo", removedAuthorsContactInfo);

        emailMessageConstructor
                .fromTemplate(AUTHOR_DELETED_TO_OTHER_AUTHORS)
                .addRecipients(dataProvider.getAuthorEmails(initiativeId))
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_DELETED_TO_OTHER_AUTHORS_SUBJECT, toArray(), Locales.LOCALE_FI))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendAuthorDeletedEmailToDeletedAuthor(Long initiativeId, String deletedAuthorEmail) {

        emailMessageConstructor
                .fromTemplate(AUTHOR_DELETED_TO_DELETED_AUTHOR)
                .addRecipient(deletedAuthorEmail)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_DELETED_TO_DELETED_AUTHOR_SUBJECT, toArray(), Locales.LOCALE_FI))
                .withDataMap(toDataMap(dataProvider.get(initiativeId), Locales.LOCALE_FI))
                .send();
    }

    
    public void sendCollaborativeToMunicipality(Long initiativeId, Locale locale) {

        Initiative initiative = dataProvider.get(initiativeId);

        String municipalityEmail = solveMunicipalityEmail(initiative);

        emailMessageConstructor
                .fromTemplate(COLLABORATIVE_TO_MUNICIPALITY)
                .addRecipient(municipalityEmail)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_COLLABORATIVE_MUNICIPALITY_SUBJECT, toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, dataProvider.findAuthors(initiativeId), locale))
                .withAttachment(initiative, dataProvider.findAllParticipants(initiativeId))
                .send();
    }

    public void sendCollaborativeToAuthors(Long initiativeId) {
        Locale locale = Locales.LOCALE_FI;

        Initiative initiative = dataProvider.get(initiativeId);

        emailMessageConstructor.fromTemplate(COLLABORATIVE_TO_MUNICIPALITY)
                .addRecipients(dataProvider.getAuthorEmails(initiativeId))
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_COLLABORATIVE_AUTHOR_SUBJECT, toArray(), locale))
                .withDataMap(toDataMap(initiative, dataProvider.findAuthors(initiativeId), locale))
                .withAttachment(initiative, dataProvider.findAllParticipants(initiativeId))
                .send();
    }


    public void sendStatusEmail(Long initiativeId, EmailMessageType emailMessageType) {

        Locale locale = Locales.LOCALE_FI;

        Initiative initiative = dataProvider.get(initiativeId);


        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("emailMessageType", emailMessageType);
        dataMap.put("municipalityEmail", dataProvider.getMunicipalityEmail(initiative.getMunicipality().getId()));

        List<String> authorEmails = dataProvider.getAuthorEmails(initiativeId);
        emailMessageConstructor
                .fromTemplate(STATUS_INFO_TEMPLATE)
                .addRecipients(authorEmails)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_STATUS_INFO_PREFIX + emailMessageType.name() + ".subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }


    public void sendPrepareCreatedEmail(Long initiativeId, Long authorId, String managementHash, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), locale);
        dataMap.put("managementHash", managementHash);

        String email = dataProvider.getAuthor(authorId).getContactInfo().getEmail();
        emailMessageConstructor
                .fromTemplate(INITIATIVE_PREPARE_VERIFICATION_TEMPLATE)
                .addRecipient(email)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_PREPARE_CREATE_SUBJECT, toArray(), locale))
                .withDataMap(dataMap)
                .send();
    }


    public void sendManagementHashRenewed(Long initiativeId, String managementHash, Long authorId) {
        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), Locales.LOCALE_FI);
        dataMap.put("managementHash", managementHash);

        emailMessageConstructor
                .fromTemplate(MANAGEMENT_HASH_RENEWED)
                .addRecipient(dataProvider.getAuthor(authorId).getContactInfo().getEmail())
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_MANAGEMENTHASH_RENEWED_SUBJECT, toArray(), Locales.LOCALE_FI))
                .withDataMap(dataMap)
                .send();
    }


    public void sendNotificationToModerator(Long initiativeId) {

        Locale locale = Locales.LOCALE_FI;


        List<Author> authors = dataProvider.findAuthors(initiativeId);
        Initiative initiative = dataProvider.get(initiativeId);

        String TEMP_MODERATOR_EMAIL_CHANGE = authors.get(0).getContactInfo().getEmail();

        emailMessageConstructor
                .fromTemplate(NOTIFICATION_TO_MODERATOR)
//                .withSendToModerator()
                .addRecipient(solveModeratorEmail(authors.get(0).getContactInfo().getEmail()))
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_NOTIFICATION_TO_MODERATOR_SUBJECT, toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, authors, locale))
                .send();
    }

    public void sendParticipationConfirmation(Long initiativeId, String participantEmail, Long participantId, String confirmationCode, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), locale);
        dataMap.put("participantId", participantId);
        dataMap.put("confirmationCode", confirmationCode);
        emailMessageConstructor
                .fromTemplate(PARTICIPATION_CONFIRMATION)
                .addRecipient(participantEmail)
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_PARTICIPATION_CONFIRMATION_SUBJECT, toArray(), locale))
                .withDataMap(dataMap)
                .send();
    }


    public void sendAuthorMessageConfirmationEmail(Long initiativeId, AuthorMessage authorMessage, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), locale);
        dataMap.put("authorMessage", authorMessage);
        emailMessageConstructor
                .fromTemplate(AUTHOR_MESSAGE_CONFIRMATION)
                .addRecipient(authorMessage.getContactEmail())
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_MESSAGE_CONFIRMATION_SUBJECT, toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }


    public void sendAuthorMessages(Long initiativeId, AuthorMessage authorMessage) {
        Locale localeFi = Locales.LOCALE_FI;
        HashMap<String, Object> dataMap = toDataMap(dataProvider.get(initiativeId), localeFi);
        dataMap.put("authorMessage", authorMessage);
        emailMessageConstructor
                .fromTemplate(AUTHOR_MESSAGE_TO_AUTHORS)
                .addRecipients(dataProvider.getAuthorEmails(initiativeId))
                .withSubject(messageSource.getMessage(EmailSubjectPropertyKeys.EMAIL_AUTHOR_MESSAGE_TO_AUTHORS_SUBJECT, toArray(), localeFi))
                .withDataMap(dataMap)
                .send();
    }


    private String solveMunicipalityEmail(Initiative initiative) {
        if (emailSettings.isTestSendMunicipalityEmailsToAuthor()) {
            String alternativeEmail = dataProvider.findAuthors(initiative.getId()).get(0).getContactInfo().getEmail();
            log.warn("Test option replaced MUNICIPALITY email with: " + alternativeEmail);
            return alternativeEmail;
        }
        return dataProvider.getMunicipalityEmail(initiative.getMunicipality().getId());
    }


    private String solveModeratorEmail(String alternativeEmail) {
        if (emailSettings.isTestSendModeratorEmailsToAuthor()) {
            log.warn("Test option replaced OM email with: " + alternativeEmail);
            return alternativeEmail;
        }
        return emailSettings.getModeratorEmail();
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

    private Map<String, Object> toDataMap(Initiative initiative, List<Author> authors, Locale locale) {
        Map<String, Object> stringObjectMap = toDataMap(initiative, locale);
        stringObjectMap.put("authors", authors);
        return stringObjectMap;
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