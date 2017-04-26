package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;

import java.util.List;

public interface MunicipalityDao {

    List<Municipality> findMunicipalities(boolean orderByFinnishNames);

    String getMunicipalityEmail(Long municipalityId);

    String getMunicipalityDescription(Long municipalityId);

    String getMunicipalityDescriptionSv(Long municipalityId);

    Municipality getMunicipality(Long id);

    void updateMunicipality(Long municipalityId, String email, boolean active, String descriptionFi, String descriptionSv);

    List<MunicipalityEditDto> findMunicipalitiesForEdit();
}
