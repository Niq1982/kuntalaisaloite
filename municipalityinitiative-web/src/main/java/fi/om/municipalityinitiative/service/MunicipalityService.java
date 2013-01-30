package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;

import javax.annotation.Resource;

import java.util.List;

public class MunicipalityService {

    @Resource
    private MunicipalityDao municipalityDao;

    public List<MunicipalityInfo> findAllMunicipalities() {
        return municipalityDao.findMunicipalities();
    }
}
