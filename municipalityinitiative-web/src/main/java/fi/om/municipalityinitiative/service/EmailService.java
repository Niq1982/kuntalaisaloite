package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Participant;

import java.util.List;
import java.util.Locale;

public interface EmailService {

    void sendAuthorConfirmedtInvitation(Initiative initiative, String email, String managementHash);

    void sendAuthorInvitation(Initiative initiative, AuthorInvitation authorInvitation);

    void sendSingleToMunicipality(Initiative initiative, String municipalityEmail, Locale locale);

    void sendCollaborativeToMunicipality(Initiative initiative, List<Participant> participants, String municipalityEmail, Locale locale);

    void sendStatusEmail(Initiative initiative, String sendTo, String municipalityEmail, EmailMessageType emailMessageType);

    void sendPrepareCreatedEmail(Initiative byIdWithOriginalAuthor, String authorEmail, Locale locale);

    void sendNotificationToModerator(Initiative initiative, Locale locale);

    void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale);
}