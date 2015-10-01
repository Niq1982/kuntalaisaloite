package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;

import java.util.List;

public interface DecisionAttachmentDao {

    Long addAttachment(Long initiativeId, DecisionAttachmentFile attachment);

    void removeAttachment(Long attachmentId);

    List<DecisionAttachmentFile> findAllAttachments(Long initiativeId);

    DecisionAttachmentFile getAttachment(Long attachmentId);
}
