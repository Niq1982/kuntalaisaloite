package fi.om.municipalityinitiative.dto.service;

import com.google.common.net.MediaType;
import org.joda.time.DateTime;

public abstract class AttachmentFileBase {

    public abstract Long getInitiativeId();

    public abstract Long getAttachmentId();

    public abstract String getFileName();

    public abstract String getContentType();

    public abstract DateTime getCreateTime();

    public abstract boolean isPdf();

    public abstract  boolean isAccepted();

    public abstract String getFileType();

    public abstract String getDescription();

    public abstract boolean isMunicipalityAttachment();

    public static boolean isPdfContentType(String contentType) {
        return contentType.equals(MediaType.PDF.toString());
    }
}
