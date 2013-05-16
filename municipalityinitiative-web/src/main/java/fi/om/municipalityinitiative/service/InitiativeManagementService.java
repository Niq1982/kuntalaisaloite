package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class InitiativeManagementService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    MunicipalityDao municipalityDao;

    @Transactional(readOnly = true)
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        return InitiativeDraftUIEditDto.parse(
                initiativeDao.get(initiativeId),
                authorDao.getAuthor(loginUserHolder.getAuthorId()).getContactInfo()
        );
    }

    private ManagementSettings getManagementSettings(Long initiativeId) {
        return ManagementSettings.of(initiativeDao.get(initiativeId));
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeDraftUIEditDto editDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), editDto.getContactInfo());
    }


}
