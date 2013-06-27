package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.vetuma.VTJData;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginRequest;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginResponse;
import fi.om.municipalityinitiative.dto.vetuma.VetumaResponse;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.service.UserService;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class LoginController extends BaseLoginController {

    private static final String TARGET_SESSION_PARAM = LoginController.class.getName() + ".target";

    private static final int MAX_TIMESTAMP_DIFF_IN_SECONDS = 10 * 60; // 10 minutes

    private final String vetumaURL;

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    UserService userService;

    @Resource
    EncryptionService encryptionService;

    @Resource VetumaLoginRequest loginRequestDefaults;

    public LoginController(String baseUrl, boolean optimizeResources, String resourcesVersion, String vetumaURL) {
        super(baseUrl, optimizeResources, resourcesVersion);
        this.vetumaURL = vetumaURL;
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
        model.addAttribute(Urls.get(locale));
        return SINGLE_LOGIN_VIEW;

    }

    @RequestMapping(value =  {LOGIN_FI, LOGIN_SV}, method = RequestMethod.POST, params = PARAM_MANAGEMENT_CODE)
    public RedirectView authorLoginPost(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                        Model model, Locale locale, HttpServletRequest request) {
        Long initiativeId = userService.authorLogin(managementHash, request);
        return new RedirectView(Urls.get(locale).getManagement(initiativeId), false, true, false);
    }

    /*
    * Login
    */
    @RequestMapping(value={LOGIN_FI, LOGIN_SV}, method=GET)
    public ModelAndView vetumaLoginGet(@RequestParam(required = false) String target, HttpServletRequest request, HttpSession session, Locale locale, Model model) {
        Urls urls = Urls.get(locale);

        target = getValidLoginTarget(target, urls);

        User user = userService.getUser(request);
        if (user.isLoggedIn()) {
            return new ModelAndView(redirect(target));
        } else {
            userService.prepareForLogin(request);
            session.setAttribute(TARGET_SESSION_PARAM, target);

            // Clone defaults
            VetumaLoginRequest vetumaRequest = loginRequestDefaults.clone();

            // Set request specific fields
            vetumaRequest.setTimestamp(new DateTime());
            vetumaRequest.setLG(locale.getLanguage());
            vetumaRequest.setRETURL(urls.login());
            vetumaRequest.setCANURL(urls.login());
            vetumaRequest.setERRURL(urls.login());

            // Assign MAC
            String mac = encryptionService.vetumaMAC(vetumaRequest.toMACString());
            vetumaRequest.setMAC(mac);

            model.addAttribute("vetumaRequest", vetumaRequest);
            model.addAttribute("vetumaURL", vetumaURL);

            return new ModelAndView(VETUMA_LOGIN_VIEW) ;
        }
    }

    @RequestMapping(value={LOGIN_FI, LOGIN_SV}, method=POST)
    public View vetumaLoginPost(
            VetumaLoginResponse vetumaResponse,
            Locale locale,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {
        Urls urls = Urls.get(locale);

        String mac = encryptionService.vetumaMAC(vetumaResponse.toMACString());

        if (!mac.equalsIgnoreCase(vetumaResponse.getMAC())) {
            log.error("VetumaLoginResponse: Illegal MAC: expected {} but was {}", mac, vetumaResponse.getMAC());
            return redirect(urls.vetumaError());
        }

        final DateTime now = new DateTime();
        final DateTime timestamp = vetumaResponse.getTimestamp();
        final VetumaResponse.Status status = vetumaResponse.getSTATUS();

        if (diffInSeconds(timestamp, now) > MAX_TIMESTAMP_DIFF_IN_SECONDS) {
            log.warn("VetumaLoginResponse is expired: " + timestamp);
            return redirect(urls.vetumaError());
        } else if (VetumaResponse.Status.SUCCESSFUL.equals(status)) {

            VTJData vtjData = VTJData.parse(vetumaResponse.getVTJDataXML());

            if (vtjData.isDead()) {
                log.error("ACCORDING TO VETUMA/VTJ, USER IS DEAD.");
                return redirect(urls.vetumaError());
            }

            String ssn = vetumaResponse.getSsn();

            userService.login(ssn,
                    vtjData.getFullName(),
                    vtjData.getAddress(),
                    vtjData.getMunicipalityCode(),
                    request, response);

            return redirectToTarget(session);
        } else {
            if (!VetumaResponse.Status.CANCELLED.equals(status)) {   // Usually errors are REJECTED or FAILURE. Failure often has "Cannot use VTJ" and errorcode 8001
                log.error("VetumaLoginResponse:\nSTATUS = {}\nEXTRADATA = {}", status, vetumaResponse.getEXTRADATA());
                return redirect(urls.vetumaError());
            }
            return redirect(urls.frontpage());
        }
    }

    private View redirectToTarget(HttpSession session) {
        String target = (String) session.getAttribute(TARGET_SESSION_PARAM);
        session.removeAttribute(TARGET_SESSION_PARAM);
        return redirect(target);
    }

    private static int diffInSeconds(DateTime a, DateTime b) {
        Seconds diff;
        if (a.isBefore(b)) {
            diff = Seconds.secondsBetween(a, b);
        } else {
            diff = Seconds.secondsBetween(b, a);
        }
        return diff.getSeconds();
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
