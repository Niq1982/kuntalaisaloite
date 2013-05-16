package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> find(InitiativeSearch search);

    Initiative getByIdWithOriginalAuthor(Long initiativeId);

    InitiativeCounts getInitiativeCounts(Maybe<Long> municipality);

    Long prepareInitiative(Long municipalityId);

    void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto);

    void updateExtraInfo(Long initiativeId, String extraInfo);

    void updateInitiativeState(Long initiativeId, InitiativeState state);

    void updateInitiativeFixState(Long initiativeId, FixState fixState);

    void updateInitiativeType(Long initiativeId, InitiativeType initiativeType);

    void markInitiativeAsSent(Long initiativeId);

    void updateModeratorComment(Long initiativeId, String moderatorComment);

    void updateSentComment(Long initiativeId, String sentComment);

}
