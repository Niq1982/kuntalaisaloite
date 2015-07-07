package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.SupportCountService;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GraphIFrameController extends BaseController {

    @Resource
    private NormalInitiativeService initiativeService;

    @Resource
    private SupportCountService supportCountService;

    public GraphIFrameController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={GRAPH_IFRAME_FI, GRAPH_IFRAME_SV}, method = GET)
    public String graphIFrame(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request){
        InitiativeViewInfo initiative = initiativeService.getPublicInitiative(initiativeId);
        model.addAttribute("initiative", initiative);
        if (initiative.isCollaborative()) {
            model.addAttribute("supportCountData", supportCountService.getSupportVotesPerDateJson(initiativeId));
        }
        model.addAttribute("participantCount", initiative.getParticipantCount());
        return ViewGenerator.graphIFrameView().view(model, Urls.get(locale).alt().graphIFrame());
    }

    @RequestMapping(value={GRAPH_IFRAME_GENERATOR_FI, GRAPH_IFRAME_GENERATOR_SV}, method = GET)
    public String graphIFrameGenerator(Model model, Locale locale){
        return ViewGenerator.graphIFrameGeneratorView().view(model,Urls.get(locale).alt().graphIFrameGenerator());
    }
}
