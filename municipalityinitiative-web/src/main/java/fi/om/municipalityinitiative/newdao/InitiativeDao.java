package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIEditDto;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> find(InitiativeSearch search);

    Long create(InitiativeCreateDto dto);

    Initiative getById(Long id);

    Initiative getById(Long initiativeId, String givenManagementHash);

    void assignAuthor(Long municipalityInitiativeId, Long participantId, String authorEmail, String managementHash);

    void markAsSendedAndUpdateContactInfo(Long initiativeId, ContactInfo contactInfo);

    ContactInfo getContactInfo(Long initiativeId);

    InitiativeCounts getInitiativeCounts(Maybe<Long> municipality);

    Long prepareInitiative(Long municipalityId, String email, String managementHash);

    InitiativeUIEditDto getInitiativeForEdit(Long initiativeId);

    void updateInitiativeDraft(Long initiativeId, InitiativeUIEditDto editDto);

    Author getAuthorInformation(Long id, String testManagementHash);

    void setInitiativeAsReview(Long initiativeId, InitiativeType single);

    void acceptInitiativeByOm(Long initiativeId);
    
    void rejectInitiativeByOm(Long initiativeId);
}
