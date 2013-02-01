package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> findNewestFirst(InitiativeSearch search);

    Long create(InitiativeCreateDto dto);

    InitiativeViewInfo getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);

    void markAsSended(Long initiativeId);

    ContactInfo getContactInfo(Long initiativeId);
}
