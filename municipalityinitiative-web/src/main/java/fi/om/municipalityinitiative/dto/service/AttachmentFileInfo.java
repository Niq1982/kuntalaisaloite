package fi.om.municipalityinitiative.dto.service;

import org.joda.time.DateTime;

public class AttachmentFileInfo {
    private Long initiativeId;
    private Long attachmentId;
    private String description;
    private String contentType;
    private DateTime createTime;
    private boolean accepted;
    private String fileType;

    public void setInitiativeId(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public boolean isPdf() {
        return isPdfContentType(contentType);
    }

    public static boolean isPdfContentType(String contentType) {
        return contentType.equals("application/pdf");
    }

    public String getFileName() {
        return description.replaceAll("[^a-zA-Z0-9]", "_") + "." + fileType;
    }
}