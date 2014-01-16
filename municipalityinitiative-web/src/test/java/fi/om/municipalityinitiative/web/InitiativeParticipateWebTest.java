package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InitiativeParticipateWebTest extends WebTestBase {

    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_PARTICIPATE = "success.participate";
    private static final String MSG_SUCCESS_PARTICIPATE_VERIFIABLE = "success.participate-verifiable.title";
    private static final String MSG_BTN_PARTICIPATE = "action.participate";
    private static final String MSG_BTN_SEND_CONFIRMATION = "action.send.confirmation";
    private static final String PARTICIPANT_SHOW_NAME = "participant.showName";
    private static final String MEMBERSHIP_RADIO = "initiative.municipalMembership.community";
    
    /**
     * Form values as constants.
     */
    private static final String PARTICIPANT_NAME = "Ossi Osallistuja";
    private static final String PARTICIPANT_EMAIL = "test@test.com";
    private static final String AUTHOR_MESSAGE = "Tässä on viesti";
    public static final String VERIFIED_INITIATIVE_AURHOR_SSN = "000000-0000";
    public static final String OTHER_USER_SSN = "111111-1111";

    private Long normalInitiativeHelsinki;
    private Long verifiedInitiativeHelsinki;

    @Override
    public void childSetup() {
        normalInitiativeHelsinki = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(HELSINKI_ID)
                        .withState(InitiativeState.PUBLISHED)
                        .withType(InitiativeType.COLLABORATIVE)
                        .withParticipantCount(0)
        );
        verifiedInitiativeHelsinki = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withParticipantCount(0)
                .applyAuthor(VERIFIED_INITIATIVE_AURHOR_SSN)
                .toInitiativeDraft()
        );
    }

    @Test
    public void participate_normal_initiative_shows_validation_errors() {
        open(urls.view(normalInitiativeHelsinki));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));
        getElemContaining(getMessage(MSG_BTN_SEND_CONFIRMATION), "button").click();
        assertPageHasValidationErrors();
    }

    @Test
    public void participate_to_normal_initiative_with_public_name() {
        open(urls.view(normalInitiativeHelsinki));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));

        inputText("participantName", PARTICIPANT_NAME);
        inputText("participantEmail", PARTICIPANT_EMAIL);
        
        getElemContaining(getMessage(MSG_BTN_SEND_CONFIRMATION), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);

        assertTotalEmailsInQueue(1);
       
        assertThat(getOptionalElemContaining(getMessage(MSG_BTN_PARTICIPATE), "a"), isNotPresent());
        
        open(urls.confirmParticipant(testHelper.getLastParticipantId(), RandomHashGenerator.getPrevious()));
        
        assertTextContainedByClass("public-names", "1 nimi julkaistu palvelussa");

    }

    @Test
    public void participating_to_initiative_when_not_logged_in_redirects_to_vetuma_and_back_to_participation_page() {
        open(urls.view(verifiedInitiativeHelsinki));

        clickLinkContaining("Tunnistaudu ja osallistu");
        enterVetumaLoginInformationAndSubmit("111111-1111", HELSINKI);
        assertTitle(TestHelper.DEFAULT_INITIATIVE_NAME + " - Kuntalaisaloitepalvelu");
        assertThat(participateToInitiativeButton(), isPresent());

    }

    @Test
    public void participate_to_verified_initiative_shows_success_message_and_increases_participant_count_on_page() {
        vetumaLogin("111111-1111", HELSINKI);
        open(urls.view(verifiedInitiativeHelsinki));

        Integer originalParticipantCountOnPage = Integer.valueOf(getElement(By.className("user-count-total")).getText());

        assertThat(participateToInitiativeButton(), isPresent());
        participateToInitiativeButton().get().click();

        // Vetuma participant has no information to fill
        getElemContaining("Tallenna", "button").click();

        assertMsgContainedByClass("modal-title", MSG_SUCCESS_PARTICIPATE_VERIFIABLE);
        Integer newParticipantCountOnPage = Integer.valueOf(getElement(By.className("user-count-total")).getText());

        assertThat(newParticipantCountOnPage, is(originalParticipantCountOnPage + 1));

        assertTextContainedByClass("msg-warning", "Olet jo osallistunut tähän aloitteeseen");
        assertThat(participateToInitiativeButton(), isNotPresent());


    }

    @Test
    public void participating_to_verified_initiative_is_not_allowed_if_wrong_homeMunicipality() {
        vetumaLogin(OTHER_USER_SSN, VANTAA);
        open(urls.view(verifiedInitiativeHelsinki));

        assertTextContainedByClass("msg-warning", "Et ole aloitteen kunnan jäsen, joten et voi osallistua aloitteeseen.");
        assertThat(participateToInitiativeButton(), isNotPresent());
    }

    @Test
    @Ignore("Implement")
    public void participating_to_verified_initiative_when_private_home_municipality_from_vetuma() {
        vetumaLogin(OTHER_USER_SSN, "Ei kuntaa");

        // Implement
    }

    private Maybe<WebElement> participateToInitiativeButton() {
        return getOptionalElemContaining("Osallistu aloitteeseen", "span");
    }

    @Test
    public void participate_to_normal_initiative_with_private_name_and_select_membership_type() {
        overrideDriverToFirefox(true); // Municipality select need firefox driver

        open(urls.view(normalInitiativeHelsinki));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));

        inputText("participantName", PARTICIPANT_NAME);
        getElemContaining(getMessage(PARTICIPANT_SHOW_NAME), "span").click();

        clickLinkContaining(HELSINKI);
        getElemContaining(VANTAA, "li").click();

        getElemContaining(getMessage(MEMBERSHIP_RADIO), "span").click();

        inputText("participantEmail", PARTICIPANT_EMAIL);

        getElemContaining(getMessage(MSG_BTN_SEND_CONFIRMATION), "button").click();

        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);

        assertTotalEmailsInQueue(1);

        assertThat(getOptionalElemContaining(getMessage(MSG_BTN_PARTICIPATE), "a"), isNotPresent());

        open(urls.confirmParticipant(testHelper.getLastParticipantId(), RandomHashGenerator.getPrevious()));

        assertTextContainedByClass("private-names", "1 nimi ei julkaistu palvelussa");
    }

    @Test
    public void public_user_contacts_authors_shows_success_message() {

        Long initiativeWithAuthor = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(HELSINKI_ID)
                        .withState(InitiativeState.PUBLISHED)
                        .withType(InitiativeType.COLLABORATIVE)
                        .withParticipantCount(0)
                        .applyAuthor()
                        .withPublicName(false)
                        .toInitiativeDraft()
        );

        open(urls.view(initiativeWithAuthor));

        clickLinkContaining("Ota yhteyttä aloitteen vastuuhenkilöön");

        inputText("message", AUTHOR_MESSAGE);
        inputText("contactName", PARTICIPANT_NAME);
        inputText("contactEmail", PARTICIPANT_EMAIL);

        getElemContaining("Lähetä viesti", "button").click();

        assertTotalEmailsInQueue(1);

        assertTextContainedByClass("msg-success", "Linkki yhteydenottopyynnön vahvistamiseen on lähetetty sähköpostiisi");

        open(urls.confirmAuthorMessage(RandomHashGenerator.getPrevious()));

        assertTextContainedByClass("msg-success", "Viesti on nyt lähetetty vastuuhenkilöille");

        assertTotalEmailsInQueue(2);

    }

    
}
