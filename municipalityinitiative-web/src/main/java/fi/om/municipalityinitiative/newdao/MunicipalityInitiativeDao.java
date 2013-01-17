package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeSearch;

import java.util.List;

public interface MunicipalityInitiativeDao {

    List<MunicipalityInitiativeInfo> findNewestFirst(MunicipalityInitiativeSearch search);

    Long create(MunicipalityInitiativeCreateDto dto);

    MunicipalityInitiativeInfo getById(Long id);

    void assignAuthor(Long municipalityInitiativeId, Long participantId);
}
