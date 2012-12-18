package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;

public interface MunicipalityInitiativeDao {

    QMunicipalityInitiative find();

    Long create(MunicipalityInitiativeCreateDto dto);

    MunicipalityInitiativeInfo getById(Long createId);
}
