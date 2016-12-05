package fi.om.municipalityinitiative.util;

import org.joda.time.LocalDate;
import org.junit.Test;

import static fi.om.municipalityinitiative.util.SsnValidator.isAdult;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SsnValidatorTest {

    @Test
    public void invalid_ssn() {
        assertFail(() -> SsnValidator.validateSsn("2323"));
        assertFail(() -> SsnValidator.validateSsn("121212B-1111"));
        assertFail(() -> SsnValidator.validateSsn("121212-222"));
        assertFail(() -> SsnValidator.validateSsn("121212-22211"));
        assertFail(() -> SsnValidator.validateSsn(" 121212-2221"));
        assertFail(() -> SsnValidator.validateSsn("121212-2221 "));
        assertFail(() -> SsnValidator.validateSsn(null));
    }

    @Test
    public void valid_is_upper_cased() {
        assertThat(SsnValidator.validateSsn("121212a1212"), is("121212A1212"));
    }

    @Test
    public void nine_hundreds() {
        assertThat(
                isAdult(new LocalDate(2005, 1, 1),
                        "010187-0000"), is(true)
        );

        assertThat(
                isAdult(new LocalDate(2005, 1, 1),
                        "010287-0000"), is(false)
        );

    }


    @Test
    public void millenium_juniors() {

        assertThat(
                isAdult(new LocalDate(2028, 1, 1),
                        "010110A0000"), is(true)
        );

        assertThat(
                isAdult(new LocalDate(2028, 1, 1),
                        "020110A0000"), is(false)
        );

        assertThat(
                isAdult(new LocalDate(2016, 1, 1),
                        "010100A0000"), is(false)
        );



    }

    @Test
    public void oldies_like_these_would_live_anyways() {
        assertThat(
                isAdult(new LocalDate(1908, 1, 1),
                        "010190+0000"), is(true)
        );

        assertThat(
                isAdult(new LocalDate(1908, 1, 1),
                        "010290+0000"), is(false)
        );

        assertThat(
                isAdult(new LocalDate(2008, 1, 1),
                        "010190+0000"), is(true)
        );

    }

    private void assertFail(Runnable runnable) {
        try {
            runnable.run();
            fail("Should have failed");

        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Invalid SSN"));
        }
    }

}