package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.service.TestDataService;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.service.YouthInitiativeWebServiceNotifier;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.TestDataTemplates;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.TEST_DATA_GENERATION_FI;
import static fi.om.municipalityinitiative.web.Urls.TEST_DATA_GENERATION_SV;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Profile("dev")
public class TestDataController extends BaseController {

    @Resource
    TestDataService testDataService;
    
    @Resource
    MunicipalityService municipalityService;
    
    public TestDataController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value="/api/kua/1.0/initiative/{youthInitiativeId}/status/create/")
    public YouthInitiativeWebServiceNotifier.Response nuaTestUri() {
        return new YouthInitiativeWebServiceNotifier.Response();
    }

    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=GET)
    public String testDataGenerationGet(Model model, Locale locale, HttpServletRequest request) {

        model.addAttribute("testParticipants", TestDataTemplates.getParticipantTemplates(-1L));
        model.addAttribute("testInitiatives", TestDataTemplates.getInitiativeTemplates(-1L, request.getParameter("email")));
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities(locale));
        
        return TEST_DATA_GENERATION;
    }
    
    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=POST)
    public String testDataGenerationPost(
            @RequestParam(defaultValue = "1") Integer amount,
            @RequestParam(defaultValue =  "PUBLISHED") InitiativeState state,
            @RequestParam String authorEmail,
            @RequestParam Long homeMunicipalityId,
            @RequestParam Long municipalityId,
            @RequestParam Long youthInitiativeId,
            Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        List<TestDataTemplates.InitiativeTemplate> initiatives = TestDataTemplates.getInitiativeTemplates(municipalityId, authorEmail);

        TestDataTemplates.InitiativeTemplate selectedInitiative = initiatives.get(parseIntegerParameter(request, "initiative", 0));
        selectedInitiative.initiative.setState(state);

        if (youthInitiativeId != null) {
            selectedInitiative.initiative.setYouthInitiativeId(youthInitiativeId);
        }

        List<ParticipantUICreateDto> participants = TestDataTemplates.getParticipantTemplates(homeMunicipalityId);

        Long initiativeId = null;
        for (int i = 0; i < amount; ++i) {
            initiativeId = testDataService.createTestMunicipalityInitiative(selectedInitiative, userService.getLoginUserHolder(request));
            userService.refreshUserData(request);

            if (selectedInitiative.initiative.isCollaborative()) {
                for (int j = 0; j < participants.size(); j++) {
                    Integer participantAmount = parseIntegerParameter(request, "participantAmount[" + j + "]", 1);
                    for (int count = 0; count < participantAmount; ++count) {
                        if (selectedInitiative.initiative.getType().isNotVerifiable()) {
                            testDataService.createTestParticipant(initiativeId, participants.get(j));
                        } else {
                            testDataService.createVerifiedTestParticipant(initiativeId, participants.get(j));
                        }
                    }
                }
            }
        }

        addRequestAttribute(Urls.get(locale).loginAuthor(RandomHashGenerator.getPrevious()), request);
        return contextRelativeRedirect(urls.testDataGeneration());
    }

    // Enable custom login-url for external testers
    @RequestMapping(value = "/testimestarimiekkonen", method = RequestMethod.GET)
    public String TESTIMESTARILOGIN(@RequestParam(required = false) String target, Model model) {
        model.addAttribute("target", target);
        return MODERATOR_LOGIN_VIEW;
    }

    @RequestMapping(value = "/testimestarimiekkonen", method = RequestMethod.POST)
    public RedirectView TESTIMESTARILOGIN(@RequestParam String u,
                                          @RequestParam String p,
                                          HttpServletRequest request) {
        userService.adminLogin(u, p, request);
        return new RedirectView(Urls.get(Locales.LOCALE_FI).frontpage(), false, true, false);
    }

    private static Integer parseIntegerParameter(HttpServletRequest request, String parameterName, Integer defaultValue) {
        if (!Strings.isNullOrEmpty(request.getParameter(parameterName))) {
            return Integer.valueOf(request.getParameter(parameterName));
        }
        return defaultValue;
    }

}
