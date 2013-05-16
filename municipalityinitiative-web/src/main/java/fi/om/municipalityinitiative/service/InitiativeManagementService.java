package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

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

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, LoginUserHolder loginUserHolder) {

        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);
        List<Author> authors = authorDao.findAuthors(initiativeId);
        ContactInfo contactInfo = null;
        for (Author author : authors) {
            if (author.getId().equals(loginUserHolder.getAuthorId())) {
                contactInfo = author.getContactInfo();
                break;
            }
        }

        if (contactInfo == null) {
            throw new RuntimeException("FIX THIS to something nicer");
        }

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(contactInfo);
        updateDto.setExtraInfo(initiative.getExtraInfo());

        return updateDto;
    }

    @Transactional(readOnly = true)
    // TODO: Tests?
    public Author getAuthorInformation(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        for (Author author : authorDao.findAuthors(initiativeId)) {
            if (author.getId().equals(loginUserHolder.getAuthorId())) {
                return author;
            }
        }
        // TODO: Hmm.. We still get the municipality information and stuff from participant table...
        throw new NotFoundException("Author", initiativeId);
    }


}
