package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;

import java.util.Locale;

public interface EmailService {

    void sendNotCollectableToMunicipality(InitiativeEmailInfo initiative, String municipalityEmail, Locale locale);

    void sendNotCollectableToAuthor(InitiativeEmailInfo initiativeEmailInfo, Locale locale);

    void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale);

    void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale);
}