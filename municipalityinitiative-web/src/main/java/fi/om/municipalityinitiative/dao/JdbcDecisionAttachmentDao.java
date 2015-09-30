package fi.om.municipalityinitiative.dao;


import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.sql.QDecisionAttachment.decisionAttachment;

public class JdbcDecisionAttachmentDao implements DecisionAttachmentDao {
    @Resource
    private PostgresQueryFactory queryFactory;

    @Override
    public Long addAttachment(Long initiativeId, DecisionAttachmentFile attachment) {
        return queryFactory.insert(decisionAttachment)
                .set(decisionAttachment.initiativeId, initiativeId)
                .set(decisionAttachment.description, attachment.getFileName())
                .set(decisionAttachment.contentType, attachment.getContentType())
                .set(decisionAttachment.fileType, attachment.getFileType())
                .set(decisionAttachment.added, DateTime.now())
                .executeWithKey(decisionAttachment.id);
    }

    @Override
    public void removeAttachment(Long attachmentId) {

    }

    @Override
    public List<DecisionAttachmentFile> findAllAttachments(Long initiativeId) {
        return  queryFactory.from(decisionAttachment)
                .where(decisionAttachment.initiativeId.eq(initiativeId))
                .list(attachmentMapper);
    }

    private static Expression<DecisionAttachmentFile> attachmentMapper
            =  new MappingProjection<DecisionAttachmentFile>(DecisionAttachmentFile.class, decisionAttachment.all()) {
        @Override
        protected DecisionAttachmentFile map(Tuple row) {
            DecisionAttachmentFile attachmentFileInfo = new DecisionAttachmentFile();

            attachmentFileInfo.setAttachmentId(row.get(decisionAttachment.id));
            attachmentFileInfo.setContentType(row.get(decisionAttachment.contentType));
            attachmentFileInfo.setFileName(row.get(decisionAttachment.description));
            attachmentFileInfo.setCreateTime(row.get(decisionAttachment.added));
            attachmentFileInfo.setInitiativeId(row.get(decisionAttachment.initiativeId));
            attachmentFileInfo.setFileType(row.get(decisionAttachment.fileType));
            return attachmentFileInfo;
        }
    };

}
