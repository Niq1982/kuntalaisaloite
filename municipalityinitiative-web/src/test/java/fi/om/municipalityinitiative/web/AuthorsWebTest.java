package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static fi.om.municipalityinitiative.util.OptionalMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.OptionalMatcher.isPresent;
import static fi.om.municipalityinitiative.web.InitiativeParticipateWebTest.OTHER_USER_SSN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class AuthorsWebTest extends WebTestBase {


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
    private static final String CONTACT_NAME = "Teppo Testaaja";
    private static final String CONTACT_EMAIL = "test@test.com";
    public static final String CONTACT_ADDRESS = "Joku Katu jossain 89";
    public static final String CONTACT_PHONE = "040111222";
    public static final String HYLKÄÄ_KUTSU = "invitation.reject";
    public static final String HYVÄKSY_KUTSUN_HYLKÄÄMINEN = "invitation.reject.confirm";
    public static final String VERIFIED_USER_AUTHOR_SSN = "010190-0001";
    public static final String USER_SSN = "010191-0000";
    private static final String MEMBERSHIP_RADIO = "initiative.municipalMembership.community";
    private Long normalInitiativeId;
    private Long verifiedInitiativeId;

    private static final boolean RUN_HOME_MUNICIPALITY_SELECTION_TESTS = false;

    @Override
    public void childSetup() {

        normalInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        verifiedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.ACCEPTED)
                .applyAuthor(VERIFIED_USER_AUTHOR_SSN)
                .toInitiativeDraft());
    }

    @Test
    public void accepting_verified_initiative_shows_login_button_to_vetuma_and_logging_in_redirects_back_to_initiative_page() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(verifiedInitiativeId, invitation.getConfirmationCode()));

        getElemContaining("Tunnistaudu ja hyväksy kutsu", "span").click();

        enterVetumaLoginInformationAndSubmit("111111-1111", HELSINKI);

        assertTitle(TestHelper.DEFAULT_INITIATIVE_NAME + " - Kuntalaisaloitepalvelu");
        assertThat(acceptInvitationButton(), isPresent());
    }

    // Email -> normal initiative -> same municipality
    @Test
    public void email_author_invitation_acceptance_dialog_has_given_values_prefilled_and_submitting_logs_user_in_as_author_with_given_information() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElemContaining("Sähköpostilla tunnistautuminen", "label").click();

        assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));

        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);
        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);

        int initiativeMunicipality = Integer.parseInt(getElement(By.id("form-invitation")).getAttribute("data-initiativemunicipality"));
        if (RUN_HOME_MUNICIPALITY_SELECTION_TESTS) {
            assertHomeMunicipality(initiativeMunicipality, "Hyväksy ja tallenna tiedot", true);
        }
        getElemContaining("Olen kunnan asukas", "label").click();

        validateMandatoryInputsAssert(true, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        email_author_invitation_acceptance_dialog_shows(invitation);
    }

    private void email_author_invitation_acceptance_dialog_shows(AuthorInvitation invitation) {
        assertSuccessMessage("Liittymisesi vastuuhenkilöksi on nyt vahvistettu ja olet kirjautunut sisään palveluun.");

        clickDialogButton("Muokkaa aloitetta");
        assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));
        assertThat(getElementByLabel("Puhelin", "input").getAttribute("value"), containsString(CONTACT_PHONE));
        assertThat(getElementByLabel("Osoite", "textarea").getText(), containsString(CONTACT_ADDRESS));

        assertInvitationPageIsGone(invitation);

        assertTotalEmailsInQueue(1);
    }

    // Email -> normal initiative -> another municipality
    @Test
    public void email_author_invitation_to_normal_initiative_with_another_home_municipality() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElemContaining("Sähköpostilla tunnistautuminen", "label").click();

        assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));

        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);
        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);

        int initiativeMunicipality = Integer.parseInt(getElement(By.id("form-invitation")).getAttribute("data-initiativemunicipality"));
        if (RUN_HOME_MUNICIPALITY_SELECTION_TESTS) {
            assertHomeMunicipality(initiativeMunicipality, "Hyväksy ja tallenna tiedot", true);
        }
        getElemContaining("Olen asukas toisessa kunnassa", "label").click();
        getElemContaining("Nimenkirjoitusoikeus yhteisössä", "span").click();
        homeMunicipalitySelect(VANTAA);

        validateMandatoryInputsAssert(true, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        email_author_invitation_acceptance_dialog_shows(invitation);
    }

    // Email -> normal initiative -> disallow
    @Test
    public void email_author_invitation_to_normal_initiative_shows_validation_messages() {
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElemContaining("Sähköpostilla tunnistautuminen", "label").click();

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        getElementByLabel("Etu- ja sukunimi", "input").clear();
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        getElemContaining("Olen asukas toisessa kunnassa", "label").click();
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        getElemContaining("Ei mitään näistä", "label").click();
        assertTextContainedByClass("msg-warning", "Jos mikään perusteista ei täyty, et voi liittyä aloitteen vastuuhenkilöksi");
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));
    }

    // Vetuma, private municipality -> normal initiative -> same municipality
    @Test
    public void author_invitation_to_normal_initiative_when_private_home_municipality_from_vetuma_succeeds() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", null);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        int initiativeMunicipality = Integer.parseInt(getElement(By.id("form-invitation")).getAttribute("data-initiativemunicipality"));
        if (RUN_HOME_MUNICIPALITY_SELECTION_TESTS) {
            assertHomeMunicipality(initiativeMunicipality, "Hyväksy ja tallenna tiedot", true);
        }
        getElemContaining("Olen kunnan asukas", "label").click();

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        author_invitation_acceptance_dialog_shows(invitation);
    }

    // Vetuma, private municipality -> normal initiative -> another municipality
    @Test
    public void accepting_invitation_to_normal_initiative_as_verified_author_allows_change_of_municipality_if_not_received_from_vetuma_and_requires_membershipType_if_selected_municipality_mismatch() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", null);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        int initiativeMunicipality = Integer.parseInt(getElement(By.id("form-invitation")).getAttribute("data-initiativemunicipality"));
        if (RUN_HOME_MUNICIPALITY_SELECTION_TESTS) {
            assertHomeMunicipality(initiativeMunicipality, "Hyväksy ja tallenna tiedot", true);
        }
        getElemContaining("Olen asukas toisessa kunnassa", "label").click();

        getElemContaining("Nimenkirjoitusoikeus yhteisössä", "span").click();
        homeMunicipalitySelect(VANTAA);

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        author_invitation_acceptance_dialog_shows(invitation);
    }

    private void author_invitation_acceptance_dialog_shows(AuthorInvitation invitation) {
        assertSuccessMessage("Liittymisesi vastuuhenkilöksi on nyt vahvistettu ja olet kirjautunut sisään palveluun.");
        assertInvitationPageIsGone(invitation);
        assertTotalEmailsInQueue(1);
    }

    // Vetuma, private municipality -> normal initiative -> disallow
    @Test
    public void accepting_invitation_to_normal_initiative_as_verified_author_requires_has_municipality_pre_selected() {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", null);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        getElemContaining("Olen asukas toisessa kunnassa", "label").click();
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        getElemContaining("Ei mitään näistä", "label").click();
        assertTextContainedByClass("msg-warning", "Jos mikään perusteista ei täyty, et voi liittyä aloitteen vastuuhenkilöksi");
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));
    }

    // Vetuma, private municipality -> verified initiative -> same municipality
    @Test
    public void accepting_verified_author_invitation_when_unknown_municipality_from_vetuma_preselects_initiatives_municipality_and_allows_accepting() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", null);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);
        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);

        getElemContaining("Olen kunnan asukas", "label").click();

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");
        author_invitation_acceptance_dialog_shows(invitation);
    }

    // Vetuma, private municipality -> verified initiative -> another municipality -> error
    @Test
    public void accepting_verified_author_invitation_when_unknown_municipality_prevents_accepting_if_user_selects_wrong_municipality() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", null);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));
        getElemContaining("Olen asukas toisessa kunnassa", "label").click();
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        //assertTextContainedByClass("msg-warning", "Et ole aloitteen kunnan asukas"); //FAILS: wrong text
    }

    //Vetuma -> normal initiative -> same municipality
    @Test
    public void accepting_invitation_to_normal_initiative_as_verified_author_with_valid_municipality() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", HELSINKI);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        author_invitation_acceptance_dialog_shows(invitation);
    }

    // Vetuma -> normal initiative -> another municipality
    @Test
    public void accepting_invitation_to_normal_initiative_as_verified_author_requires_membership_selection_if_municipality_mismatch() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", VANTAA);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        assertThat(getElemContaining("Ei mitään näistä", "span").isDisplayed(), is(true));

        getElemContaining("Nimenkirjoitusoikeus yhteisössä", "span").click();

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");

        author_invitation_acceptance_dialog_shows(invitation);
    }

    // Vetuma -> normal initiative -> another municipality -> error
    @Test
    public void author_invitation_to_normal_initiative_as_verified_with_another_municipality_disallowed_options() {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin("111111-1111", VANTAA);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));
        getElemContaining("Ei mitään näistä", "label").click();
        assertTextContainedByClass("msg-warning", "Jos mikään perusteista ei täyty, et voi liittyä aloitteen vastuuhenkilöksi");
        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));
    }

    // Vetuma -> verified initiative -> same municipality
    @Test
    public void author_invitation_to_verified_initiative_as_verified_with_same_municipality() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", HELSINKI);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);
        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);

        validateMandatoryInputsAssert(false, true);

        clickDialogButton("Hyväksy ja tallenna tiedot");
        author_invitation_acceptance_dialog_shows(invitation);
    }

    // Vetuma -> verified initiative -> another municipality -> error
    @Test
    public void author_invitation_to_verified_initiative_as_verified_with_another_municipality_disallowed_options() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", VANTAA);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        assertTextContainedByClass("msg-warning", "Väestötietojärjestelmän mukaan kotikuntasi ei ole kunta, jota aloite koskee, joten et voi liittyä aloitteen vastuuhenkilöksi");
        assertThat(acceptInvitationButton(), isNotPresent());
        assertThat(rejectInvitationButton(), isPresent());
    }

    // Vetuma -> verified initiative -> disallow
    @Test
    public void author_invitation_to_verified_initiative_shows_validation_messages() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", HELSINKI);

        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        // Clear email field so we get a validation error
        getElementByLabel("Sähköpostiosoite", "input").clear();
        //TODO: disable the save button
        clickDialogButton("Hyväksy ja tallenna tiedot");
        assertPageHasValidationErrors();
    }

    @Test
    public void add_author() {
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.management(normalInitiativeId));
        clickLink(getMessage(MSG_ADD_AUTHORS_LINK));
        clickLink(getMessage(MSG_BTN_ADD_AUTHOR));

        inputText("authorName", CONTACT_NAME);
        inputText("authorEmail", CONTACT_EMAIL);

        getElemContaining(getMessage(MSG_BTN_SEND), "button").click();

        assertSuccessMessage("Kutsu lähetetty");
        assertTextContainedByXPath("//div[@class='view-block last']//span[@class='status']", getMessage(MSG_INVITATION_UNCONFIRMED));
        assertTotalEmailsInQueue(1);
    }

    @Test
    public void accepting_normal_author_invitation_lets_user_to_accept_invitation_even_if_logged_in_as_author() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertThat(acceptInvitationButton(), isPresent());
        assertThat(rejectInvitationButton(), isPresent());
    }

    @Test
    public void accepting_invitation_to_normal_initiative_redirects_to_authentication_and_back_if_user_wants_verified_authentication() {

        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        AuthorInvitation invitation = testHelper.createInvitation(publishedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        acceptInvitationButton().get().click();

        clickLink("Tunnistaudu ja osallistu");
        enterVetumaLoginInformationAndSubmit(OTHER_USER_SSN, HELSINKI);
        clickButton("Hyväksy ja tallenna tiedot");

        assertInvitationPageIsGone(invitation);
        assertTotalEmailsInQueue(1);
    }

    @Test
    public void accepting_verified_author_invitation_shows_warning_and_hides_buttons_if_already_author() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);

        vetumaLogin(VERIFIED_USER_AUTHOR_SSN, HELSINKI);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertWarningMessage("Olet jo aloitteen vastuuhenkilö, joten et voi hyväksyä vastuuhenkilökutsua");
        assertThat(acceptInvitationButton(), isNotPresent());
        assertThat(rejectInvitationButton(), isNotPresent());
    }

    @Test
    public void accepting_expired_invitation_shows_gone_page() {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setConfirmationCode("asd");
        authorInvitation.setInvitationTime(DateTime.now().minusMonths(1));
        authorInvitation.setInitiativeId(normalInitiativeId);
        authorInvitation.setEmail("asd@example.com");
        authorInvitation.setName("name");
        testHelper.addAuthorInvitation(authorInvitation, false);

        assertInvitationPageIsGone(authorInvitation);
    }

    @Test
    public void accepting_verified_author_invitation_shows_warning_and_hides_accept_button_if_wrong_homeMunicipality() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", VANTAA);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertWarningMessage("Väestötietojärjestelmän mukaan kotikuntasi ei ole kunta, jota aloite koskee, joten et voi liittyä aloitteen vastuuhenkilöksi. Kiitos mielenkiinnosta!");
        assertThat(acceptInvitationButton(), isNotPresent());
        assertThat(rejectInvitationButton(), isPresent());
    }

    @Test
    public void accepting_verified_author_invitation_shows_success_message_shows_management_page_and_increases_participant_count_with_one() {
        AuthorInvitation invitation = testHelper.createInvitation(verifiedInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        vetumaLogin("111111-1111", HELSINKI);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        acceptInvitationButton().get().click();

        getElementByLabel("Osoite", "textarea").sendKeys(CONTACT_ADDRESS);
        getElementByLabel("Puhelin", "input").sendKeys(CONTACT_PHONE);

        clickDialogButton("Hyväksy ja tallenna tiedot");
        assertSuccessMessage("Liittymisesi vastuuhenkilöksi on nyt vahvistettu ja olet kirjautunut sisään palveluun.");
        assertTotalEmailsInQueue(1);
    }

    @Test
    public void reject_author_invitation() throws InterruptedException {
        overrideDriverToFirefox(true);
        AuthorInvitation invitation = testHelper.createInvitation(normalInitiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        clickDialogButtonMsg(HYLKÄÄ_KUTSU);
        clickDialogButtonMsg(HYVÄKSY_KUTSUN_HYLKÄÄMINEN);

        assertSuccessMessage("Olet hylännyt kutsun vastuuhenkilöksi eikä tietojasi ole tallennettu aloitteeseen");

        assertInvitationPageIsGone(invitation);
        assertTotalEmailsInQueue(0);
    }

    @Test
    public void author_removes_participant(){
        Long publishedInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(publishedInitiativeId, HELSINKI_ID));

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(publishedInitiativeId));

        clickLink("Osallistujahallinta");
        clickLink("Poista osallistuja");

        // NOTE: We could assert that modal has correct Participant details,
        //       but as DOM is updated after the modal is loaded we would need a tiny delay for that

        getElemContaining("Poista", "button").click();

        assertSuccessMessage("Osallistuja poistettu");
        assertTotalEmailsInQueue(0);
    }

    @Test
    public void author_removes_verified_participant() throws InterruptedException {
        Long publishedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID).withState(InitiativeState.PUBLISHED).applyAuthor(USER_SSN).toInitiativeDraft());

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(publishedInitiativeId, HELSINKI_ID).withShowName(false));

        vetumaLogin(USER_SSN, HELSINKI);

        open(urls.management(publishedInitiativeId));

        clickLink("Osallistujahallinta");
        clickLink("Poista osallistuja");

        // NOTE: We could assert that modal has correct Participant details,
        //       but as DOM is updated after the modal is loaded we would need a tiny delay for that

        getElemContaining("Poista", "button").click();

        assertSuccessMessage("Osallistuja poistettu");
        assertTotalEmailsInQueue(0);

    }
    @Test
    public void author_removes_author(){
        testHelper.createDefaultAuthorAndParticipant(new TestHelper.AuthorDraft(normalInitiativeId, HELSINKI_ID));

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(normalInitiativeId));

        clickLink("Ylläpidä vastuuhenkilöitä");
        clickLink("Poista vastuuhenkilö");

        // NOTE: We could assert that modal has correct Author details,
        //       but as DOM is updated after the modal is loaded we would need a tiny delay for that

        getElemContaining("Poista vastuuhenkilö", "button").click();

        assertSuccessMessage("Vastuuhenkilö poistettu");
        assertTotalEmailsInQueue(2);
    }

    private Optional<WebElement> rejectInvitationButton() {
        return getOptionalElemContaining("Hylkää kutsu", "span");
    }

    private Optional<WebElement> acceptInvitationButton() {
        return getOptionalElemContaining("Hyväksy kutsu", "span");
    }

    private void assertInvitationPageIsGone(AuthorInvitation invitation) {
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));
        assertThat(getElement(By.tagName("h1")).getText(), is(getMessage("error.410.invitation.not.valid.error.message.title")));
    }

    private void validateMandatoryInputsAssert(Boolean validateName, boolean validateEmail) throws InterruptedException {
        if (validateName) {
            removeInputValue("Etu- ja sukunimi");
        }

        if (validateEmail) {
            removeInputValue("Sähköpostiosoite");
        }

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(false));

        if (validateName) {
            setInputValue("contactInfo.name", CONTACT_NAME);
            assertThat(getElementByLabel("Etu- ja sukunimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        }

        if (validateEmail) {
            setInputValue("contactInfo.email", CONTACT_EMAIL);
            assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));
        }

        getElement(By.id("homeMunicipality_chzn")).click();

        assertThat(getElemContaining("Hyväksy ja tallenna tiedot", "button").isEnabled(), is(true));
    }

}