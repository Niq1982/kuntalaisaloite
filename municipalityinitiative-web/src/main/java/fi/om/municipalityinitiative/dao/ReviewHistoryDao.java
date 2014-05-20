package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;

import java.util.List;

public interface ReviewHistoryDao {

    void addRejected(Long initiativeId, String moderatorComment);

    void addAccepted(Long initiativeId, String moderatorComment);

    List<ReviewHistoryRow> findReviewHistoriesOrderedByTime(Long initiativeId);

    void addReview(Long initiativeId, String initiativeSnapshot);
}
