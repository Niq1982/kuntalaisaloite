package fi.om.municipalityinitiative.service;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.ui.MunicipalityDecisionInfo;
import fi.om.municipalityinitiative.util.ImageModifier;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.joda.time.DateTime;
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
import java.util.Locale;
import java.util.Optional;


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

    @Resource
    private EmailService emailService;

    @Resource
    private MunicipalityUserDao municipalityUserDao;

    public MunicipalityDecisionService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }

    public MunicipalityDecisionService() { // For spring AOP
    }

    public static boolean decisionPresent(InitiativeViewInfo initiative, AttachmentUtil.Attachments attachments) {
        return initiative.getDecisionText().isPresent() || attachments.count() > 0;
    }


    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId, MunicipalityUserHolder user,  Locale locale) throws FileUploadException, InvalidAttachmentException {
        user.assertMunicipalityLoginUser(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        saveAttachments(decision.getFiles(), initiativeId);

        if (decisionExists(initiative)) {
            initiativeDao.updateInitiativeDecision(initiativeId, decision.getDescription());

        } else {
            initiativeDao.createInitiativeDecision(initiativeId, decision.getDescription());
            emailService.sendMunicipalityDecisionToAuthors(initiativeId, locale);
            emailService.sendMunicipalityDecisionToFollowers(initiativeId);
        }

    }


    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void updateAttachments(Long initiativeId, List<MunicipalityDecisionDto.FileWithName> files, MunicipalityUserHolder user) throws FileUploadException, InvalidAttachmentException {
        user.assertMunicipalityLoginUser(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!initiative.getDecisionDate().isPresent()) {
            throw new InvalidAttachmentException("Can't attach files to decision that doesn't exist");
        }

        saveAttachments(files, initiativeId);
        initiativeDao.updateInitiativeDecisionModifiedDate(initiativeId);
    }

    @Transactional
    public void removeAttachmentFromDecision(Long attachmentId, MunicipalityUserHolder user){
        DecisionAttachmentFile file = decisionAttachmentDao.getAttachment(attachmentId);
        user.assertMunicipalityLoginUser(file.getInitiativeId());
        initiativeDao.updateInitiativeDecisionModifiedDate(file.getInitiativeId());
        decisionAttachmentDao.removeAttachment(attachmentId);
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

    public void saveAttachments(List<MunicipalityDecisionDto.FileWithName> files, Long initiativeId) throws FileUploadException, InvalidAttachmentException {

        for (MunicipalityDecisionDto.FileWithName attachment: files) {

            DecisionAttachmentFile fileInfo = new DecisionAttachmentFile(attachment.getName(), AttachmentUtil.getFileType(attachment.getFile()), attachment.getFile().getContentType(), initiativeId);

            Long attachmentId = decisionAttachmentDao.addAttachment(initiativeId, fileInfo);

            saveFile(attachment, fileInfo, attachmentId);
        }
    }

    @Transactional(readOnly = true)
    public Optional<MunicipalityDecisionInfo> getMunicipalityDecisionInfoOptional(InitiativeViewInfo initiative) {

        Optional<MunicipalityDecisionInfo> municipalityDecisionInfo = Optional.empty();
        if (initiative.getDecisionDate().isPresent()) {
            AttachmentUtil.Attachments attachments = getDecisionAttachments(initiative.getId());
            if (decisionPresent(initiative, attachments)) {
                municipalityDecisionInfo = Optional.of(MunicipalityDecisionInfo.build(
                        initiative.getDecisionText(),
                        initiative.getDecisionDate().get(),
                        initiative.getDecisionModifiedDate(),
                        attachments));
            }
        }
        return municipalityDecisionInfo;
    }

    private void saveFile(MunicipalityDecisionDto.FileWithName attachment, DecisionAttachmentFile fileInfo, Long attachmentId) throws InvalidAttachmentException, FileUploadException {
        File tempFile = null;
        try {

            tempFile = AttachmentUtil.createTempFile(attachment.getFile(), fileInfo.getFileType());

            AttachmentUtil.saveMunicipalityAttachmentToDiskAndCreateThumbnail(imageModifier, fileInfo.getContentType(), fileInfo.getFileType(), tempFile, attachmentId, attachmentDir);

        } catch (InvalidAttachmentException e) {
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

    public boolean validationSuccessful(MunicipalityDecisionDto decision, boolean oldDecision, BindingResult bindingResult, Model model) {

        decision.setFiles(clearEmptyFiles(decision.getFiles()));

        if (!oldDecision) {
            if (decision.getFiles().isEmpty() && decisionTextIsEmpty(decision)) {
                addAttachmentValidationError(bindingResult, "filesAndDescription", "DecisionDescriptionAndAttachmentsBothEmpty");
            }
        }
        validateFiles(decision.getFiles(), bindingResult);

        return validationService.validationSuccessful(decision, bindingResult, model);
    }

    private boolean decisionTextIsEmpty(MunicipalityDecisionDto decision) {
        return decision.getDescription().isEmpty() || decision.getDescription().equals("");
    }

    public boolean validationSuccessful(List<MunicipalityDecisionDto.FileWithName> files, BindingResult bindingResult, Model model) {

        if(files.isEmpty()) {
            addAttachmentValidationError(bindingResult, "files", "AttachmentsEmpty");
        } else {
            validateFiles(files, bindingResult);
        }
        return validationService.validationSuccessful(files, bindingResult, model);
    }

    private void validateFiles(List<MunicipalityDecisionDto.FileWithName> files, BindingResult bindingResult) {

        for (MunicipalityDecisionDto.FileWithName file : files) {

            if (file.getName() == null || file.getName().isEmpty()) {
                addAttachmentValidationError(bindingResult, "file.name", "NotEmpty");
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
    }

    private void addAttachmentValidationError(BindingResult bindingResult, String field, String error) {
        bindingResult.addError(new FieldError("decision", field, "", false, new String[]{error}, new String[]{error}, ""));
    }

    public List<MunicipalityDecisionDto.FileWithName> clearEmptyFiles(List<MunicipalityDecisionDto.FileWithName> files) {
        return Lists.newArrayList(Iterables.filter(files, new Predicate<MunicipalityDecisionDto.FileWithName>() {
            @Override
            public boolean apply(MunicipalityDecisionDto.FileWithName fileWithName) {
                return fileWithName.getFile() != null;
            }
        }));

    }

    private boolean decisionExists(Initiative initiative) {
        return initiative.getDecisionDate().isPresent();
    }


    @Transactional
    public Long createAndSendMunicipalityLoginLink(String managementHash) {

        Long initiativeId = municipalityUserDao.getInitiativeId(managementHash);

        if (initiativeId == null) {
            throw new NotFoundException("Municipality user", managementHash);
        }

        String loginHash = RandomHashGenerator.longHash();

        municipalityUserDao.assignMunicipalityUserLoginHash(
                initiativeId,
                managementHash,
                loginHash,
                DateTime.now()
        );

        emailService.sendLoginLinkToMunicipality(initiativeId);
        return initiativeId;

    }
}
