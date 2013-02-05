package fi.om.municipalityinitiative.web;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ViewInitiativeWebTest extends WebTestBase {

    @Test
    @Ignore("Implement")
    public void manage_view_shows_send_to_municipality_button() {

        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Testi aloite", true, true);

        open(urls.management(initiativeId, "0000000000111111111122222222223333333333"));

        assertTitle("Hallintasivu");
        assertThat(driver.findElement(By.tagName("button")).getText(), is("Lähetä kuntaan tms"));
    }

    @Test
    public void opens_default_view_if_management_hash_wrong() {
        Long municipalityId = testHelper.createTestMunicipality("Tuusula");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Testi aloite", true, true);

        open(urls.management(initiativeId, "wrong_hash"));

        System.out.println(driver.getPageSource());
        // TODO: assert that is default view instead of management view

    }


}
