package fi.om.municipalityinitiative.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

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
    public void participate_initiative() {
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        Long initiativeId = testHelper.createTestInitiative(municipality1Id, "Testi aloite", true, true);
        
        open(urls.view(initiativeId));
        
        wait100();
        
        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));
        
        wait100();
        
        inputText("participantName", PARTICIPANT_NAME);
        clickByName("showName"); // TODO: Use text instead
        
        /* TODO: Select another municipality
        waitms(500);;
        clickLinkContaining(MUNICIPALITY_1);
        waitms(500);
        getElemContaining(MUNICIPALITY_2, "li").click(); */

        
        // TODO: Other options: franchise and municipality membership
        wait100();
        
        getElemContaining(getMessage(RADIO_FRANCHISE_TRUE), "label").click();
        
        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);
    }
    
}
