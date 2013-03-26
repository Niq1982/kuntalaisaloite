package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.service.InitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SearchParameterGenerator;
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
        
        List<MunicipalityInfo> municipalities = municipalityService.findAllMunicipalities(locale);

        model.addAttribute("initiatives", initiativeService.findMunicipalityInitiatives(search));
        model.addAttribute("municipalities", municipalities);
        model.addAttribute("locale", locale);
        model.addAttribute("currentSearch", search);
        model.addAttribute("searchParameters", new SearchParameterGenerator(search));
        model.addAttribute("currentMunicipality", solveMunicipalityFromListById(municipalities, search.getMunicipality()));
        model.addAttribute("initiativeCounts", initiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality())));
        return SEARCH_VIEW;
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().view(initiativeId));
        
        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isCollectable()){// TODO: If not sent to municipality

            addModelAttributesToCollectView(model,
                    initiativeInfo,
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));
            model.addAttribute("participant", new ParticipantUICreateDto());

            return PUBLIC_COLLECT_VIEW;
        }
        else {
            model.addAttribute("initiative", initiativeInfo);
            return PUBLIC_SINGLE_VIEW;
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
                    initiativeService.getMunicipalityInitiative(initiativeId, locale),
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            return PUBLIC_COLLECT_VIEW;
        }
    }
    
    @RequestMapping(value={ PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV }, method=GET)
    public String participantList(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().view(initiativeId));
        
        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isCollectable()){
            model.addAttribute("initiative",  initiativeInfo);
            model.addAttribute("participantCount", participantService.getParticipantCount(initiativeId));
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            
            String managementURI = urls.management(initiativeId, initiativeInfo.getManagementHash().get());
            
            if (request.getHeader("referer") != null && request.getHeader("referer").equals(managementURI)) {
                model.addAttribute("previousPageURI", managementURI);
            } else {
                model.addAttribute("previousPageURI", urls.view(initiativeId));
            }

            model.addAttribute("locale", locale);

            return PARTICIPANT_LIST;
        }
        else {
            model.addAttribute("initiative", initiativeInfo);
            return PUBLIC_SINGLE_VIEW;
        }
    }

    // TODO: Secure view with managementHash, author email-address, this view should exist only in initiative PREPARE-state.
    @RequestMapping(value={ PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV }, method=GET)
    public String pendingConfirmation(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        
        Urls urls = Urls.get(locale);
        
        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        model.addAttribute("initiative", initiativeInfo);

        // TODO: redirect to 404 if view is not accessible (wrong managementHash or wrong initiative state)
        return PENDING_CONFIRMATION;

    }
    
    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {
        
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().management(initiativeId, managementHash));

        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(urls.view(initiativeId),RequestMessage.ALREADY_SENT, request);
        } else if (initiativeInfo.getState() != InitiativeState.DRAFT) { // Only draft may be modified. Use moderation-view after sent for review
            return contextRelativeRedirect(urls.view(initiativeId));
        }

        if (managementHash.equals(initiativeInfo.getManagementHash().get())){
            addModelAttributesToCollectView(model,
                    initiativeInfo,
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));

            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            model.addAttribute("author", initiativeService.getAuthorInformation(initiativeId, managementHash));
            return MANAGEMENT_VIEW;
        } else {
            return ERROR_404_VIEW;
        }
    }
    
    // TODO: Finalize. Now just a dummy for template development.
    @RequestMapping(value={ MODERATION_FI, MODERATION_SV }, method=GET)
    public String moderationView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {
        
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().management(initiativeId, managementHash));

        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isSent() && managementHash.equals(initiativeInfo.getManagementHash().get())) {
            return redirectWithMessage(urls.view(initiativeId),RequestMessage.ALREADY_SENT, request);
        } else if (!initiativeInfo.isCollectable() || initiativeInfo.isSent()) { // Practically initiative should always be sent if it's not collectable...
            return contextRelativeRedirect(urls.view(initiativeId));
        }

        addModelAttributesToCollectView(model,
                initiativeInfo,
                municipalityService.findAllMunicipalities(locale),
                participantService.getParticipantCount(initiativeId),
                participantService.findPublicParticipants(initiativeId));

        if (managementHash.equals(initiativeInfo.getManagementHash().get())){
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            model.addAttribute("sendToMunicipality", SendToMunicipalityDto.parse(managementHash, initiativeService.getContactInfo(initiativeId)));
            return MODERATION_VIEW;
        } else {
            return ERROR_404_VIEW;
        }
    }
    
    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=POST)
    public String sendToMunicipality(@PathVariable("id") Long initiativeId,
                                     @ModelAttribute("sendToMunicipality") SendToMunicipalityDto sendToMunicipalityDto,
                                     BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {
       
        if (validationService.validationSuccessful(sendToMunicipalityDto, bindingResult, model)) {
            initiativeService.sendToMunicipality(initiativeId, sendToMunicipalityDto, locale); // TODO: Get hashcode from post request.
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId),RequestMessage.SEND, request);
        }
        else {
            addModelAttributesToCollectView(model,
                    initiativeService.getMunicipalityInitiative(initiativeId, locale),
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));

            model.addAttribute("sendToMunicipality", sendToMunicipalityDto);
            return MANAGEMENT_VIEW;
        }
        
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                               Locale locale) {

        initiativeService.sendReview(initiativeId, managementHash, InitiativeType.SINGLE);
        return contextRelativeRedirect(Urls.get(locale).view(initiativeId));
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                            Locale locale) {

        initiativeService.sendReview(initiativeId, managementHash, InitiativeType.COLLABORATIVE);
        return contextRelativeRedirect(Urls.get(locale).view(initiativeId));
    }

    
    
    @RequestMapping(value={IFRAME_FI, IFRAME_SV}, method=GET)
    public String iframe(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().search());
        
        List<MunicipalityInfo> municipalities = municipalityService.findAllMunicipalities(locale);

        model.addAttribute("initiatives", initiativeService.findMunicipalityInitiatives(search));
        model.addAttribute("municipalities", municipalities);
        model.addAttribute("currentSearch", search);
        model.addAttribute("searchParameters", new SearchParameterGenerator(search));
        model.addAttribute("currentMunicipality", solveMunicipalityFromListById(municipalities, search.getMunicipality()));
        model.addAttribute("initiativeCounts", initiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality())));
        return IFRAME_VIEW;
    }

    private void addModelAttributesToCollectView(Model model, InitiativeViewInfo municipalityInitiative, List<MunicipalityInfo> allMunicipalities, ParticipantCount participantCount, Participants participants) {
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
