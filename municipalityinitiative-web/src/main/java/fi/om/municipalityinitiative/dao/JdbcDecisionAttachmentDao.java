package fi.om.municipalityinitiative.dao;


import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.sql.QDecisionAttachment.decisionAttachment;

public class JdbcDecisionAttachmentDao implements DecisionAttachmentDao {
    @Resource
    private PostgresQueryFactory queryFactory;

    @Override
    public void addAttachment(DecisionAttachmentFile attachment, Long initiativeId) {
        queryFactory.insert(decisionAttachment)
                .set(decisionAttachment.initiativeId, initiativeId)
                .set(decisionAttachment.description, attachment.getFileName())
                .set(decisionAttachment.contentType, attachment.getContentType())
                .set(decisionAttachment.fileType, attachment.getFileType())
                .executeWithKey(decisionAttachment.id);
    }

    @Override
    public void removeAttachments(Long initiativeId) {

    }

    @Override
    public List<DecisionAttachmentFile> findAllAttachments(Long initiativeId) {
        return null;
    }


}
