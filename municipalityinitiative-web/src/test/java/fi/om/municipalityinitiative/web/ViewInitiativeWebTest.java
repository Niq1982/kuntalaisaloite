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
    private static final String MSG_BTN_SEND = "action.send";
    private static final String MSG_BTN_PARTICIPATE = "action.participate";
    
    @Test
    public void manage_view_shows_send_to_municipality_button() {

        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Testi aloite", true, true);

        open(urls.management(initiativeId, TestHelper.TEST_MANAGEMENT_HASH));

//        assertTitle("Hallintasivu"); // There is no such title. 
        assertThat(driver.findElement(By.id("js-send-to-municipality")).getText(), is(getMessage(MSG_BTN_SEND)));
    }

    @Test
    public void opens_default_view_if_management_hash_wrong() {
        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Testi aloite", true, true);

        open(urls.management(initiativeId, "wrong_hash"));

        // NOTE: ATM the check for public view is that Participate button exists. This might change in the future.
        assertThat(driver.findElement(By.className("js-participate")).getText(), is(getMessage(MSG_BTN_PARTICIPATE)));

    }


}
