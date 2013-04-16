package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;

import java.util.Locale;

public interface EmailService {

    void sendNotCollectableToMunicipality(Initiative initiative, String municipalityEmail, Locale locale);

    void sendStatusEmail(Initiative initiative, String sendTo, EmailMessageType emailMessageType, Locale locale);

    void sendPrepareCreatedEmail(Initiative byIdWithOriginalAuthor, String authorEmail, Locale locale);

    void sendNotificationToModerator(Initiative initiative, Locale locale);

    void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale);
}