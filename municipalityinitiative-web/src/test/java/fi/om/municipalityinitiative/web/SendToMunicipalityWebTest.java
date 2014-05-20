package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
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
    private static final String COMMENT = "Saate kunnalle";

    @Override
    public void childSetup() {

    }
    
    @Test
    public void send_to_municipality() {

        Long initiativeId = testHelper.create(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        
        open(urls.management(initiativeId));
        sendToMunicipality();
        assertMsgContainedByClass("msg-success",  MSG_SUCCESS_SEND);
        assertTotalEmailsInQueue(2);
    }

    @Test
    public void trying_operation_which_initiative_state_prevents_shows_error_page() {
        Long initiativeId = testHelper.create(HELSINKI_ID, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(initiativeId));

        testHelper.updateField(initiativeId, QMunicipalityInitiative.municipalityInitiative.sent, DateTime.now());

        sendToMunicipality();

        assertTitle("Toiminto ei ole sallittu - Kuntalaisaloitepalvelu");
    }

    private void sendToMunicipality() {
        clickLink(getMessage(MSG_BTN_SEND));
        inputText("comment", COMMENT);
        getElemContaining(getMessage(MSG_BTN_SEND_CONFIRM), "button").click();
    }

}
