package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;
import difflib.Delta;
import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ReviewHistoryDiffTest {


    private List<ReviewHistoryRow> rows;

    @Before
    public void setup() {
        rows = Lists.newArrayList();

        rows.add(row(1, ReviewHistoryType.REVIEW_SENT, new LocalDate(2010, 1, 1), "First sent"));
        rows.add(row(2, ReviewHistoryType.REVIEW_REJECT, new LocalDate(2010, 1, 2), ""));
        rows.add(row(3, ReviewHistoryType.REVIEW_SENT, new LocalDate(2010, 1, 3), "Second sent"));
        rows.add(row(4, ReviewHistoryType.REVIEW_ACCEPT, new LocalDate(2010, 1, 4), ""));
        rows.add(row(5, ReviewHistoryType.REVIEW_REJECT, new LocalDate(2010, 1, 5), ""));
        rows.add(row(6, ReviewHistoryType.REVIEW_SENT, new LocalDate(2010, 1, 3), "Third sent"));
        rows.add(row(7, ReviewHistoryType.REVIEW_ACCEPT, new LocalDate(2010, 1, 4), ""));
        Collections.reverse(rows);
    }


    @Test
    public void returns_row_with_given_id_and_diff_to_previous() {
        assertThat(ReviewHistoryDiff.from(rows, 1L).getNewText().get(0), is("First sent"));
        assertThat(ReviewHistoryDiff.from(rows, 1L).getOldText(), isNotPresent());

        assertThat(ReviewHistoryDiff.from(rows, 3L).getNewText().get(0), is("Second sent"));
        assertThat(ReviewHistoryDiff.from(rows, 3L).getOldText(), isPresent());
        assertThat(ReviewHistoryDiff.from(rows, 3L).getOldText().get().get(0), is("First sent"));

        assertThat(ReviewHistoryDiff.from(rows, 6L).getNewText().get(0), is("Third sent"));
        assertThat(ReviewHistoryDiff.from(rows, 6L).getOldText(), isPresent());
        assertThat(ReviewHistoryDiff.from(rows, 6L).getOldText().get().get(0), is("Second sent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throws_exception_if_no_review_sent_with_given_id() {
        ReviewHistoryDiff.from(rows, -2l);
    }

    @Test
    public void returns_diff_line_by_line() {
        rows.clear();
        rows.add(row(1, ReviewHistoryType.REVIEW_SENT, new LocalDate(2010, 1, 1), "Rivi yksi\n" +
                        "Rivi kaksi\n" +
                        "Rivi kolme\n" +
                        "Rivi koljapuol\n" +
                        "Rivi viis\n"+
                        "Rivi kuus\n"
        ));
        rows.add(row(3, ReviewHistoryType.REVIEW_SENT, new LocalDate(2010, 1, 3), "Rivi yksi\n" +
                "Rivi kaksi\n" +
                "Rivi kolmee\n" +
                "Rivi koljapuol\n" +
                "Rivi neljä\n"+
                "Rivi neljäpuol\n"+
                "Rivi viis"));
        Collections.reverse(rows);
        List<ReviewHistoryDiff.DiffLine> diff = ReviewHistoryDiff.from(rows, 3L).getDiff();

        assertThat(diff.get(0).modificationType, isNotPresent());
        assertThat(diff.get(0).line, is("Rivi yksi"));
        assertThat(diff.get(1).modificationType, isNotPresent());
        assertThat(diff.get(1).line, is("Rivi kaksi"));

        assertThat(diff.get(2).modificationType, isPresent());
        assertThat(diff.get(2).modificationType.get(), is(Delta.TYPE.DELETE));
        assertThat(diff.get(2).line, is("Rivi kolme"));
        assertThat(diff.get(3).modificationType, isPresent());
        assertThat(diff.get(3).modificationType.get(), is(Delta.TYPE.INSERT));
        assertThat(diff.get(3).line, is("Rivi kolmee"));

        assertThat(diff.get(4).modificationType, isNotPresent());
        assertThat(diff.get(4).line, is("Rivi koljapuol"));

        assertThat(diff.get(5).modificationType, isPresent());
        assertThat(diff.get(5).modificationType.get(), is(Delta.TYPE.INSERT));
        assertThat(diff.get(5).line, is("Rivi neljä"));

        assertThat(diff.get(6).modificationType, isPresent());
        assertThat(diff.get(6).modificationType.get(), is(Delta.TYPE.INSERT));
        assertThat(diff.get(6).line, is("Rivi neljäpuol"));

        assertThat(diff.get(7).modificationType, isNotPresent());
        assertThat(diff.get(7).line, is("Rivi viis"));

        assertThat(diff.get(8).modificationType, isPresent());
        assertThat(diff.get(8).modificationType.get(), is(Delta.TYPE.DELETE));
        assertThat(diff.get(8).line, is("Rivi kuus"));

    }

    @Test
    public void review_history_diff_shows_all_lines_as_new_if_no_old_revision() {
        ReviewHistoryDiff diffWithoutPrevious = ReviewHistoryDiff.from(rows, 1L);

        precondition(diffWithoutPrevious.getNewText().get(0), is("First sent"));
        precondition(diffWithoutPrevious.getOldText(), isNotPresent());

        assertThat(diffWithoutPrevious.getDiff(), hasSize(1));
        assertThat(diffWithoutPrevious.getDiff().get(0).modificationType, isPresent());
        assertThat(diffWithoutPrevious.getDiff().get(0).modificationType.get(), is(Delta.TYPE.INSERT));
        assertThat(diffWithoutPrevious.getDiff().get(0).line, is("First sent"));

    }

    private static ReviewHistoryRow row(int i, ReviewHistoryType type, LocalDate localDate, String s) {
        ReviewHistoryRow reviewHistoryRow = new ReviewHistoryRow();
        reviewHistoryRow.setId((long) i);
        reviewHistoryRow.setType(type);
        reviewHistoryRow.setCreated(localDate.toDateTime(new LocalTime(0, 0)));
        reviewHistoryRow.setSnapshot(Maybe.of(s));
        return reviewHistoryRow;
    }

}
