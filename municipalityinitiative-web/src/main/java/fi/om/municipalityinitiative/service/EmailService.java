package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;

import java.util.Locale;

public interface EmailService {

    void sendNotCollectableToMunicipality(InitiativeEmailInfo initiative, String municipalityEmail, Locale locale);

    void sendNotCollectableToAuthor(InitiativeEmailInfo initiativeEmailInfo, Locale locale);

    void sendCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale);

    void sendCollectableToAuthor(InitiativeEmailInfo emailInfo, Locale locale);
}