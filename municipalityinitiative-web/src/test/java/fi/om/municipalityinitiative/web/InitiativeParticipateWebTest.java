package fi.om.municipalityinitiative.web;

import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;

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
    private static final String PARTICIPANT_EMAIL = "test@test.com";

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
    public void participate_initiative_does_something_when_something() {
        
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        Long initiativeId = testHelper.create(municipality1Id, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        open(urls.view(initiativeId));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));

        inputText("participantName", PARTICIPANT_NAME);
        clickByName("showName"); // TODO: Use text instead
        
        /* TODO: Select another municipality
        clickLinkContaining(MUNICIPALITY_1);
        getElemContaining(MUNICIPALITY_2, "li").click(); */

        
        inputText("participantEmail", PARTICIPANT_EMAIL);
        
        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);
       
        assertThat(getOptionalElemContaining(getMessage(MSG_BTN_PARTICIPATE), "a").isPresent(), is(false));
        
        
        
    }
    
}
