package fi.om.municipalityinitiative.newdao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.sql.QAuthorMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.newdao.JdbcInitiativeDao.assertSingleAffection;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcAuthorMessageDao implements AuthorMessageDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = false)
    public Long put(AuthorMessage authorMessage) {
        return queryFactory.insert(QAuthorMessage.authorMessage)
                .set(QAuthorMessage.authorMessage.contactor, authorMessage.getContactName())
                .set(QAuthorMessage.authorMessage.contactorEmail, authorMessage.getContactEmail())
                .set(QAuthorMessage.authorMessage.initiativeId, authorMessage.getInitiativeId())
                .set(QAuthorMessage.authorMessage.message, authorMessage.getMessage())
                .set(QAuthorMessage.authorMessage.confirmationCode, authorMessage.getConfirmationCode())
                .executeWithKey(QAuthorMessage.authorMessage.id);

    }

    @Override
    @Transactional(readOnly = false)
    public AuthorMessage pop(String confirmationCode) {
        AuthorMessage authorMessage = queryFactory.from(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .uniqueResult(Mappings.authorMessageMapping);

        if (authorMessage == null) {
            throw new NotFoundException("AuthorMessage", confirmationCode);
        }

        assertSingleAffection(queryFactory.delete(QAuthorMessage.authorMessage)
                .where(QAuthorMessage.authorMessage.confirmationCode.eq(confirmationCode))
                .execute());

        return authorMessage;
    }

}
