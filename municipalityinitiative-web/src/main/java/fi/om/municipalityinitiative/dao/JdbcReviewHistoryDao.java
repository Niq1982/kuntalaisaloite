package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReviewHistoryType;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.sql.QReviewHistory.reviewHistory;

@SQLExceptionTranslated
public class JdbcReviewHistoryDao implements ReviewHistoryDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public void addRejected(Long initiativeId, String moderatorComment) {
        queryFactory.insert(reviewHistory)
                .set(reviewHistory.initiativeId, initiativeId)
                .set(reviewHistory.message, moderatorComment)
                .set(reviewHistory.type, ReviewHistoryType.REVIEW_REJECT)
                .executeWithKey(reviewHistory.id);
    }

    @Override
    public void addAccepted(Long initiativeId, String moderatorComment) {
        queryFactory.insert(reviewHistory)
                .set(reviewHistory.initiativeId, initiativeId)
                .set(reviewHistory.message, moderatorComment)
                .set(reviewHistory.type, ReviewHistoryType.REVIEW_ACCEPT)
                .executeWithKeys(reviewHistory.id);
    }

    @Override
    public void addReviewComment(Long initiativeId, String message) {
        queryFactory.insert(reviewHistory)
                .set(reviewHistory.initiativeId, initiativeId)
                .set(reviewHistory.message, message)
                .set(reviewHistory.type, ReviewHistoryType.REVIEW_COMMENT)
                .executeWithKeys(reviewHistory.id);

    }

    @Override
    public void addReview(Long initiativeId, String initiativeSnapshot) {
        queryFactory.insert(reviewHistory)
                .set(reviewHistory.initiativeId, initiativeId)
                .set(reviewHistory.initiativeSnapshot, initiativeSnapshot)
                .set(reviewHistory.type, ReviewHistoryType.REVIEW_SENT)
                .executeWithKey(reviewHistory.id);
    }

    @Override
    public List<ReviewHistoryRow> findReviewHistoriesAndCommentsOrderedByTime(Long initiativeId) {
        return queryFactory.from(reviewHistory)
                .where(reviewHistory.initiativeId.eq(initiativeId))
                .orderBy(reviewHistory.created.desc())
                .list(reviewHistoryRowWrapper);
    }

    @Override
    public List<ReviewHistoryRow> findReviewHistoriesOrderedByTime(Long initiativeId) {
        return queryFactory.from(reviewHistory)
                .where(reviewHistory.initiativeId.eq(initiativeId))
                .where(reviewHistory.type.ne(ReviewHistoryType.REVIEW_COMMENT))
                .orderBy(reviewHistory.created.desc())
                .list(reviewHistoryRowWrapper);
    }

    public static Expression<ReviewHistoryRow> reviewHistoryRowWrapper =
            new MappingProjection<ReviewHistoryRow>(ReviewHistoryRow.class, reviewHistory.all()) {

                @Override
                protected ReviewHistoryRow map(Tuple row) {
                    ReviewHistoryRow reviewHistoryRow = new ReviewHistoryRow();
                    reviewHistoryRow.setId(row.get(reviewHistory.id));
                    reviewHistoryRow.setCreated(row.get(reviewHistory.created));
                    reviewHistoryRow.setMessage(Maybe.fromNullable(row.get(reviewHistory.message)));
                    reviewHistoryRow.setSnapshot(Maybe.fromNullable(row.get(reviewHistory.initiativeSnapshot)));
                    reviewHistoryRow.setType(row.get(reviewHistory.type));
                    return reviewHistoryRow;
                }
            };

}
