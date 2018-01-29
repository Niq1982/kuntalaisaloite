package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.service.UserService;
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
public abstract class DefaultLoginController extends BaseLoginController {

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
        model.addAttribute("target", target);
        if (userService.getUser(request).isOmUser() && !Strings.isNullOrEmpty(target)) {
            return contextRelativeRedirect(target);
        }
        return MODERATOR_LOGIN_VIEW;
    }

    @RequestMapping(value = MODERATOR_LOGIN, method = RequestMethod.POST)
    public RedirectView moderatorLoginPost(@RequestParam String u,
                                           @RequestParam String p,
                                           @RequestParam(required = false) String target,
                                           Model model, Locale locale, HttpServletRequest request) {

        userService.adminLogin(u, p, request);
        if (Strings.isNullOrEmpty(target)) {
            return new RedirectView(Urls.get(locale).search() + "?orderBy=latest&show=review&type=all", false, true, false);
        }
        else {
            return new RedirectView(Urls.get(locale).getBaseUrl() + target, false, true, false);
        }

    }

    @RequestMapping(value =  {LOGIN_FI, LOGIN_SV}, method = RequestMethod.GET, params = PARAM_MANAGEMENT_CODE)
    public String authorLoginGet(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                 Model model, Locale locale) {
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
    public String logout(@RequestParam(required = false) String target, Locale locale, HttpServletRequest request) {
        UserService.logout(request);
        Urls urls = Urls.get(locale);
        if (target != null) {
            target = getValidLoginTarget(target, urls);
            return redirectWithMessage(target, RequestMessage.LOGOUT, request);
        }
        return redirectWithMessage(urls.frontpage(), RequestMessage.LOGOUT, request);
    }

}
