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
        
        newTestHelper.createTestMunicipality("Vantaa");
        newTestHelper.createTestMunicipality("Helsinki");

        open(urls.createNew());

        WebElement selectBox = getSelectByLabel("Valitse kunta");
        List<WebElement> optionValues = selectBox.findElements(By.tagName("option"));

        // First is empty, start from second
        assertThat(optionValues.get(1).getText(), is("Helsinki"));
        assertThat(optionValues.get(2).getText(), is("Vantaa"));
    }
    
    // Create an initiative that has only one author
    @Test
    public void create_and_send_initiative() {
        //select_municipality();
        //add_initiative_content();
        //add_contact_info();
        //save_initiative();
    }
    
    /*
     * TODO:
     *  - Different municipality options
     *  - Use constants for input data and assert-texts
     *  - Select municipality
     */
    public void select_municipality() {
        //newTestHelper.createTestMunicipality("Vantaa");
        //newTestHelper.createTestMunicipality("Helsinki");

        open(urls.createNew());

//        WebElement selectBox = getSelectByLabel("Valitse kunta");
//        List<WebElement> optionValues = selectBox.findElements(By.tagName("option"));

        // First is empty, start from second
        //assertThat(optionValues.get(1).getText(), is("Helsinki"));
        //assertThat(optionValues.get(2).getText(), is("Vantaa"));
        
        // TODO
        waitms(500);
        clickById("chzn-single"); // Click link instead of DIV
        wait100();
        clickById("municipality_chzn_o_1");
        
        clickLinkContaining("Jatka");
        
        assertTextContainedByXPath("//p[@id='selected-municipality']","Helsinki");
    }
    
    public void add_initiative_content() {
        inputText("name", "Aloitteen otsikko");
        inputText("proposal", "Aloitteen sisältö");
        
        clickLinkContaining("Jatka");
        
        assertTextByTag("label", "Aloitteen sisältö");
        
        
        
        // TODO: Assert that right block opens
        
    }
    
    public void add_contact_info() {
        inputText("contactName", "Teppo Testaaja");
        inputText("contactEmail", "test@test.com");
        inputText("contactPhone", "012-3456789");
        inputText("contactAddress", "Osoitekatu 1 A, 00500 Helsinki");
        
        clickLinkContaining("Jatka");
        
        // TODO: Assert that right block opens
    }
    
    // TODO: Option for "Tallenna ja aloita kerääminen"
    public void save_initiative() {
        clickLinkContaining("Tallenna ja lähetä");

        open(urls.search());
        
        // TODO: Assert that redirects in the initiative's view page and has right success-msg
        assertTextContainedByXPath("//span[@class='name']","Aloitteen otsikko");
        
        
    }

    private WebElement getSelectByLabel(String labelText) {
        return driver.findElement(By.xpath("//label[contains(normalize-space(text()), '" + labelText + "')]/following-sibling::select"));
    }
}
