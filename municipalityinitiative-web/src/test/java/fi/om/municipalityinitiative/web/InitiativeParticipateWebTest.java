package fi.om.municipalityinitiative.web;

import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;

public class InitiativeParticipateWebTest extends WebTestBase {
    
    /**
     * Localization keys as constants.
     */
    private static final String MSG_SUCCESS_PARTICIPATE = "success.participate";
    private static final String MSG_BTN_PARTICIPATE = "action.participate";
    private static final String MSG_BTN_SAVE = "action.save";
    private static final String PARTICIPANT_SHOW_NAME = "participant.showName";
    private static final String MEMBERSHIP_RADIO = "initiative.municipalMembership.community";
    
    /**
     * Form values as constants.
     */
    private static final String MUNICIPALITY_1 = "Vantaa";
    private static final String MUNICIPALITY_2 = "Helsinki";
    private static final String PARTICIPANT_NAME = "Ossi Osallistuja";
    private static final String PARTICIPANT_EMAIL = "test@test.com";
    
    @Test
    public void participate_initiative_with_public_name() {
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        Long initiativeId = testHelper.create(municipality1Id, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        open(urls.view(initiativeId));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));

        inputText("participantName", PARTICIPANT_NAME);
        inputText("participantEmail", PARTICIPANT_EMAIL);
        
        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);
       
        assertThat(getOptionalElemContaining(getMessage(MSG_BTN_PARTICIPATE), "a").isPresent(), is(false));
        
        open(urls.confirmParticipant(testHelper.getLastParticipantId(), RandomHashGenerator.getPrevious()));
        
        assertTextContainedByClass("public-names", "2 nimeä julkaistu palvelussa");
    }
    
    @Test
    public void participate_initiative_with_private_name_and_select_membership_type() {
        overrideDriverToFirefox(true); // Municipality select need firefox driver
        
        Long municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        Long municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        
        Long initiativeId = testHelper.create(municipality1Id, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        
        open(urls.view(initiativeId));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));

        inputText("participantName", PARTICIPANT_NAME);
        getElemContaining(getMessage(PARTICIPANT_SHOW_NAME), "span").click();
        
        clickLinkContaining(MUNICIPALITY_1);
        getElemContaining(MUNICIPALITY_2, "li").click();
        
        getElemContaining(getMessage(MEMBERSHIP_RADIO), "span").click();
        
        inputText("participantEmail", PARTICIPANT_EMAIL);
       
        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_PARTICIPATE);
       
        assertThat(getOptionalElemContaining(getMessage(MSG_BTN_PARTICIPATE), "a").isPresent(), is(false));
        
        open(urls.confirmParticipant(testHelper.getLastParticipantId(), RandomHashGenerator.getPrevious()));
        
        assertTextContainedByClass("private-names", "1 nimi ei julkaistu palvelussa");
    }
    

    
}
