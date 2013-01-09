package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.SEARCH_VIEW;
import static fi.om.municipalityinitiative.web.Views.VIEW_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MunicipalityInitiativeViewController extends BaseController {

    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;

    public MunicipalityInitiativeViewController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(Model model, Locale locale, HttpServletRequest request) {
        model.addAttribute("initiatives", municipalityInitiativeService.findAllMunicipalityInitiatives());
        return SEARCH_VIEW;
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityInitiativeInfo initiativeInfo = municipalityInitiativeService.getMunicipalityInitiative(initiativeId);

        model.addAttribute("initiative", initiativeInfo);
        return VIEW_VIEW;

    }
}
