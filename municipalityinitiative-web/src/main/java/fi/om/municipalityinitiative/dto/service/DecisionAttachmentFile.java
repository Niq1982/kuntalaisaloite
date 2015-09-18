package fi.om.municipalityinitiative.dto.service;


import org.joda.time.DateTime;

public class DecisionAttachmentFile extends AttachmentFileBase{

    private final Long initiativeId;

    private final String fileName;

    private final String contentType;

    private String fileType;

    public DecisionAttachmentFile(String fileName, String fileType, String contentType, Long initiativeId){
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileType  = fileType;
        this.initiativeId = initiativeId;
    }

    @Override
    public Long getInitiativeId() {
        return null;
    }

    @Override
    public Long getAttachmentId() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public DateTime getCreateTime() {
        return null;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
