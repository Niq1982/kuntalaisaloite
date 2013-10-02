package fi.om.municipalityinitiative.dao;

import java.util.List;

public interface AttachmentDao {
    Long addAttachment(Long initiativeId, String originalFilename);

    List<Long> findAttachments(Long initiativeId);
}
