package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
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
import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityInitiativeCreateController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(MunicipalityInitiativeCreateController.class);

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;

    @Resource
    SmartValidator validator;

    public MunicipalityInitiativeCreateController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=GET)
    public String createGet(Model model, Locale locale, HttpServletRequest request) {
        MunicipalityInitiativeUICreateDto initiative = new MunicipalityInitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") MunicipalityInitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {


        // TODO: Extract all validations to own service for encapsulation and testability
        validator.validate(initiative, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            model.addAttribute("errors", bindingResult);
            return CREATE_VIEW;
        }

        Long initiativeId = municipalityInitiativeService.addMunicipalityInitiative(initiative);

        Urls urls = Urls.get(locale);
        return contextRelativeRedirect(urls.view(initiativeId));

    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


}
