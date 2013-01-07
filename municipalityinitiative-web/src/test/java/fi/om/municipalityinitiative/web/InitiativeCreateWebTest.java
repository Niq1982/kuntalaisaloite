package fi.om.municipalityinitiative.web;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeCreateWebTest extends NEWWebTestBase {

    @Test
    public void municipalities_are_listed() {

        newTestHelper.createTestMunicipality("Korso");
        newTestHelper.createTestMunicipality("Helsinki");

        open(urls.createNew());

        WebElement selectBox = getSelectByLabel("Valitse kunta");
        List<WebElement> optionValues = selectBox.findElements(By.tagName("option"));

        // First is empty, start from second
        assertThat(optionValues.get(1).getText(), is("Helsinki"));
        assertThat(optionValues.get(2).getText(), is("Korso"));
    }

    private WebElement getSelectByLabel(String labelText) {
        return driver.findElement(By.xpath("//label[contains(normalize-space(text()), '" + labelText + "')]/following-sibling::select"));
    }
}
