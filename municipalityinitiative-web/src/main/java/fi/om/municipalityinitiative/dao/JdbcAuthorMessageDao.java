package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.sql.QAuthorMessage;

import javax.annotation.Resource;

@SQLExceptionTranslated
public class JdbcAuthorMessageDao implements AuthorMessageDao {

    static public Expression<AuthorMessage> authorMessageMapping = new MappingProjection<AuthorMessage>(AuthorMessage.class,
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
    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long put(AuthorMessage authorMessage) {
        return queryFactory.insert(QAuthorMessage.authorMessage)
                .set(QAuthorMessage.authorMessage.contactor, authorMessage.getContactName())
                .set(QAuthorMessage.authorMessage.contactorEmail, authorMessage.getContactEmail())
                .set(QAuthorMessage.authorMessage.initiativeId, authorMessage.getInitiativeId())
                .set(QAuthorMessage.authorMessage.message, authorMessage.getMessage())
                .set(QAuthorMessage.authorMessage.confirmationCode, authorMessage.getConfirmationCode())
                .execute();

    }

    @Override
    public AuthorMessage pop(String confirmationCode) {
        AuthorMessage authorMessage = queryFactory.from(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .uniqueResult(authorMessageMapping);

        if (authorMessage == null) {
            throw new NotFoundException("AuthorMessage", confirmationCode);
        }

        JdbcInitiativeDao.assertSingleAffection(queryFactory.delete(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .execute());

        return authorMessage;
    }

}
