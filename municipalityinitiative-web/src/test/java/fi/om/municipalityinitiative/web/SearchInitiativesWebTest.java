package fi.om.municipalityinitiative.web;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class SearchInitiativesWebTest extends WebTestBase {

    @Override
    public void childSetup() {

    }

    @Test
    public void page_opens_when_navigation_link_clicked() {
        open(urls.search());
        assertThat(pageTitle(), is("Selaa kuntalaisaloitteita - Kuntalaisaloitepalvelu"));
    }

    @Test
    public void municipalities_are_listed() {
        overrideDriverToFirefox(true);
        open(urls.search());

        WebElement municipalities = getElement(By.id("municipality_chzn"));

        System.out.println(municipalities.getText());
        String municipalitiesRawText = municipalities.getText();
        assertThat(municipalitiesRawText, containsString(HELSINKI));
        assertThat(municipalitiesRawText, containsString(VANTAA));


    }
}
