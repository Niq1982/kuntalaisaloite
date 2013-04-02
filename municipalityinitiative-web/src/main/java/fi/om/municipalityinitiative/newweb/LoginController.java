package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.service.UserService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class LoginController extends BaseController {

    @Resource
    UserService userService;

    public LoginController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = {LOGIN_FI, LOGIN_SV}, method = RequestMethod.GET)
    public String loginGet(@RequestParam(required=false) String target, Model model, Locale locale, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=ISO-8859-1");
        model.addAttribute("target", target);
        return Views.DUMMY_LOGIN_VIEW;
    }

    @RequestMapping(value = {LOGIN_FI, LOGIN_SV}, method = RequestMethod.POST)
    public RedirectView login(@RequestParam String u,
                              @RequestParam String p,
                              @RequestParam String target,
                              Model model, Locale locale, HttpServletRequest request) {
        userService.login(u, p, request);

        return new RedirectView(target, false, true, false);

    }

    @RequestMapping(value={LOGOUT_FI, LOGOUT_SV}, method=GET)
    public String logout(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        userService.logout(request, response);
        return redirectWithMessage(urls.frontpage(), RequestMessage.LOGOUT, request);
    }
}
