package fi.om.municipalityinitiative.web;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.User;
import fi.om.municipalityinitiative.service.Role;
import fi.om.municipalityinitiative.service.TestDataService;
import fi.om.municipalityinitiative.util.TestDataTemplates;
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
import static fi.om.municipalityinitiative.web.Views.DUMMY_LOGIN_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Profile("dev")
public class DevController extends BaseLoginController {

    @Resource TestDataService testDataService;
    
    public DevController(String baseUrl, boolean optimizeResources, String resourcesVersion) {
        super(baseUrl, optimizeResources, resourcesVersion);
    }
    
    /*
     * Login
     */
    @RequestMapping(value={LOGIN_FI, LOGIN_SV}, method=GET)
    public String loginGet(@RequestParam(required=false) String target, Model model, HttpServletRequest request, HttpServletResponse response) {
        SecurityFilter.setNoCache(response);
        response.setContentType("text/html;charset=ISO-8859-1");
        User user = userService.getCurrentUser(false);
        if (user.isAuthenticated()) {
            return Views.contextRelativeRedirect(Urls.FRONT);
        } else {
            userService.prepareForLogin(request);
            model.addAttribute("target", target);
            return DUMMY_LOGIN_VIEW;
        }
    }

    @RequestMapping(value={LOGIN_FI, LOGIN_SV}, method=POST)
    public View loginPost(
            @RequestParam(required=true) String ssn, 
            @RequestParam(required=true) String firstName, 
            @RequestParam(required=true) String lastName, 
            @RequestParam(required=true) String homeMunicipality, 
            @RequestParam(required=false, defaultValue="false") boolean finnishCitizen,
            @RequestParam(required=false) String target, 
            Model model, 
            Locale locale,
            HttpServletRequest request,
            HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        target = getValidLoginTarget(target, urls);
        userService.login(ssn, firstName, lastName, finnishCitizen, asLocalizedString(homeMunicipality, homeMunicipality), request, response);
        return redirect(target);
    }
    
    @RequestMapping(value={LOGOUT_FI, LOGOUT_SV}, method=GET)
    public View logout(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        userService.logout(request, response);
        return redirect(urls.frontpage());
    }


    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=GET)
    public String testDataGenerationGet(Model model, Locale locale, HttpServletRequest request) {                               //TODO: move to separate (DEV-profile!!!) controller or rename this?
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().myAccount());
        
        User currentUser = userService.getUserInRole(Role.AUTHENTICATED);
        model.addAttribute("currentUser", currentUser);

        
        String resultInfo = getResultInfo(request);
        
        if (resultInfo != null) {
            model.addAttribute("resultInfo", resultInfo);
        }
        model.addAttribute("testUsers", TestDataTemplates.getUserTemplates());
        model.addAttribute("testReserveAuthorUser", TestDataTemplates.RESERVE_AUTHOR_USER);
        model.addAttribute("testInitiatives", TestDataTemplates.getInitiativeTemplates());
        //model.addAttribute("selections", selections);
        
        return Views.TEST_DATA_GENERATION;
    }
    
    @RequestMapping(value={TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV}, method=POST)
    public String testDataGenerationPost(Model model, Locale locale, HttpServletRequest request) {  //TODO: move to separate (DEV-profile!!!) controller or rename this?
        Urls urls = Urls.get(locale);
        User currentUser = userService.currentAsRegisteredUser();
        //model.addAttribute("currentUser", user);
        
        testDataService.createTestUsersFromTemplates(TestDataTemplates.getUserTemplates());

        List<InitiativeManagement> initiatives = TestDataTemplates.getInitiativeTemplates();
        List<InitiativeManagement> selectedInitiatives = Lists.newArrayList();
        
        for (int i = 0; i < initiatives.size(); i++) {
            if (request.getParameter("selections[" + i + "]") != null) {
                selectedInitiatives.add(initiatives.get(i));
            } 
        }

        String authorEmail0 = request.getParameter("emails[0]");
        String authorEmail1 = request.getParameter("emails[1]");
        
        testDataService.createTestInitiativesFromTemplates(selectedInitiatives, currentUser, authorEmail0, authorEmail1);
        
        
        //TODO: return links to this page instead of redirect to search_old ?
        putResultInfo("Linkit aloitteisiin ...", request);
        //return redirect(urls.fullUrl(request.getPathInfo()));
        return Views.contextRelativeRedirect(urls.searchOwnOnly());
    }

    protected static final String RESULT_INFO_KEY = "resultInfo";
    
//    @SuppressWarnings("unchecked")
    protected void putResultInfo(String resultInfo, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        //List<RequestMessage> resultInfo = (String) flashMap.get("resultInfo");
//        if (resultInfo == null) {
            flashMap.put(RESULT_INFO_KEY, resultInfo);
//        }
    }


//    @SuppressWarnings("unchecked")
    protected String getResultInfo(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            return (String) flashMap.get(RESULT_INFO_KEY);
        } else {
            return null;
        }
    }
    
}
