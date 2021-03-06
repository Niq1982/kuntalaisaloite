package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;
import fi.om.municipalityinitiative.service.AttachmentUtil;
import fi.om.municipalityinitiative.sql.QAttachment;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;

@SQLExceptionTranslated
public class JdbcAttachmentDao implements AttachmentDao{

    @Resource
    private PostgresQueryFactory queryFactory;

    @Override
    public Long addAttachment(Long initiativeId, String description, String contentType, String fileType) {
        return queryFactory.insert(QAttachment.attachment)
                .set(QAttachment.attachment.initiativeId, initiativeId)
                .set(QAttachment.attachment.description, description)
                .set(QAttachment.attachment.contentType, contentType)
                .set(QAttachment.attachment.fileType, fileType)
                .executeWithKey(QAttachment.attachment.id);

    }

    @Override
    public List<AttachmentFileInfo> findAcceptedAttachments(Long initiativeId) {
        return queryFactory.from(QAttachment.attachment)
                .where(QAttachment.attachment.initiativeId.eq(initiativeId))
                .where(QAttachment.attachment.accepted.eq(true))
                .orderBy(QAttachment.attachment.description.asc())
                .list(attachmentMapper);

    }

    @Override
    public List<AttachmentFileInfo> findAllAttachments(Long initiativeId) {
        return queryFactory.from(QAttachment.attachment)
                .where(QAttachment.attachment.initiativeId.eq(initiativeId))
                .orderBy(QAttachment.attachment.description.asc())
                .list(attachmentMapper);
    }

    @Override
    public AttachmentFileInfo getAttachment(Long attachmentId) {
        return AttachmentUtil.notNull(queryFactory.from(QAttachment.attachment)
                        .where(QAttachment.attachment.id.eq(attachmentId))
                        .uniqueResult(attachmentMapper),
                AttachmentFileInfo.class, attachmentId);
    }

    @Override
    public void acceptAttachments(long initiativeId) {
        queryFactory.update(QAttachment.attachment)
                .set(QAttachment.attachment.accepted, true)
                .where(QAttachment.attachment.initiativeId.eq(initiativeId))
                .execute();
    }

    @Override
    public void rejectAttachments(Long initiativeId) {
        queryFactory.update(QAttachment.attachment)
                .set(QAttachment.attachment.accepted, false)
                .where(QAttachment.attachment.initiativeId.eq(initiativeId))
                .execute();
    }

    @Override
    public void deleteAttachment(Long attachmentId) {
        assertSingleAffection(queryFactory.delete(QAttachment.attachment)
                .where(QAttachment.attachment.id.eq(attachmentId))
                .execute());
    }

    static Expression<AttachmentFileInfo> attachmentMapper
            =  new MappingProjection<AttachmentFileInfo>(AttachmentFileInfo.class,QAttachment.attachment.all()) {
        @Override
        protected AttachmentFileInfo map(Tuple row) {
            AttachmentFileInfo attachmentFileInfo = new AttachmentFileInfo();

            attachmentFileInfo.setAttachmentId(row.get(QAttachment.attachment.id));
            attachmentFileInfo.setContentType(row.get(QAttachment.attachment.contentType));
            attachmentFileInfo.setDescription(row.get(QAttachment.attachment.description));
            attachmentFileInfo.setCreateTime(row.get(QAttachment.attachment.added));
            attachmentFileInfo.setInitiativeId(row.get(QAttachment.attachment.initiativeId));
            attachmentFileInfo.setAccepted(row.get(QAttachment.attachment.accepted));
            attachmentFileInfo.setFileType(row.get(QAttachment.attachment.fileType));
            return attachmentFileInfo;
        }
    };

}
