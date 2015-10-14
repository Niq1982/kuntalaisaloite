package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class MunicipalityUserService {

    @Resource
    private
    MunicipalityUserDao municipalityUserDao;

    @Resource
    InitiativeDao initiativeDao;

    @Transactional
    public void createMunicipalityUser(Long initiativeId) {

        String managementHash = RandomHashGenerator.longHash();
        municipalityUserDao.createMunicipalityUser(initiativeId, managementHash);

    }

    @Transactional
    public void renewManagementHash(OmLoginUserHolder omLoginUserHolder, Long initiativeId) {
        omLoginUserHolder.assertOmUser();
        municipalityUserDao.removeMunicipalityUser(initiativeId);
        createMunicipalityUser(initiativeId);
    }
}
