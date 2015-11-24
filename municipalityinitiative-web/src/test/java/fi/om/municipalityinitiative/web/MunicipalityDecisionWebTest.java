package fi.om.municipalityinitiative.web;


import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.junit.Test;

public class MunicipalityDecisionWebTest  extends WebTestBase {

    public static final String DECISIONTEXT = "Kunnalla ei ole varattu määrärahoja tällä valtuustokaudella.";
    public static final String PUBLISH = "Julkaise vastaus";
    public static final String SUCCESS = "Vastaus lisätty";
    public static final String VERIFIED_USER_AUTHOR_SSN = "010190-0001";


    private Long verifiedInitiativeId;

    @Override
    protected void childSetup() {
        verifiedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withSent(DateTime.now())
                .applyAuthor(VERIFIED_USER_AUTHOR_SSN)
                .toInitiativeDraft());

    }

    @Test
    public void municipality_can_answer_to_initiative() {
        testHelper.sendToMunicipality(verifiedInitiativeId);
        openMunicipalityDecisionViewForLastSentInitiative();
        open(urls.getMunicipalityDecisionView(verifiedInitiativeId));
        inputTextByCSS("#description", DECISIONTEXT);
        clickButton(PUBLISH);
        assertSuccessMessage(SUCCESS);
    }

    @Test
    public void municipality_description_text_and_attachment_cant_both_be_empty() {
        testHelper.sendToMunicipality(verifiedInitiativeId);
        openMunicipalityDecisionViewForLastSentInitiative();
        open(urls.getMunicipalityDecisionView(verifiedInitiativeId));
        clickButton(PUBLISH);
        this.assertPageHasValidationErrors();
    }

    private void openMunicipalityDecisionViewForLastSentInitiative() {
        open(urls.loginMunicipality(testHelper.getPreviousMunicipalityHash()));

    }
}
