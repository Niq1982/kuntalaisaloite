package fi.om.municipalityinitiative.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

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
    
    @Test
    public void filling_prepare_page_and_submitting_shows_success_message() {
        overrideDriverToFirefox(true);

        openAndAssertPreparePage();
        select_municipality();
        fill_in_preparation_form();
    }

    @Test
    public void editing_initiative_shows_success_message() {
        Long initiativeId = testHelper.createEmptyDraft(testMunicipality1Id);

        loginAsAuthorForLastTestHelperCreatedInitiative();

        open(urls.edit(initiativeId));
        
        fill_in_initiative_content();
    }
    
    @Test
    public void edit_page_opens_if_logged_in_as_author() {
        Long initiative = testHelper.createDraft(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedInitiative();
        open(urls.getEdit(initiative));
//        assertThat(driver.getTitle(), is("asdasd"));
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

    @Test
    public void edit_page_fails_if_not_logged_in() {
        Long initiative = testHelper.createDraft(testMunicipality1Id);
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_om() {
        Long initiative = testHelper.createDraft(testMunicipality1Id);
        loginAsOmUser();
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_another_author() {
        Long otherInitiative = testHelper.createDraft(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedInitiative();

        Long initiative = testHelper.createDraft(testMunicipality1Id);
        open(urls.getEdit(initiative));
        assert404();
    }

    // Create initiative with state DRAFT and send it to REVIEW
    @Test
    public void send_to_review() {
        Long initiativeId = testHelper.createDraft(testMunicipality1Id);

        loginAsAuthorForLastTestHelperCreatedInitiative();
        open(urls.management(initiativeId));

        clickById("js-send-to-review");
        assertMsgContainedByClass("modal-title", MSG_SEND_TO_REVIEW_CONFIRM);

        clickByName(Urls.ACTION_SEND_TO_REVIEW);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_SEND_TO_REVIEW);
        
        // Assert that initiative name and proposal cannot be edited in REVIEW-state
        update_initiative(initiativeId); // XXX: Why does send_to_review -test update initiative?
    }

    @Test
    public void update_page_fails_if_not_logged_in() {
        open(urls.update(testHelper.createDraft(testMunicipality1Id)));
        assert404();
    }

    @Test
    public void update_fails_if_logged_in_as_om_user() {
        loginAsOmUser();
        open(urls.update(testHelper.createDraft(testMunicipality1Id)));
        assert404();
    }

    @Test
    public void update_page_opens_if_logged_in_as_author() {
        Long initiative = testHelper.createCollaborativeAccepted(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedInitiative();
        open(urls.update(initiative));
        assertTitle("Muokkaa kuntalaisaloitetta - Kuntalaisaloitepalvelu");
    }

    public void select_municipality() {
        clickLinkContaining(getMessage(SELECT_MUNICIPALITY));
        getElemContaining(MUNICIPALITY_1, "li").click();

        assertTextContainedByXPath("//div[@id='homeMunicipality_chzn']/a/span", MUNICIPALITY_1);

        System.out.println("--- select_municipality OK");
    }

    public void fill_in_preparation_form() {
        getElemContaining(getMessage(MSG_INITIATIVE_TYPE_NORMAL), "span").click();
        inputText("participantEmail", CONTACT_EMAIL);
        getElemContaining(getMessage(MSG_BTN_PREPARE_SEND), "button").click();

        String msgSuccessPrepare = MSG_SUCCESS_PREPARE;
        assertSuccesPageWithMessage(msgSuccessPrepare);
        assertTextByTag("strong", CONTACT_EMAIL);
        System.out.println("--- add_initiative_content OK");
    }

    public void fill_in_initiative_content() {
        inputText("name", NAME);
        inputText("proposal", PROPOSAL);
        
        assertEquals("author_email@example.com", getElement(By.name("contactInfo.email")).getAttribute("value"));
        
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
        assertTextByTag("h2", getMessage(MSG_INITIATIVE_AUTHORS, 1));

        inputText("extraInfo", "extraInfo");
        inputText("contactInfo.name", "Updated");
        inputText("contactInfo.phone", "Updated");
        inputText("contactInfo.address", "Updated");

        clickByName(Urls.ACTION_UPDATE_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_UPDATE);
    }

    private void assertSuccesPageWithMessage(String msgSuccessPrepare) {
        assertMsgContainedByClass("msg-success", msgSuccessPrepare);
    }

    private void openAndAssertPreparePage() {
        open(urls.prepare());
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

}
