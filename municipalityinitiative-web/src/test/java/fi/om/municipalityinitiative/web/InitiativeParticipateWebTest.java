package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
    private static final String AUTHOR_MESSAGE = "Tässä on viesti";
    
    private Long municipality1Id;
    private Long municipality2Id;
    private Long initiativeId;
    
    @Before
    public void setup() {
        testHelper.dbCleanup();
        municipality1Id = testHelper.createTestMunicipality(MUNICIPALITY_1);
        municipality2Id = testHelper.createTestMunicipality(MUNICIPALITY_2);
        initiativeId = testHelper.create(municipality1Id, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
    }

    @Test
    public void participate_normal_initiative_shows_validation_errors() {
        open(urls.view(initiativeId));

        clickLinkContaining(getMessage(MSG_BTN_PARTICIPATE));
        getElemContaining(getMessage(MSG_BTN_SAVE), "button").click();
        assertPageHasValidationErrors();
    }

    @Test
    public void participate_initiative_with_public_name() {
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
    
    @Test
    public void public_user_contacts_authors(){
        open(urls.view(initiativeId));
        
        clickLinkContaining("Ota yhteyttä aloitteen vastuuhenkilöön");
        
        inputText("message", AUTHOR_MESSAGE);
        inputText("contactName", PARTICIPANT_NAME);
        inputText("contactEmail", PARTICIPANT_EMAIL);
        
        getElemContaining("Lähetä viesti", "button").click();
        
        assertTextContainedByClass("msg-success", "Linkki yhteydenottopyynnön vahvistamiseen on lähetetty sähköpostiisi");
        
        open(urls.confirmAuthorMessage(RandomHashGenerator.getPrevious()));
        
        assertTextContainedByClass("msg-success", "Viesti on nyt lähetetty vastuuhenkilöille");
        
    }

    
}
