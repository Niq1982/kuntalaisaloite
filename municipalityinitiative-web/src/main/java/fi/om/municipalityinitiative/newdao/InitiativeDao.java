package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> find(InitiativeSearch search);

    Long create(InitiativeCreateDto dto);

    InitiativeViewInfo getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);

    void markAsSendedAndUpdateContactInfo(Long initiativeId, ContactInfo contactInfo);

    ContactInfo getContactInfo(Long initiativeId);
}
