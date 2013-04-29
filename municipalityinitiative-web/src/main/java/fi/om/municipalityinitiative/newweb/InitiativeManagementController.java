package fi.om.municipalityinitiative.newweb;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
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
    private ValidationService validationService;

    public InitiativeManagementController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (hasNeverBeenSaved(initiativeInfo)) {
            return contextRelativeRedirect(Urls.get(locale).edit(initiativeId));
        }

        return ViewGenerator.managementView(initiativeInfo,
                publicInitiativeService.getManagementSettings(initiativeId),
                publicInitiativeService.getAuthorInformation(initiativeId, loginUserHolder)
        ).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }

    private static boolean hasNeverBeenSaved(InitiativeViewInfo initiativeInfo) {
        return Strings.isNullOrEmpty(initiativeInfo.getName());
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=GET)
    public String updateView(@PathVariable("id") Long initiativeId,
                             Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = publicInitiativeService.getManagementSettings(initiativeId);

        if (managementSettings.isAllowUpdate()) {

            return ViewGenerator.updateView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    publicInitiativeService.getInitiativeForUpdate(initiativeId, loginUserHolder),
                    publicInitiativeService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.getManagement(initiativeId)
            ).view(model, urls.alt().update(initiativeId));

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

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        if (validationService.validationErrors(updateDto, bindingResult, model)) {

            return ViewGenerator.updateView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    updateDto,
                    publicInitiativeService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.getManagement(initiativeId)
            ).view(model, urls.alt().update(initiativeId));
        }

        publicInitiativeService.updateInitiative(initiativeId, loginUserHolder, updateDto);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.UPDATE_INITIATIVE, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_SENT_COMMENT) String sentComment,
                               Locale locale, HttpServletRequest request) {
//        publicInitiativeService.sendReview(initiativeId, userService.getRequiredLoginUserHolder(request), true, locale);
        publicInitiativeService.sendReviewAndStraightToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), sentComment, locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            Locale locale, HttpServletRequest request) {
        publicInitiativeService.sendReviewOnlyForAcceptance(initiativeId, userService.getRequiredLoginUserHolder(request), locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_START_COLLECTING)
    public String publishAndStartCollecting(@PathVariable("id") Long initiativeId,
                               Locale locale, HttpServletRequest request) {
        publicInitiativeService.publishAndStartCollecting(initiativeId, userService.getRequiredLoginUserHolder(request), locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.START_COLLECTING, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_MUNICIPALITY)
    public String sendToMunicipality(@PathVariable("id") Long initiativeId,
                                     @RequestParam(PARAM_SENT_COMMENT) String sentComment,
                                     Locale locale, HttpServletRequest request) {
        publicInitiativeService.sendToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), sentComment, locale);
        return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.PUBLISH_AND_SEND, request);
    }
}
