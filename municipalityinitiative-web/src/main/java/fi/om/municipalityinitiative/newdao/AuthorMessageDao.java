package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.service.AuthorMessage;

public interface AuthorMessageDao {

    Long put(AuthorMessage authorMessage);

    AuthorMessage pop(String confirmationCode);

}
