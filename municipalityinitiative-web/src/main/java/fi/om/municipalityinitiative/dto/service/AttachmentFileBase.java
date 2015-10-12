package fi.om.municipalityinitiative.dto.service;


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



}
