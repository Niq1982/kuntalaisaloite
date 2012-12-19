package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;

import java.util.List;

public interface MunicipalityInitiativeDao {

    List<MunicipalityInitiativeInfo> findAllNewestFirst();

    Long create(MunicipalityInitiativeCreateDto dto);

    MunicipalityInitiativeInfo getById(Long createId);
}
