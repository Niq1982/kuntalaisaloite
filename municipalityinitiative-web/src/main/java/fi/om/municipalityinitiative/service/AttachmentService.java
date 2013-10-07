package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.AttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.util.ImageModifier;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.im4java.core.InfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class AttachmentService {

    public static final Integer MAX_WIDTH = 1000;
    public static final Integer MAX_HEIGHT = 500;
    public static final Integer THUMBNAIL_MAX_WIDTH = 100;
    public static final Integer THUMBNAIL_MAX_HEIGHT = 100;

    public static final String[] FILE_TYPES = { "png", "jpg", "pdf" };
    public static final String[] CONTENT_TYPES = { "image/png", "image/jpg", "image/jpeg", "application/pdf" };

    private String attachmentDir;

    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    @Resource
    private AttachmentDao attachmentDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private ImageModifier imageModifier;

    public AttachmentService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }

    public AttachmentService() { // For spring AOP
    }

    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public void addAttachment(Long initiativeId, LoginUserHolder<User> loginUserHolder, MultipartFile file, String description) throws FileUploadException, InvalidAttachmentException {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        if (!ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowAddAttachments()) {
            throw new OperationNotAllowedException("Add attachments");
        }

        file.getSize(); // TODO: Don't allow too large files

        String fileType = parseFileType(file.getOriginalFilename());
        assertFileType(fileType);
        assertContentType(file.getContentType());

        Long attachmentId = attachmentDao.addAttachment(initiativeId, description, file.getContentType(), fileType);

        try {
            if (AttachmentFileInfo.isPdfContentType(file.getContentType())) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(getFilePath(attachmentId))) {
                    fileOutputStream.write(file.getBytes());
                }
             }
            else {
                imageModifier.modify(file.getInputStream(), getFilePath(attachmentId), fileType, MAX_WIDTH, MAX_HEIGHT);
                imageModifier.modify(file.getInputStream(), getThumbnailPath(attachmentId), fileType, THUMBNAIL_MAX_WIDTH, THUMBNAIL_MAX_HEIGHT);
            }
        } catch (Throwable t) {
            log.error("Error while uploading file: " + file.getOriginalFilename(), t);
            throw new FileUploadException(t);
        }

    }

    private String getFilePath(Long attachmentId) {
        return attachmentDir + "/" + attachmentId;
    }

    private String getThumbnailPath(Long attachmentId) {
        return attachmentDir + "/" + attachmentId + "_thumbnail";
    }

    @Transactional(readOnly = true)
    // TODO: Cache
    // TODO: Handle if errors?
    public AttachmentFile getAttachment(Long attachmentId, LoginUserHolder loginUserHolder) throws IOException {
        AttachmentFileInfo attachmentInfo = attachmentDao.getAttachment(attachmentId);
        assertViewAllowance(loginUserHolder, attachmentInfo);
        byte[] attachmentBytes = getFileBytes(getFilePath(attachmentId));
        return new AttachmentFile(attachmentInfo, attachmentBytes);
    }

    @Transactional(readOnly = true)
    // TODO: Cache
    // TODO: Return empty if errors
    public AttachmentFile getThumbnail(Long attachmentId, LoginUserHolder loginUserHolder) throws IOException {
        AttachmentFileInfo attachmentInfo = attachmentDao.getAttachment(attachmentId);
        assertViewAllowance(loginUserHolder, attachmentInfo);
        if (attachmentInfo.isPdf()) {
            throw new AccessDeniedException("no thumbnail for pdf");
        }

        byte[] attachmentBytes = getFileBytes(getThumbnailPath(attachmentId));
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



    private static void assertFileType(String givenFileType) throws InvalidAttachmentException {
        for (String fileType : FILE_TYPES) {
            if (fileType.equals(givenFileType))
                return;
        }
        throw new InvalidAttachmentException("Invalid fileName");
    }

    private static String parseFileType(String fileName) throws InvalidAttachmentException {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new InvalidAttachmentException("Invalid filename");
        }

        return split[split.length-1];
    }

    private static void assertContentType(String contentType) throws InvalidAttachmentException {
        for (String type : CONTENT_TYPES) {
            if (type.equals(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
    }

    @Transactional(readOnly = true)
    public List<AttachmentFileInfo> findAcceptedAttachments(Long initiativeId) {
        return attachmentDao.findAcceptedAttachments(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<AttachmentFileInfo> findAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        if (loginUserHolder.getUser().isOmUser() || loginUserHolder.hasManagementRightsForInitiative(initiativeId)) {
            return attachmentDao.findAllAttachments(initiativeId);
        }
        else {
            return attachmentDao.findAcceptedAttachments(initiativeId);
        }
    }

    @Transactional(readOnly = true)
    public List<AttachmentFileInfo> findAllAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertViewRightsForInitiative(initiativeId);
        return attachmentDao.findAllAttachments(initiativeId);
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
}
