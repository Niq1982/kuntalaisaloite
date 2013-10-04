package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;

import java.util.List;

public interface AttachmentDao {
    Long addAttachment(Long initiativeId, String description, String contentType);

    List<AttachmentFileInfo> findAcceptedAttachments(Long initiativeId);

    List<AttachmentFileInfo> findAllAttachments(Long initiativeId);

    AttachmentFileInfo getAttachment(Long attachmentId);

    void acceptAttachments(long initiativeId);

    void rejectAttachments(Long initiativeId);

    void deleteAttachment(Long attachmentId);
}
