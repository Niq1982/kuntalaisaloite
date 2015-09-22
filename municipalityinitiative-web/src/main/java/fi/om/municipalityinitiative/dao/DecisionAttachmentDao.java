package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;

import java.util.List;

public interface DecisionAttachmentDao {

    void addAttachment(Long initiativeId, DecisionAttachmentFile attachment);

    void removeAttachment(Long attachmentId);

    List<DecisionAttachmentFile> findAllAttachments(Long initiativeId);
}
