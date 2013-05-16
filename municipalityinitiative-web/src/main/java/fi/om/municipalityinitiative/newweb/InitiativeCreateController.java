package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeCreateController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    InitiativeManagementService initiativeManagementService;

    @Resource
    ValidationService validationService;

    @Resource
    UserService userService;

    public InitiativeCreateController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = { PREPARE_FI, PREPARE_SV }, method = GET)
    public String prepareGet(Model model, Locale locale, HttpServletRequest request) {
        return ViewGenerator.prepareView(new PrepareInitiativeUICreateDto(), municipalityService.findAllMunicipalities(locale))
                .view(model, Urls.get(locale).alt().prepare());
    }

    @RequestMapping(value={ PREPARE_FI, PREPARE_SV }, method=POST)
    public String preparePost(@ModelAttribute("initiative") PrepareInitiativeUICreateDto initiative,
                             BindingResult bindingResult,
                             Model model,
                             Locale locale,
                             HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        if (validationService.validationErrors(initiative, bindingResult, model)) {
            return ViewGenerator.prepareView(initiative, municipalityService.findAllMunicipalities(locale))
                    .view(model, urls.prepare());
        }

        Long initiativeId = publicInitiativeService.prepareInitiative(initiative, locale);
        
        addRequestAttribute(initiative.getParticipantEmail(), request); // To be shown at confirmation page
        return redirectWithMessage(urls.pendingConfirmation(initiativeId), RequestMessage.PREPARE, request);

    }

    // TODO: Combine update and edit views?
    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=GET)
    public String editView(@PathVariable("id") Long initiativeId,
                           Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        ManagementSettings managementSettings = publicInitiativeService.getManagementSettings(initiativeId);

        Urls urls = Urls.get(locale);
        if (managementSettings.isAllowEdit()) {
            return ViewGenerator.editView(
                    initiativeManagementService.getInitiativeDraftForEdit(initiativeId, loginUserHolder),
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.moderation(initiativeId)
            ).view(model, urls.alt().edit(initiativeId));
        }
        else if (managementSettings.isAllowUpdate()) {
            return contextRelativeRedirect(urls.update(initiativeId));
        }
        else {
            return ERROR_500; // TODO: Custom error page or some message that operation is not allowed
        }
    }

    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=POST)
    public String editPost(@PathVariable("id") Long initiativeId,
                           @ModelAttribute("initiative") InitiativeDraftUIEditDto editDto,
                           BindingResult bindingResult,
                           Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        if (validationService.validationErrors(editDto, bindingResult, model)) {
            return ViewGenerator.editView(
                    editDto,
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.moderation(initiativeId)
            ).view(model, urls.alt().edit(initiativeId));
        }

        initiativeManagementService.editInitiativeDraft(initiativeId, loginUserHolder, editDto);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.SAVE_DRAFT, request);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
