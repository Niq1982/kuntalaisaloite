package fi.om.municipalityinitiative.service;

import com.google.common.io.Files;
import fi.om.municipalityinitiative.dao.AttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.AttachmentCreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.util.ImageModifier;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AttachmentService {

    private String attachmentDir;

    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    @Resource
    private AttachmentDao attachmentDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private ImageModifier imageModifier;

    @Resource
    private ValidationService validationService;

    public AttachmentService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }

    public AttachmentService() { // For spring AOP
    }

    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void addAttachment(Long initiativeId, LoginUserHolder<User> loginUserHolder, MultipartFile file, String description) throws FileUploadException, InvalidAttachmentException {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        assertPrivilege(initiativeId);
        assertFileSize(file);


        String fileType = AttachmentUtil.getFileType(file);


        File tempFile = null;
        try {
            tempFile = AttachmentUtil.createTempFile(file, fileType);

            Long attachmentId = attachmentDao.addAttachment(initiativeId, description, file.getContentType(), fileType);

            AttachmentUtil.saveFileToDisk(imageModifier, file, fileType, tempFile, attachmentId, attachmentDir);

        } catch (InvalidAttachmentException e) {
            throw e;
        }
        catch (Throwable t) {
            log.error("Error while uploading file: " + file.getOriginalFilename(), t);
            throw new FileUploadException(t);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }

        }
    }

    private void assertPrivilege(Long initiativeId) {
        if (!ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowAddAttachments()) {
            throw new OperationNotAllowedException("Add attachments");
        }
    }

    private void assertFileSize(MultipartFile file) throws InvalidAttachmentException {
        // Double check, is checked at validation also
        if (file.getSize() > AttachmentUtil.ImageProperties.MAX_FILESIZE_IN_BYTES) {
            throw new InvalidAttachmentException("Too large file: " + file.getSize());
        }
    }


    static boolean isPDF(File file) throws IOException {

        byte[] ba = Files.toByteArray(file);
        return ba[0] == 37
                && ba[1] == 80
                && ba[2] == 68
                && ba[3] == 70;
    }

    static boolean isJPEG(File file) throws IOException {
        byte[] ba = Files.toByteArray(file);
        return (ba[0] & 255) == 255 && (ba[1] & 255) == 216 && (ba[ba.length - 2] & 255) == 255
                && (ba[ba.length - 1] & 255) == 217;
    }

    static boolean isPNG(File file) throws IOException {
        int numRead;
        byte[] signature = new byte[8];
        byte[] pngIdBytes = { -119, 80, 78, 71, 13, 10, 26, 10 };

        try (InputStream is = new FileInputStream(file)) {
            numRead = is.read(signature);
            if (numRead == -1)
                throw new IOException("Trying to read from 0 byte stream");
        }
        return numRead == 8 && Arrays.equals(signature, pngIdBytes);
    }



    @Transactional(readOnly = true)
    // TODO: Cache
    public AttachmentFile getAttachment(Long attachmentId, String fileName, LoginUserHolder loginUserHolder) throws IOException {
        AttachmentFileInfo attachmentInfo = attachmentDao.getAttachment(attachmentId);

        if (!attachmentInfo.getFileName().equals(fileName)) {
            throw new AccessDeniedException("Invalid filename for attachment " + attachmentId + " - " + fileName);
        }

        assertViewAllowance(loginUserHolder, attachmentInfo);
        byte[] attachmentBytes = getFileBytes(AttachmentUtil.getFilePath(attachmentId, attachmentInfo.getFileType(), attachmentDir));
        return new AttachmentFile(attachmentInfo, attachmentBytes);
    }

    @Transactional(readOnly = true)
    // TODO: Cache
    public AttachmentFile getThumbnail(Long attachmentId, LoginUserHolder loginUserHolder) throws IOException {
        AttachmentFileInfo attachmentInfo = attachmentDao.getAttachment(attachmentId);
        assertViewAllowance(loginUserHolder, attachmentInfo);
        if (attachmentInfo.isPdf()) {
            throw new AccessDeniedException("no thumbnail for pdf");
        }

        byte[] attachmentBytes = getFileBytes(AttachmentUtil.getThumbnailPath(attachmentId, attachmentInfo.getFileType(), attachmentDir));
        return new AttachmentFile(attachmentInfo, attachmentBytes);
    }

    private void assertViewAllowance(LoginUserHolder loginUserHolder, AttachmentFileInfo attachmentInfo) {
        if (!attachmentInfo.isAccepted()) {
            loginUserHolder.assertViewRightsForInitiative(attachmentInfo.getInitiativeId());
        }
    }

    private byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = FileUtil.readAsByteArray(file);
        return Arrays.copyOf(bytes, bytes.length);
    }


    private static String parseFileType(String fileName) throws InvalidAttachmentException {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new InvalidAttachmentException("Invalid filename: " + fileName);
        }

        return split[split.length-1];
    }

    private static void assertContentType(String contentType) throws InvalidAttachmentException {
        for (String type : AttachmentUtil.ImageProperties.CONTENT_TYPES) {
            if (type.equalsIgnoreCase(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
    }

    @Transactional(readOnly = true)
    public AttachmentUtil.Attachments findAcceptedAttachments(Long initiativeId) {
        return new AttachmentUtil.Attachments(attachmentDao.findAcceptedAttachments(initiativeId));
    }

    @Transactional(readOnly = true)
    public AttachmentUtil.Attachments findAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        if (loginUserHolder.getUser().isOmUser() || loginUserHolder.hasManagementRightsForInitiative(initiativeId)) {
            return new AttachmentUtil.Attachments(attachmentDao.findAllAttachments(initiativeId));
        }
        else {
            return new AttachmentUtil.Attachments(attachmentDao.findAcceptedAttachments(initiativeId));
        }
    }

    @Transactional(readOnly = true)
    public AttachmentUtil.Attachments findAllAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertViewRightsForInitiative(initiativeId);
        return new AttachmentUtil.Attachments(attachmentDao.findAllAttachments(initiativeId));
    }

    String getAttachmentDir() { // For tests
        return attachmentDir;
    }

    @Transactional(readOnly = false)
    public Long deleteAttachment(Long attachmentId, LoginUserHolder loginUserHolder) {

        AttachmentFileInfo attachmentFileInfo = attachmentDao.getAttachment(attachmentId);
        loginUserHolder.assertManagementRightsForInitiative(attachmentFileInfo.getInitiativeId());
        attachmentDao.deleteAttachment(attachmentId);
        return attachmentFileInfo.getInitiativeId();
    }

    @Transactional(readOnly = true)
    public boolean validationSuccessful(Long initiativeId, AttachmentCreateDto attachmentCreateDto, BindingResult bindingResult, Model model) {

        if (attachmentDao.findAllAttachments(initiativeId).size() >= AttachmentUtil.ImageProperties.MAX_ATTACHMENTS) {
            addAttachmentValidationError(bindingResult, "attachment.error.too.many.attachments", String.valueOf(AttachmentUtil.ImageProperties.MAX_ATTACHMENTS));
        }
        else {
            validationService.validationSuccessful(attachmentCreateDto, bindingResult, model);
            if (attachmentCreateDto.getImage().getSize() == 0) {
                addAttachmentValidationError(bindingResult, "attachment.error.NotEmpty", "");
            }
            else {
                try {
                    AttachmentUtil.assertFileType(parseFileType(attachmentCreateDto.getImage().getOriginalFilename()));
                } catch (InvalidAttachmentException e) {
                    addAttachmentValidationError(bindingResult, "attachment.error.invalid.file.type", Arrays.toString(AttachmentUtil.ImageProperties.FILE_TYPES));
                }

                if (attachmentCreateDto.getImage().getSize() > AttachmentUtil.ImageProperties.MAX_FILESIZE_IN_BYTES) {
                    addAttachmentValidationError(bindingResult, "attachment.error.too.large.file", AttachmentUtil.ImageProperties.MAX_FILESIZE_IN_KILOBYTES);
                }
            }
        }


        return bindingResult.getErrorCount() == 0;
    }

    private void addAttachmentValidationError(BindingResult bindingResult, String message, String argument) {
        bindingResult.addError(new FieldError("attachment", "image", "", false, new String[]{message}, new String[]{argument}, message));
    }


}
