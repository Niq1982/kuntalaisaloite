package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.InitiativeEditDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> find(InitiativeSearch search);

    Long create(InitiativeCreateDto dto);

    Initiative getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);

    void markAsSendedAndUpdateContactInfo(Long initiativeId, ContactInfo contactInfo);

    ContactInfo getContactInfo(Long initiativeId);

    InitiativeCounts getInitiativeCounts(Maybe<Long> municipality);

    Long prepareInitiative(Long municipalityId, String managementHash);

    InitiativeEditDto getInitiativeForEdit(Long initiativeId);
}
