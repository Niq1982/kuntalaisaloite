package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.*;
import static org.junit.Assert.assertEquals;

public class InitiativeCreateWebTest extends WebTestBase {

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
    private Long testMunicipality1Id;
    private Long testMunicipality2Id;

    @Before
    public void setup() {
        testMunicipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        testMunicipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
    }

    @Test
    public void page_opens() {
        openAndAssertPreparePage();
    }
    
    // Fill in the preparation form
    @Test
    public void prepare_initiative() {
        
        openAndAssertPreparePage();
        select_municipality();
        fill_in_preparation_form();
    }

    // Create an initiative and fill in initiative details
    @Test
    public void create_initiative() {
        Long initiativeId = testHelper.createEmptyDraft(testMunicipality1Id);

        loginAsAuthor(initiativeId);

        open(urls.edit(initiativeId));
        
        fill_in_initiative_content();
    }
    
    // Create initiative with state DRAFT and send it to REVIEW
    @Test
    public void send_to_review() {
        Long initiativeId = testHelper.createSingleDraft(testMunicipality1Id);

        loginAsAuthor(initiativeId);
        open(urls.management(initiativeId, TestHelper.TEST_MANAGEMENT_HASH));
        
        clickById("js-send-to-review");
        assertMsgContainedByClass("modal-title", MSG_SEND_TO_REVIEW_CONFIRM);
        
        clickByName(Urls.ACTION_SEND_TO_REVIEW);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_SEND_TO_REVIEW);
     
        // Assert that initiative name and proposal cannot be edited in REVIEW-state
        update_initiative(initiativeId);
    }

    @Test
    public void edit_page_opens_if_logged_in_as_author() {
        Long initiative = testHelper.createSingleDraft(testMunicipality1Id);
        loginAsAuthor(initiative);
        open(urls.getEdit(initiative));
//        assertThat(driver.getTitle(), is("asdasd"));
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

    @Test
    public void edit_page_fails_if_not_logged_in() {
        Long initiative = testHelper.createSingleDraft(testMunicipality1Id);
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_om() {
        Long initiative = testHelper.createSingleDraft(testMunicipality1Id);
        loginAsOmUser();
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_another_author() {
        Long initiative = testHelper.createSingleDraft(testMunicipality1Id);
        loginAsAuthor(testHelper.createSingleSent(testMunicipality1Id));
        open(urls.getEdit(initiative));
        assert404();
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

    public void fill_in_initiative_content() {
        inputText("name", NAME);
        inputText("proposal", PROPOSAL);
        
        assertEquals("author_email@example.com", driver.findElement(By.name("contactInfo.email")).getAttribute("value"));
        
        inputText("contactInfo.name", CONTACT_NAME);
        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);
        
        clickByName(Urls.ACTION_SAVE);

        assertSuccesPageWithMessage(MSG_SUCCESS_SAVE_DRAFT);

        System.out.println("--- add_contact_info OK");
    }
    
    public void update_initiative(Long initiativeId) {
        open(urls.edit(initiativeId));

        assertTextByTag("h2", getMessage(MSG_INITIATIVE_PROPOSAL));
        assertTextByTag("h2", getMessage(MSG_INITIATIVE_CONTACTINFO));

        inputText("extraInfo", "extraInfo");
        inputText("contactInfo.name", "Updated");
        inputText("contactInfo.phone", "Updated");
        inputText("contactInfo.address", "Updated");

        clickByName(Urls.ACTION_UPDATE_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_UPDATE);
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

    private void openAndAssertPreparePage() {
        open(urls.prepare());
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

    private WebElement getSelectByLabel(String labelText) {
        return driver.findElement(By.xpath("//label[contains(normalize-space(text()), '" + labelText + "')]/following-sibling::select"));
    }
}
