package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Participant;

import java.util.List;
import java.util.Locale;

public interface EmailService {

    void sendAuthorConfirmedInvitation(Initiative initiative, String email, String managementHash, Locale locale);

    void sendAuthorInvitation(Initiative initiative, AuthorInvitation authorInvitation);

    void sendSingleToMunicipality(Initiative initiative, List<Author> authors, String municipalityEmail, Locale locale);

    void sendCollaborativeToMunicipality(Initiative initiative, List<Author> authors, List<Participant> participants, String municipalityEmail, Locale locale);

    void sendStatusEmail(Initiative initiative, List<String> sendTo, String municipalityEmail, EmailMessageType emailMessageType);

    void sendNotificationToModerator(Initiative initiative, List<Author> authors, Locale locale, String TEMPORARILY_REPLACING_OM_EMAIL);

    void sendParticipationConfirmation(Initiative initiative, String participantEmail, Long participantId, String confirmationCode, Locale locale);

    void sendPrepareCreatedEmail(Initiative initiative, Long authorId, String managementHash, String authorEmail, Locale locale);
}