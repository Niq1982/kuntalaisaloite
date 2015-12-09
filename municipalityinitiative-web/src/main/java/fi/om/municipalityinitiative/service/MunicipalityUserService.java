package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;

public class MunicipalityUserService {

    @Resource
    private
    MunicipalityUserDao municipalityUserDao;

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private EmailService emailService;

    @Transactional
    public void createMunicipalityUser(Long initiativeId) {

        String managementHash = RandomHashGenerator.longHash();
        municipalityUserDao.createMunicipalityUser(initiativeId, managementHash);

    }

    @Transactional
    public void renewManagementHash(OmLoginUserHolder omLoginUserHolder, Long initiativeId, Locale locale) {
        omLoginUserHolder.assertOmUser();
        municipalityUserDao.removeMunicipalityUser(initiativeId);
        createMunicipalityUser(initiativeId);

        sendRenewedHashToMunicipality(initiativeId, locale);

    }

    private void sendRenewedHashToMunicipality(Long initiativeId, Locale locale) {
        Initiative initiative = initiativeDao.get(initiativeId);
        if (initiative.getType().isCollaborative()) {
            emailService.sendCollaborativeToMunicipality(initiativeId, locale);
        } else{
            emailService.sendSingleToMunicipality(initiativeId, locale);
        }
    }
}
