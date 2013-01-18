package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeSearch;

import java.util.List;

public interface MunicipalityInitiativeDao {

    List<InitiativeListInfo> findNewestFirst(MunicipalityInitiativeSearch search);

    Long create(MunicipalityInitiativeCreateDto dto);

    InitiativeViewInfo getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);
}
