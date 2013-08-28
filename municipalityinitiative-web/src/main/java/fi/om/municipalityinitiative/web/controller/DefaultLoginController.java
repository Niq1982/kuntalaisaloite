package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class DefaultLoginController extends BaseLoginController {

    protected static final String TARGET_SESSION_PARAM = DefaultLoginController.class.getName() + ".target";

    public DefaultLoginController(String baseUrl, boolean optimizeResources, String resourcesVersion) {
        super(baseUrl, optimizeResources, resourcesVersion);
    }
    
    @RequestMapping(value =  {AUTHENTICATE_FI, AUTHENTICATE_SV}, method = RequestMethod.GET)
    public String authenticateGet(@RequestParam(value = Urls.TARGET, required = false) String target, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().authenticate());
        model.addAttribute("target", target == null ? (locale.equals(Locales.LOCALE_FI) ? Urls.AUTHENTICATE_FI : Urls.AUTHENTICATE_SV) : target);
        return AUTHENTICATE_VIEW;
    }

    @RequestMapping(value = MODERATOR_LOGIN, method = RequestMethod.GET)
    public String moderatorLoginGet(@RequestParam(required = false) String target, Model model, Locale locale, HttpServletRequest request, HttpServletResponse response) {
//        // NOTE: Needed for VETUMA
//        response.setContentType("text/html;charset=ISO-8859-1");
        model.addAttribute("target", target);
        return MODERATOR_LOGIN_VIEW;
    }

    @RequestMapping(value = MODERATOR_LOGIN, method = RequestMethod.POST)
    public RedirectView moderatorLoginPost(@RequestParam String u,
                                           @RequestParam String p,
                                           Model model, Locale locale, HttpServletRequest request) {
        userService.adminLogin(u, p, request);
        return new RedirectView(Urls.get(Locales.LOCALE_FI).frontpage());
    }

    @RequestMapping(value =  {LOGIN_FI, LOGIN_SV}, method = RequestMethod.GET, params = PARAM_MANAGEMENT_CODE)
    public String authorLoginGet(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale, HttpServletRequest request) {
        model.addAttribute("managementHash", managementHash);
        model.addAttribute(ALT_URI_ATTR, Urls.get(locale).alt().loginAuthor(managementHash));
        return SINGLE_LOGIN_VIEW;

    }

    @RequestMapping(value =  {LOGIN_FI, LOGIN_SV}, method = RequestMethod.POST, params = PARAM_MANAGEMENT_CODE)
    public RedirectView authorLoginPost(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                        Model model, Locale locale, HttpServletRequest request) {
        Long initiativeId = userService.authorLogin(managementHash, request);
        return new RedirectView(Urls.get(locale).getManagement(initiativeId), false, true, false);
    }

    @RequestMapping(value={LOGOUT_FI, LOGOUT_SV}, method=GET)
    public String logout(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        userService.logout(request);
        return redirectWithMessage(urls.frontpage(), RequestMessage.LOGOUT, request);
    }

    @RequestMapping(value = "/testimestarimiekkonen", method = RequestMethod.GET)
    public String TESTIMESTARILOGIN(@RequestParam(required = false) String target, Model model, Locale locale, HttpServletRequest request, HttpServletResponse response) {
//        // NOTE: Needed for VETUMA
//        response.setContentType("text/html;charset=ISO-8859-1");
        model.addAttribute("target", target);
        return MODERATOR_LOGIN_VIEW;
    }

    @RequestMapping(value = "/testimestarimiekkonen", method = RequestMethod.POST)
    public RedirectView TESTIMESTARILOGIN(@RequestParam String u,
                                          @RequestParam String p,
                                          Model model, Locale locale, HttpServletRequest request) {
        userService.adminLogin(u, p, request);
        return new RedirectView(Urls.get(Locales.LOCALE_FI).frontpage());
    }
}
