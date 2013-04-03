package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.PrepareInitiativeDto;
import fi.om.municipalityinitiative.service.PublicInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(InitiativeCreateController.class);

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    ValidationService validionService;

    public InitiativeCreateController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = { PREPARE_FI, PREPARE_SV }, method = GET)
    public String prepareGet(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().createNew());

        model.addAttribute("initiative", new PrepareInitiativeDto());
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        return PREPARE_VIEW;
    }

    @RequestMapping(value={ PREPARE_FI, PREPARE_SV }, method=POST)
    public String preparePost(@ModelAttribute("initiative") PrepareInitiativeDto initiative,
                             BindingResult bindingResult,
                             Model model,
                             Locale locale,
                             HttpServletRequest request) {

        if (validionService.validationErrors(initiative, bindingResult, model)) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
            return PREPARE_VIEW;
        }

        Urls urls = Urls.get(locale);
        Long initiativeId = publicInitiativeService.prepareInitiative(initiative, locale);
        
        addRequestAttribute(initiative.getAuthorEmail(), model, request);
        return redirectWithMessage(urls.pendingConfirmation(initiativeId), RequestMessage.PREPARE, request);

    }

    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=GET)
    public String editView(@PathVariable("id") Long initiativeId,
                           @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                           Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = publicInitiativeService.managementSettings(initiativeId);

        if (managementSettings.isAllowEdit()) {
            InitiativeDraftUIEditDto initiative = publicInitiativeService.getInitiativeDraftForEdit(initiativeId, managementHash);

            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, managementHash));
            model.addAttribute("initiative", initiative);
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, managementHash));

            model.addAttribute("previousPageURI", urls.prepare());
            return EDIT_VIEW;
        }
        else if (managementSettings.isAllowUpdate()) {
            return contextRelativeRedirect(urls.update(initiativeId, managementHash));
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

        if (validionService.validationErrors(editDto, bindingResult, model)) {
            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, editDto.getManagementHash()));
            model.addAttribute("initiative", editDto);
            model.addAttribute("author", publicInitiativeService.getAuthorInformation(initiativeId, editDto.getManagementHash()));
            return EDIT_VIEW;
        }

        publicInitiativeService.editInitiativeDraft(initiativeId, editDto);
        return redirectWithMessage(urls.management(initiativeId,editDto.getManagementHash()), RequestMessage.SAVE_DRAFT, request);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
