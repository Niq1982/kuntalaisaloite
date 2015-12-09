package fi.om.municipalityinitiative.web;


import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class MunicipalityDecisionWebTest  extends WebTestBase {

    public static final String DECISIONTEXT = "Kunnalla ei ole varattu määrärahoja tällä valtuustokaudella.";
    public static final String PUBLISH = "Julkaise vastaus";
    public static final String SUCCESS = "Vastaus lisätty";
    public static final String VERIFIED_USER_AUTHOR_SSN = "010190-0001";
    public static final String MUNICIPALITY_ANSWER = "Kunnan vastaus";


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

        open(urls.view(verifiedInitiativeId));
        WebElement element = getElement(By.tagName("h2"));
        assertThat(element.getText(), containsString(MUNICIPALITY_ANSWER));

        assertTotalEmailsInQueue(1);
    }

    @Test
    public void municipality_description_text_and_attachment_cant_both_be_empty() {
        testHelper.sendToMunicipality(verifiedInitiativeId);
        openMunicipalityDecisionViewForLastSentInitiative();
        open(urls.getMunicipalityDecisionView(verifiedInitiativeId));
        clickButton(PUBLISH);

        this.assertPageHasValidationErrors();
        open(urls.view(verifiedInitiativeId));
        WebElement element = getElement(By.tagName("h2"));
        assertThat(element.getText(), not(MUNICIPALITY_ANSWER));

        assertTotalEmailsInQueue(0);

    }

    private void openMunicipalityDecisionViewForLastSentInitiative() {
        open(urls.loginMunicipality(testHelper.getPreviousMunicipalityHash()));

    }
}
