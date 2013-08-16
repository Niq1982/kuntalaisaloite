package fi.om.municipalityinitiative.web;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Json data is tested at @see fi.om.municipalityinitiative.web.controller.JsonTest
 *
 */
public class ApiTest extends WebTestBase {

    @Override
    public void childSetup() {

    }

    @Test
    public void api_page_opens() {
        open(urls.api());
        assertTitle("Open Data API - Kuntalaisaloitepalvelu");
    }

    @Test
    public void any_api_localizations_are_not_missing() throws Exception {
        open(urls.api());
        assertThat(driver.getPageSource(), not(containsString("[api.")));
    }
}
