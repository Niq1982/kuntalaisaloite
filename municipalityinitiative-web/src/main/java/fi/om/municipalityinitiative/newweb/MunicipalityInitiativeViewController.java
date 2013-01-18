package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeSearch;
import fi.om.municipalityinitiative.newdto.ParticipantUIICreateDto;
import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityInitiativeViewController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;
    
    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;

    @Resource
    private ValidationService validationService;

    @Resource
    private ParticipantService participantService;

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
            model.addAttribute("participant", new ParticipantUIICreateDto()); // TODO: If not sent to municipality
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            model.addAttribute("participantCount", participantService.getParticipantCount(initiativeId));
            model.addAttribute("participants", participantService.findParticipants(initiativeId));
            return COLLECT_VIEW;
        } else {
            return SINGLE_VIEW;
        }
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=POST)
    public String participate(@PathVariable("id") Long initiativeId, ParticipantUIICreateDto participant,
                              BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {

        // TODO Check id collectable
        // TODO: If not sent to municipality

        if (validationService.validationSuccessful(participant, bindingResult, model)) {
            municipalityInitiativeService.createParticipant(participant, initiativeId);
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.PARTICIPATE, request);
        }
        else {
            model.addAttribute("participant", participant);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            model.addAttribute("initiative", municipalityInitiativeService.getMunicipalityInitiative(initiativeId));
            return COLLECT_VIEW;
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
