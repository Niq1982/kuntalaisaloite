package fi.om.municipalityinitiative.util;

import org.hamcrest.Matcher;

import static org.junit.Assert.assertThat;

public class TestUtil {

    public static <T> void precondition(T actual, Matcher<? super T> matcher) {
        assertThat("Precondition failed", actual, matcher);
    }
}
