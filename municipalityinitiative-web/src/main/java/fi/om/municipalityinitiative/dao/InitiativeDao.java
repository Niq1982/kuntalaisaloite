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
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

public interface InitiativeDao {

    InitiativeListWithCount findCached(InitiativeSearch search);

    Initiative get(Long initiativeId);

    InitiativeCounts getPublicInitiativeCounts(Maybe<List<Long>> municipalities, InitiativeSearch.Type all);

    Long prepareInitiative(Long municipalityId);

    Long prepareVerifiedInitiative(Long municipalityId, InitiativeType initiativeType);

    void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto);

    void updateExtraInfo(Long initiativeId, String extraInfo, Integer externalParticipantCount);

    void updateInitiativeState(Long initiativeId, InitiativeState state);

    void updateInitiativeFixState(Long initiativeId, FixState fixState);

    void updateInitiativeType(Long initiativeId, InitiativeType initiativeType);

    void markInitiativeAsSent(Long initiativeId);

    void updateModeratorComment(Long initiativeId, String moderatorComment);

    void updateSentComment(Long initiativeId, String sentComment);

    void createInitiativeDecision(Long initiativeId, String decisionText);

    void updateInitiativeDecision(Long initiativeId, String decisionText);

    void updateInitiativeDecisionModifiedDate(Long initiativeId);

    InitiativeCounts getAllInitiativeCounts(Maybe<List<Long>> municipalities, InitiativeSearch.Type initiativeTypeMaybe);

    boolean isVerifiableInitiative(Long initiativeId);

    List<InitiativeListInfo> findInitiatives(VerifiedUserId verifiedUserId);

    void denormalizeParticipantCountForNormalInitiative(Long initiativeId);

    InitiativeListWithCount findUnCached(InitiativeSearch search);

    void denormalizeParticipantCountForVerifiedInitiative(Long initiativeId);

    List<Initiative> findAllByStateChangeBefore(InitiativeState accepted, LocalDate date);

    void markInitiativeReportSent(Long id, EmailReportType type, DateTime today);

    List<Initiative> findAllPublishedNotSent();

    Long prepareYouthInitiative(long youthInitiativeId, String name, String proposal, String extraInfo, Long municipality);

    List<Long> getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate date);

    Map<LocalDate,Long> getSupportVoteCountByDateUntil(Long initiativeId, LocalDate tillDay);

}
