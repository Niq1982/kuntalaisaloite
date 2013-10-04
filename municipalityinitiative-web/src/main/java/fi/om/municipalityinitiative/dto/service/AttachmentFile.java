package fi.om.municipalityinitiative.dto.service;

import org.joda.time.DateTime;

public class AttachmentFile {

    private AttachmentFileInfo info;
    private byte[] bytes;

    public AttachmentFile(AttachmentFileInfo info, byte[] bytes) {
        this.info = info;
        this.bytes = bytes;
    }

    public Long getInitiativeId() {
        return info.getInitiativeId();
    }

    public Long getAttachmentId() {
        return info.getAttachmentId();
    }

    public String getFileName() {
        return info.getDescription();
    }

    public String getContentType() {
        return info.getContentType();
    }

    public DateTime getCreateTime() {
        return info.getCreateTime();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
