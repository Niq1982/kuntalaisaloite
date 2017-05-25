package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeListWithCount;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InitiativeDao {

    InitiativeListWithCount findCached(InitiativeSearch search);

    Initiative get(Long initiativeId);

    InitiativeCounts getPublicInitiativeCounts(Optional<List<Long>> municipalities, InitiativeSearch.Type all);

    Long prepareInitiative(Long municipalityId, InitiativeType initiativeType);

    void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto);

    void updateExtraInfo(Long initiativeId, String extraInfo, Integer externalParticipantCount, String videoUrl);

    void updateInitiativeState(Long initiativeId, InitiativeState state);

    void updateInitiativeFixState(Long initiativeId, FixState fixState);

    void updateInitiativeType(Long initiativeId, InitiativeType initiativeType);

    void markInitiativeAsSent(Long initiativeId);

    void updateModeratorComment(Long initiativeId, String moderatorComment);

    void updateSentComment(Long initiativeId, String sentComment);

    void createInitiativeDecision(Long initiativeId, String decisionText);

    void updateInitiativeDecision(Long initiativeId, String decisionText);

    void updateInitiativeDecisionModifiedDate(Long initiativeId);

    InitiativeCounts getAllInitiativeCounts(Optional<List<Long>> municipalities, InitiativeSearch.Type initiativeTypeOptional);

    boolean isVerifiableInitiative(Long initiativeId);

    List<InitiativeListInfo> findInitiatives(VerifiedUserId verifiedUserId);

    void denormalizeParticipantCounts(Long initiativeId);

    InitiativeListWithCount findUnCached(InitiativeSearch search);

    List<Initiative> findAllByStateChangeBefore(InitiativeState accepted, LocalDate date);

    void markInitiativeReportSent(Long id, EmailReportType type, DateTime today);

    List<Initiative> findAllPublishedNotSent();

    Long prepareYouthInitiative(long youthInitiativeId, String name, String proposal, String extraInfo, Long municipality);

    List<Long> getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate date);

    Map<LocalDate,Long> getSupportVoteCountByDateUntil(Long initiativeId, LocalDate tillDay);

    void addVideoUrl(String url, Long initiativeId);

    List<InitiativeListInfo> findInitiativesByParticipation(VerifiedUserId authorId);
}
