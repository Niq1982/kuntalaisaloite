package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.service.InitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private InitiativeService initiativeService;

    @Resource
    private ValidationService validationService;

    @Resource
    private ParticipantService participantService;

    public MunicipalityInitiativeViewController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().search());
        
        List<MunicipalityInfo> municipalities = municipalityService.findAllMunicipalities();

        model.addAttribute("initiatives", initiativeService.findMunicipalityInitiatives(search));
        model.addAttribute("municipalities", municipalities);
        model.addAttribute("currentSearch", search);

        model.addAttribute("currentMunicipality", solveMunicipalityFromListById(municipalities, search.getMunicipality()));
        return SEARCH_VIEW;
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().view(initiativeId));
        
        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isCollectable()){// TODO: If not sent to municipality

            addModelAttributesToCollectView(model,
                    initiativeInfo,
                    municipalityService.findAllMunicipalities(),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));
            model.addAttribute("participant", new ParticipantUICreateDto());

            return COLLECT_VIEW;
        }
        else {
            model.addAttribute("initiative", initiativeInfo);
            return SINGLE_VIEW;
        }
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=POST)
    public String participate(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("participant") ParticipantUICreateDto participant,
                              BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {

        if (validationService.validationSuccessful(participant, bindingResult, model)) {
            initiativeService.createParticipant(participant, initiativeId);
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.PARTICIPATE, request);
        }
        else {
            addModelAttributesToCollectView(model,
                    initiativeService.getMunicipalityInitiative(initiativeId),
                    municipalityService.findAllMunicipalities(),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            return COLLECT_VIEW;
        }
    }
    
    @RequestMapping(value={ PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV }, method=GET)
    public String participantList(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().view(initiativeId));
        
        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isCollectable()){
            model.addAttribute("initiative",  initiativeInfo);
            model.addAttribute("participantCount", participantService.getParticipantCount(initiativeId));
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));

            return PARTICIPANT_LIST;
        }
        else {
            model.addAttribute("initiative", initiativeInfo);
            return SINGLE_VIEW;
        }
    }

    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {
        
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().management(initiativeId, managementHash));

        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId);

        if (!initiativeInfo.isCollectable() || initiativeInfo.isSent()) { // Practically initiative should always be sent if it's not collectable...
            return contextRelativeRedirect(urls.view(initiativeId));
        }

        addModelAttributesToCollectView(model,
                initiativeService.getMunicipalityInitiative(initiativeId),
                municipalityService.findAllMunicipalities(),
                participantService.getParticipantCount(initiativeId),
                participantService.findPublicParticipants(initiativeId));

        if (managementHash.equals(initiativeInfo.getManagementHash().get())){
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            model.addAttribute("sendToMunicipality", initiativeService.getSendToMunicipalityData(initiativeId));
            return MANAGEMENT_VIEW;
        }
        else {
            // Redirecting would be nicer but let's make it more difficult for the possible hackers to brute force the management-hash
            model.addAttribute("participant", new ParticipantUICreateDto());
            return COLLECT_VIEW;
        }
    }
    
    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=POST)
    public String managementView(@PathVariable("id") Long initiativeId, 
                                @ModelAttribute ("sendToMunicipality") SendToMunicipalityDto sendToMunicipalityDto,
                                BindingResult bindingResult,Model model, Locale locale, HttpServletRequest request) {
       
        if (validationService.validationSuccessful(sendToMunicipalityDto, bindingResult, model)) {
            initiativeService.sendToMunicipality(initiativeId, sendToMunicipalityDto, "0000000000111111111122222222223333333333", locale);
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId),RequestMessage.SEND, request);
        }
        else {
            addModelAttributesToCollectView(model,
                    initiativeService.getMunicipalityInitiative(initiativeId),
                    municipalityService.findAllMunicipalities(),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));

            model.addAttribute("sendToMunicipality", sendToMunicipalityDto);
            return MANAGEMENT_VIEW;
        }
        
    }

    private void addModelAttributesToCollectView(Model model, InitiativeViewInfo municipalityInitiative, List<MunicipalityInfo> allMunicipalities, ParticipantCount participantCount, ParticipantNames participants) {
        model.addAttribute("initiative", municipalityInitiative);
        model.addAttribute("municipalities", allMunicipalities);
        model.addAttribute("participantCount", participantCount);
        model.addAttribute("participants", participants);
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
