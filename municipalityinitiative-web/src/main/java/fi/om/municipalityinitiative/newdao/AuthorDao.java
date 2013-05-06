package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;

import java.util.List;

public interface AuthorDao {

    void deleteAuthorInvitation(Long initiativeId, String confirmationCode);

    Long addAuthorInvitation(AuthorInvitation authorInvitation);

    AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode);

    void rejectAuthorInvitation(Long initiativeId, String confirmationCode);

    List<AuthorInvitation> findInvitations(Long initiativeId);

    List<Author> findAuthors(Long initiativeId);

    Long createAuthor(Long initiativeId, Long participantId, String managementHash);

    void assignAuthor(Long initiativeId, Long authorId);

    Author getAuthorInformation(Long initiativeId, String managementHash);

    void updateAuthorInformation(Long authorId, ContactInfo contactInfo);
}
