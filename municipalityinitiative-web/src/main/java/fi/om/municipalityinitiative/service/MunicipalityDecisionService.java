package fi.om.municipalityinitiative.service;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MunicipalityDecisionService {

    private String attachmentDir;

    private static final Logger log = LoggerFactory.getLogger(MunicipalityDecisionService.class);

    @Resource
    private DecisionAttachmentDao decisionAttachmentDao;

    @Resource
    private InitiativeDao initiativeDao;


    @Resource
    private ImageModifier imageModifier;

    @Resource
    private ValidationService validationService;

    public MunicipalityDecisionService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }

    public MunicipalityDecisionService() { // For spring AOP
    }

    @Transactional
    public void removeAttachmentFromDecision(Long attachmentId, MunicipalityUserHolder user){
        DecisionAttachmentFile file = decisionAttachmentDao.getAttachment(attachmentId);
        user.assertManagementRightsForInitiative(file.getInitiativeId());
        decisionAttachmentDao.removeAttachment(attachmentId);
    }

    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId, MunicipalityUserHolder user) throws FileUploadException, InvalidAttachmentException {

        user.assertManagementRightsForInitiative(initiativeId);

        initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());

        saveAttachments(decision, initiativeId);

    }

    private void saveAttachments(MunicipalityDecisionDto decision, Long initiativeId) throws FileUploadException, InvalidAttachmentException {

        for (MunicipalityDecisionDto.FileWithName attachment: decision.getFiles()) {

            File tempFile = null;
            try {
                tempFile = AttachmentUtil.createTempFile(attachment.getFile(), AttachmentUtil.getFileType(attachment.getFile()));

                DecisionAttachmentFile fileInfo = new DecisionAttachmentFile(attachment.getName(), AttachmentUtil.getFileType(attachment.getFile()), attachment.getFile().getContentType(), initiativeId);

                Long attachmentId = decisionAttachmentDao.addAttachment(initiativeId, fileInfo);

                AttachmentUtil.saveMunicipalityAttachmentToDiskAndCreateThumbnail(imageModifier, fileInfo.getContentType(), fileInfo.getFileType(), tempFile, attachmentId, attachmentDir);

            }catch (InvalidAttachmentException e) {
                throw e;

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
        return AttachmentUtil.getAttachmentFile(fileName, attachmentInfo, attachmentDir);

    }

    public boolean validationSuccessful(MunicipalityDecisionDto decision, BindingResult bindingResult, Model model) {

        decision.setFiles(clearEmptyFiles(decision.getFiles()));

        if (decision.getFiles().size() == 0  & (decision.getDescription().isEmpty() || decision.getDescription().equals(""))) {
            addAttachmentValidationError(bindingResult, "filesAndDescription", "DecisionDescriptionAndAttachmentsBothEmpty");
        }

        for (MunicipalityDecisionDto.FileWithName file : decision.getFiles()) {

            if (file.getName() == null || file.getName().isEmpty()) {
                addAttachmentValidationError(bindingResult, "files", "NotEmpty");
            }
            try {
                AttachmentUtil.assertValidFileType(AttachmentUtil.parseFileType(file.getFile().getOriginalFilename()));
            } catch (InvalidAttachmentException e) {
                addAttachmentValidationError(bindingResult, "attachment.error.invalid.file.type", Arrays.toString(AttachmentUtil.ImageProperties.FILE_TYPES));
            }

            if (file.getFile().getSize() > AttachmentUtil.ImageProperties.MAX_FILESIZE_IN_BYTES) {
                addAttachmentValidationError(bindingResult, "attachment.error.too.large.file", AttachmentUtil.ImageProperties.MAX_FILESIZE_IN_KILOBYTES);
            }
        }
        validationService.validationSuccessful(decision, bindingResult, model);

        return bindingResult.getErrorCount() == 0;
    }

    private List<MunicipalityDecisionDto.FileWithName> clearEmptyFiles(List<MunicipalityDecisionDto.FileWithName> files) {
        return Lists.newArrayList(Iterables.filter(files, new Predicate<MunicipalityDecisionDto.FileWithName>() {
            @Override
            public boolean apply(MunicipalityDecisionDto.FileWithName fileWithName) {
                return fileWithName.getFile() != null;
            }
        }));

    }

    public void addAttachmentValidationError(BindingResult bindingResult, String field, String error) {
        bindingResult.addError(new FieldError("decision", field, "", false, new String[]{error}, new String[]{error}, ""));
    }
}
