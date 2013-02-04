package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;

import java.util.List;

public interface MunicipalityDao {

    List<MunicipalityInfo> findMunicipalities();

    public String getMunicipalityEmail(Long municipalityId);
}
