package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


public class DecisionService {

    @Resource
    private DecisionAttachmentDao decisionAttachmentDao;

    @Resource
    private InitiativeDao initiativeDao;



    @Transactional
    public void removeAttachmentFromDecision(Long attachmentId, Long initiativeId){
        decisionAttachmentDao.removeAttachment(attachmentId);
        // TODO actually remove file from disk
    }

    @Transactional
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId) throws InvalidAttachmentException {
        initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());
        addAttachments(decision.getFiles(), initiativeId);
        // TODO actually save attachments to disk
    }

    @Transactional(readOnly = true)
    public List<DecisionAttachmentFile> getDecisionAttachments(Long initiativeId) {
        return decisionAttachmentDao.findAllAttachments(initiativeId);
    }


    private void addAttachments(List<MultipartFile> attachments, Long initiativeId) throws InvalidAttachmentException {
        for (MultipartFile attachment: attachments) {
            decisionAttachmentDao.addAttachment(initiativeId, new DecisionAttachmentFile(attachment.getOriginalFilename(), AttachmentUtil.getFileType(attachment), attachment.getContentType(), initiativeId));
        }
    }
}
