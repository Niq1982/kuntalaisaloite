package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.service.PublicInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
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
public class InitiativeViewController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private ValidationService validationService;

    @Resource
    private ParticipantService participantService;

    public InitiativeViewController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        List<Municipality> municipalities = municipalityService.findAllMunicipalities(locale);
        return ViewGenerator.searchView(publicInitiativeService.findMunicipalityInitiatives(search),
                municipalities,
                search,
                new SearchParameterQueryString(search),
                solveMunicipalityFromListById(municipalities, search.getMunicipality()),
                publicInitiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality())))
                .view(model, Urls.get(locale).alt().search());
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId,
                       Model model, Locale locale, HttpServletRequest request) {

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.getState() != InitiativeState.PUBLISHED) {
            LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
            if (loginUserHolder.getUser().isNotOmUser()) {
                loginUserHolder.assertManagementRightsForInitiative(initiativeId);
            }
        }

        if (initiativeInfo.isCollectable()) {
            return ViewGenerator.collaborativeView(initiativeInfo,
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId),
                    new ParticipantUICreateDto())
                    .view(model, Urls.get(locale).alt().view(initiativeId));
        }
        else {
            return ViewGenerator.singleView(initiativeInfo).view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=POST)
    public String participate(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("participant") ParticipantUICreateDto participant,
                              BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {

        if (validationService.validationSuccessful(participant, bindingResult, model)) {
            publicInitiativeService.createParticipant(participant, initiativeId, locale);
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.PARTICIPATE, request);
        }
        else {
            addModelAttributesToCollectView(model,
                    publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));
            return PUBLIC_COLLECT_VIEW;
        }
    }

    @RequestMapping(value={ PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV }, method=GET)
    public String participantList(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().view(initiativeId));

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isCollectable()){
            model.addAttribute("initiative",  initiativeInfo);
            model.addAttribute("participantCount", participantService.getParticipantCount(initiativeId));
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));

            String managementURI = urls.management(initiativeId);

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

    @RequestMapping(value = {PARTICIPATING_CONFIRMATION_FI, PARTICIPATING_CONFIRMATION_SV}, method = GET)
    public String confirmParticipationg(@PathVariable("id") Long participantId,
                                        @RequestParam(PARAM_PARTICIPANT_CONFIRMATION_CODE) String confirmationCode,
                                        Locale locale,
                                        HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        Long initiativeId = publicInitiativeService.confirmParticipation(participantId, confirmationCode);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.CONFIRM_PARTICIPATION, request);
    }

    @RequestMapping(value={ PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV }, method=GET)
    public String pendingConfirmation(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        model.addAttribute("initiative", initiativeInfo);

        if (initiativeInfo.getState().equals(InitiativeState.DRAFT) && getRequestAttribute(request) != null) {
            return PENDING_CONFIRMATION;
        } else {
            return redirectWithMessage(urls.prepare(), RequestMessage.PREPARE_CONFIRM_EXPIRED, request);
        }
    }

    @RequestMapping(value={IFRAME_FI, IFRAME_SV}, method=GET)
    public String iframe(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().search());

        List<Municipality> municipalities = municipalityService.findAllMunicipalities(locale);

        search.setShow(InitiativeSearch.Show.all);

        model.addAttribute("initiatives", publicInitiativeService.findMunicipalityInitiatives(search));
        model.addAttribute("municipalities", municipalities);
        model.addAttribute("currentSearch", search);
        model.addAttribute("queryString", new SearchParameterQueryString(search));
        model.addAttribute("currentMunicipality", solveMunicipalityFromListById(municipalities, search.getMunicipality()));
        model.addAttribute("initiativeCounts", publicInitiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality())));
        return IFRAME_VIEW;
    }

    private static Maybe<Municipality> solveMunicipalityFromListById(List<Municipality> municipalities, Long municipalityId){
        for (Municipality municipality : municipalities) {
            if (municipality.getId().equals(municipalityId))
                return Maybe.of(municipality);
        }
        return Maybe.absent();
    }
}
