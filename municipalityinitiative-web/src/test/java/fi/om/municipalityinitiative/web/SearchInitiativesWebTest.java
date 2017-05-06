package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;

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

        selectNthMunicipality(1);

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

        selectNthMunicipality(1);

        assertTextContainedByClass("search-results", collaborativeCitizenInitiativeNameHelsinki);
        assertTextNotContainedByClass("search-results", collaborativeCitizenInitiativeNameVantaa);

        clickInput();

        municipalitiesRawText = getChosenResultsRawText();
        assertThat(municipalitiesRawText, containsString(VANTAA));
        assertThat(municipalitiesRawText, containsString(HYVINKAA));

        selectNthMunicipality(3);

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

    @Test
    public void show_info_for_single_municipality() {
        long municipalityId = testHelper.createTestMunicipality("Akaa", true, "Akaa on hieno paikka", "Akaa är en bra plats");

        overrideDriverToFirefox(true);

        open(urls.search());

        assertThat(this.elementExists(By.className("municipality-email")), is(false));

        clickInput();

        selectNthMunicipality(1);

        WebElement municipalityImgElement =  this.getElement(By.className("municipality-img"));
        assertThat(municipalityImgElement.getAttribute("src"), endsWith(municipalityId +".gif"));

        String email = "Akaa@example.com";
        String description = "Akaa on hieno paikka";
        String descriptionSv = "Akaa är en bra plats";

        assertThat(this.elementExists(By.className("municipality-email")), is(true));
        assertTextContainedByClass("municipality-email", email);
        assertTextContainedByClass("municipality-description", description);

        clickElementByClass("language-selection");
        assertTextContainedByClass("municipality-description", descriptionSv);

        clickInput();
        selectNthMunicipality(2);
        assertThat(this.elementExists(By.className("municipality-email")), is(false));

    }

    private void selectNthMunicipality(int i) {
        clickById("municipalities_chzn_o_" + i);
    }
}
