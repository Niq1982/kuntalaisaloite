package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.MSG_SUCCESS_INVITATION_SENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class AuthorsWebTest  extends WebTestBase {


    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_SEND = "success.send.title";
    private static final String MSG_ADD_AUTHORS_LINK = "addAuthors.link";
    private static final String MSG_BTN_ADD_AUTHOR = "action.addAuthor";
    private static final String MSG_BTN_SEND = "action.sendInvitation";
    private static final String MSG_INVITATION_UNCONFIRMED = "invitation.unconfirmed";
    
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String MUNICIPALITY_2 = "Helsinki";
    private static final String CONTACT_NAME = "Teppo Testaaja";
    private static final String CONTACT_EMAIL = "test@test.com";
    public static final String CONTACT_ADDRESS = "Joku Katu jossain 89";
    public static final String CONTACT_PHONE = "040111222";
    public static final String HYLKÄÄ_KUTSU = "invitation.reject";
    public static final String HYVÄKSY_KUTSUN_HYLKÄÄMINEN = "invitation.reject.confirm";
    public static final String VERIFIED_USER_AUTHOR_SSN = "000000-0000";
    private Long municipalityId;
    private Long normalInitiativeId;
    private Long verifiedInitiativeId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        municipalityId = testHelper.createTestMunicipality(MUNICIPALITY_1);
        testHelper.createTestMunicipality(MUNICIPALITY_2);

        normalInitiativeId = testHelper.create(municipalityId, InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        verifiedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(municipalityId)
                .withState(InitiativeState.ACCEPTED)
                .applyAuthor(VERIFIED_USER_AUTHOR_SSN)
                .toInitiativeDraft());
    }

    @Test
    public void add_author() {

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        
        open(urls.management(normalInitiativeId));
        clickLinkContaining(getMessage(MSG_ADD_AUTHORS_LINK));
        
        clickLinkContaining(getMessage(MSG_BTN_ADD_AUTHOR));
        
        inputText("authorName", CONTACT_NAME);
        inputText("authorEmail", CONTACT_EMAIL);
        
        getElemContaining(getMessage(MSG_BTN_SEND), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_INVITATION_SENT);
        assertTextContainedByXPath("//div[@class='view-block last']//span[@class='status']", getMessage(MSG_INVITATION_UNCONFIRMED));
    }

    @Test
    public void author_invitation_acceptance_dialog_has_given_values_prefilled_and_submitting_logs_user_in_as_author_with_given_information() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));

        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);
        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);
        clickDialogButton("Hyväksy ja tallenna tiedot");

        assertTextContainedByClass("msg-success", "Liittymisesi vastuuhenkilöksi on nyt vahvistettu ja olet kirjautunut sisään palveluun.");

        clickDialogButton("Muokkaa aloitetta");
        assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));
        assertThat(getElementByLabel("Puhelin", "input").getAttribute("value"), containsString(CONTACT_PHONE));
        assertThat(getElementByLabel("Osoite", "textarea").getText(), containsString(CONTACT_ADDRESS));

        assertInvitationPageIsGone(invitation);

    }

    @Test
    public void author_invitation_to_normal_initiative_shows_validation_messages() {

        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElementByLabel("Etu- ja sukunimi", "input").clear();
        clickDialogButton("Hyväksy ja tallenna tiedot");

        assertPageHasValidationErrors();
    }

    @Test
    public void accepting_verified_initiative_shows_login_button_to_vetuma_and_logging_in_redirects_back_to_initiative_page() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(verifiedInitiativeId, invitation.getConfirmationCode()));

        getElemContaining("Tunnistaudu ja hyväksy kutsu", "span").click();

        enterVetumaLoginInformationAndSubmit("111111-1111", MUNICIPALITY_1);

        assertTitle(TestHelper.DEFAULT_INITIATIVE_NAME + " - Kuntalaisaloitepalvelu");
        assertThat(acceptInvitationButton().isPresent(), is(true));
    }

    @Test
    public void author_invitation_to_verified_initiative_shows_validation_messages() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", MUNICIPALITY_1);

        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        // NOTE: Only validation message now is generated from email-field.
        // In this case the email field is empty by default, because user is new and has never entered his email.
        // Should the email be filled from the created invitation instead?
        // If yes, it will practically replace the original users email (if exists) everywhere.
        clickDialogButton("Hyväksy ja tallenna tiedot");
        assertPageHasValidationErrors();
    }

    @Test
    public void accepting_normal_author_invitation_lets_user_to_accept_invitation_even_if_logged_in_as_author() {
        Long publishedInitiativeId = testHelper.create(municipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertThat(acceptInvitationButton().isPresent(), is(true));
        assertThat(rejectInvitationButton().isPresent(), is(true));
    }

    @Test
    public void accepting_verified_author_invitation_shows_warning_and_hides_buttons_if_already_author() {

        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin(VERIFIED_USER_AUTHOR_SSN, MUNICIPALITY_1);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertTextContainedByClass("msg-warning", "Olet jo aloitteen vastuuhenkilö, joten et voi hyväksyä vastuuhenkilökutsua");
        assertThat(acceptInvitationButton().isPresent(), is(false));
        assertThat(rejectInvitationButton().isPresent(), is(false));
    }

    @Test
    public void accepting_verified_author_invitation_shows_warning_and_hides_accept_button_if_wrong_homeMunicipality() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", MUNICIPALITY_2);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertTextContainedByClass("msg-warning", "Väestötietojärjestelmän mukaan kotikuntasi ei ole kunta jota aloite koskee, joten et voi liittyä aloitteen vastuuhenkilöksi. Kiitos mielenkiinnosta!");
        assertThat(acceptInvitationButton().isPresent(), is(false));
        assertThat(rejectInvitationButton().isPresent(), is(true));
    }

    @Test
    public void accepting_verified_author_invitation_shows_success_message_shows_management_page_and_increases_participant_count_with_one() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", MUNICIPALITY_1);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertThat(acceptInvitationButton().isPresent(), is(true));
        assertThat(rejectInvitationButton().isPresent(), is(true));

        acceptInvitationButton().get().click();

        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);
        getElementByLabel("Sähköpostiosoite", "input").sendKeys(CONTACT_EMAIL);
        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);

        clickDialogButton("Hyväksy ja tallenna tiedot");
        assertTextContainedByClass("msg-success", "Liittymisesi vastuuhenkilöksi on nyt vahvistettu ja olet kirjautunut sisään palveluun.");

    }

    private Maybe<WebElement> rejectInvitationButton() {
        return getOptionalElemContaining("Hylkää kutsu", "span");
    }

    private Maybe<WebElement> acceptInvitationButton() {
        return getOptionalElemContaining("Hyväksy kutsu", "span");
    }

    @Test
    @Ignore("Implement")
    public void accepting_verified_author_invitation_when_unknown_municipality_from_vetuma_does_something(){
        // TODO: Implemente
    }

    @Test
    public void reject_author_invitation() throws InterruptedException {
        overrideDriverToFirefox(true);
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        clickDialogButtonMsg(HYLKÄÄ_KUTSU);
        clickDialogButtonMsg(HYVÄKSY_KUTSUN_HYLKÄÄMINEN);

        assertTextContainedByClass("msg-success", "Olet hylännyt kutsun vastuuhenkilöksi eikä tietojasi ole tallennettu aloitteeseen");

        assertInvitationPageIsGone(invitation);

    }
    
    @Test
    public void author_removes_participant(){
        Long publishedInitiativeId = testHelper.create(municipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(publishedInitiativeId, municipalityId));
        
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(publishedInitiativeId));
        
        clickLinkContaining("Osallistujahallinta");
        clickLinkContaining("Poista osallistuja");
        
        // NOTE: We could assert that modal has correct Participant details,
        //       but as DOM is updated after the modal is loaded we would need a tiny delay for that
        
        getElemContaining("Poista", "button").click();
        
        assertTextContainedByClass("msg-success", "Osallistuja poistettu");
        
    }
    
    @Test
    public void author_removes_author(){
        testHelper.createDefaultAuthorAndParticipant(new TestHelper.AuthorDraft(normalInitiativeId, municipalityId));
        
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(normalInitiativeId));
        
        clickLinkContaining("Ylläpidä vastuuhenkilöitä");
        clickLinkContaining("Poista vastuuhenkilö");
        
        // NOTE: We could assert that modal has correct Author details,
        //       but as DOM is updated after the modal is loaded we would need a tiny delay for that
        
        getElemContaining("Poista vastuuhenkilö", "button").click();
        
        assertTextContainedByClass("msg-success", "Vastuuhenkilö poistettu");
    }

    private void assertInvitationPageIsGone(AuthorInvitation invitation) {
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertThat(getElement(By.tagName("h1")).getText(), is(getMessage("error.410.invitation.not.valid.error.message.title")));
    }

}