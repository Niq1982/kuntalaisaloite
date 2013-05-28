package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InitiativeSearchTest {

    @Test
    public void copy_fills_all_fields() {
        InitiativeSearch original = ReflectionTestUtils.modifyAllFields(new InitiativeSearch());

        InitiativeSearch copy = original.copy();

        ReflectionTestUtils.assertNoNullFields(copy);
        ReflectionTestUtils.assertReflectionEquals(original, copy);
    }

    @Test
    public void has_max_limit_value() {
        assertThat(new InitiativeSearch().setLimit(100000).getLimit(), is(Urls.MAX_INITIATIVE_SEARCH_LIMIT));
    }

    @Test
    public void limit_is_what_is_given_if_not_max_limit() {
        assertThat(new InitiativeSearch().setLimit(10).getLimit(), is(10));
    }

    @Test
    public void limit_is_default_if_not_set() {
        assertThat(new InitiativeSearch().getLimit(), is(Urls.DEFAULT_INITIATIVE_SEARCH_LIMIT));
    }
}
