package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import org.springframework.web.multipart.MultipartFile;

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

    private static void assertContentType(String contentType) throws InvalidAttachmentException {
        for (String type : ImageProperties.CONTENT_TYPES) {
            if (type.equalsIgnoreCase(contentType))
                return;
        }
        throw new InvalidAttachmentException("Invalid content-type:" + contentType);
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

}
