package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.dto.vetuma.VetumaResponse.Status.CANCELLED;
import static fi.om.municipalityinitiative.dto.vetuma.VetumaResponse.Status.SUCCESSFUL;
import static fi.om.municipalityinitiative.web.Urls.LOGIN_FI;
import static fi.om.municipalityinitiative.web.Urls.LOGIN_SV;
import static fi.om.municipalityinitiative.web.Urls.LOGOUT_FI;
import static fi.om.municipalityinitiative.web.Urls.LOGOUT_SV;
import static fi.om.municipalityinitiative.web.Views.VETUMA_LOGIN_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import fi.om.municipalityinitiative.dto.User;
import fi.om.municipalityinitiative.dto.vetuma.VTJData;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginRequest;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginResponse;
import fi.om.municipalityinitiative.dto.vetuma.VetumaResponse.Status;
import fi.om.municipalityinitiative.service.EncryptionService;

@Controller
public class VetumaController extends BaseLoginController {

    private final Logger log = LoggerFactory.getLogger(VetumaController.class); 
    
    private static final String TARGET_SESSION_PARAM = VetumaController.class.getName() + ".target";
    
    private static final int MAX_TIMESTAMP_DIFF_IN_SECONDS = 10 * 60; // 10 minutes
    
    @Resource EncryptionService encryptionService;
    
    @Resource VetumaLoginRequest loginRequestDefaults;

    @Resource MessageSource messageSource;
    
    private String vetumaURL;
    
    public VetumaController(String vetumaURL, String baseUrl, boolean optimizeResources, String resourcesVersion) {
        super(baseUrl, optimizeResources, resourcesVersion);
        this.vetumaURL = vetumaURL;
    }

    /*
     * Login
     */
    @RequestMapping(value={LOGIN_FI, LOGIN_SV}, method=GET)
    public ModelAndView loginGet(@RequestParam(required=false) String target,  HttpServletRequest request, HttpSession session, Locale locale, Model model) {
        Urls urls = Urls.get(locale);
        
        target = getValidLoginTarget(target, urls);

        User user = userService.getCurrentUser();
        if (user.isAuthenticated()) {
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
    public View loginPost(
            VetumaLoginResponse vetumaResponse,
            Locale locale, 
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        
        String mac = encryptionService.vetumaMAC(vetumaResponse.toMACString());
        
        if (!mac.equalsIgnoreCase(vetumaResponse.getMAC())) {
            log.error("VetumaLoginResponse: Illegal MAC: expected {} but was {}", mac, vetumaResponse.getMAC());
            // TODO Error message for user?
            return redirect(urls.frontpage());
        }
        
        final DateTime now = new DateTime();
        final DateTime timestamp = vetumaResponse.getTimestamp();
        final Status status = vetumaResponse.getSTATUS();
        
        if (diffInSeconds(timestamp, now) > MAX_TIMESTAMP_DIFF_IN_SECONDS) {
            log.warn("VetumaLoginResponse is expired: " + timestamp);
            // TODO Error message for user?
            return redirectToTarget(session);
        } else if (SUCCESSFUL.equals(status)) {
    
            VTJData vtjData = VTJData.parse(vetumaResponse.getVTJDataXML(), messageSource);
            
            if (vtjData.isDead()) {
                log.error("ACCORDING TO VETUMA/VTJ, USER IS DEAD.");
                // TODO Error message for user?
                return redirect(urls.frontpage());
            }
            
            String ssn = vetumaResponse.getSsn(); 
            
            userService.login(ssn, vtjData.getFirstNames(), vtjData.getLastName(), vtjData.isFinnishCitizen(), vtjData.getHomeMunicipality(),
                    request, response);
    
            return redirectToTarget(session);
        } else {
            if (!CANCELLED.equals(status)) {
                log.error("VetumaLoginResponse:\nSTATUS = {}\nEXTRADATA = {}", status, vetumaResponse.getEXTRADATA());
            }
            // TODO Error message for user?
            return redirect(urls.frontpage());
        }
    }
    
    private View redirectToTarget(HttpSession session) {
        String target = (String) session.getAttribute(TARGET_SESSION_PARAM);  
        session.removeAttribute(TARGET_SESSION_PARAM);
        return redirect(target);
    }
    
    private int diffInSeconds(DateTime a, DateTime b) {
        Seconds diff;
        if (a.isBefore(b)) {
            diff = Seconds.secondsBetween(a, b);
        } else {
            diff = Seconds.secondsBetween(b, a);
        }
        return diff.getSeconds();
    }
    
    @RequestMapping(value={LOGOUT_FI, LOGOUT_SV}, method=GET)
    public View logout(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        Urls urls = Urls.get(locale);
        userService.logout(request, response);
        return redirect(urls.frontpage());
    }

}
