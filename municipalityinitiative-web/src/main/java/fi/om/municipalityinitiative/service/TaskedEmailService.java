package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.util.Task;

import java.util.Locale;

/**
 * This class makes it possible to use EmailServices functionality as tasks run in the background.
 */
@Task
public class TaskedEmailService extends EmailService {

    @Override
    public void sendAuthorMessages(Long initiativeId, AuthorMessage authorMessage) {
        super.sendAuthorMessages(initiativeId, authorMessage);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendAuthorMessageConfirmationEmail(Long initiativeId, AuthorMessage authorMessage, Locale locale) {
        super.sendAuthorMessageConfirmationEmail(initiativeId, authorMessage, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendParticipationConfirmation(Long initiativeId, String participantEmail, Long participantId, String confirmationCode, Locale locale) {
        super.sendParticipationConfirmation(initiativeId, participantEmail, participantId, confirmationCode, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendNotificationToModerator(Long initiativeId) {
        super.sendNotificationToModerator(initiativeId);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendManagementHashRenewed(Long initiativeId, String managementHash, Long authorId) {
        super.sendManagementHashRenewed(initiativeId, managementHash, authorId);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendPrepareCreatedEmail(Long initiativeId, NormalAuthorId authorId, String managementHash, Locale locale) {
        super.sendPrepareCreatedEmail(initiativeId, authorId, managementHash, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendStatusEmail(Long initiativeId, EmailMessageType emailMessageType) {
        super.sendStatusEmail(initiativeId, emailMessageType);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendCollaborativeToAuthors(Long initiativeId) {
        super.sendCollaborativeToAuthors(initiativeId);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendCollaborativeToMunicipality(Long initiativeId, Locale locale) {
        super.sendCollaborativeToMunicipality(initiativeId, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendAuthorDeletedEmailToDeletedAuthor(Long initiativeId, String deletedAuthorEmail) {
        super.sendAuthorDeletedEmailToDeletedAuthor(initiativeId, deletedAuthorEmail);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendAuthorDeletedEmailToOtherAuthors(Long initiativeId, ContactInfo removedAuthorsContactInfo) {
        super.sendAuthorDeletedEmailToOtherAuthors(initiativeId, removedAuthorsContactInfo);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendSingleToMunicipality(Long initiativeId, Locale locale) {
        super.sendSingleToMunicipality(initiativeId, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendAuthorInvitation(Long initiativeId, AuthorInvitation authorInvitation) {
        super.sendAuthorInvitation(initiativeId, authorInvitation);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendAuthorConfirmedInvitation(Long initiativeId, String authorsEmail, String managementHash, Locale locale) {
        super.sendAuthorConfirmedInvitation(initiativeId, authorsEmail, managementHash, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void sendVeritiedInitiativeManagementLink(Long initiativeId, Locale locale) {
        super.sendVeritiedInitiativeManagementLink(initiativeId, locale);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
