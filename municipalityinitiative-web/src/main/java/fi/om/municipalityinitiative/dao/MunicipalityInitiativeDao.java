package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;

import java.util.List;

public interface MunicipalityInitiativeDao {

    List<MunicipalityInitiativeInfo> findAllNewestFirst();

    Long create(MunicipalityInitiativeCreateDto dto);

    MunicipalityInitiativeInfo getById(Long createId);
}
