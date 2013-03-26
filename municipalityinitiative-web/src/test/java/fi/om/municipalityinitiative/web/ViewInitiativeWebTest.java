package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ViewInitiativeWebTest extends WebTestBase {

    /**
     * Localization keys as constants.
     */
    private static final String MANAGEMENT_WARNING_TITLE = "management.warning.title";
    private static final String ERROR_404_TITLE = "error.404.title";
    
    @Test
    public void manage_view_shows_send_to_municipality_button() {

        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createCollectableDraft(municipalityId);

        open(urls.management(initiativeId, TestHelper.TEST_MANAGEMENT_HASH));

        assertThat(driver.findElement(By.tagName("h2")).getText(), is(getMessage(MANAGEMENT_WARNING_TITLE)));
    }

    @Test
    public void opens_error_404_if_management_hash_wrong() {
        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createCollectableDraft(municipalityId);

        open(urls.management(initiativeId, "wrong_hash"));

        assertThat(driver.findElement(By.tagName("h1")).getText(), is(getMessage(ERROR_404_TITLE)));
    }

    // TODO: Redirect-tests if initiative at REVIEW, ACCEPTED, sent etc and trying to open edit/management-page


}
