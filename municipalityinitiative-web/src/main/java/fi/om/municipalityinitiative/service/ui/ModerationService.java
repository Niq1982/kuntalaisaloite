package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.operations.ModerationServiceOperations;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.service.operations.ModerationServiceOperations.ManagementHashRenewData;

public class ModerationService {

    @Resource
    EmailService emailService;

    @Resource
    ModerationServiceOperations moderationServiceOperations;

    public void accept(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment, Locale locale) {
        loginUserHolder.assertOmUser();
        ModerationServiceOperations.AcceptResult acceptResult = moderationServiceOperations.doAccept(initiativeId, moderatorComment);

        if (acceptResult == ModerationServiceOperations.AcceptResult.ACCEPTED_DRAFT_AND_SENT) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiativeId, locale);
        }
        else if (acceptResult == ModerationServiceOperations.AcceptResult.ACCEPTED_DRAFT) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM);
        }
        else if (acceptResult == ModerationServiceOperations.AcceptResult.ACCEPTED_FIX) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_FIX);
        }
        else {
            throw new IllegalStateException("Unable to accept initiative with id:"+ initiativeId);
        }
    }

    public void reject(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment) {
        loginUserHolder.assertOmUser();
        moderationServiceOperations.doReject(initiativeId, moderatorComment);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    public List<? extends Author> findAuthors(OmLoginUserHolder loginUserHolder, Long initiativeId) {
        loginUserHolder.assertOmUser();
        return moderationServiceOperations.findAuthors(initiativeId);
    }

    public List<MunicipalityEditDto> findMunicipalitiesForEdit(OmLoginUserHolder loginUserHolder) {
        loginUserHolder.assertOmUser();
        return moderationServiceOperations.findMunicipalitiesForEdit();
    }

    public void updateMunicipality(OmLoginUserHolder omLoginUserHolder, MunicipalityUIEditDto editDto) {
        omLoginUserHolder.assertOmUser();
        moderationServiceOperations.doUpdateMunicipality(editDto);
    }

    public void sendInitiativeBackForFixing(OmLoginUserHolder omLoginUserHolder, Long initiativeId, String moderatorComment) {

        omLoginUserHolder.assertOmUser();
        moderationServiceOperations.doSendInitiativeBackForFixing(initiativeId, moderatorComment);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    public void renewManagementHash(OmLoginUserHolder omLoginUserHolder, Long authorId) {
        omLoginUserHolder.assertOmUser();

        ManagementHashRenewData managementHashRenewData = moderationServiceOperations.doRenewManagementHash(authorId);
        emailService.sendManagementHashRenewed(managementHashRenewData.initiativeId, managementHashRenewData.newManagementHash, authorId);
    }

}
