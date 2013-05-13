package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.MSG_SUCCESS_INVITATION_SENT;

import org.junit.Test;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;

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
    
    @Test
    public void add_author() {
        Long municipalityId = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long initiativeId = testHelper.create(municipalityId, InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        
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
    public void accept_author_invitation() {
        
    }
    
    @Test
    public void reject_author_invitation() {
        
    }
    
}