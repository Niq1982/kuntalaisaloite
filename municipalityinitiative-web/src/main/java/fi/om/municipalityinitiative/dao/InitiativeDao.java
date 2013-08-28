package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeListWithCount;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;

public interface InitiativeDao {

    InitiativeListWithCount findCached(InitiativeSearch search);

    Initiative get(Long initiativeId);

    InitiativeCounts getPublicInitiativeCounts(Maybe<Long> municipality, InitiativeSearch.Type all);

    Long prepareInitiative(Long municipalityId);

    Long prepareSafeInitiative(Long municipalityId, InitiativeType initiativeType);

    void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto);

    void updateExtraInfo(Long initiativeId, String extraInfo, Integer externalParticipantCount);

    void updateInitiativeState(Long initiativeId, InitiativeState state);

    void updateInitiativeFixState(Long initiativeId, FixState fixState);

    void updateInitiativeType(Long initiativeId, InitiativeType initiativeType);

    void markInitiativeAsSent(Long initiativeId);

    void updateModeratorComment(Long initiativeId, String moderatorComment);

    void updateSentComment(Long initiativeId, String sentComment);

    InitiativeCounts getAllInitiativeCounts(Maybe<Long> municipality);

    boolean isVerifiableInitiative(Long initiativeId);

    List<InitiativeListInfo> findInitiatives(VerifiedUserId verifiedUserId);

    void denormalizeParticipantCountForNormalInitiative(Long initiativeId);

    InitiativeListWithCount findUnCached(InitiativeSearch search);
}
