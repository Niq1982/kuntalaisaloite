package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;

import javax.annotation.Resource;

import java.util.List;

public class MunicipalityInitiativeService {

    @Resource
    private MunicipalityInitiativeDao municipalityInitiativeDao;

    public List<MunicipalityInitiativeInfo> findAllMunicipalityInitiatives() {
        return municipalityInitiativeDao.findAllNewestFirst();
    }

}
