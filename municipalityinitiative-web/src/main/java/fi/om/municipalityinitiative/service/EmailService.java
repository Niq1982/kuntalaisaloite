package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;

import java.util.List;
import java.util.Locale;

public interface EmailService {

    void sendAuthorConfirmedInvitation(Initiative initiative, String email, String managementHash, Locale locale);

    void sendAuthorInvitation(Initiative initiative, AuthorInvitation authorInvitation);

    void sendSingleToMunicipality(Initiative initiative, List<Author> authors, String municipalityEmail, Locale locale);

    void sendCollaborativeToMunicipality(Initiative initiative, List<Author> authors, List<Participant> participants, String municipalityEmail, Locale locale);

    void sendStatusEmail(Initiative initiative, List<String> sendTo, String municipalityEmail, EmailMessageType emailMessageType);

    void sendAuthorDeletedEmailToOtherAuthors(Initiative initiative, List<String> sendTo, ContactInfo removedAuthorsContactInfo);

    void sendAuthorDeletedEmailToDeletedAuthor(Initiative initiative, String authorEmail);

    void sendNotificationToModerator(Initiative initiative, List<Author> authors, String TEMPORARILY_REPLACING_OM_EMAIL);

    void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale);

    void sendPrepareCreatedEmail(Initiative initiative, Long authorId, String managementHash, String authorEmail, Locale locale);

    void sendManagementHashRenewed(Initiative initiative, String managementHash, String authorEmail);

    void sendAuthorMessageConfirmationEmail(String contactEmail, String randomHash);

    void sendAuthorMessages(AuthorMessage authorMessage, List<Author> authors);
}