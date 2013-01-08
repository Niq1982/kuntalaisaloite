package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.MunicipalityInitiativeDao;
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
import java.util.Random;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.CREATE_VIEW;
import static fi.om.municipalityinitiative.web.Views.SEARCH_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityInitiativeController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(MunicipalityInitiativeController.class);

    @Resource
    private MunicipalityDao municipalityDao;

    @Resource
    private MunicipalityInitiativeDao municipalityInitiativeDao;


    public MunicipalityInitiativeController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }


    /*
 * Search
 */
    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {

        String at = "omg";
        System.out.println("old: "+ request.getSession().getAttribute(at));
        request.getSession().setAttribute(at, String.valueOf(new Random().nextInt()));

        model.addAttribute("municipalities", municipalityDao.findMunicipalities());

        return SEARCH_VIEW;

    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=GET)
    public String createGet(Model model, Locale locale, HttpServletRequest request) {
        MunicipalityInitiativeUICreateDto initiative = new MunicipalityInitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityDao.findMunicipalities());
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") MunicipalityInitiativeUICreateDto createDto,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {

        // TODO: Add initiative
        return SEARCH_VIEW;

    }

}
