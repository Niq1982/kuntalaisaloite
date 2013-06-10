package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class InitiativeManagementServiceOperations {

    @Resource
    InitiativeDao initiativeDao;

    @Transactional(readOnly = false)
    public InitiativeType doSendToMunicipality(Long initiativeId, String sentComment) {

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Send to municipality", ManagementSettings.of(initiative).isAllowSendToMunicipality());

        if (initiative.getType().isCollaborative()) {
            sendCollaborativeToMunicipality(initiativeId, sentComment);
        } else {
            publishAndMarkAsSent(initiativeId, sentComment);
        }

        return initiative.getType();
    }

    private void publishAndMarkAsSent(Long initiativeId, String sentComment){
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
    }

    private void sendCollaborativeToMunicipality(Long initiativeId, String sentComment) {
        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
    }

    @Transactional(readOnly = false)
    public void doPublishAndStartCollecting(Long initiativeId) {
        assertAllowance("Publish initiative", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
    }
}
