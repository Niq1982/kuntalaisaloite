package fi.om.municipalityinitiative.newweb;

import com.google.common.base.Joiner;
import fi.om.municipalityinitiative.json.JsonJokuParseri;
import fi.om.municipalityinitiative.util.IntendedStringComparator;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Collections;
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
        model = new ExtendedModelMap();
        apiController.api(model, Locales.LOCALE_FI, mock(HttpServletRequest.class));

    }

    @Test
    public void initiative_details_have_not_changed() throws IOException {
        List<JsonJokuParseri.IndentedString> initiatives = (List<JsonJokuParseri.IndentedString>) model.get("initiative");
        String join = sortAndJoinAsString(initiatives);
        System.out.println(join);
        assertThat(join, is(initiativeDetailData()));
    }

    private String initiativeDetailData() {
        return "\"authorName\":\"Teemu Teekkari\",\n" +
                "\"collectable\":true,\n" +
                "\"createTime\":\"2010-01-01\",\n" +
                "\"franchise\":true,\n" +
                "\"franchise\":{\n" +
                "\"homeMunicipality\":{\n" +
                "\"id\":1,\n" +
                "\"id\":1,\n" +
                "\"id\":1,\n" +
                "\"municipality\":{\n" +
                "\"name\":\"Koirat pois lähiöistä\",\n" +
                "\"name\":\"Teemu Teekkari\",\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "\"noFranchise\":{\n" +
                "\"participantCount\":{\n" +
                "\"participateDate\":\"2010-01-01\",\n" +
                "\"privateNames\":10,\n" +
                "\"privateNames\":12,\n" +
                "\"proposal\":\"Kakkaa on joka paikassa\",\n" +
                "\"publicNames\":0,\n" +
                "\"publicNames\":1,\n" +
                "\"publicParticipants\":[\n" +
                "\"sentTime\":null\n" +
                "\"total\":11\n" +
                "\"total\":12\n" +
                "\"total\":23\n" +
                ",\n" +
                "{\n" +
                "{\n" +
                "}\n" +
                "}\n" +
                "},\n" +
                "},\n" +
                "},\n" +
                "},\n" +
                "}]";
    }

    @Test
    public void municipalities_have_not_changed() throws IOException {
        List<JsonJokuParseri.IndentedString> municipalities = (List<JsonJokuParseri.IndentedString>) model.get("municipalities");
        String join = sortAndJoinAsString(municipalities);

        assertThat(join, is(municipalityListData()));
    }

    private String municipalityListData() {
        return "\"id\":1,\n" +
                "\"nameFi\":\"Tampere\",\n" +
                "\"nameSv\":\"Tammerfors\"\n" +
                "[\n" +
                "{\n" +
                "}]";
    }

    private String sortAndJoinAsString(List<JsonJokuParseri.IndentedString> initiatives) {
        Collections.sort(initiatives, new IntendedStringComparator());
        return Joiner.on("\n").join(initiatives);
    }
}
