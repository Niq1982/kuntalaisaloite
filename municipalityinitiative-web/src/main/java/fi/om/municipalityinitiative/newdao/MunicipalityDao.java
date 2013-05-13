package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityEditDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MunicipalityDao {

    List<Municipality> findMunicipalities(boolean orderByFinnishNames);

    String getMunicipalityEmail(Long municipalityId);

    Municipality getMunicipality(Long id);

    void updateMunicipality(Long municipalityId, String email, boolean active);

    List<MunicipalityEditDto> findMunicipalitiesForEdit();
}
