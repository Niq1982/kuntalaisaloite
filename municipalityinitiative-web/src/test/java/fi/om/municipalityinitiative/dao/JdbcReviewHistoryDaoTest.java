package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;
import fi.om.municipalityinitiative.util.ReviewHistoryType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional(readOnly = false)
public class JdbcReviewHistoryDaoTest {

    @Resource
    TestHelper testHelper;

    @Resource
    ReviewHistoryDao reviewHistoryDao;

    private Long initiativeId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        Long testMunicipality = testHelper.createTestMunicipality("Some municipality");
        initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality));
    }

    @Test
    public void add_rejected_review_history() {

        String moderatorComment = "Moderator comment";
        reviewHistoryDao.addRejected(initiativeId, moderatorComment);

        List<ReviewHistoryRow> histories = reviewHistoryDao.findReviewHistories(initiativeId);

        assertThat(histories, hasSize(1));
        assertThat(histories.get(0).getMessage(), isPresent());
        assertThat(histories.get(0).getMessage().get(), is(moderatorComment));
        assertThat(histories.get(0).getType(), is(ReviewHistoryType.REVIEW_REJECT));
    }

    @Test
    public void add_accepted_review_history() {

        String moderatorComment = "Moderator comment";
        reviewHistoryDao.addAccepted(initiativeId, moderatorComment);

        List<ReviewHistoryRow> histories = reviewHistoryDao.findReviewHistories(initiativeId);

        assertThat(histories, hasSize(1));
        assertThat(histories.get(0).getMessage(), isPresent());
        assertThat(histories.get(0).getMessage().get(), is(moderatorComment));
        assertThat(histories.get(0).getSnapshot(), isNotPresent());
        assertThat(histories.get(0).getType(), is(ReviewHistoryType.REVIEW_ACCEPT));
    }

    @Test
    public void add_send_to_review_history() {
        String initiativeSnapshot = "Some snapshot text";

        reviewHistoryDao.addReview(initiativeId, initiativeSnapshot);

        List<ReviewHistoryRow> histories = reviewHistoryDao.findReviewHistories(initiativeId);

        assertThat(histories, hasSize(1));
        assertThat(histories.get(0).getMessage(), isNotPresent());
        assertThat(histories.get(0).getSnapshot(), isPresent());
        assertThat(histories.get(0).getSnapshot().get(), is(initiativeSnapshot));
        assertThat(histories.get(0).getType(), is(ReviewHistoryType.REVIEW_SENT));

    }
}
