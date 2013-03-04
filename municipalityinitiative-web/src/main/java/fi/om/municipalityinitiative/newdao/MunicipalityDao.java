package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.Municipality;

import java.util.List;

public interface MunicipalityDao {

    List<Municipality> findMunicipalities(boolean orderByFinnishNames);

    public String getMunicipalityEmail(Long municipalityId);
}
