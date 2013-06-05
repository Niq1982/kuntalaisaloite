package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;

import java.util.List;

public interface MunicipalityDao {

    List<Municipality> findMunicipalities(boolean orderByFinnishNames);

    String getMunicipalityEmail(Long municipalityId);

    Municipality getMunicipality(Long id);

    void updateMunicipality(Long municipalityId, String email, boolean active);

    List<MunicipalityEditDto> findMunicipalitiesForEdit();
}
