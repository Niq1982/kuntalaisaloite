package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.MSG_SUCCESS_INVITATION_SENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import org.junit.Before;
import org.junit.Test;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.openqa.selenium.By;

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
    private static final String CONTACT_NAME = "Teppo Testaaja";
    private static final String CONTACT_EMAIL = "test@test.com";
    private Long municipalityId;
    private Long initiativeId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        municipalityId = testHelper.createTestMunicipality(MUNICIPALITY_1);
        initiativeId = testHelper.create(municipalityId, InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        overrideDriverToFirefox(true);
    }

    @Test
    public void add_author() {

        loginAsAuthorForLastTestHelperCreatedInitiative();
        
        open(urls.management(initiativeId));
        clickLinkContaining(getMessage(MSG_ADD_AUTHORS_LINK));
        
        clickLinkContaining(getMessage(MSG_BTN_ADD_AUTHOR));
        
        inputText("authorName", CONTACT_NAME);
        inputText("authorEmail", CONTACT_EMAIL);
        
        getElemContaining(getMessage(MSG_BTN_SEND), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_INVITATION_SENT);
        assertTextContainedByXPath("//div[@class='view-block last']//span[@class='status']", getMessage(MSG_INVITATION_UNCONFIRMED));
    }
    
    @Test
    public void author_invitation_acceptance_dialog_shows_given_information_prefilled() throws InterruptedException {
        AuthorInvitation invitation = testHelper.createInvitation(initiativeId, CONTACT_NAME, CONTACT_EMAIL);
        open(urls.invitation(invitation.getInitiativeId(), invitation.getConfirmationCode()));

        getElemContaining("Hyväksy kutsu", "span").click();
        assertThat(getElementByLabel("Nimi", "input").getAttribute("value"), containsString(CONTACT_NAME));
        assertThat(getElementByLabel("Sähköpostiosoite", "input").getAttribute("value"), containsString(CONTACT_EMAIL));
    }
    
    @Test
    public void reject_author_invitation() {
        
    }
    
}