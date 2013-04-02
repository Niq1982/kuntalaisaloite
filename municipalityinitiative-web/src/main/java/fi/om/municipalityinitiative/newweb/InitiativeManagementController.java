package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.InitiativeService;
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
    private InitiativeService initiativeService;

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

        InitiativeViewInfo initiativeInfo = initiativeService.getMunicipalityInitiative(initiativeId, locale);

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
            model.addAttribute("author", initiativeService.getAuthorInformation(initiativeId, managementHash));
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
        ManagementSettings managementSettings = initiativeService.managementSettings(initiativeId);

        if (managementSettings.isAllowUpdate()) {

            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, managementHash));
            model.addAttribute("initiative", initiativeService.getInitiativeDraftForEdit(initiativeId, managementHash)); // TODO UpdateDto, not edit
            model.addAttribute("author", initiativeService.getAuthorInformation(initiativeId, managementHash));

            model.addAttribute("previousPageURI", urls.prepare());
            return UPDATE_VIEW;

        }
        else {
            return ERROR_500; // TODO: Custom error page or some message that operation is not allowed
        }

    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=POST)
    public String updatePost(@PathVariable("id") Long initiativeId,
                             @ModelAttribute("initiative") InitiativeDraftUIEditDto updateDto,
                             BindingResult bindingResult,
                             Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        if (validationService.validationErrors(updateDto, bindingResult, model)) {
            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, updateDto.getManagementHash()));
            model.addAttribute("initiative", initiativeService.getMunicipalityInitiative(initiativeId, updateDto.getManagementHash(), locale));
            model.addAttribute("author", initiativeService.getAuthorInformation(initiativeId, updateDto.getManagementHash()));
            return UPDATE_VIEW;
        }

        // TODO: Get update dto instead of edit-dto

        InitiativeUIUpdateDto copiedUpdateDto = new InitiativeUIUpdateDto();
        copiedUpdateDto.setContactInfo(updateDto.getContactInfo());
        copiedUpdateDto.setManagementHash(updateDto.getManagementHash());
        copiedUpdateDto.setShowName(updateDto.getShowName());
        copiedUpdateDto.setExtraInfo(updateDto.getExtraInfo());
        initiativeService.updateInitiative(initiativeId, copiedUpdateDto);
        return redirectWithMessage(urls.management(initiativeId, updateDto.getManagementHash()), RequestMessage.UPDATE_INITIATIVE, request);
    }


    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                               Locale locale, HttpServletRequest request) {

        initiativeService.sendReview(initiativeId, managementHash, InitiativeType.SINGLE);
        return redirectWithMessage(Urls.get(locale).management(initiativeId, managementHash),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                            Locale locale, HttpServletRequest request) {

        initiativeService.sendReview(initiativeId, managementHash, InitiativeType.COLLABORATIVE);
        return redirectWithMessage(Urls.get(locale).management(initiativeId, managementHash),RequestMessage.SEND_TO_REVIEW, request);
    }
}
