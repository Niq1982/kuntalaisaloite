package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.json.InitiativeJson;
import fi.om.municipalityinitiative.newdto.json.InitiativeListJson;
import fi.om.municipalityinitiative.service.JsonDataService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.JsonpObject;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.WebConstants.JSON;
import static fi.om.municipalityinitiative.web.WebConstants.JSONP;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ApiController extends BaseController {

    @Resource
    JsonDataService jsonDataService;

    public ApiController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(API)
    public String api() {
        return Views.API_VIEW;
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSON)
    public @ResponseBody
    List<InitiativeListJson> jsonList(@RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                      @RequestParam(value = JSON_LIMIT, required = false) Integer limit) {

        if (limit == null) {
            limit = DEFAULT_INITIATIVE_JSON_RESULT_COUNT;
        }

        // Do not expose own initiatives through this method
        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(Math.min(MAX_INITIATIVE_JSON_RESULT_COUNT, limit));
        search.setOrderBy(InitiativeSearch.OrderBy.id);
        search.setShow(InitiativeSearch.Show.all);
        if (offset != null) {
            search.setOffset(offset);
        }
        return jsonDataService.findJsonInitiatives(search);
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<List<InitiativeListJson>> jsonpList(@RequestParam(JSONP_CALLBACK) String callback,
                                                    @RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                                    @RequestParam(value = JSON_LIMIT, required = false) Integer limit) {
        return new JsonpObject<List<InitiativeListJson>>(callback, jsonList(offset, limit));
    }

    @RequestMapping(value=INITIATIVE, method=GET, produces=JSON)
    public @ResponseBody InitiativeJson jsonGet(@PathVariable Long id) {
        return jsonDataService.getInitiative(id);
    }
    @RequestMapping(value=INITIATIVE, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<InitiativeJson> jsonGet(
            @PathVariable Long id,
            @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<InitiativeJson>(callback, jsonGet(id));
    }
}
