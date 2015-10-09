package fi.om.municipalityinitiative.dto.service;

import org.joda.time.DateTime;

public class AttachmentFileInfo extends AttachmentFileBase{
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

    @Override
    public Long getInitiativeId() {
        return initiativeId;
    }

    @Override
    public Long getAttachmentId() {
        return attachmentId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isMunicipalityAttachment() {
        return false;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public DateTime getCreateTime() {
        return createTime;
    }

    @Override
    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    public boolean isPdf() {
        return isPdfContentType(contentType);
    }


    public String getFileName() {
        return description
                .replace('ä', 'a')
                .replace("Ä", "A")
                .replace("ö", "o")
                .replace("Ö", "O")
                .replace("å", "a")
                .replace("Å", "A")
                .replaceAll("[^a-zA-Z0-9]", "_") + "." + fileType;
    }
}