package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> find(InitiativeSearch search);

    Initiative getByIdWithOriginalAuthor(Long id);

    Initiative getById(Long initiativeId, String authorsManagementHash);

    void assignAuthor(Long municipalityInitiativeId, Long participantId, String managementHash);

    InitiativeCounts getInitiativeCounts(Maybe<Long> municipality);

    Long prepareInitiative(Long municipalityId);

    void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto);

    Author getAuthorInformation(Long id, String managementHash);

    void updateAcceptedInitiative(Long initiativeId, String managementHash, InitiativeUIUpdateDto updateDto);

    void updateInitiativeState(Long initiativeId, InitiativeState state);

    void updateInitiativeType(Long initiativeId, InitiativeType initiativeType);

    void markInitiativeAsSent(Long initiativeId);

    void updateModeratorComment(Long initiativeId, String moderatorComment);
}
