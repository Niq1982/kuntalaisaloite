package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
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
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.aspectj.util.FileUtil;
import org.im4java.core.IM4JavaException;
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
import java.util.List;

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


        String fileType = getFileType(file);


        File tempFile = null;
        try {
            tempFile = createTempFile(file, fileType);

            Long attachmentId = attachmentDao.addAttachment(initiativeId, description, file.getContentType(), fileType);

            saveFileToDisk(file, fileType, tempFile, attachmentId);

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
        if (file.getSize() > ImageProperties.MAX_FILESIZE_IN_BYTES) {
            throw new InvalidAttachmentException("Too large file: " + file.getSize());
        }
    }

    private String getFileType(MultipartFile file) throws InvalidAttachmentException {
        // Some checks for valid filename
        String fileType = parseFileType(file.getOriginalFilename());
        assertFileType(fileType);
        assertContentType(file.getContentType());
        return fileType;
    }

    private void saveFileToDisk(MultipartFile file, String fileType, File tempFile, Long attachmentId) throws IOException, IM4JavaException, InterruptedException {
        if (AttachmentFileInfo.isPdfContentType(file.getContentType())) {
            FileUtil.copyValidFiles(tempFile, new File(getFilePath(attachmentId, fileType)));
        }
        else {
            imageModifier.modify(tempFile, getFilePath(attachmentId, fileType), ImageProperties.MAX_WIDTH, ImageProperties.MAX_HEIGHT);
            imageModifier.modify(tempFile, getThumbnailPath(attachmentId, fileType), ImageProperties.THUMBNAIL_MAX_WIDTH, ImageProperties.THUMBNAIL_MAX_HEIGHT);
        }
    }

    private File createTempFile(MultipartFile file, String fileType) throws IOException, InvalidAttachmentException {
        File tempFile = null;
        // Create temp-file for proper file handling
        tempFile = File.createTempFile(RandomHashGenerator.shortHash(), "." + fileType);

        file.transferTo(tempFile);

        assertRealFileContent(tempFile, fileType);
        return tempFile;
    }

    private static void assertRealFileContent(File tempFile, String fileType) throws IOException, InvalidAttachmentException {
        if ((fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")) && isJPEG(tempFile)) {
            return;
        }
        if (fileType.equalsIgnoreCase("png") && isPNG(tempFile)) {
            return;
        }
        if (fileType.equalsIgnoreCase("pdf") && isPDF(tempFile)) {
            return;
        }
        else {
            throw new InvalidAttachmentException("File content was invalid for filetype: " + fileType);
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

    private String getFilePath(Long attachmentId, String fileType) {
        return attachmentDir + "/" + attachmentId + "." + fileType;
    }

    private String getThumbnailPath(Long attachmentId, String fileType) {
        return attachmentDir + "/" + attachmentId + "_thumbnail" + "." + fileType;
    }

    @Transactional(readOnly = true)
    // TODO: Cache
    public AttachmentFile getAttachment(Long attachmentId, String fileName, LoginUserHolder loginUserHolder) throws IOException {
        AttachmentFileInfo attachmentInfo = attachmentDao.getAttachment(attachmentId);

        if (!attachmentInfo.getFileName().equals(fileName)) {
            throw new AccessDeniedException("Invalid filename for attachment " + attachmentId + " - " + fileName);
        }

        assertViewAllowance(loginUserHolder, attachmentInfo);
        byte[] attachmentBytes = getFileBytes(getFilePath(attachmentId, attachmentInfo.getFileType()));
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

        byte[] attachmentBytes = getFileBytes(getThumbnailPath(attachmentId, attachmentInfo.getFileType()));
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
        for (String fileType : ImageProperties.FILE_TYPES) {
            if (fileType.equalsIgnoreCase(givenFileType))
                return;
        }
        throw new InvalidAttachmentException("Invalid fileName: "+givenFileType);
    }

    private static String parseFileType(String fileName) throws InvalidAttachmentException {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new InvalidAttachmentException("Invalid filename: " + fileName);
        }

        return split[split.length-1];
    }

    private static void assertContentType(String contentType) throws InvalidAttachmentException {
        for (String type : ImageProperties.CONTENT_TYPES) {
            if (type.equalsIgnoreCase(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
    }

    @Transactional(readOnly = true)
    public Attachments findAcceptedAttachments(Long initiativeId) {
        return new Attachments(attachmentDao.findAcceptedAttachments(initiativeId));
    }

    @Transactional(readOnly = true)
    public Attachments findAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        if (loginUserHolder.getUser().isOmUser() || loginUserHolder.hasManagementRightsForInitiative(initiativeId)) {
            return new Attachments(attachmentDao.findAllAttachments(initiativeId));
        }
        else {
            return new Attachments(attachmentDao.findAcceptedAttachments(initiativeId));
        }
    }

    @Transactional(readOnly = true)
    public Attachments findAllAttachments(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertViewRightsForInitiative(initiativeId);
        return new Attachments(attachmentDao.findAllAttachments(initiativeId));
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

        if (attachmentDao.findAllAttachments(initiativeId).size() >= ImageProperties.MAX_ATTACHMENTS) {
            addAttachmentValidationError(bindingResult, "attachment.error.too.many.attachments", String.valueOf(ImageProperties.MAX_ATTACHMENTS));
        }
        else {
            validationService.validationSuccessful(attachmentCreateDto, bindingResult, model);
            if (attachmentCreateDto.getImage().getSize() == 0) {
                addAttachmentValidationError(bindingResult, "attachment.error.NotEmpty", "");
            }
            else {
                try {
                    assertFileType(parseFileType(attachmentCreateDto.getImage().getOriginalFilename()));
                } catch (InvalidAttachmentException e) {
                    addAttachmentValidationError(bindingResult, "attachment.error.invalid.file.type", Arrays.toString(ImageProperties.FILE_TYPES));
                }

                if (attachmentCreateDto.getImage().getSize() > ImageProperties.MAX_FILESIZE_IN_BYTES) {
                    addAttachmentValidationError(bindingResult, "attachment.error.too.large.file", ImageProperties.MAX_FILESIZE_IN_KILOBYTES);
                }
            }
        }


        return bindingResult.getErrorCount() == 0;
    }

    private void addAttachmentValidationError(BindingResult bindingResult, String message, String argument) {
        bindingResult.addError(new FieldError("attachment", "image", "", false, new String[]{message}, new String[]{argument}, message));
    }

    public static class Attachments {
        private final List<AttachmentFileInfo> images = Lists.newArrayList();
        private final List<AttachmentFileInfo> pdfs = Lists.newArrayList();

        public Attachments(List<AttachmentFileInfo> attachments) {
            for (AttachmentFileInfo attachment : attachments) {
                if (attachment.isPdf()) {
                    pdfs.add(attachment);
                }
                else {
                    images.add(attachment);
                }
            }
        }

        public List<AttachmentFileInfo> getImages() {
            return images;
        }

        public List<AttachmentFileInfo> getPdfs() {
            return pdfs;
        }

        public List<AttachmentFileInfo> getAll() {
            List<AttachmentFileInfo> all = Lists.newArrayList(images);
            all.addAll(pdfs);
            return all;
        }
    }

    public static final class ImageProperties {

        private static final int MAX_WIDTH = 1000;

        private static final int MAX_HEIGHT = 1000;
        private static final int THUMBNAIL_MAX_WIDTH = 200;
        private static final int THUMBNAIL_MAX_HEIGHT = 200;
        private static final String[] FILE_TYPES = { "png", "jpg", "jpeg", "pdf" };
        private static final int MAX_FILESIZE_IN_BYTES = 1024 * 1024 * 2;
        private static final String MAX_FILESIZE_IN_KILOBYTES = String.valueOf(ImageProperties.MAX_FILESIZE_IN_BYTES / 1024 / 1024) + " MB";
        private static final int MAX_ATTACHMENTS = 10;

        private static final String[] CONTENT_TYPES = { "image/png", "image/jpg", "image/jpeg", "application/pdf", "image/pjpeg", "image/x-png" };
        private static final ImageProperties imageProperties = new ImageProperties();

        private ImageProperties() { }

        public static ImageProperties instance() {
            return imageProperties;
        }

        public Integer getMaxWidth() {
            return MAX_WIDTH;
        }

        public Integer getMaxHeight() {
            return MAX_HEIGHT;
        }

        public Integer getThumbnailMaxWidth() {
            return THUMBNAIL_MAX_WIDTH;
        }

        public Integer getThumbnailMaxHeight() {
            return THUMBNAIL_MAX_HEIGHT;
        }

        public String[] getFileTypes() {
            return FILE_TYPES;
        }

        public int getMaxFilesizeInBytes() {
            return MAX_FILESIZE_IN_BYTES;
        }

        public String getMaxFilesizeInKilobytes() {
            return MAX_FILESIZE_IN_KILOBYTES;
        }

        public int getMaxAttachments() {
            return MAX_ATTACHMENTS;
        }

        public String[] getContentTypes() {
            return CONTENT_TYPES;
        }
    }


}
