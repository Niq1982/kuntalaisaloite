package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.MunicipalityInfo;

import java.util.List;

public interface MunicipalityDao {

    List<MunicipalityInfo> findMunicipalities();
}
