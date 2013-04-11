package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.MODERATION_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeModerationController extends BaseController{

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private OmInitiativeService omInitiativeService;

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private UserService userService;

    public InitiativeModerationController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MODERATION_FI, MODERATION_SV }, method=GET)
    public String moderationView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        userService.getRequiredOmLoginUserHolder(request);

        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().moderation(initiativeId));

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        addModelAttributesToCollectView(model,
                initiativeInfo,
                municipalityService.findAllMunicipalities(locale),
                participantService.getParticipantCount(initiativeId),
                participantService.findPublicParticipants(initiativeId));

        model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
        model.addAttribute("managementSettings", publicInitiativeService.managementSettings(initiativeId));
        // TODO: Return all authors when possible
        model.addAttribute("author", omInitiativeService.getAuthorInformation(initiativeId));
        return MODERATION_VIEW;
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_ACCEPT_INITIATIVE)
    public String acceptInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam("comment") String comment,
                                   Locale locale, HttpServletRequest request) {

        // TODO: Saate / Comment
        omInitiativeService.accept(initiativeId, locale);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.ACCEPT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_REJECT_INITIATIVE)
    public String rejectInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam("comment") String comment,
                                   Locale locale, HttpServletRequest request) {

        // TODO: Saate / Comment
        omInitiativeService.reject(initiativeId, locale);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.REJECT_INITIATIVE, request);
    }
}
