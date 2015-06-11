package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Optional;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.json.InitiativeJson;
import fi.om.municipalityinitiative.dto.json.InitiativeListJson;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.json.JsonStringParser;
import fi.om.municipalityinitiative.service.JsonDataService;
import fi.om.municipalityinitiative.web.JsonpObject;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.WebConstants.JSON;
import static fi.om.municipalityinitiative.web.WebConstants.JSONP;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ApiController extends BaseController {

    @Resource
    private JsonDataService jsonDataService;

    @Resource
    MappingJackson2HttpMessageConverter jsonConverter;

    public static final Municipality TAMPERE = new Municipality(1, "Tampere", "Tammerfors", false);

    public ApiController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(API)
    public String api(Model model) throws IOException {

        InitiativeJson initiativeJsonObject = jsonDataService.createInitiativeJsonObject();
        String json = jsonConverter.getObjectMapper().writeValueAsString(initiativeJsonObject);
        model.addAttribute("initiativeDetails", JsonStringParser.toParts(json));

        List<InitiativeListJson> initiativeListJson = jsonDataService.createInitiativeListJsonObject();
        json = jsonConverter.getObjectMapper().writeValueAsString(initiativeListJson);
        model.addAttribute("initiativeList", JsonStringParser.toParts(json));

        json = jsonConverter.getObjectMapper().writeValueAsString(Collections.singletonList(TAMPERE));
        model.addAttribute("municipalities", JsonStringParser.toParts(json));


        return Views.API_VIEW;
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSON)
    public @ResponseBody
    List<InitiativeListJson> initiativeList(@RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                            @RequestParam(value = JSON_LIMIT, required = false) Integer givenLimit,
                                            @RequestParam(value = JSON_MUNICIPALITY, required = false) Long municipality) {

        int limit = Optional.fromNullable(givenLimit).or(DEFAULT_INITIATIVE_JSON_RESULT_COUNT);

        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(Math.min(MAX_INITIATIVE_JSON_RESULT_COUNT, limit));
        search.setOrderBy(InitiativeSearch.OrderBy.id);
        search.setShow(InitiativeSearch.Show.all);
        if (offset != null) {
            search.setOffset(offset);
        }
        search.setMunicipalities(municipality);
        return jsonDataService.findJsonInitiatives(search);
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<List<InitiativeListJson>> initiativeListJsonp(@RequestParam(JSONP_CALLBACK) String callback,
                                                                                   @RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                                                                   @RequestParam(value = JSON_LIMIT, required = false) Integer limit,
                                                                                   @RequestParam(value = JSON_MUNICIPALITY, required = false) Long municipality) {
        return new JsonpObject<>(callback, initiativeList(offset, limit, municipality));
    }

    @RequestMapping(value=INITIATIVE, method=GET, produces=JSON)
    public @ResponseBody InitiativeJson initiativeGet(@PathVariable Long id) {
        return jsonDataService.getInitiative(id);
    }
    @RequestMapping(value=INITIATIVE, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<InitiativeJson> initiativeGetJsonp(
            @PathVariable Long id,
            @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<InitiativeJson>(callback, initiativeGet(id));
    }

    @RequestMapping(value=MUNICIPALITIES, method=GET, produces=JSON)
    public @ResponseBody
    List<Municipality> municipalityList() {
        return jsonDataService.getMunicipalities();
    }
    @RequestMapping(value=MUNICIPALITIES, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody
    JsonpObject<List<Municipality>> municipalityListJsonp(
            @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<>(callback, municipalityList());
    }

    @RequestMapping(value=MUNICIPALITY, method=GET, produces=JSON)
    public @ResponseBody
    Municipality municipalityGet(@PathVariable Long id) {
        return jsonDataService.getMunicipality(id);
    }

    @RequestMapping(value=MUNICIPALITY, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody
    JsonpObject<Municipality> municipalityGetJsonp(@PathVariable Long id,
                                                   @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<Municipality>(callback, municipalityGet(id));
    }

}
