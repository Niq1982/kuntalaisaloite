package fi.om.municipalityinitiative.service;


import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.service.AttachmentFileBase;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.util.ImageModifier;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.aspectj.util.FileUtil;
import org.im4java.core.IM4JavaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class AttachmentUtil {


    private AttachmentUtil(){

    }


    public static String getFileType(MultipartFile file) throws InvalidAttachmentException {
        // Some checks for valid filename
        String fileType = parseFileType(file.getOriginalFilename());
        assertFileType(fileType);
        assertContentType(file.getContentType());
        return fileType;
    }


    private static String parseFileType(String fileName) throws InvalidAttachmentException {
        String[] split = fileName.split("\\.");
        if (split.length == 1) {
            throw new InvalidAttachmentException("Invalid filename: " + fileName);
        }

        return split[split.length-1];
    }

    public static void assertFileType(String givenFileType) throws InvalidAttachmentException {
        for (String fileType : ImageProperties.FILE_TYPES) {
            if (fileType.equalsIgnoreCase(givenFileType))
                return;
        }
        throw new InvalidAttachmentException("Invalid fileName: "+givenFileType);
    }

    public static void saveFileToDisk(ImageModifier imageModifier, MultipartFile file, String fileType, File tempFile, Long attachmentId, String attachmentDir) throws IOException, IM4JavaException, InterruptedException {
        if (AttachmentFileInfo.isPdfContentType(file.getContentType())) {
            FileUtil.copyValidFiles(tempFile, new File(AttachmentUtil.getFilePath(attachmentId, fileType, attachmentDir)));
        }
        else {
            imageModifier.modify(tempFile, AttachmentUtil.getFilePath(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.MAX_WIDTH, AttachmentUtil.ImageProperties.MAX_HEIGHT);
            imageModifier.modify(tempFile, AttachmentUtil.getThumbnailPath(attachmentId, fileType, attachmentDir), AttachmentUtil.ImageProperties.THUMBNAIL_MAX_WIDTH, AttachmentUtil.ImageProperties.THUMBNAIL_MAX_HEIGHT);
        }
    }
    public static String getFilePath(Long attachmentId, String fileType, String attachmentDir) {
        return attachmentDir + "/" + attachmentId + "." + fileType;
    }

    public static String getThumbnailPath(Long attachmentId, String fileType, String attachmentDir ) {
        return attachmentDir + "/" + attachmentId + "_thumbnail" + "." + fileType;
    }

    private static void assertContentType(String contentType) throws InvalidAttachmentException {
        for (String type : ImageProperties.CONTENT_TYPES) {
            if (type.equalsIgnoreCase(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
    }

    public static File createTempFile(MultipartFile file, String fileType) throws IOException, InvalidAttachmentException {
        File tempFile = null;
        // Create temp-file for proper file handling
        tempFile = File.createTempFile(RandomHashGenerator.shortHash(), "." + fileType);

        file.transferTo(tempFile);

        assertRealFileContent(tempFile, fileType);
        return tempFile;
    }

    public static void assertRealFileContent(File tempFile, String fileType) throws IOException, InvalidAttachmentException {
        if ((fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("jpeg")) && AttachmentService.isJPEG(tempFile)) {
            return;
        }
        if (fileType.equalsIgnoreCase("png") && AttachmentService.isPNG(tempFile)) {
            return;
        }
        if (fileType.equalsIgnoreCase("pdf") && AttachmentService.isPDF(tempFile)) {
            return;
        }
        else {
            throw new InvalidAttachmentException("File content was invalid for filetype: " + fileType);
        }
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
