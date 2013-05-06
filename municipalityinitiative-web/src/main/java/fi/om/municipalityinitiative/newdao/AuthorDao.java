package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;

import java.util.List;

public interface AuthorDao {

    void deleteAuthorInvitation(Long initiativeId, String confirmationCode);

    Long addAuthorInvitation(AuthorInvitation authorInvitation);

    AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode);

    void rejectAuthorInvitation(Long initiativeId, String confirmationCode);

    List<AuthorInvitation> findInvitations(Long initiativeId);

    List<Author> findAuthors(Long initiativeId);

    void assignAuthor(Long initiativeId, Long authorId);

    Long createAuthor(Long initiativeId, Long participantId, String managementHash);
}
