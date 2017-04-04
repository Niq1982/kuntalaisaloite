package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class SearchInitiativesWebTest extends WebTestBase {

    @Override
    public void childSetup() {

    }

    @Test
    public void page_opens_when_navigation_link_clicked() {
        open(urls.search());
        assertThat(pageTitle(), is("Selaa kuntalaisaloitteita - Kuntalaisaloitepalvelu"));
    }

    @Test
    public void municipalities_are_listed() {
        overrideDriverToFirefox(true);
        open(urls.search());

        clickInput();

        String municipalitiesRawText = getChosenResultsRawText();
        assertThat(municipalitiesRawText, containsString(HELSINKI));
        assertThat(municipalitiesRawText, containsString(VANTAA));
    }

    @Test
    public void search_by_municipality() {
        overrideDriverToFirefox(true);

        String collaborativeCitizenInitiativeNameHelsinki = "Collaborative Citizen Helsinki";

        String collaborativeCitizenInitiativeNameVantaa = "Collaborative Citizen Vantaa";


        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withName(collaborativeCitizenInitiativeNameHelsinki));

        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(VANTAA_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withName(collaborativeCitizenInitiativeNameVantaa));

        open(urls.search());

        clickInput();

        String municipalitiesRawText = getChosenResultsRawText();
        assertThat(municipalitiesRawText, containsString(HELSINKI));
        assertThat(municipalitiesRawText, containsString(VANTAA));

        clickById("municipalities_chzn_o_1");

        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeNameHelsinki);
        assertTextNotContainedByClass("search-results", collaborativeCitizenInitiativeNameVantaa);
    }

    @Test
    public void search_by_several_municipalities() {
        overrideDriverToFirefox(true);

        String collaborativeCitizenInitiativeNameHelsinki = "Collaborative Citizen Helsinki";

        String collaborativeCitizenInitiativeNameVantaa = "Collaborative Citizen Vantaa";


        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withName(collaborativeCitizenInitiativeNameHelsinki));

        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(VANTAA_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withName(collaborativeCitizenInitiativeNameVantaa));

        open(urls.search());

        clickInput();

        String municipalitiesRawText = getChosenResultsRawText();
        assertThat(municipalitiesRawText, containsString(HELSINKI));
        assertThat(municipalitiesRawText, containsString(HYVINKAA));
        assertThat(municipalitiesRawText, containsString(VANTAA));

        clickById("municipalities_chzn_o_1");

        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeNameHelsinki);
        assertTextNotContainedByClass("search-results", collaborativeCitizenInitiativeNameVantaa);

        clickInput();

        municipalitiesRawText = getChosenResultsRawText();
        assertThat(municipalitiesRawText, containsString(VANTAA));
        assertThat(municipalitiesRawText, containsString(HYVINKAA));

        clickById("municipalities_chzn_o_3");

        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeNameHelsinki);
        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeNameVantaa);

    }


    private String getChosenResultsRawText() {
        return getElement(By.className("chzn-results")).getText();
    }

    @Test
    public void filtering_by_type_and_state_works() {
        String collaborativeCitizenInitiativeName = "Collaborative Citizen";
        String singleSent = "Single sent to municipality";

        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withName(collaborativeCitizenInitiativeName));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.SINGLE)
                .withSent(new DateTime())
                .withState(InitiativeState.PUBLISHED)
                .withName(singleSent));

        open(urls.search() + new SearchParameterQueryString(new InitiativeSearch()).getWithTypeCitizen());
        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeName);
        assertTextNotContainedByClass("search-results", singleSent);

        open(urls.search() + new SearchParameterQueryString(new InitiativeSearch()).getWithStateSent());
        assertTextContainedByClass("search-results", singleSent);
        assertTextNotContainedByClass("search-results", collaborativeCitizenInitiativeName);

    }
}
