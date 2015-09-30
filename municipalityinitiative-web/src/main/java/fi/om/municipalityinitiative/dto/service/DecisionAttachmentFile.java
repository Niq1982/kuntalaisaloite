package fi.om.municipalityinitiative.dto.service;


import org.joda.time.DateTime;

public class DecisionAttachmentFile extends AttachmentFileBase{

    private Long attachmentId;

    private Long initiativeId;

    private String fileName;

    private String contentType;

    private String fileType;

    private DateTime createTime;

    public DecisionAttachmentFile(String fileName, String fileType, String contentType, Long initiativeId){
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileType  = fileType;
        this.initiativeId = initiativeId;
    }
    public DecisionAttachmentFile(){


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
    public String getFileName() {
        return fileName;
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
    public boolean isPdf() {
        return isPdfContentType(contentType);
    }

    @Override
    public boolean isAccepted() {
        return true;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public String getDescription() {
        return fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setInitiativeId(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setAttachmentId(Long id) {
        this.attachmentId = id;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }
}
