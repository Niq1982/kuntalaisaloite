package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;

import java.util.List;

public interface DecisionAttachmentDao {



    public void addAttachment(DecisionAttachmentFile attachment, Long initiativeId);

    public void removeAttachments(Long initiativeId);

    public List<DecisionAttachmentFile>  findAllAttachments(Long initiativeId);

}
