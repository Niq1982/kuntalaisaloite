package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.junit.Test;
import org.openqa.selenium.By;

import static fi.om.municipalityinitiative.dao.TestHelper.InitiativeDraft;
import static fi.om.municipalityinitiative.web.MessageSourceKeys.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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
    private static final String USER_SSN = "010190-0000";

    @Override
    public void childSetup() {
    }

    @Test
    public void page_opens() {
        openAndAssertPreparePage();
    }
    
    @Test
    public void filling_prepare_page_and_submitting_shows_success_message() {
        overrideDriverToFirefox(true);

        openAndAssertPreparePage();
        select_municipality(false);
        fill_in_preparation_form();
        assertTotalEmailsInQueue(1);
    }

    @Test
    public void prepare_page_shows_validation_errors_no_matter_what_initiativeType_is_selected() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();

        getElemContaining("Kuntalaisaloite", "span").click();
        getElemContaining("Lähetä", "span").click();
        assertPageHasValidationErrors();

        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Siirry tunnistautumaan", "span").click();
        assertPageHasValidationErrors();

    }

    @Test
    public void filling_prepare_page_with_verified_initiative_redirects_to_vetuma_and_edit_page() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();
        select_municipality(false);
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Siirry tunnistautumaan", "button").click();
        // Get redirected to vetuma
        enterVetumaLoginInformationAndSubmit(USER_SSN, MUNICIPALITY_1);
        assertTitle("Tee kuntalaisaloite - Kuntalaisaloitepalvelu");
    }

    @Test
    public void filling_prepare_page_with_verified_initiative_redirects_to_vetuma_but_back_to_prepare_page_with_error_if_wrong_homeMunicipality_from_vetuma() {
        overrideDriverToFirefox(true);
        openAndAssertPreparePage();
        select_municipality(false);
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Siirry tunnistautumaan", "button").click();
        // Get redirected to vetuma
        enterVetumaLoginInformationAndSubmit(USER_SSN, MUNICIPALITY_2);

        assertPreparePageWithInvalidMunicipalityWarning();
    }

    @Test
    public void first_logging_in_before_creating_verified_initiative_creates_draft_straigtly() {
        overrideDriverToFirefox(true);
        vetumaLogin(USER_SSN, MUNICIPALITY_1);

        openAndAssertPreparePage();
        select_municipality(true);
        getElemContaining("Valtuustokäsittelyyn tähtäävä aloite", "span").click();
        getElemContaining("Aloita aloitteen tekeminen", "button").click();
        assertTitle("Tee kuntalaisaloite - Kuntalaisaloitepalvelu");
    }

    private void assertPreparePageWithInvalidMunicipalityWarning() {
        assertPreparePageTitle();
        assertWarningMessage("Väestötietojärjestelmän mukaan kotikuntasi ei ole kunta, jolle aloite on osoitettu. Et voi tehdä aloitetta valitsemaasi kuntaan.");
    }

    @Test
    public void editing_normal_initiative_shows_success_message() {
        Long initiativeId = testHelper.createEmptyDraft(HELSINKI_ID);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.edit(initiativeId));
        
        fill_in_initiative_content();
    }

    @Test
    public void editing_verified_initiative_shows_success_message() {
        Long initiativeId = testHelper.createVerifiedInitiative(new InitiativeDraft(HELSINKI_ID).applyAuthor(USER_SSN).toInitiativeDraft());
        vetumaLogin(USER_SSN, MUNICIPALITY_1);
        open(urls.edit(initiativeId));

        inputText("name", NAME);
        inputText("proposal", PROPOSAL);

        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);
        inputText("contactInfo.email", CONTACT_EMAIL);

        clickByName(Urls.ACTION_SAVE);

        assertSuccessDraftSaved();
        assertTotalEmailsInQueue(0);
    }

    @Test
    public void editing_verified_initiative_for_first_time_shows_success_message_and_sends_email() {
        Long initiativeId = testHelper.createVerifiedInitiative(new InitiativeDraft(HELSINKI_ID)
                .withName("")
                .applyAuthor(USER_SSN).toInitiativeDraft());

        vetumaLogin(USER_SSN, MUNICIPALITY_1);
        open(urls.edit(initiativeId));

        inputText("name", NAME);
        inputText("proposal", PROPOSAL);

        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);
        inputText("contactInfo.email", CONTACT_EMAIL);

        clickByName(Urls.ACTION_SAVE);

        assertSuccessDraftSaved();

        assertTotalEmailsInQueue(1);
    }

    @Test
    public void editing_verified_initiative_shows_validation_errors() {

        Long initiativeId = testHelper.createVerifiedInitiative(new InitiativeDraft(HELSINKI_ID)
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
        Long initiative = testHelper.createDraft(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.getEdit(initiative));
//        assertThat(driver.getTitle(), is("asdasd"));
        assertPreparePageTitle();
    }

    @Test
    public void edit_page_fails_if_not_logged_in() {
        Long initiative = testHelper.createDraft(HELSINKI_ID);
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_om() {
        Long initiative = testHelper.createDraft(HELSINKI_ID);
        loginAsOmUser();
        open(urls.getEdit(initiative));
        assert404();
    }

    @Test
    public void edit_page_fails_if_logged_in_as_another_author() {
        Long otherInitiative = testHelper.createDraft(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        Long initiative = testHelper.createDraft(HELSINKI_ID);
        open(urls.getEdit(initiative));
        assert404();
    }

    // Create initiative with state DRAFT and send it to REVIEW
    @Test
    public void send_to_review() {
        Long initiativeId = testHelper.createDraft(HELSINKI_ID);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(initiativeId));

        clickById("js-send-to-review");
        assertTextContainedByClass("modal-title", "Julkaise aloite Kuntalaisaloite.fi-palvelussa ja lähetä se kuntaan.");

        clickByName(Urls.ACTION_SEND_TO_REVIEW);
        assertSuccessMessage("Aloite lähetetty tarkastettavaksi");
        
        // Assert that initiative name and proposal cannot be edited in REVIEW-state
        update_initiative(initiativeId); // XXX: Why does send_to_review -test update initiative?
        assertTotalEmailsInQueue(2);
    }

    @Test
    public void update_page_fails_if_not_logged_in() {
        open(urls.update(testHelper.createDraft(HELSINKI_ID)));
        assert404();
    }

    @Test
    public void update_fails_if_logged_in_as_om_user() {
        loginAsOmUser();
        open(urls.update(testHelper.createDraft(HELSINKI_ID)));
        assert404();
    }

    @Test
    public void update_page_opens_if_logged_in_as_author() {
        Long initiative = testHelper.createCollaborativeAccepted(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.update(initiative));
        assertTitle("Muokkaa kuntalaisaloitetta - Kuntalaisaloitepalvelu");
    }


    @Test
    public void attachment_page_lists_attachments() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID).withState(InitiativeState.DRAFT).applyAuthor().toInitiativeDraft());

        testHelper.addAttachment(initiativeId, "pdf name", false, "pdf");

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.manageAttachments(initiativeId));
        assertThat(driver.getPageSource(), containsString("/img/pdficon_large.png"));
        assertThat(driver.getPageSource(), containsString("/pdf_name.pdf"));

        // FF-driver apparently can't handle the js-trick for changinf #attachmentId:s value - don't know how to fix.

