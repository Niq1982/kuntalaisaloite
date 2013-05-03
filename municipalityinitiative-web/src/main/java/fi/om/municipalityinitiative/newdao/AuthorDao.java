package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;

public interface AuthorDao {

    void deleteAuthorInvitation(Long initiativeId, String confirmationCode);

    Long addAuthorInvitation(AuthorInvitation authorInvitation);

    AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode);
}
