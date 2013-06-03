package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.sql.QAuthorMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.newdao.JdbcInitiativeDao.assertSingleAffection;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcAuthorMessageDao implements AuthorMessageDao {

    @Resource
    PostgresQueryFactory queryFactory;
    private Expression<AuthorMessage> authorMessageMapping = new MappingProjection<AuthorMessage>(AuthorMessage.class,
            QAuthorMessage.authorMessage.all()) {
        @Override
        protected AuthorMessage map(Tuple row) {

            AuthorMessage authorMessage = new AuthorMessage();
            authorMessage.setInitiativeId(row.get(QAuthorMessage.authorMessage.initiativeId));
            authorMessage.setContactName(row.get(QAuthorMessage.authorMessage.contactor));
            authorMessage.setContactEmail(row.get(QAuthorMessage.authorMessage.contactorEmail));
            authorMessage.setMessage(row.get(QAuthorMessage.authorMessage.message));
            authorMessage.setConfirmationCode(row.get(QAuthorMessage.authorMessage.confirmationCode));
            return authorMessage;
        }
    };

    @Override
    @Transactional(readOnly = false)
    public Long addAuthorMessage(AuthorMessage authorMessage) {
        return queryFactory.insert(QAuthorMessage.authorMessage)
                .set(QAuthorMessage.authorMessage.contactor, authorMessage.getContactName())
                .set(QAuthorMessage.authorMessage.contactorEmail, authorMessage.getContactEmail())
                .set(QAuthorMessage.authorMessage.initiativeId, authorMessage.getInitiativeId())
                .set(QAuthorMessage.authorMessage.message, authorMessage.getMessage())
                .set(QAuthorMessage.authorMessage.confirmationCode, authorMessage.getConfirmationCode())
                .executeWithKey(QAuthorMessage.authorMessage.id);

    }

    @Override
    public AuthorMessage getAuthorMessage(String confirmationCode) {
        return queryFactory.from(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .uniqueResult(authorMessageMapping);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAuthorMessage(String confirmationCode) {
        assertSingleAffection(queryFactory.delete(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .execute());
    }
}
