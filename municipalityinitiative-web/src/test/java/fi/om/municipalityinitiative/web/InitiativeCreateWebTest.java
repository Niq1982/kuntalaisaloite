package fi.om.municipalityinitiative.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static fi.om.municipalityinitiative.dao.TestHelper.InitiativeDraft;
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
    private static final String USER_SSN = "000000-0000";
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
    public void prepare_page_shows_validation_errors_no_matter_what_initiativeType_is_selected() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();

        getElemContaining("Lähetä", "span").click();
        assertPageHasValidationErrors();

        getElemContaining("Kuntalaisaloite", "span").click();
        getElemContaining("Lähetä", "span").click();
        assertPageHasValidationErrors();

        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Lähetä", "span").click();
        assertPageHasValidationErrors();

    }

    @Test
    public void filling_prepare_page_with_verified_initiative_redirects_to_vetuma_and_edit_page() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();
        select_municipality();
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Lähetä", "button").click();
        // Get redirected to vetuma
        enterVetumaLoginInformationAndSubmit(USER_SSN, MUNICIPALITY_1);
        assertTitle("Tee kuntalaisaloite - Kuntalaisaloitepalvelu");
    }

    @Test
    public void filling_prepare_page_with_verified_initiative_redirects_to_vetuma_but_back_to_prepare_page_with_error_if_wrong_homeMunicipality_from_vetuma() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();
        select_municipality();
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Lähetä", "button").click();
        // Get redirected to vetuma
        enterVetumaLoginInformationAndSubmit(USER_SSN, MUNICIPALITY_2);

        assertPreparePageWithInvalidMunicipalityWarning();
    }

    @Test
    public void first_logging_in_before_creating_verified_initiative_creates_draft_straigtly() {
        overrideDriverToFirefox(true);
        vetumaLogin(USER_SSN, MUNICIPALITY_1);

        openAndAssertPreparePage();
        select_municipality();
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Lähetä", "button").click();
        assertTitle("Tee kuntalaisaloite - Kuntalaisaloitepalvelu");
    }

    // This test probably is not needed for very long, because we should prevent the submit with etc. javascript.
    // Although it would be nice to keep this for non-javascript-versions if needed...
    @Test
    public void first_logging_in_before_creating_verified_initiative_shows_error_if_wrong_municipality_after_submitting() {
        overrideDriverToFirefox(true);
        vetumaLogin(USER_SSN, MUNICIPALITY_2);

        openAndAssertPreparePage();
        select_municipality();
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Lähetä", "button").click();
        assertPreparePageWithInvalidMunicipalityWarning();
    }

    private void assertPreparePageWithInvalidMunicipalityWarning() {
        assertPreparePageTitle();
        assertTextContainedByClass("msg-warning", "Kotikuntasi ei väestötietojärjestelmän mukaan ole aloitteen kunta. Et voi tehdä aloitetta valitsemaasi kuntaan");
    }

    @Test
    public void editing_normal_initiative_shows_success_message() {
        Long initiativeId = testHelper.createEmptyDraft(testMunicipality1Id);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.edit(initiativeId));
        
        fill_in_initiative_content();
    }

    @Test
    public void editing_verified_initiative_shows_success_message() {
        Long initiativeId = testHelper.createVerifiedInitiative(new InitiativeDraft(testMunicipality1Id).applyAuthor(USER_SSN).toInitiativeDraft());
        vetumaLogin(USER_SSN, MUNICIPALITY_1);
        open(urls.edit(initiativeId));

        inputText("name", NAME);
        inputText("proposal", PROPOSAL);

        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);
        inputText("contactInfo.email", CONTACT_EMAIL);

        clickByName(Urls.ACTION_SAVE);

        assertSuccesPageWithMessage(MSG_SUCCESS_SAVE_DRAFT);
    }

    @Test
    public void editing_verified_initiative_shows_validation_errors() {

        Long initiativeId = testHelper.createVerifiedInitiative(new InitiativeDraft(testMunicipality1Id)
                        .withName("")
                        .applyAuthor(USER_SSN)
                        .toInitiativeDraft()
        );

        vetumaLogin(USER_SSN, MUNICIPALITY_1);
        open(urls.edit(initiativeId));

        clickByName(Urls.ACTION_SAVE);

        assertPageHasValidationErrors();
    }

    @Test
    public void edit_page_opens_if_logged_in_as_author() {
        Long initiative = testHelper.createDraft(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.getEdit(initiative));
//        assertThat(driver.getTitle(), is("asdasd"));
        assertPreparePageTitle();
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
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        Long initiative = testHelper.createDraft(testMunicipality1Id);
        open(urls.getEdit(initiative));
        assert404();
    }

    // Create initiative with state DRAFT and send it to REVIEW
    @Test
    public void send_to_review() {
        Long initiativeId = testHelper.createDraft(testMunicipality1Id);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
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
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
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
        assertPreparePageTitle();
    }

    private void assertPreparePageTitle() {
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

}
