package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.AuthorMessage;

public interface AuthorMessageDao {

    void put(AuthorMessage authorMessage);

    AuthorMessage pop(String confirmationCode);

}
