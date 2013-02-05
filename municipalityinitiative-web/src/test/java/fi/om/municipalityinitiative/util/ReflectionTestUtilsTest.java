package fi.om.municipalityinitiative.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;

public class ReflectionTestUtilsTest {

    @Test
    public void not_null_fields_checks_children_also() {

        class InnerClass {
            private String innerString = null;
        }

        class OuterClass {
            private String outerString = "some value";
            private InnerClass innerClass = new InnerClass();
        }
        try {
            ReflectionTestUtils.assertNoNullFields(new OuterClass());
            fail("Should have failed because value for inner class was null");
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("not a string containing \":null\""));
        }
    }
}

