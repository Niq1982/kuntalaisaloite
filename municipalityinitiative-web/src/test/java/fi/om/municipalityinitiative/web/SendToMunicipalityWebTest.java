package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import org.junit.Test;

public class SendToMunicipalityWebTest extends WebTestBase {

    
    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_SEND = "success.send.title";
    private static final String MSG_BTN_SEND = "action.send";
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String COMMENT = "Saate kunnalle";
    
    @Test
    public void send_to_municipality() {
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long initiativeId = testHelper.createTestInitiative(municipality1Id, "Testi aloite", true, true);
        
        open(urls.management(initiativeId, TestHelper.TEST_MANAGEMENT_HASH));
        clickLinkContaining(getMessage(MSG_BTN_SEND));
        inputText("comment", COMMENT);
        getElemContaining(getMessage(MSG_BTN_SEND), "button").click();
        assertMsgContainedByClass("modal-title", MSG_SUCCESS_SEND);
    }
    
}
