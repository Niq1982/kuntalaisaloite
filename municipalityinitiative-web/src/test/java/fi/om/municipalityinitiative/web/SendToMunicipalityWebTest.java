package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.junit.Test;

public class SendToMunicipalityWebTest extends WebTestBase {

    
    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_SEND = "success.publish-and-send";
    private static final String MSG_BTN_SEND = "action.sendToMunicipality";
    private static final String MSG_BTN_SEND_CONFIRM = "action.sendToMunicipality.confirm";
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String COMMENT = "Saate kunnalle";
    
    @Test
    public void send_to_municipality() {
        
        Long municipalityId = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long initiativeId = testHelper.create(municipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        
        open(urls.management(initiativeId));
        clickLinkContaining(getMessage(MSG_BTN_SEND));
        inputText("comment", COMMENT);
        getElemContaining(getMessage(MSG_BTN_SEND_CONFIRM), "button").click();
        assertMsgContainedByClass("msg-success",  MSG_SUCCESS_SEND);
    }
    
}
