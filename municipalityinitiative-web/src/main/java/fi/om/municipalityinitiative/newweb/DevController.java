package fi.om.municipalityinitiative.newweb;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.service.TestDataService;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.TestDataTemplates;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.util.Locales.asLocalizedString;
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
        
        model.addAttribute("testParticipants", TestDataTemplates.getParticipantTemplates(null,null));
        model.addAttribute("testInitiatives", TestDataTemplates.getInitiativeTemplates(null));
        model.addAttribute("municipalities", municipalityDao.findMunicipalities(true));
        
        return TEST_DATA_GENERATION;
    }
    
    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=POST)
    public String testDataGenerationPost(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        
        Long municipalityId = Long.valueOf(request.getParameter("municipalityId"));
        Long homeMunicipalityId = municipalityId;

        List<InitiativeUICreateDto> initiatives = TestDataTemplates.getInitiativeTemplates(municipalityId);
        
        Integer initiativeSelection = 0;
        if (!Strings.isNullOrEmpty(request.getParameter("initiative"))) {
            initiativeSelection = Integer.valueOf(request.getParameter("initiative"));
        }
        InitiativeUICreateDto selectedInitiative = initiatives.get(initiativeSelection);
        
        Integer amount = 1;
        if (!Strings.isNullOrEmpty(request.getParameter("amount"))) {
            amount = Integer.valueOf(request.getParameter("amount"));
        }

        homeMunicipalityId = Long.valueOf(request.getParameter("homeMunicipalityId"));
        List<ParticipantUICreateDto> participants = TestDataTemplates.getParticipantTemplates(municipalityId, homeMunicipalityId);

        for (int i = 0; i < amount; ++i) {
            Long initiativeId = testDataService.createTestMunicipalityInitiative(selectedInitiative);
            
            if (selectedInitiative.isCollectable()) {
                for (int j = 0; j < participants.size(); j++) {
                    Integer participantAmount = 1;
                    if (!Strings.isNullOrEmpty(request.getParameter("participantAmount["+j+"]"))) {
                        participantAmount = Integer.valueOf(request.getParameter("participantAmount["+j+"]"));
                    }

                    testDataService.createTestParticipant(initiativeId, participants.get(j), participantAmount);
                }
            }
        }

        putResultInfo("Linkit aloitteisiin ...", request);
        return contextRelativeRedirect(urls.search());
    }

    protected static final String RESULT_INFO_KEY = "resultInfo";
    
    protected void putResultInfo(String resultInfo, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(RESULT_INFO_KEY, resultInfo);
    }


    protected String getResultInfo(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            return (String) flashMap.get(RESULT_INFO_KEY);
        } else {
            return null;
        }
    }
    
}
