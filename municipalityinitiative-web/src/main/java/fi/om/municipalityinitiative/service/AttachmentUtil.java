package fi.om.municipalityinitiative.service;


import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.AttachmentFileBase;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.util.ImageModifier;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.aspectj.util.FileUtil;
import org.im4java.core.IM4JavaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public final class AttachmentUtil {


    private AttachmentUtil(){

    }


    public static String getFileType(MultipartFile file) throws InvalidAttachmentException {
        // Some checks for valid filename
        String fileType = parseFileType(file.getOriginalFilename());
        assertValidFileType(fileType);
        assertValidContentType(file.getContentType());
        return fileType;
    }



    public static void assertValidFileType(String givenFileType) throws InvalidAttachmentException {
        for (String fileType : ImageProperties.FILE_TYPES) {
            if (fileType.equalsIgnoreCase(givenFileType))
                return;
        }
        throw new InvalidAttachmentException("Invalid fileName: "+givenFileType);
    }



    public static void saveAttachmentToDiskAndCreateThumbnail(ImageModifier imageModifier, String contentType, String fileType, File tempFile, Long attachmentId, String attachmentDir) throws IOException, IM4JavaException, InterruptedException {
        if (isPdfContentType(contentType)) {
            FileUtil.copyValidFiles(tempFile, new File(AttachmentUtil.getFilePath(attachmentId, fileType, attachmentDir)));
        }
        else {
            imageModifier.modify(tempFile, AttachmentUtil.getFilePath(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.MAX_WIDTH, AttachmentUtil.ImageProperties.MAX_HEIGHT);
            imageModifier.modify(tempFile, AttachmentUtil.getThumbnailPath(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.THUMBNAIL_MAX_WIDTH, AttachmentUtil.ImageProperties.THUMBNAIL_MAX_HEIGHT);
        }
    }
    public static void saveMunicipalityAttachmentToDiskAndCreateThumbnail(ImageModifier imageModifier, String contentType, String fileType, File tempFile, Long attachmentId, String attachmentDir) throws IOException, IM4JavaException, InterruptedException {
        if (isPdfContentType(contentType)) {
            FileUtil.copyValidFiles(tempFile, new File(AttachmentUtil.getFilePathForMunicipalityAttachment(attachmentId, fileType, attachmentDir)));
        }
        else {
            imageModifier.modify(tempFile, AttachmentUtil.getFilePathForMunicipalityAttachment(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.MAX_WIDTH, AttachmentUtil.ImageProperties.MAX_HEIGHT);
            imageModifier.modify(tempFile, AttachmentUtil.getThumbnailFilePathForMunicipalityAttachment(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.THUMBNAIL_MAX_WIDTH, AttachmentUtil.ImageProperties.THUMBNAIL_MAX_HEIGHT);
        }
    }

    public static AttachmentFile getAttachmentFile(String fileName, AttachmentFileBase attachmentInfo, String attachmentDir) throws IOException {
        if (!attachmentInfo.getFileName().equals(fileName)) {
            throw new AccessDeniedException("Invalid filename for attachment " + attachmentInfo.getAttachmentId() + " - " + fileName);
        }
        String attachmentPath;
        if (attachmentInfo.isMunicipalityAttachment()) {
            attachmentPath = getFilePathForMunicipalityAttachment(attachmentInfo.getAttachmentId(), attachmentInfo.getFileType(), attachmentDir);
        } else {
            attachmentPath = getFilePath(attachmentInfo.getAttachmentId(), attachmentInfo.getFileType(), attachmentDir);
        }
        byte[] attachmentBytes = getFileBytes(attachmentPath);
        return new AttachmentFile(attachmentInfo, attachmentBytes);
    }

    public static AttachmentFile getThumbnailForImageAttachment(Long attachmentId, AttachmentFileBase attachmentInfo, String attachmentDir) throws IOException {
        if (attachmentInfo.isPdf()) {
            throw new AccessDeniedException("no thumbnail for pdf");
        }
        String thumbnailPath;
        if (attachmentInfo.isMunicipalityAttachment()) {
            thumbnailPath = getThumbnailFilePathForMunicipalityAttachment(attachmentId, attachmentInfo.getFileType(), attachmentDir);
        } else {
            thumbnailPath = getThumbnailPath(attachmentId, attachmentInfo.getFileType(), attachmentDir);
        }
        byte[] attachmentBytes = getFileBytes(thumbnailPath);
        return new AttachmentFile(attachmentInfo, attachmentBytes);
    }

    public static String getFilePath(Long attachmentId, String fileType, String attachmentDir) {
        return attachmentDir + "/" + attachmentId + "." + fileType;
    }

    public static String getThumbnailPath(Long attachmentId, String fileType, String attachmentDir ) {
        return attachmentDir + "/" + attachmentId + "_thumbnail" + "." + fileType;
    }

    public static String getFilePathForMunicipalityAttachment(long attachmentId, String fileType, String attachmentDir) {
        return attachmentDir + "/" + "decision_" + attachmentId + "." + fileType;
    }
    public static String getThumbnailFilePathForMunicipalityAttachment(long attachmentId, String fileType, String attachmentDir) {
        return attachmentDir + "/" +  "decision_" + attachmentId + "_thumbnail" + "." + fileType;
    }

    private static void assertValidContentType(String contentType) throws InvalidAttachmentException {
        for (String type : ImageProperties.CONTENT_TYPES) {
            if (type.equalsIgnoreCase(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
    }

    public static File createTempFile(MultipartFile file, String fileType) throws IOException, InvalidAttachmentException {
        // Create temp-file for proper file handling
        File tempFile = File.createTempFile(RandomHashGenerator.shortHash(), "." + fileType);

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



    private static byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = FileUtil.readAsByteArray(file);
        return Arrays.copyOf(bytes, bytes.length);
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

    static void assertFileSize(MultipartFile file) throws InvalidAttachmentException {
        // Double check, is checked at validation also
        if (file.getSize() > ImageProperties.MAX_FILESIZE_IN_BYTES) {
            throw new InvalidAttachmentException("Too large file: " + file.getSize());
        }
    }

    public static <T> T notNull(T object, Class clazz, Long id) {
        if (object == null) {
            throw new NotFoundException(clazz.toString(), id);
        }
        return object;
    }

    public static boolean isPdfContentType(String contentType) {
        return contentType.equals(MediaType.PDF.toString());
    }

    static String parseFileType(String fileName) throws InvalidAttachmentException {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new InvalidAttachmentException("Invalid filename: " + fileName);
        }

        return split[split.length-1];
    }


    public static final class ImageProperties {

        public static final int MAX_WIDTH = 1000;

        public static final int MAX_HEIGHT = 1000;
        public static final int THUMBNAIL_MAX_WIDTH = 200;
        public static final int THUMBNAIL_MAX_HEIGHT = 200;
        public static final String[] FILE_TYPES = { "png", "jpg", "jpeg", "pdf" };
        public static final int MAX_FILESIZE_IN_BYTES = 1024 * 1024 * 2;
        public static final String MAX_FILESIZE_IN_KILOBYTES = String.valueOf(ImageProperties.MAX_FILESIZE_IN_BYTES / 1024 / 1024) + " MB";
        public static final int MAX_ATTACHMENTS = 10;

        public static final String[] CONTENT_TYPES = { "image/png", "image/jpg", "image/jpeg", "application/pdf", "image/pjpeg", "image/x-png" };
        public static final ImageProperties imageProperties = new ImageProperties();

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

    public static class Attachments {
        private final List<AttachmentFileBase> images = Lists.newArrayList();
        private final List<AttachmentFileBase> pdfs = Lists.newArrayList();

        public int count() {
            return getAll().size();
        }

        public Attachments(List<? extends AttachmentFileBase> attachments) {
            for (AttachmentFileBase attachment : attachments) {
                if (attachment.isPdf()) {
                    pdfs.add(attachment);
                }
                else {
                    images.add(attachment);
                }
            }
        }

        public List<AttachmentFileBase> getImages() {
            return images;
        }

        public List<AttachmentFileBase> getPdfs() {
            return pdfs;
        }

        public List<AttachmentFileBase> getAll() {
            List<AttachmentFileBase> all = Lists.newArrayList(images);
            all.addAll(pdfs);
            return all;
        }
    }
}
