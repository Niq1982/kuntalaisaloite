package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityInfoDto;

import java.util.List;

public interface MunicipalityDao {

    List<Municipality> findMunicipalities(boolean orderByFinnishNames);

    MunicipalityInfoDto getMunicipalityInfo(Long municipality);

    Municipality getMunicipality(Long id);

    void updateMunicipality(Long municipalityId, String email, boolean active, String descriptionFi, String descriptionSv);

    List<MunicipalityInfoDto> findMunicipalitiesForEdit();

}
