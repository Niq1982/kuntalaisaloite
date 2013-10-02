package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.util.ImageModifier;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class AttachmentService {

    public static final String[] FILE_TYPES = { "png", "jpg" };
    public static final String[] CONTENT_TYPES = { "image/png", "image/jpg", "image/jpeg" };

    private long id = 0;

    private String attachmentDir;

    public AttachmentService(String attachmentDir) {
        this.attachmentDir = attachmentDir;
    }


    public void addAttachment(Long initiativeId, LoginUserHolder<User> loginUserHolder, MultipartFile file) throws IOException {
        // loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        validateAndSaveFile(file);
    }

    public void validateAndSaveFile(MultipartFile multipartFile) throws IOException {
        assertFileName(multipartFile.getOriginalFilename());
        assertContentType(multipartFile.getContentType());

        String fileType = multipartFile.getOriginalFilename().split("\\.")[1];
        File file = new File(attachmentDir + "/" + id + "." + fileType);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
            ImageModifier.modify(multipartFile.getInputStream(), fileOutputStream, fileType);
            fileOutputStream.write(multipartFile.getBytes());
        }

    }

    private static void assertFileName(String fileName) {
        if (!isAcceptableFile(fileName)) {
            throw new RuntimeException("Invalid filename");
        }
    }

    public static boolean isAcceptableFile(String fileName) {

        String[] split = fileName.split("\\.");
        if (split.length != 2)
            return false;

        String filePattern = split[1];

        for (String fileType : FILE_TYPES) {
            if (filePattern.equals(fileType))
                return true;
        }

        return false;
    }

    private static void assertContentType(String contentType) {
        for (String type : CONTENT_TYPES) {
            if (type.equals(contentType))
                return;
        }
        throw new RuntimeException("Invalid content-type:" + contentType);
    }
}
