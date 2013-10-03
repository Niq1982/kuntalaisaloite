package fi.om.municipalityinitiative.dto.service;

import org.joda.time.DateTime;

public class AttachmentFileInfo {
    private Long initiativeId;
    private Long attachmentId;
    private String fileName;
    private String contentType;
    private DateTime createTime;

    public void setInitiativeId(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public Long getInitiativeId() {
        return initiativeId;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public DateTime getCreateTime() {
        return createTime;
    }
}
