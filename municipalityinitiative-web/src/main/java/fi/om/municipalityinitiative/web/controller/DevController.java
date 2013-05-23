package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.service.TestDataService;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.TestDataTemplates;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Profile("dev")
public class DevController extends BaseController {

    @Resource TestDataService testDataService;
    
    @Resource
    MunicipalityDao municipalityDao;
    
    public DevController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=GET)
    public String testDataGenerationGet(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        
        model.addAttribute("testParticipants", TestDataTemplates.getParticipantTemplates(-1L));
        model.addAttribute("testInitiatives", TestDataTemplates.getInitiativeTemplates(-1L, request.getParameter("email")));
        model.addAttribute("municipalities", municipalityDao.findMunicipalities(true));
        
        return TEST_DATA_GENERATION;
    }
    
    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=POST)
    public String testDataGenerationPost(
            @RequestParam(defaultValue = "1") Integer amount,
            @RequestParam(defaultValue =  "PUBLISHED") InitiativeState state,
            @RequestParam String authorEmail,
            @RequestParam Long homeMunicipalityId,
            @RequestParam Long municipalityId,
            Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        List<TestDataTemplates.InitiativeTemplate> initiatives = TestDataTemplates.getInitiativeTemplates(municipalityId, authorEmail);

        TestDataTemplates.InitiativeTemplate selectedInitiative = initiatives.get(parseIntegerParameter(request, "initiative", 0));
        selectedInitiative.initiative.setState(state);

        List<ParticipantUICreateDto> participants = TestDataTemplates.getParticipantTemplates(homeMunicipalityId);

        Long initiativeId = null;
        for (int i = 0; i < amount; ++i) {
            initiativeId = testDataService.createTestMunicipalityInitiative(selectedInitiative);
            
            if (selectedInitiative.initiative.isCollaborative()) {
                for (int j = 0; j < participants.size(); j++) {
                    Integer participantAmount = parseIntegerParameter(request, "participantAmount[" + j + "]", 1);
                    testDataService.createTestParticipant(initiativeId, participants.get(j), participantAmount);
                }
            }
        }

        addRequestAttribute(Urls.get(locale).loginAuthor(RandomHashGenerator.getPrevious()), request);
        // putResultInfo("Linkit aloitteisiin ...", request);
        return contextRelativeRedirect(urls.testDataGeneration());
    }

    private static Integer parseIntegerParameter(HttpServletRequest request, String parameterName, Integer defaultValue) {
        Integer participantAmount = defaultValue;
        if (!Strings.isNullOrEmpty(request.getParameter(parameterName))) {
            participantAmount = Integer.valueOf(request.getParameter(parameterName));
        }
        return participantAmount;
    }

    protected static final String RESULT_INFO_KEY = "resultInfo";
    
    protected static void putResultInfo(String resultInfo, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(RESULT_INFO_KEY, resultInfo);
    }


    protected static String getResultInfo(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            return (String) flashMap.get(RESULT_INFO_KEY);
        } else {
            return null;
        }
    }
    
}
