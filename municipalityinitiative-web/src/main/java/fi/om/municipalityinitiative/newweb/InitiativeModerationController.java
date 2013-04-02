package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.SendToMunicipalityDto;
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
import static fi.om.municipalityinitiative.web.Urls.PARAM_MANAGEMENT_CODE;
import static fi.om.municipalityinitiative.web.Views.ERROR_404_VIEW;
import static fi.om.municipalityinitiative.web.Views.MODERATION_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeModerationController extends BaseController{

    @Resource
    private InitiativeService initiativeService;

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private UserService userService;

    public InitiativeModerationController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    // TODO: Permission only for logged in users with moderation rights
    // TODO: No need for management hash
    @RequestMapping(value={ MODERATION_FI, MODERATION_SV }, method=GET)
    public String moderationView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {

        userService.requireOmUser();

        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().management(initiativeId, managementHash));

        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

        // NOTE: Should moderation view be always accessible?
        if (initiativeInfo.isSent() && managementHash.equals(initiativeInfo.getManagementHash().get())) {
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        addModelAttributesToCollectView(model,
                initiativeInfo,
                municipalityService.findAllMunicipalities(locale),
                participantService.getParticipantCount(initiativeId),
                participantService.findPublicParticipants(initiativeId));

        if (managementHash.equals(initiativeInfo.getManagementHash().get())){
            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            model.addAttribute("author", initiativeService.getAuthorInformation(initiativeId, managementHash));
            // TODO: Remove this when moderation supports commenting. Update also moderation-view.ftl (sendToMunicipality.comment)
            model.addAttribute("sendToMunicipality", SendToMunicipalityDto.parse(managementHash, initiativeService.getContactInfo(initiativeId)));
            return MODERATION_VIEW;
        } else {
            return ERROR_404_VIEW;
        }
    }


    // TODO: Permission only for logged in users with moderation rights
    // TODO: No need for management hash
    @RequestMapping(value = {MODERATION_FI, MODERATION_FI}, method = POST, params = ACTION_ACCEPT_INITIATIVE)
    public String acceptInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                   Locale locale, HttpServletRequest request) {

        userService.requireOmUser();

        // TODO: Saate / Comment

        initiativeService.accept(initiativeId, managementHash);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId, managementHash), RequestMessage.ACCEPT_INITIATIVE, request);
    }

    // TODO: Permission only for logged in users with moderation rights
    // TODO: No need for management hash
    @RequestMapping(value = {MODERATION_FI, MODERATION_FI}, method = POST, params = ACTION_REJECT_INITIATIVE)
    public String rejectInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                   Locale locale, HttpServletRequest request) {

        userService.requireOmUser();

        // TODO: Saate / Comment

        initiativeService.reject(initiativeId, managementHash);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId, managementHash), RequestMessage.REJECT_INITIATIVE, request);
    }
}
