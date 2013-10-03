package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;

import java.util.List;

public interface AttachmentDao {
    Long addAttachment(Long initiativeId, String originalFilename, String contentType);

    List<AttachmentFileInfo> findAttachments(Long initiativeId);

    List<AttachmentFileInfo> find(Long initiativeId);

    AttachmentFileInfo getAttachment(Long attachmentId);
}
