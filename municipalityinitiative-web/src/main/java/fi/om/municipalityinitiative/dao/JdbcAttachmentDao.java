package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QAttachment;

import javax.annotation.Resource;
import java.util.List;

@SQLExceptionTranslated
public class JdbcAttachmentDao implements AttachmentDao{

    @Resource
    private PostgresQueryFactory queryFactory;

    @Override
    public Long addAttachment(Long initiativeId, String originalFilename) {
        return queryFactory.insert(QAttachment.attachment)
                .set(QAttachment.attachment.initiativeId, initiativeId)
                .set(QAttachment.attachment.filename, originalFilename)
                .executeWithKey(QAttachment.attachment.id);

    }

    @Override
    public List<Long> findAttachments(Long initiativeId) {
        return queryFactory.from(QAttachment.attachment)
                .where(QAttachment.attachment.initiativeId.eq(initiativeId))
                .list(QAttachment.attachment.id);

    }
}
