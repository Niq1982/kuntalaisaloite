package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.PublicInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.util.InitiativeType;
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
                                 @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().management(initiativeId, managementHash));

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId, locale);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (managementHash.equals(initiativeInfo.getManagementHash().get())){
            addModelAttributesToCollectView(model,
                    initiativeInfo,
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId));

            model.addAttribute("participants", participantService.findPublicParticipants(initiativeId));
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, managementHash));
            return MANAGEMENT_VIEW;
        } else {
            return ERROR_404_VIEW;
        }
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=GET)
    public String updateView(@PathVariable("id") Long initiativeId,
                             @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                             Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = publicInitiativeService.managementSettings(initiativeId);

        if (managementSettings.isAllowUpdate()) {

            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, managementHash));
//            model.addAttribute("initiative", publicInitiativeService.getInitiativeDraftForEdit(initiativeId, managementHash)); // TODO UpdateDto, not edit
            model.addAttribute("initiative", publicInitiativeService.getMunicipalityInitiative(initiativeId, managementHash, locale));
            model.addAttribute("updateData", publicInitiativeService.getInitiativeForUpdate(initiativeId, managementHash));
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, managementHash));

            model.addAttribute("previousPageURI", urls.prepare());
            return UPDATE_VIEW;

        }
        else {
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
            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, updateDto.getManagementHash()));
            model.addAttribute("initiative", publicInitiativeService.getMunicipalityInitiative(initiativeId, updateDto.getManagementHash(), locale));
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, updateDto.getManagementHash()));
            model.addAttribute("updateData", updateDto);
            return UPDATE_VIEW;
        }

        publicInitiativeService.updateInitiative(initiativeId, updateDto);
        return redirectWithMessage(urls.management(initiativeId, updateDto.getManagementHash()), RequestMessage.UPDATE_INITIATIVE, request);
    }


    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                               Locale locale, HttpServletRequest request) {

        publicInitiativeService.sendReview(initiativeId, managementHash, true);
        return redirectWithMessage(Urls.get(locale).management(initiativeId, managementHash),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                            Locale locale, HttpServletRequest request) {

        publicInitiativeService.sendReview(initiativeId, managementHash, false);
        return redirectWithMessage(Urls.get(locale).management(initiativeId, managementHash),RequestMessage.SEND_TO_REVIEW, request);
    }
}
