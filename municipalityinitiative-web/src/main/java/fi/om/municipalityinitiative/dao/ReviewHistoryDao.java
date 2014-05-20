package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;

import java.util.List;

public interface ReviewHistoryDao {

    void addRejected(Long initiativeId, String moderatorComment);

    void addAccepted(Long initiativeId, String moderatorComment);

    void addReviewComment(Long initiativeId, String message);

    List<ReviewHistoryRow> findReviewHistoriesOrderedByTime(Long initiativeId);

    void addReview(Long initiativeId, String initiativeSnapshot);

    List<ReviewHistoryRow> findReviewHistoriesAndCommentsOrderedByTime(Long initiativeId);
}
