package fi.om.municipalityinitiative.newdao;


import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.expr.DateTimeExpression;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QAuthorInvitation;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QParticipant;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcAuthorDao implements AuthorDao {

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = false)
    public Long addAuthorInvitation(AuthorInvitation authorInvitation) {
        return queryFactory.insert(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.confirmationCode, authorInvitation.getConfirmationCode())
                .set(QAuthorInvitation.authorInvitation.email, authorInvitation.getEmail())
                .set(QAuthorInvitation.authorInvitation.name, authorInvitation.getName())
                .set(QAuthorInvitation.authorInvitation.invitationTime, authorInvitation.getInvitationTime())
                .executeWithKey(QAuthorInvitation.authorInvitation.id);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode) {
        AuthorInvitation authorInvitation = queryFactory.from(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .singleResult(authorInvitationMapping);
        if (authorInvitation == null) {
            throw new NotFoundException(QAuthorInvitation.authorInvitation.getTableName(), initiativeId + ":" + confirmationCode);
        }
        return authorInvitation;
    }

    private static <T> T notNull(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAuthorInvitation(Long initiativeId, String confirmationCode) {
        long affectedRows = queryFactory.delete(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .execute();
        if (affectedRows != 1) {
            throw new NotFoundException(QAuthorInvitation.authorInvitation.getTableName(), initiativeId + ":" + confirmationCode);
        }
    }

    Expression<AuthorInvitation> authorInvitationMapping =
            new MappingProjection<AuthorInvitation>(AuthorInvitation.class,
                    QAuthorInvitation.authorInvitation.all()) {

                @Override
                protected AuthorInvitation map(Tuple row) {
                    AuthorInvitation authorInvitation = new AuthorInvitation();

                    authorInvitation.setConfirmationCode(row.get(QAuthorInvitation.authorInvitation.confirmationCode));
                    authorInvitation.setInitiativeId(row.get(QAuthorInvitation.authorInvitation.initiativeId));
                    authorInvitation.setEmail(row.get(QAuthorInvitation.authorInvitation.email));
                    authorInvitation.setInvitationTime(row.get(QAuthorInvitation.authorInvitation.invitationTime));
                    authorInvitation.setName(row.get(QAuthorInvitation.authorInvitation.name));

                    return authorInvitation;

                }
            };

}
