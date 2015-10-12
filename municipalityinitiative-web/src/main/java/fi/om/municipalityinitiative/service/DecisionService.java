package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.util.ImageModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;


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
    public void removeAttachmentFromDecision(Long attachmentId, MunicipalityUserHolder user){
        decisionAttachmentDao.removeAttachment(attachmentId);
    }

    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId, MunicipalityUserHolder user) throws InvalidAttachmentException, FileUploadException {

        initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());

        for (MunicipalityDecisionDto.FileWithName attachment: decision.getFiles()) {

            File tempFile = null;
            try {
                tempFile = AttachmentUtil.createTempFile(attachment.getFile(), AttachmentUtil.getFileType(attachment.getFile()));

                DecisionAttachmentFile fileInfo = new DecisionAttachmentFile(attachment.getName(), AttachmentUtil.getFileType(attachment.getFile()), attachment.getFile().getContentType(), initiativeId);

                Long attachmentId = decisionAttachmentDao.addAttachment(initiativeId, fileInfo);

                AttachmentUtil.saveMunicipalityAttachmentToDiskAndCreateThumbnail(imageModifier, fileInfo.getContentType(), fileInfo.getFileType(), tempFile, attachmentId, attachmentDir);

            } catch (Throwable t) {
                log.error("Error while uploading file: " + attachment.getFile().getOriginalFilename(), t);
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

    @Transactional(readOnly = true)
    public AttachmentFile getThumbnail(Long attachmentId, LoginUserHolder<User> loginUserHolder) throws IOException {
        DecisionAttachmentFile attachmentInfo = decisionAttachmentDao.getAttachment(attachmentId);
        return AttachmentUtil.getThumbnailForImageAttachment(attachmentId, attachmentInfo, attachmentDir);
    }

    @Transactional(readOnly = true)
    public AttachmentFile getAttachment(Long attachmentId, String fileName, LoginUserHolder loginUserHolder) throws IOException {
        DecisionAttachmentFile attachmentInfo = decisionAttachmentDao.getAttachment(attachmentId);
        return AttachmentUtil.getAttachmentFile(fileName, attachmentInfo,  attachmentDir);

    }
}
