package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.service.CachedInitiativeFinder;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.INDEX_VIEW;

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
    public RedirectView frontpage() {
        RedirectView redirectView = new RedirectView(Urls.FRONT_FI, true, true, false);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;
    }
    
    @RequestMapping({ FRONT_FI, FRONT_SV })
    public String frontpage(Model model, HttpServletRequest request, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().frontpage());
        model.addAttribute("initiatives", initiativeFinder.frontPageInitiatives());
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        addPiwicIdIfNotAuthenticated(model, request);

        return INDEX_VIEW;
    }

}
