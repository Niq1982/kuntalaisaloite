package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.service.CachedInitiativeFinder;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.INDEX_VIEW;
import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;

@Controller
public class FrontPageController extends BaseController {
    
    @Resource
    private CachedInitiativeFinder initiativeFinder;
    
    @Resource
    private MunicipalityService municipalityService;
    
    public FrontPageController(boolean optimizeResources, String resourcesVersion, Maybe<Integer> omPiwicId) {
        super(optimizeResources, resourcesVersion, omPiwicId);
    }
    
    /*
     * Front page
     */
    @RequestMapping(FRONT)
    public String frontpage() {
        // XXX Select locale using Accept-Language header
        return contextRelativeRedirect(Urls.FRONT_FI);
    }
    
    @RequestMapping({ FRONT_FI, FRONT_SV })
    public String frontpage(Model model, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().frontpage());
        model.addAttribute("initiatives", initiativeFinder.frontPageInitiatives());
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        addPiwicIdIfNotAuthenticated(model);

        return INDEX_VIEW;
    }

}
