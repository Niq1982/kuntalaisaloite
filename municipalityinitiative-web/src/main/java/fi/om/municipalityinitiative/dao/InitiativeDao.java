package fi.om.municipalityinitiative.dao;

import java.util.List;

import org.joda.time.LocalDate;

import fi.om.municipalityinitiative.dto.*;

public interface InitiativeDao {

    InitiativePublic getInitiativeForPublic(Long id);

    InitiativeManagement getInitiativeForManagement(Long id, boolean forUpdate);

    Long create(InitiativeManagement initiative, Long userId);

    void updateInitiative(InitiativeManagement initiative, Long userId, boolean basic, boolean extra);

    void updateInitiativeState(Long initiativeId, Long userId, InitiativeState state, String comment);

    void updateInitiativeStateAndAcceptanceIdentifier(Long initiativeId, Long userId, InitiativeState state, String comment, String acceptanceIdentifier);

    List<InitiativeInfo> findInitiatives(InitiativeSearch search, Long userId, int minSupportCount);

    void insertAuthor(Long initiativeId, Long userId, Author author);

    Author getAuthor(Long initiativeId, Long userId);

    void updateAuthor(Long initiativeId, Long userId, Author author);

    void updateInvitationSent(Long initiativeId, Long invitationId, String invitationCode);

    Invitation getOpenInvitation(Long initiativeId, String invitationCode, Integer invitationExpirationDays);

    void removeInvitation(Long initiativeId, String invitationCode);

    void updateLinks(Long initiativeId, List<Link> links);

    void updateInvitations(Long initiativeId, List<Invitation> initiatorInvitations, List<Invitation> representativeInvitations, List<Invitation> reserveInvitations);

    void removeInvitations(Long initiativeId);

    void clearConfirmations(Long initiativeId, Long userId);

    void confirmAuthor(Long initiativeId, Long userId);

    void deleteAuthor(Long initiativeId, Long userId);

    void updateConfirmationRequestSent(Long initiativeId, Long userId);

    List<String> getAuthorEmails(Long initiativeId);

    void removeUnconfirmedAuthors(Long initiativeId);

    void updateVRKResolution(Long initiativeId, int verifiedSupportCount, LocalDate verified, String verificationIdentifier, Long userId);

    InitiativeInfo lockInitiativeForUpdate(Long id);

    List<InitiativeInfo> findInitiativesWithUnremovedVotes();

    long getInitiativeCount();

    List<SchemaVersion> getSchemaVersions();
}
