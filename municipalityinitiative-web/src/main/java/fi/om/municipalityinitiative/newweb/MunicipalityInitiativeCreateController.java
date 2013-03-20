package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.PrepareInitiativeDto;
import fi.om.municipalityinitiative.newdto.ui.SendToMunicipalityDto;
import fi.om.municipalityinitiative.service.InitiativeService;
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
public class MunicipalityInitiativeCreateController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(MunicipalityInitiativeCreateController.class);

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private InitiativeService initiativeService;

    @Resource
    ValidationService validionService;

    public MunicipalityInitiativeCreateController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=GET)
    public String createGet(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().createNew());
        
        InitiativeUICreateDto initiative = new InitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST, params=ACTION_SAVE_AND_SEND)
    public String createAndSendPost(@ModelAttribute("initiative") InitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {

        if (validionService.validationErrors(initiative, bindingResult, model)) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
            return CREATE_VIEW;
        }

        Urls urls = Urls.get(locale);

        Long initiativeId = initiativeService.createMunicipalityInitiative(initiative, locale);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE_AND_SEND, request);
    }
    
    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") InitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {

        if (validionService.validationErrors(initiative, bindingResult, model)) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
            return CREATE_VIEW;
        }

        Urls urls = Urls.get(locale);

        initiative.setCollectable(true);
        
        Long initiativeId = initiativeService.createMunicipalityInitiative(initiative, locale);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE, request);
        
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

        initiative.setCollectable(true);

        Long initiativeId = initiativeService.prepareInitiative(initiative, locale);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE, request);
    }

    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=GET)
    public String editView(@PathVariable("id") Long initiativeId,
                           @RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                           Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, managementHash));
        model.addAttribute("initiative", initiativeService.getInitiativeForEdit(initiativeId, managementHash));
        return EDIT_VIEW;
    }

    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=POST)
    public String editPost(@PathVariable("id") Long initiativeId,
                           @ModelAttribute("initiative") InitiativeUIEditDto editDto,
                           BindingResult bindingResult,
                           Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        if (validionService.validationErrors(editDto, bindingResult, model)) {
            model.addAttribute(ALT_URI_ATTR, urls.alt().edit(initiativeId, editDto.getManagementHash()));
            model.addAttribute("initiative", editDto);
            return EDIT_VIEW;
        }

        initiativeService.updateInitiativeDraft(initiativeId, editDto);
        return contextRelativeRedirect(urls.management(initiativeId,editDto.getManagementHash()));
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
