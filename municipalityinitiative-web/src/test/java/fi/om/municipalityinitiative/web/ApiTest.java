package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Json data is tested at @see fi.om.municipalityinitiative.web.controller.JsonTest
 *
 */
public class ApiTest extends WebTestBase {

    @Override
    public void childSetup() {

    }

    @Test
    public void api_page_opens() {
        open(urls.api());
        assertTitle("Open Data API - Kuntalaisaloitepalvelu");
    }

    @Test
    public void any_api_localizations_are_not_missing() throws Exception {
        open(urls.api());
        assertThat(driver.getPageSource(), not(containsString("[api.")));
    }
    @Test
    @Ignore
    public void api_initiatives() throws Exception {

        String collaborativeCitizenInitiativeNameHelsinki = "Aloite helsingista";
        Long id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withModified(new DateTime("2015-07-14"))
                .withName(collaborativeCitizenInitiativeNameHelsinki));
        open(urls.initiatives());


        assertThat(getElement(By.tagName("pre")).getText(), is("[{\"collaborative\":true,\"id\":" +
                "\"http://localhost:8090/api/api/v1/initiatives/" + id + "\"," +
                "\"municipality\":{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/" + HELSINKI_ID + "\"," +
                "\"nameFi\":\"Helsinki\",\"nameSv\":\"Helsinki sv\"}," +
                "\"name\":\"Aloite helsingista\",\"participantCount\":0," +
                "\"publishDate\":\"2015-07-14\",\"sentTime\":null,\"type\":\"COLLABORATIVE_CITIZEN\"}]"));

    }
}
