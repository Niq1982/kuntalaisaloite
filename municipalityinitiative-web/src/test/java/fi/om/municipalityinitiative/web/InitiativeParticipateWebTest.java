package fi.om.municipalityinitiative.web;

import org.junit.Ignore;
import org.junit.Test;

public class InitiativeParticipateWebTest extends WebTestBase {
    
    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_PARTICIPATE = "success.participate";
    private static final String MSG_BTN_PARTICIPATE = "action.participate";
    private static final String MSG_BTN_SAVE = "action.save";
    private static final String SELECT_MUNICIPALITY = "initiative.chooseMunicipality";
    private static final String RADIO_FRANCHISE_TRUE = "initiative.franchise.true";
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String MUNICIPALITY_2 = "Helsinki";
    private static final String PARTICIPANT_NAME = "Ossi Osallistuja";
    
    /*
     * TODO: Participate initiative Web test
     * 
     * Open the modal
     * Fill in the form (different options)
     * Save
     * Assert that success message is visible and correct
     * Assert that participant was added to right column
     * Assert that participant-button is NOT visible 
     * 
     */
    
    @Test
    @Ignore("TODO")
    public void participate_initiative() {
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        Long initiativeId = testHelper.createTestInitiative(municipality1Id, "Testi aloite", true, true);
        
        open(urls.view(initiativeId));
        
        waitms(500);
        
        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));
        
        waitms(500); // Shorten or remove if possible
        
        inputText("participantName", PARTICIPANT_NAME);
        
        /* TODO: Enable this when user wants to select another municipality
        waitms(500); // Tiny delay is required if run from Eclipse.
        clickLinkContaining(SELECT_MUNICIPALITY);
        waitms(500); // Tiny delay is required if run from Eclipse.
        clickById("municipality_chzn_o_1");
        */
        
        // TODO: Other options: franchise and municipality membership
        waitms(500);
        clickLinkContaining(getMessage(RADIO_FRANCHISE_TRUE));
        
        
        clickLinkContaining(MSG_BTN_SAVE);
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);
    }
}
