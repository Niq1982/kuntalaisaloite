package fi.om.municipalityinitiative.web;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fi.om.municipalityinitiative.dao.TestHelper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class InitiativeCreateWebTest extends WebTestBase {
    
    /**
     * Localization keys as constants.
     */
    
    private static final String MSG_SITE_NAME = "siteName";
    private static final String MSG_PAGE_CREATE_NEW = "page.createNew";
    private static final String MSG_SUCCESS_SAVE_AND_SEND = "success.save-and-send";
    private static final String MSG_SUCCESS_PREPARE = "success.prepare";
    private static final String MSG_SUCCESS_SAVE_TITLE = "success.save.title";
    private static final String MSG_SUCCESS_SAVE_DRAFT = "success.save-draft";
    private static final String MSG_BTN_PREPARE_SEND = "action.prepare.send";
    private static final String MSG_BTN_SAVE = "action.save";
    private static final String MSG_BTN_SAVE_AND_SEND = "action.saveAndSend";
    private static final String MSG_BTN_SAVE_AND_COLLECT = "action.saveAndCollect";
    private static final String SELECT_MUNICIPALITY = "initiative.chooseMunicipality";
    private static final String MSG_INITIATIVE_TYPE_NORMAL= "initiative.type.normal";
    private static final String RADIO_FRANCHISE_TRUE = "initiative.franchise.true";
    
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String MUNICIPALITY_2 = "Helsinki";
    
    private static final String NAME = "Aloitteen otsikko";
    private static final String PROPOSAL = "Aloitteen sisältö";
    
    private static final String CONTACT_NAME = "Teppo Testaaja";
    private static final String CONTACT_EMAIL = "test@test.com";
    private static final String CONTACT_PHONE = "012-3456789";
    private static final String CONTACT_ADDRESS = "Osoitekatu 1 A, 00000 Helsinki";

    @Test
    public void page_opens() {
        openAndAssertCreatePage();
    }
    
    // Fill in the preparation form
    @Test
    public void prepare_initiative() {
        testHelper.createTestMunicipality(MUNICIPALITY_1);
        testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        openAndAssertCreatePage();
        select_municipality();
        fill_in_preparation_form();
    }

    // Create an initiative
    @Test
    @Ignore("FIXME: Causes null pointer exception")
    public void create_initiative() {
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long initiativeId = testHelper.createTestInitiative(municipality1Id, "Testi aloite", true, true);
        
        // FIXME: Causes null pointer exception
        open(urls.edit(initiativeId, TestHelper.TEST_MANAGEMENT_HASH));

        add_initiative_content();
    }
    
    public void select_municipality() {
        clickLinkContaining(getMessage(SELECT_MUNICIPALITY));
        getElemContaining(MUNICIPALITY_1, "li").click();
        
        assertTextContainedByXPath("//div[@id='homeMunicipality_chzn']/a/span", MUNICIPALITY_1);

        System.out.println("--- select_municipality OK");
    }
    
    public void fill_in_preparation_form() {
        getElemContaining(getMessage(MSG_INITIATIVE_TYPE_NORMAL), "span").click();
        inputText("authorEmail", CONTACT_EMAIL);
        getElemContaining(getMessage(MSG_BTN_PREPARE_SEND), "button").click();

        String msgSuccessPrepare = MSG_SUCCESS_PREPARE;
        assertSuccesPageWithMessage(msgSuccessPrepare);
        assertTextByTag("strong", CONTACT_EMAIL);
        System.out.println("--- add_initiative_content OK");
    }

    public void add_initiative_content() {
        inputText("name", NAME);
        inputText("proposal", PROPOSAL);
        inputText("contactInfo.name", CONTACT_NAME);
        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);

        assertTextContainedByXPath("//div[@id='contactInfo.email']", "contact_email@xxx.yyy");

        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();

        assertSuccesPageWithMessage(MSG_SUCCESS_SAVE_DRAFT);

        System.out.println("--- add_contact_info OK");
    }

    public void save_initiative(boolean startCollecting) {

        if (startCollecting) {
            getElemContaining(getMessage(RADIO_FRANCHISE_TRUE), "label").click();
            getElemContaining(getMessage(MSG_BTN_SAVE_AND_COLLECT), "button").click();

            assertMsgContainedByClass("modal-title", MSG_SUCCESS_SAVE_TITLE);
        } else {
            getElemContaining(getMessage(MSG_BTN_SAVE_AND_SEND), "button").click();

            assertSuccesPageWithMessage(MSG_SUCCESS_SAVE_AND_SEND);
            assertTextByTag("h1", NAME);
        }

        System.out.println("--- save_initiative OK");
    }

    private void assertSuccesPageWithMessage(String msgSuccessPrepare) {
        assertMsgContainedByClass("msg-success", msgSuccessPrepare);
    }

    private void openAndAssertCreatePage() {
        open(urls.prepare());
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

    private WebElement getSelectByLabel(String labelText) {
        return driver.findElement(By.xpath("//label[contains(normalize-space(text()), '" + labelText + "')]/following-sibling::select"));
    }
}
