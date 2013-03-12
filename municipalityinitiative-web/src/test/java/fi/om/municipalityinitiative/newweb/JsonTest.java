package fi.om.municipalityinitiative.newweb;

import com.fasterxml.jackson.databind.MapperFeature;
import com.google.common.base.Joiner;
import fi.om.municipalityinitiative.json.JsonJokuParseri;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * This class is used to verify that generated json data will never change without noticing.
 * If json changes, this test must be updated.
 */
public class JsonTest {

    private MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    private ApiController apiController = new ApiController(false, "");
    private ExtendedModelMap model;

    @Before
    public void setup() throws IOException {
        apiController.jsonConverter = jsonConverter;
        jsonConverter.getObjectMapper().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        model = new ExtendedModelMap();
        apiController.api(model, Locales.LOCALE_FI, mock(HttpServletRequest.class));

    }

    @Test
    public void initiative_details_have_not_changed() throws IOException {
        List<JsonJokuParseri.IndentedString> initiatives = getJsonDataListFromModel("initiative");
        String join = joinAsString(initiatives);
        System.out.println(join);
        assertThat(join, is("{\n" +
                "\"authorName\":\"Teemu Teekkari\",\n" +
                "\"collectable\":true,\n" +
                "\"createTime\":\"2010-01-01\",\n" +
                "\"id\":1,\n" +
                "\"municipality\":{\n" +
                "\"id\":1,\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "},\n" +
                "\"name\":\"Koirat pois lähiöistä\",\n" +
                "\"participantCount\":{\n" +
                "\"franchise\":{\n" +
                "\"privateNames\":10,\n" +
                "\"publicNames\":1,\n" +
                "\"total\":11\n" +
                "},\n" +
                "\"noFranchise\":{\n" +
                "\"privateNames\":12,\n" +
                "\"publicNames\":0,\n" +
                "\"total\":12\n" +
                "},\n" +
                "\"total\":23\n" +
                "},\n" +
                "\"proposal\":\"Kakkaa on joka paikassa\",\n" +
                "\"publicParticipants\":[\n" +
                "{\n" +
                "\"franchise\":true,\n" +
                "\"homeMunicipality\":{\n" +
                "\"id\":1,\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "},\n" +
                "\"name\":\"Teemu Teekkari\",\n" +
                "\"participateDate\":\"2010-01-01\"\n" +
                "}]\n" +
                ",\n" +
                "\"sentTime\":null\n" +
                "}"));
    }

    @Test
    public void initiative_list_has_not_changed() throws IOException {
        List<JsonJokuParseri.IndentedString> initiatives = getJsonDataListFromModel("initiativeList");
        String join = joinAsString(initiatives);
        assertThat(join, is("[\n" +
                "{\n" +
                "\"collectable\":true,\n" +
                "\"createTime\":\"2012-12-01\",\n" +
                "\"id\":1,\n" +
                "\"municipality\":{\n" +
                "\"id\":1,\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "},\n" +
                "\"name\":\"Koirat pois lähiöistä\",\n" +
                "\"participantCount\":2,\n" +
                "\"sentTime\":\"2012-12-24\"\n" +
                "}]"));
    }

    @Test
    public void municipalities_have_not_changed() throws IOException {
        List<JsonJokuParseri.IndentedString> municipalities = getJsonDataListFromModel("municipalities");
        String join = joinAsString(municipalities);

        assertThat(join, is("[\n" +
                "{\n" +
                "\"id\":1,\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "}]"));
    }

    private List<JsonJokuParseri.IndentedString> getJsonDataListFromModel(String modelAttributeName) {
        return (List<JsonJokuParseri.IndentedString>) model.get(modelAttributeName);
    }

    private String joinAsString(List<JsonJokuParseri.IndentedString> initiatives) {
        return Joiner.on("\n").join(initiatives);
    }
}
