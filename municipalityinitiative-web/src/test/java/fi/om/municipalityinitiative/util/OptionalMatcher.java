package fi.om.municipalityinitiative.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

public abstract class OptionalMatcher extends TypeSafeMatcher<Optional<?>> {

    public static Matcher<? super Optional<?>> isPresent() {
            return new OptionalMatcher() {

                @Override
                protected boolean matchesSafely(Optional<?> item) {
                    return isOptionalObject(item) && item.isPresent();
                }

                @Override
                protected void describeMismatchSafely(Optional<?> item, Description mismatchDescription) {
                    mismatchDescription.appendText("was isNotPresent()");
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("isPresent()");
                }
            };
        }

    public static Matcher<? super Optional<?>> isNotPresent() {
        return new OptionalMatcher() {
            @Override
            protected boolean matchesSafely(Optional<?> item) {
                return isOptionalObject(item) && !item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("isNotPresent()");
            }

            @Override
            protected void describeMismatchSafely(Optional<?> item, Description mismatchDescription) {
                mismatchDescription.appendText("was isPresent()");
            }
        };
    }

    private static boolean isOptionalObject(Optional<?> item) {
        return item != null;
    }
}
