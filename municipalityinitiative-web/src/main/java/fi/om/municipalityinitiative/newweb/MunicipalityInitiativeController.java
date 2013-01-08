package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.CREATE_VIEW;
import static fi.om.municipalityinitiative.web.Views.SEARCH_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityInitiativeController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(MunicipalityInitiativeController.class);

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;


    public MunicipalityInitiativeController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }


    /*
 * Search
 */
    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(Model model, Locale locale, HttpServletRequest request) {
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
        return SEARCH_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=GET)
    public String createGet(Model model, Locale locale, HttpServletRequest request) {
        MunicipalityInitiativeUICreateDto initiative = new MunicipalityInitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") MunicipalityInitiativeUICreateDto createDto,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {

        municipalityInitiativeService.addMunicipalityInitiative(createDto);
        return SEARCH_VIEW;

    }

}