//        getElement(By.className("js-delete-attachment")).click();
//        clickDialogButton("Poista liitetiedosto");
//
//        assertThat(driver.getPageSource(), not(containsString("/img/pdficon_large.png")));
//        assertThat(driver.getPageSource(), not(containsString("/pdf_name.pdf")));
//        assertTitle("Ylläpidä liitteitä - Kuntalaisaloitepalvelu");
    }

    @Test
    public void adding_attachment_validates_fields() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID).withState(InitiativeState.DRAFT).applyAuthor().toInitiativeDraft());
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.manageAttachments(initiativeId));

        clickDialogButton("Tallenna tiedosto");
        assertPageHasValidationErrors();
    }

    public void select_municipality(boolean homeMunicipalityVerified) {
        clickLink(getMessage(SELECT_MUNICIPALITY));
        getElemContaining(MUNICIPALITY_1, "li").click();

        if (!homeMunicipalityVerified) {
            assertTextContainedByXPath("//div[@id='homeMunicipality_chzn']/a/span", MUNICIPALITY_1);
        }

        System.out.println("--- select_municipality OK");
    }

    public void fill_in_preparation_form() {
        getElemContaining(getMessage(MSG_INITIATIVE_TYPE_NORMAL), "span").click();
        inputText("participantEmail", CONTACT_EMAIL);
        getElemContaining(getMessage(MSG_BTN_PREPARE_SEND), "button").click();

        String msgSuccessPrepare = MSG_SUCCESS_PREPARE;
        assertTextByTag("h1", "Linkki aloitteen tekemiseen on lähetetty sähköpostiisi");
        assertTextByTag("strong", CONTACT_EMAIL);
        System.out.println("--- add_initiative_content OK");
        assertTotalEmailsInQueue(1);

    }

    public void fill_in_initiative_content() {
        inputText("name", NAME);
        inputText("proposal", PROPOSAL);
        
        assertEquals("author_email@example.com", getElement(By.name("contactInfo.email")).getAttribute("value"));
        
        inputText("contactInfo.name", CONTACT_NAME);
        inputText("contactInfo.phone", CONTACT_PHONE);
        inputText("contactInfo.address", CONTACT_ADDRESS);
        
        clickByName(Urls.ACTION_SAVE);

        assertSuccessDraftSaved();

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
        assertSuccessMessage("Aloitteen tiedot päivitetty");
    }

    private void assertSuccessDraftSaved() {
        assertSuccessMessage("Luonnos tallennettu");
    }

    private void openAndAssertPreparePage() {
        open(urls.prepare());
        assertPreparePageTitle();
    }

    private void assertPreparePageTitle() {
        assertTitle(getMessage(MSG_PAGE_CREATE_NEW) + " - " + getMessage(MSG_SITE_NAME));
    }

}
