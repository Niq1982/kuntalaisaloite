package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Joiner;
import fi.om.municipalityinitiative.conf.JsonConverterFactory;
import fi.om.municipalityinitiative.json.JsonStringParser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.ExtendedModelMap;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * This class is used to verify that generated json data will never change without noticing.
 * If json changes, this test must be updated.
 */
public class JsonTest {

    public static final String API_BASE_URL = "http://api.baseUrl";
    public static final String BASE_URL = "http://baseUrl";

    private MappingJackson2HttpMessageConverter jsonConverter = JsonConverterFactory.JacksonHttpConverterWithModules(API_BASE_URL, BASE_URL);
    private ApiController apiController = new ApiController(false, "");
    private ExtendedModelMap model;

    @Before
    public void setup() throws IOException {
        apiController.jsonConverter = jsonConverter;
        model = new ExtendedModelMap();
        apiController.api(model);

    }

    @Test
    public void initiative_details_have_not_changed() throws IOException {
        List<JsonStringParser.IndentedString> initiatives = getJsonDataListFromModel("initiativeDetails");
        String join = joinAsString(initiatives);
        assertThat(join, is("{\n" +
                "\"authors\":{\n" +
                "\"privateNames\":1,\n" +
                "\"publicNames\":1\n" +
                "},\n" +
                "\"collaborative\":true,\n" +
                "\"id\":\"http://api.baseUrl/api/v1/initiatives/1\",\n" +
                "\"municipality\":{\n" +
                "\"active\":false,\n" +
                "\"id\":\"http://api.baseUrl/api/v1/municipalities/1\",\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "},\n" +
                "\"name\":\"Tämä on esimerkkialoitteen otsikko\",\n" +
                "\"participantCount\":{\n" +
                "\"externalNames\":0,\n" +
                "\"privateNames\":10,\n" +
                "\"publicNames\":1,\n" +
                "\"total\":11\n" +
                "},\n" +
                "\"proposal\":\"Tämä on esimerkkialoitteen sisältö\",\n" +
                "\"publishDate\":\"2010-01-01\",\n" +
                "\"sentTime\":null,\n" +
                "\"type\":\"COLLABORATIVE\",\n"+
                "\"url\":{\n\"fi\":\"http://baseUrl/fi/aloite/1\",\n\"sv\":\"http://baseUrl/sv/initiativ/1\"\n}\n"+
                "}"));
    }

    @Test
    public void initiative_list_has_not_changed() throws IOException {
        List<JsonStringParser.IndentedString> initiatives = getJsonDataListFromModel("initiativeList");
        String join = joinAsString(initiatives);

        assertThat(join, is("[\n" +
                "{\n" +
                "\"collaborative\":true,\n" +
                "\"id\":\"http://api.baseUrl/api/v1/initiatives/1\",\n" +
                "\"municipality\":{\n" +
                "\"active\":false,\n" +
                "\"id\":\"http://api.baseUrl/api/v1/municipalities/1\",\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "},\n" +
                "\"name\":\"Tämä on esimerkkialoitteen otsikko\",\n" +
                "\"participantCount\":2,\n" +
                "\"publishDate\":\"2012-12-01\",\n" +
                "\"sentTime\":\"2012-12-24\",\n" +
                "\"type\":\"COLLABORATIVE_CITIZEN\",\n"+
                "\"url\":{\n\"fi\":\"http://baseUrl/fi/aloite/1\",\n\"sv\":\"http://baseUrl/sv/initiativ/1\"\n}\n"+
                "}]"));
    }

    @Test
    public void municipalities_have_not_changed() throws IOException {
        List<JsonStringParser.IndentedString> municipalities = getJsonDataListFromModel("municipalities");
        String join = joinAsString(municipalities);

        assertThat(join, is("[\n" +
                "{\n" +
                "\"active\":false,\n" +
                "\"id\":\"http://api.baseUrl/api/v1/municipalities/1\",\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "}]"));
    }

    private List<JsonStringParser.IndentedString> getJsonDataListFromModel(String modelAttributeName) {
        return (List<JsonStringParser.IndentedString>) model.get(modelAttributeName);
    }

    private String joinAsString(List<JsonStringParser.IndentedString> initiatives) {
        return Joiner.on("\n").join(initiatives);
    }
}
