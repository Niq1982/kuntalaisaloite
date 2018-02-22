package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
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
    public void api_initiatives() throws Exception {

        String collaborativeCitizenInitiativeNameHelsinki = "Aloite helsingista";
        Long id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(new DateTime("2015-07-14"))
                .withName(collaborativeCitizenInitiativeNameHelsinki));
        open(urls.initiatives());

        assertThat(getElement(By.tagName("pre")).getText(), is("[{\"collaborative\":true,\"id\":" +
                "\"http://localhost:8090/api/api/v1/initiatives/" + id + "\"," +
                "\"municipality\":{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/" + HELSINKI_ID + "\"," +
                "\"nameFi\":\"Helsinki\",\"nameSv\":\"Helsinki sv\"}," +
                "\"name\":\"Aloite helsingista\",\"participantCount\":0," +
                "\"publishDate\":\"2015-07-14\",\"sentTime\":null,\"type\":\"COLLABORATIVE\"," +
                "\"url\":{\"fi\":\"http://localhost:8090/fi/aloite/"+id+"\",\"sv\":\"http://localhost:8090/sv/initiativ/"+id+"\"}"+
                "}]"));

    }
    @Test
    public void api_initiatives_filter_by_municipality() throws Exception {

        String collaborativeCitizenInitiativeNameHelsinki = "Aloite helsingista";
        Long id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(new DateTime("2015-07-14"))
                .withName(collaborativeCitizenInitiativeNameHelsinki));

        String collaborativeCitizenInitiativeNameHyvinkaa = "Aloite hyvinkäältä";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HYVINKAA_ID)
                .withType(InitiativeType.COLLABORATIVE)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(new DateTime("2015-07-14"))
                .withName(collaborativeCitizenInitiativeNameHyvinkaa));

        open(urls.initiatives() + "?municipality=" + HELSINKI_ID);

        assertThat(getElement(By.tagName("pre")).getText(), is("[{\"collaborative\":true,\"id\":" +
                "\"http://localhost:8090/api/api/v1/initiatives/" + id + "\"," +
                "\"municipality\":{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/" + HELSINKI_ID + "\"," +
                "\"nameFi\":\"Helsinki\",\"nameSv\":\"Helsinki sv\"}," +
                "\"name\":\"Aloite helsingista\",\"participantCount\":0," +
                "\"publishDate\":\"2015-07-14\",\"sentTime\":null,\"type\":\"COLLABORATIVE\"," +
                "\"url\":{\"fi\":\"http://localhost:8090/fi/aloite/"+id+"\",\"sv\":\"http://localhost:8090/sv/initiativ/"+id+"\"}"+
                "}]"));

    }

    @Test
    public void api_municipalities() {
        // Prerequisites. Assumes that there are tree municipalities: Helsinki, Vantaa, Hyvinkää.
        assertThat(testHelper.getAllMunicipalities().size(), is(3));
        open(urls.municipalities());
        assertThat(getElement(By.tagName("pre")).getText(), is("" +
                "[{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/"+ HELSINKI_ID +"\"," +
                "\"nameFi\":\"Helsinki\"," +
                "\"nameSv\":\"Helsinki sv\"}," +
                "{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/"+ HYVINKAA_ID +"\"," +
                "\"nameFi\":\"Hyvinkää\"," +
                "\"nameSv\":\"Hyvinkää sv\"}," +
                "{\"active\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/municipalities/"+ VANTAA_ID +"\"," +
                "\"nameFi\":\"Vantaa\"," +
                "\"nameSv\":\"Vantaa sv\"}]"));
       }


    @Test
    public void api_single_initiative(){
        String collaborativeCitizenInitiativeNameHelsinki = "Aloite helsingista";
        Long id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withType(InitiativeType.COLLABORATIVE)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(new DateTime("2015-07-14"))
                .withName(collaborativeCitizenInitiativeNameHelsinki));
       open(urls.initiatives() + "/" + id);

        assertThat(getElement(By.tagName("pre")).getText(), is("{\"authors\":{\"privateNames\":0,\"publicAuthors\":[]," +
                "\"publicNames\":0}," +
                "\"collaborative\":true," +
                "\"id\":\"http://localhost:8090/api/api/v1/initiatives/"+id +"\"," +
                "\"municipality\":{\"active\":true,\"id\":\"http://localhost:8090/api/api/v1/municipalities/"+ HELSINKI_ID+"\"," +
                "\"nameFi\":\"Helsinki\",\"nameSv\":\"Helsinki sv\"}," +
                "\"name\":\"Aloite helsingista\"," +
                "\"participantCount\":" +
                "{\"externalNames\":0," +
                "\"privateNames\":0," +
                "\"publicNames\":0,\"total\":0}," +
                "\"proposal\":\"Proposal\"," +
                "\"publishDate\":\"2015-07-14\",\"sentTime\":null,\"type\":\"COLLABORATIVE\"," +
                "\"url\":{\"fi\":\"http://localhost:8090/fi/aloite/"+id+"\",\"sv\":\"http://localhost:8090/sv/initiativ/"+id+"\"}"+
                "}"));
    }
}
