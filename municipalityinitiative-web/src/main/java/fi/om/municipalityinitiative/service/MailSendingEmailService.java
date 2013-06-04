package fi.om.municipalityinitiative.service;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
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
public class MailSendingEmailService {

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
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    private MessageSource messageSource;

    @Resource
    private EmailMessageConstructor emailMessageConstructor;

    public void sendAuthorConfirmedInvitation(Initiative initiative, String authorsEmail, String managementHash, Locale locale) {

        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("managementHash", managementHash);

        emailMessageConstructor
                .fromTemplate(INVITATION_ACCEPTANCE)
                .addRecipient(authorsEmail)
                .withSubject(messageSource.getMessage("email.author.invitation.accepted.subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }

    public void sendAuthorInvitation(Initiative initiative, AuthorInvitation authorInvitation) {
        Locale locale = Locales.LOCALE_FI;
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("authorInvitation", authorInvitation);

        emailMessageConstructor
                .fromTemplate(AUTHOR_INVITATION)
                .addRecipient(authorInvitation.getEmail())
                .withSubject(messageSource.getMessage("email.author.invitation.subject", toArray(initiative.getName()), locale))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendSingleToMunicipality(Initiative initiative, List<Author> authors, String municipalityEmail, Locale locale) {
        emailMessageConstructor
                .fromTemplate(NOT_COLLECTABLE_TEMPLATE)
                .addRecipient(municipalityDao.getMunicipalityEmail(initiativeDao.get(initiative.getId()).getMunicipality().getId()))
                .withSubject(messageSource.getMessage("email.not.collaborative.municipality.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiative, authorDao.findAuthors(initiative.getId()), locale))
                .send();
    }

    
    public void sendAuthorDeletedEmailToOtherAuthors(Initiative initiative, List<String> sendTo, ContactInfo removedAuthorsContactInfo) {

        HashMap<String, Object> dataMap = toDataMap(initiativeDao.get(initiative.getId()), Locales.LOCALE_FI);
        dataMap.put("contactInfo", removedAuthorsContactInfo);

        emailMessageConstructor
                .fromTemplate(AUTHOR_DELETED_TO_OTHER_AUTHORS)
                .addRecipients(authorDao.getAuthorEmails(initiative.getId()))
                .withSubject(messageSource.getMessage("email.author.deleted.to.other.authors.subject", toArray(), Locales.LOCALE_FI))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendAuthorDeletedEmailToDeletedAuthor(Initiative initiative, Long authorId) {

        emailMessageConstructor
                .fromTemplate(AUTHOR_DELETED_TO_DELETED_AUTHOR)
                .addRecipient(authorDao.getAuthor(authorId).getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.author.deleted.to.deleted.author.subject", toArray(), Locales.LOCALE_FI))
                .withDataMap(toDataMap(initiative, Locales.LOCALE_FI))
                .send();
    }

    
    public void sendCollaborativeToMunicipality(Initiative initiative, List<Author> authors, List<Participant> participants, String municipalityEmail, Locale locale) {

        Initiative initiativeeee = initiativeDao.get(initiative.getId());

        emailMessageConstructor
                .fromTemplate(COLLABORATIVE_TO_MUNICIPALITY)
                .addRecipient(municipalityDao.getMunicipalityEmail(initiativeeee.getMunicipality().getId()))
                .withSubject(messageSource.getMessage("email.collaborative.municipality.subject", toArray(initiative.getName()), locale))
                .withDataMap(toDataMap(initiativeeee, authorDao.findAuthors(initiative.getId()), locale))
                .withAttachment(initiativeeee, participantDao.findAllParticipants(initiative.getId()))
                .send();
    }

    
    public void sendCollaborativeToAuthors(Initiative initiative,
                                           List<Author> authors,
                                           List<Participant> participants,
                                           List<String> authorEmails) {
        Locale locale = Locales.LOCALE_FI;

        Initiative initiativeeee = initiativeDao.get(initiative.getId());

        emailMessageConstructor.fromTemplate(COLLABORATIVE_TO_MUNICIPALITY)
                .addRecipients(authorDao.getAuthorEmails(initiative.getId()))
                .withSubject(messageSource.getMessage("email.collaborative.author.subject", toArray(), locale))
                .withDataMap(toDataMap(initiativeeee, authorDao.findAuthors(initiative.getId()), locale))
                .withAttachment(initiativeeee, participantDao.findAllParticipants(initiative.getId()))
                .send();
    }

    
    public void sendStatusEmail(Initiative initiative, List<String> sendTo, String municipalityEmail, EmailMessageType emailMessageType) {

        Locale locale = Locales.LOCALE_FI;

        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("emailMessageType", emailMessageType);
        if (municipalityEmail != null) { // XXX: Hmm. Shiiit.
            dataMap.put("municipalityEmail", municipalityEmail);
        }

        emailMessageConstructor
                .fromTemplate(STATUS_INFO_TEMPLATE)
                .addRecipients(sendTo)
                .withSubject(messageSource.getMessage("email.status.info." + emailMessageType.name() + ".subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }

    
    public void sendPrepareCreatedEmail(Initiative initiative, Long authorId, String managementHash, String authorEmail, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(initiativeDao.get(initiative.getId()), locale);
        dataMap.put("managementHash", managementHash);

        String email = authorDao.getAuthor(authorId).getContactInfo().getEmail();
        emailMessageConstructor
                .fromTemplate(INITIATIVE_PREPARE_VERIFICATION_TEMPLATE)
                .addRecipient(email)
                .withSubject(messageSource.getMessage("email.prepare.create.subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendManagementHashRenewed(Initiative initiative, String managementHash, Long authorId) {
        HashMap<String, Object> dataMap = toDataMap(initiativeDao.get(initiative.getId()), Locales.LOCALE_FI);
        dataMap.put("managementHash", managementHash);

        emailMessageConstructor
                .fromTemplate(MANAGEMENT_HASH_RENEWED)
                .addRecipient(authorDao.getAuthor(authorId).getContactInfo().getEmail())
                .withSubject(messageSource.getMessage("email.managementhash.renewed.subject", toArray(), Locales.LOCALE_FI))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendNotificationToModerator(Initiative initiative, List<Author> authors, String TEMPORARILY_REPLACING_OM_EMAIL) {

        Locale locale = Locales.LOCALE_FI;


        List<Author> authorsss = authorDao.findAuthors(initiative.getId());
        Initiative initiativeeee = initiativeDao.get(initiative.getId());

        String TEMP_MODERATOR_EMAIL_CHANGE = authors.get(0).getContactInfo().getEmail();

        emailMessageConstructor
                .fromTemplate(NOTIFICATION_TO_MODERATOR)
                        //.withSendToModerator()
                .addRecipient(TEMP_MODERATOR_EMAIL_CHANGE)
                .withSubject(messageSource.getMessage("email.notification.to.moderator.subject", toArray(initiativeeee.getName()), locale))
                .withDataMap(toDataMap(initiativeeee, authorsss, locale))
                .send();
    }

    
    public void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("participantId", participantId);
        dataMap.put("confirmationCode", confirmationCode);
        emailMessageConstructor
                .fromTemplate(PARTICIPATION_CONFIRMATION)
                .addRecipient(participantEmail)
                .withSubject(messageSource.getMessage("email.participation.confirmation.subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();
    }

    
    public void sendAuthorMessageConfirmationEmail(Initiative initiative, AuthorMessage authorMessage, Locale locale) {
        HashMap<String, Object> dataMap = toDataMap(initiative, locale);
        dataMap.put("authorMessage", authorMessage);
        emailMessageConstructor
                .fromTemplate(AUTHOR_MESSAGE_CONFIRMATION)
                .addRecipient(authorMessage.getContactEmail())
                .withSubject(messageSource.getMessage("email.author.message.confirmation.subject", toArray(), locale))
                .withDataMap(dataMap)
                .send();

    }

    
    public void sendAuthorMessages(Initiative initiative, AuthorMessage authorMessage, List<String> authorEmails) {
        Locale localeFi = Locales.LOCALE_FI;
        HashMap<String, Object> dataMap = toDataMap(initiativeDao.get(initiative.getId()), localeFi);
        dataMap.put("authorMessage", authorMessage);
        emailMessageConstructor
                .fromTemplate(AUTHOR_MESSAGE_TO_AUTHORS)
                .addRecipients(authorDao.getAuthorEmails(initiative.getId()))
                .withSubject(messageSource.getMessage("email.author.message.to.authors.subject", toArray(), localeFi))
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
