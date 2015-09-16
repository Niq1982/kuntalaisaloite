package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.ui.ModerationService;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.MUNICIPALITY_DECISION_FI;
import static fi.om.municipalityinitiative.web.Urls.MUNICIPALITY_DECISION_SV;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityDecisionController extends BaseController{

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private ModerationService moderationService;

    @Resource
    private AttachmentService attachmentService;

    public MunicipalityDecisionController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = {MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV}, method = GET)
    public String municipalityModerationView(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        //TODO write tests for get Required MunicipalityUserHolder
        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);
       /* return ViewGenerator.municipalityDecisionView(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                normalInitiativeService.getManagementSettings(initiativeId),
                moderationService.findAuthors(loginUserHolder, initiativeId),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder)


        ).view(model, Urls.get(locale).alt().municipalityModeration());*/
        return "";
    }

    @RequestMapping(value = {MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV}, method = POST)
    public String addDecision(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("decision") MunicipalityDecisionDto decision) {

        //TODO save the decision
        return "";
    }

}
