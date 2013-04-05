package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;

import java.util.Locale;

public interface EmailService {

    void sendNotCollectableToAuthor(InitiativeEmailInfo initiativeEmailInfo, Locale locale);

    void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale);

    void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale);

    void sendStatusEmail(Initiative initiative, String sendTo, EmailMessageType emailMessageType, Locale locale);

    void sendNotCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale);
}