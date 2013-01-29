package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InitiativeDao {

    List<InitiativeListInfo> findNewestFirst(InitiativeSearch search);

    Long create(InitiativeCreateDto dto);

    InitiativeViewInfo getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);

    @Transactional(readOnly = false)
    void markAsSended(Long initiativeId);
}
