package fi.om.municipalityinitiative.web;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InitiativeCreateWebTest extends WebTestBase {
    
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String MUNICIPALITY_2 = "Helsinki";
    
    private static final String NAME = "Aloitteen otsikko";
    private static final String PROPOSAL = "Aloitteen sisältö";
    
    private static final String CONTACT_NAME = "Teppo Testaaja";
    private static final String CONTACT_EMAIL = "test@test.com";
    private static final String CONTACT_PHONE = "012-3456789";
    private static final String CONTACT_ADDRESS = "Osoitekatu 1 A, 00000 Helsinki";
    
    private static final String BTN_CONTINUE = "Jatka";
    private static final String SELECT_MUNICIPALITY = "Valitse kunta";

    @Test
    public void page_opens() {
        open(urls.createNew());
        assertTitle("Tee kuntalaisaloite - Kuntalaisaloitepalvelu");
    }
    
    // Create an initiative that has only one author
    @Test
    @Ignore("Fix this test")
    public void create_and_send_initiative() {
        select_municipality();
        add_initiative_content();
        add_contact_info();
        save_initiative();
    }

    public void select_municipality() {
        testHelper.createTestMunicipality(MUNICIPALITY_1);
        testHelper.createTestMunicipality(MUNICIPALITY_2);

        open(urls.createNew());
        
        waitms(500); // Tiny delay is required if run from Eclipse.
        clickLinkContaining(SELECT_MUNICIPALITY);
        waitms(500); // Tiny delay is required if run from Eclipse.
        clickById("municipality_chzn_o_1");
        
        clickLinkContaining(BTN_CONTINUE);
        
        assertTextContainedByXPath("//p[@id='selected-municipality']", MUNICIPALITY_2);
        
        wait100();
        
        System.out.println("--- select_municipality OK");
    }
    
    public void add_initiative_content() {
        wait100();
        
        inputText("name", NAME);
        inputText("proposal", PROPOSAL);
        
        clickLinkContaining(BTN_CONTINUE);

        wait100();
        
        driver.findElement(By.xpath("//div[@id='step-3']")).isDisplayed(); // This is just call-function which returns true/false if the element is displayed
        // TODO: assertThat(driver.findElement(By.xpath("//div[@id='step-3']")).isDisplayed(), is(true));
        
        System.out.println("--- add_initiative_content OK");
    }
    
    public void add_contact_info() {
        wait100();
        
        inputText("contactName", CONTACT_NAME);
        inputText("contactEmail", CONTACT_EMAIL);
        inputText("contactPhone", CONTACT_PHONE);
        inputText("contactAddress", CONTACT_ADDRESS);
        
        clickLinkContaining(BTN_CONTINUE);
        
        wait100();
        
        driver.findElement(By.xpath("//div[@id='step-4']")).isDisplayed();
        
        System.out.println("--- add_contact_info OK");
    }
    
    // TODO: Option for "Tallenna ja aloita kerääminen"
    public void save_initiative() {
        wait100();
        
        clickByName("save");

        wait100();
        
        assertTextContainedByXPath("//div[@class='search-results']//li[@class='first']//span[@class='name']",NAME);
        
        System.out.println("--- save_initiative OK");
    }

    private WebElement getSelectByLabel(String labelText) {
        return driver.findElement(By.xpath("//label[contains(normalize-space(text()), '" + labelText + "')]/following-sibling::select"));
    }
}
