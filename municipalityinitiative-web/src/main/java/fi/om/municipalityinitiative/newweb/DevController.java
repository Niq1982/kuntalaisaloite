package fi.om.municipalityinitiative.newweb;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.newdto.service.TestDataService;
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
import static fi.om.municipalityinitiative.web.Views.TEST_DATA_GENERATION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Profile("dev")
public class DevController extends BaseController {

    @Resource TestDataService testDataService;
    
    public DevController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=GET)
    public String testDataGenerationGet(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
//        model.addAttribute(ALT_URI_ATTR, urls.alt().myAccount());
//        
//        User currentUser = userService.getUserInRole(Role.AUTHENTICATED);
//        model.addAttribute("currentUser", currentUser);
//
//        
//        String resultInfo = getResultInfo(request);
//        
//        if (resultInfo != null) {
//            model.addAttribute("resultInfo", resultInfo);
//        }
//        model.addAttribute("testUsers", TestDataTemplates.getUserTemplates());
//        model.addAttribute("testReserveAuthorUser", TestDataTemplates.RESERVE_AUTHOR_USER);
//        model.addAttribute("testInitiatives", TestDataTemplates.getInitiativeTemplates());
        
        
        return TEST_DATA_GENERATION;
        
    }
    
//    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=POST)
//    public String testDataGenerationPost(Model model, Locale locale, HttpServletRequest request) {
//        Urls urls = Urls.get(locale);
//        User currentUser = userService.currentAsRegisteredUser();
//
//        testDataService.createTestUsersFromTemplates(TestDataTemplates.getUserTemplates());
//
//        List<InitiativeManagement> initiatives = TestDataTemplates.getInitiativeTemplates();
//        List<InitiativeManagement> selectedInitiatives = Lists.newArrayList();
//        
//        for (int i = 0; i < initiatives.size(); i++) {
//            if (request.getParameter("selections[" + i + "]") != null) {
//                selectedInitiatives.add(initiatives.get(i));
//                if (!Strings.isNullOrEmpty(request.getParameter("start_date")))
//                    initiatives.get(i).setStartDate(new LocalDate(request.getParameter("start_date")));
//                if (!Strings.isNullOrEmpty(request.getParameter("supportcount")))
//                    initiatives.get(i).assignSupportCount(Integer.valueOf(request.getParameter("supportcount")));
//                if (!Strings.isNullOrEmpty(request.getParameter("state"))) {
//                    initiatives.get(i).assignState(InitiativeState.valueOf(request.getParameter("state")));
//                }
//            } 
//        }
//
//        Integer amount = 1;
//        if (!Strings.isNullOrEmpty(request.getParameter("amount"))) {
//            amount = Integer.valueOf(request.getParameter("amount"));
//        }
//
//
//        String authorEmail0 = request.getParameter("emails[0]");
//        String authorEmail1 = request.getParameter("emails[1]");
//
//        for (int i = 0; i < amount; ++i) {
//            testDataService.createTestInitiativesFromTemplates(selectedInitiatives, currentUser, authorEmail0, authorEmail1);
//        }
//
//        putResultInfo("Linkit aloitteisiin ...", request);
//        return contextRelativeRedirect(urls.searchOwnOnly());
//    }

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
