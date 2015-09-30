package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.util.ImageModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;


public class DecisionService {

    private String attachmentDir;

    private static final Logger log = LoggerFactory.getLogger(DecisionService.class);

    @Resource
    private DecisionAttachmentDao decisionAttachmentDao;

    @Resource
    private InitiativeDao initiativeDao;


    @Resource
    private ImageModifier imageModifier;


    public DecisionService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }

    public DecisionService() { // For spring AOP
    }

    @Transactional
    public void removeAttachmentFromDecision(Long attachmentId, Long initiativeId){
        decisionAttachmentDao.removeAttachment(attachmentId);
        // TODO actually remove file from disk
    }

    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId) throws InvalidAttachmentException, FileUploadException {

        initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());

        for (MultipartFile attachment: decision.getFiles()) {
            File tempFile = null;
            try {
                tempFile = AttachmentUtil.createTempFile(attachment, AttachmentUtil.getFileType(attachment));

                Long attachmentId = decisionAttachmentDao.addAttachment(initiativeId, new DecisionAttachmentFile(attachment.getOriginalFilename(), AttachmentUtil.getFileType(attachment), attachment.getContentType(), initiativeId));

                AttachmentUtil.saveFileToDisk(imageModifier, attachment, AttachmentUtil.getFileType(attachment), tempFile, attachmentId, attachmentDir);

            } catch (Throwable t) {
                log.error("Error while uploading file: " + attachment.getOriginalFilename(), t);
                throw new FileUploadException(t);
            } finally {
                if (tempFile != null){
                    tempFile.delete();
                }

            }

        }

    }

    @Transactional(readOnly = true)
    public AttachmentUtil.Attachments getDecisionAttachments(Long initiativeId) {
        return new AttachmentUtil.Attachments(decisionAttachmentDao.findAllAttachments(initiativeId));
    }


}
