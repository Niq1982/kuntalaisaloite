package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.service.AuthorMessage;

public interface AuthorMessageDao {

    Long addAuthorMessage(AuthorMessage authorMessage);

    AuthorMessage getAuthorMessage(String verificationCode);
}
