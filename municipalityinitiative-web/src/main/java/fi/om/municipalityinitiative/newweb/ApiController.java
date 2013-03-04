package fi.om.municipalityinitiative.newweb;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.json.JsonJokuParseri;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.json.InitiativeJson;
import fi.om.municipalityinitiative.newdto.json.InitiativeListJson;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.service.JsonDataService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.JsonpObject;
import fi.om.municipalityinitiative.web.Views;
import org.joda.time.LocalDate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.WebConstants.JSON;
import static fi.om.municipalityinitiative.web.WebConstants.JSONP;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ApiController extends BaseController {

    @Resource
    JsonDataService jsonDataService;

    @Resource
    MappingJackson2HttpMessageConverter jsonConverter;

    public ApiController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(API)
    public String api(Model model, Locale locale, HttpServletRequest request) throws IOException {

        InitiativeJson initiativeJsonObject = createInitiativeJsonObject();
        String json = jsonConverter.getObjectMapper().writeValueAsString(initiativeJsonObject);
        model.addAttribute("publicInitiative", JsonJokuParseri.toParts(json));

        List<InitiativeListJson> initiativeListJson = createInitiativeListJsonObject();
        json = jsonConverter.getObjectMapper().writeValueAsString(initiativeListJson);
        model.addAttribute("initiativeList", JsonJokuParseri.toParts(json));
        return Views.API_VIEW;
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSON)
    public @ResponseBody
    List<InitiativeListJson> initiativeList(@RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                            @RequestParam(value = JSON_LIMIT, required = false) Integer limit,
                                            @RequestParam(value = JSON_MUNICIPALITY, required = false) Long municipality) {

        if (limit == null) {
            limit = DEFAULT_INITIATIVE_JSON_RESULT_COUNT;
        }

        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(Math.min(MAX_INITIATIVE_JSON_RESULT_COUNT, limit));
        search.setOrderBy(InitiativeSearch.OrderBy.id);
        search.setShow(InitiativeSearch.Show.all);
        if (offset != null) {
            search.setOffset(offset);
        }
        search.setMunicipality(municipality);
        return jsonDataService.findJsonInitiatives(search);
    }

    @RequestMapping(value=INITIATIVES, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<List<InitiativeListJson>> initiativeList(@RequestParam(JSONP_CALLBACK) String callback,
                                                                              @RequestParam(value = JSON_OFFSET, required = false) Integer offset,
                                                                              @RequestParam(value = JSON_LIMIT, required = false) Integer limit,
                                                                              @RequestParam(value = JSON_MUNICIPALITY, required = false) Long municipality) {
        return new JsonpObject<List<InitiativeListJson>>(callback, initiativeList(offset, limit, municipality));
    }

    @RequestMapping(value=INITIATIVE, method=GET, produces=JSON)
    public @ResponseBody InitiativeJson initiativeGet(@PathVariable Long id) {
        return jsonDataService.getInitiative(id);
    }
    @RequestMapping(value=INITIATIVE, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody JsonpObject<InitiativeJson> initiativeGet(
            @PathVariable Long id,
            @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<InitiativeJson>(callback, initiativeGet(id));
    }

    @RequestMapping(value=MUNICIPALITIES, method=GET, produces=JSON)
    public @ResponseBody
    List<MunicipalityInfo> municipalityList() {
        return jsonDataService.getMunicipalities();
    }
    @RequestMapping(value=MUNICIPALITIES, method=GET, produces=JSONP, params=JSONP_CALLBACK)
    public @ResponseBody
    JsonpObject<List<MunicipalityInfo>> municipalityGet(
            @RequestParam(JSONP_CALLBACK) String callback) {
        return new JsonpObject<>(callback, municipalityList());
    }

    private List<InitiativeListJson> createInitiativeListJsonObject() {
        InitiativeListInfo initiative = new InitiativeListInfo();
        initiative.setMunicipality(new Municipality(1, "Tampere", "Tammerfors"));
        initiative.setSentTime(Maybe.of(new LocalDate(2012, 12, 24)));
        initiative.setCollectable(true);
        initiative.setCreateTime(new LocalDate(2012, 12, 1));
        initiative.setId(1L);
        initiative.setName("Koirat pois lähiöistä");
        initiative.setParticipantCount(2);
        return Collections.singletonList(new InitiativeListJson(initiative));
    }

    private InitiativeJson createInitiativeJsonObject() {
        final Municipality TAMPERE = new Municipality(1, "Tampere", "Tammerfors");

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.getFranchise().setPrivateNames(10);
        participantCount.getFranchise().setPublicNames(1);
        participantCount.getNoFranchise().setPrivateNames(12);
        participantCount.getFranchise().setPublicNames(1);

        ArrayList<Participant> publicParticipants = Lists.<Participant>newArrayList();
        publicParticipants.add(new Participant(new LocalDate(2010, 1, 1), "Teemu Teekkari", true, TAMPERE));

        Initiative initiativeInfo = new Initiative();
        initiativeInfo.setId(1L);
        initiativeInfo.setName("Koirat pois lähiöistä");
        initiativeInfo.setProposal("Kakkaa on joka paikassa");
        initiativeInfo.setMunicipality(TAMPERE);
        initiativeInfo.setSentTime(Maybe.<LocalDate>fromNullable(null));
        initiativeInfo.setCreateTime(new LocalDate(2010, 1, 1));
        initiativeInfo.setAuthorName("Teemu Teekkari");
        initiativeInfo.setShowName(true);
        initiativeInfo.setManagementHash(Maybe.of("any"));

        InitiativeJson initiativeJson = InitiativeJson.from(initiativeInfo, publicParticipants, participantCount);

        return initiativeJson;

    }
}
