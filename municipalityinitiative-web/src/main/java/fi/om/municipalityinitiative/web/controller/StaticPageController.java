package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.ui.PublicInitiativeService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;

@Controller
public class StaticPageController extends BaseController {
    
    @Resource
    private PublicInitiativeService publicInitiativeService;
    
    @Resource
    private MunicipalityService municipalityService;
    
    public StaticPageController(boolean optimizeResources, String resourcesVersion, Maybe<Integer> omPiwicId) {
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
        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(3);
        search.setShow(InitiativeSearch.Show.all);
        search.setOrderBy(InitiativeSearch.OrderBy.latest);

        model.addAttribute("initiatives", publicInitiativeService.findMunicipalityInitiatives(search, new LoginUserHolder<>(User.anonym())));
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        addPiwicIdIfNotAuthenticated(model);

        return INDEX_VIEW;
    }

}
