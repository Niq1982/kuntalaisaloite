package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;

public interface EmailService {

    void sendNotCollectableToMunicipality(InitiativeEmailInfo initiative, String municipalityEmail);

    void sendNotCollectableToAuthor(InitiativeEmailInfo initiativeEmailInfo);
}