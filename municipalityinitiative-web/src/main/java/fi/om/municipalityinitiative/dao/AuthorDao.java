package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AuthorDao {

    void deleteAuthorInvitation(Long initiativeId, String confirmationCode);

    Long addAuthorInvitation(AuthorInvitation authorInvitation);

    AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode);

    void rejectAuthorInvitation(Long initiativeId, String confirmationCode);

    List<AuthorInvitation> findInvitations(Long initiativeId);

    List<NormalAuthor> findNormalAuthors(Long initiativeId);

    NormalAuthorId createAuthor(Long initiativeId, Long participantId, String managementHash);

    void updateAuthorInformation(Long authorId, ContactInfo contactInfo);

    List<String> findNormalAuthorEmails(Long initiativeId);

    Set<Long> getAuthorsInitiatives(String managementHash);

    Maybe<NormalAuthorId> getAuthorId(String managementHash);

    NormalAuthor getNormalAuthor(NormalAuthorId authorId);

    VerifiedAuthor getVerifiedAuthor(Long initiativeId, VerifiedUserId userId);

    void deleteAuthor(Long authorId);

    void updateManagementHash(NormalAuthorId authorId, String newManagementHash);

    Map<String,String> getManagementLinksByAuthorEmails(Long initiativeId);

    void addVerifiedAuthor(Long initiativeId, VerifiedUserId userId);

    List<VerifiedAuthor> findVerifiedAuthors(Long initiativeId);

    List<String> findVerifiedAuthorEmails(Long initiativeId);
}
