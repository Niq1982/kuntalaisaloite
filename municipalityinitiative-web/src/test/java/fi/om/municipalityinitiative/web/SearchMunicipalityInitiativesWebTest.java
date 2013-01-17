package fi.om.municipalityinitiative.web;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class SearchMunicipalityInitiativesWebTest extends WebTestBase {

    @Test
    public void page_opens_when_navigation_link_clicked() {
        open(urls.search());
        assertThat(pageTitle(), is("Selaa kuntalaisaloitteita - Kuntalaisaloitepalvelu"));
    }

    @Test
    @Ignore("Wont work due usage of chosen.js. Fix or implement another test which verifies that the list works.")
    public void municipalities_are_listed() {
        testHelper.createTestMunicipality("Tuusula");
        open(urls.search());

        WebElement municipalities = driver.findElement(By.id("municipality_chzn"));

        System.out.println(municipalities.getText());
        assertThat(municipalities.getText(), containsString("Tuusula"));


    }
}
