package fi.om.municipalityinitiative.newweb;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeManagementController extends BaseController {

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private ValidationService validationService;

    public InitiativeManagementController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

//        userService.assertManagementRightsForInitiative(initiativeId);
        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().getManagement(initiativeId));

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (Strings.isNullOrEmpty(initiativeInfo.getName())) {
            return contextRelativeRedirect(urls.edit(initiativeId));
        }

        addModelAttributesToCollectView(model,
                initiativeInfo,
                municipalityService.findAllMunicipalities(locale),
                participantService.getParticipantCount(initiativeId),
                participantService.findPublicParticipants(initiativeId));

        model.addAttribute("managementSettings", publicInitiativeService.managementSettings(initiativeId));
        model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
        model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, userService.getManagementHash()));
        return MANAGEMENT_VIEW;
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=GET)
    public String updateView(@PathVariable("id") Long initiativeId,
                             Model model, Locale locale, HttpServletRequest request) {

        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = publicInitiativeService.managementSettings(initiativeId);

        String managementHash = userService.getManagementHash();

        if (managementSettings.isAllowUpdate()) {

            model.addAttribute(ALT_URI_ATTR, urls.alt().update(initiativeId));
            model.addAttribute("initiative", publicInitiativeService.getMunicipalityInitiative(initiativeId, managementHash, locale));
            model.addAttribute("updateData", publicInitiativeService.getInitiativeForUpdate(initiativeId, managementHash));
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, managementHash));

            model.addAttribute("previousPageURI", urls.getManagement(initiativeId));
            return UPDATE_VIEW;

        } else {
            return ERROR_500; // TODO: Custom error page or some message that operation is not allowed
        }

    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=POST)
    public String updatePost(@PathVariable("id") Long initiativeId,
                             @ModelAttribute("updateData") InitiativeUIUpdateDto updateDto,
                             BindingResult bindingResult,
                             Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        if (validationService.validationErrors(updateDto, bindingResult, model)) {
            model.addAttribute(ALT_URI_ATTR, urls.alt().update(initiativeId));
            model.addAttribute("initiative", publicInitiativeService.getMunicipalityInitiative(initiativeId, updateDto.getManagementHash(), locale));
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, updateDto.getManagementHash()));
            model.addAttribute("updateData", updateDto);
            return UPDATE_VIEW;
        }

        updateDto.setManagementHash(userService.getManagementHash());

        publicInitiativeService.updateInitiative(initiativeId, updateDto);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.UPDATE_INITIATIVE, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               Locale locale, HttpServletRequest request) {

//        userService.assertManagementRightsForInitiative(initiativeId);
        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        publicInitiativeService.sendReview(initiativeId, userService.getManagementHash(), true, locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            Locale locale, HttpServletRequest request) {

//        userService.assertManagementRightsForInitiative(initiativeId);
        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        publicInitiativeService.sendReview(initiativeId, userService.getManagementHash(), false, locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_START_COLLECTING)
    public String publishAndStartCollecting(@PathVariable("id") Long initiativeId,
                               Locale locale, HttpServletRequest request) {

//        userService.assertManagementRightsForInitiative(initiativeId);
        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        publicInitiativeService.publishInitiative(initiativeId, true, locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.START_COLLECTING, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_MUNICIPALITY)
    public String publishAndSendToMunicipality(@PathVariable("id") Long initiativeId,
                                            Locale locale, HttpServletRequest request) {

//        userService.assertManagementRightsForInitiative(initiativeId);
        userService.getRequiredLoginUserHolder(request).requireManagementRightsForInitiative(initiativeId);

        publicInitiativeService.publishInitiative(initiativeId, false, locale);
        return redirectWithMessage(Urls.get(locale).view(initiativeId),RequestMessage.PUBLISH_AND_SEND, request);
    }
}
