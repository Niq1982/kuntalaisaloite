package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.CREATE_FI;
import static fi.om.municipalityinitiative.web.Urls.CREATE_SV;
import static fi.om.municipalityinitiative.web.Views.CREATE_VIEW;
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
        InitiativeUICreateDto initiative = new InitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") InitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {

        if (validionService.validationErrors(initiative, bindingResult, model)) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            return CREATE_VIEW;
        }

        Urls urls = Urls.get(locale);
        if (Boolean.TRUE.equals(initiative.getCollectable())) {
            Long initiativeId = initiativeService.createMunicipalityInitiative(initiative, true);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE, request);
        }
        else {
            Long initiativeId = initiativeService.createMunicipalityInitiative(initiative, false);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE_AND_SEND, request);
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
