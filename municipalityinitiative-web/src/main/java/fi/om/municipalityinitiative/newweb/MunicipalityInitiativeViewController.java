package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeSearch;
import fi.om.municipalityinitiative.newdto.ParticipantUICreateDto;
import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MunicipalityInitiativeViewController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;
    
    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;

    public MunicipalityInitiativeViewController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(MunicipalityInitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        List<MunicipalityInfo> municipalities = municipalityService.findAllMunicipalities();

        model.addAttribute("initiatives", municipalityInitiativeService.findMunicipalityInitiatives(search));
        model.addAttribute("municipalities", municipalities);
        model.addAttribute("currentSearch", search);

        model.addAttribute("currentMunicipality", solveMunicipalityFromListById(municipalities, search.getMunicipality()));
        return SEARCH_VIEW;
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        InitiativeViewInfo initiativeInfo = municipalityInitiativeService.getMunicipalityInitiative(initiativeId);

        model.addAttribute("initiative", initiativeInfo);

        if (initiativeInfo.isCollectable()){
            model.addAttribute("participant", new ParticipantUICreateDto()); // TODO: If not sent to municipality
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            return COLLECT_VIEW;
        } else {
            return SINGLE_VIEW;
        }
    }

    private static String solveMunicipalityFromListById(List<MunicipalityInfo> municipalities, Long municipalityId){
        if (municipalityId == null)
            return null;
        for (MunicipalityInfo municipality : municipalities) {
            if (municipality.getId().equals(municipalityId))
                return municipality.getName();
        }
        return null;
    }
}
