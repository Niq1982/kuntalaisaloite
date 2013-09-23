package fi.om.municipalityinitiative.web.controller;

import com.mysema.commons.lang.Assert;
import fi.om.municipalityinitiative.dto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.vetuma.VTJData;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginRequest;
import fi.om.municipalityinitiative.dto.vetuma.VetumaLoginResponse;
import fi.om.municipalityinitiative.dto.vetuma.VetumaResponse;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.service.ui.VerifiedInitiativeService;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.VETUMA_LOGIN_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class VetumaLoginController extends DefaultLoginController {

    protected final Logger log = LoggerFactory.getLogger(VetumaLoginController.class);

    private static final int MAX_TIMESTAMP_DIFF_IN_SECONDS = 10 * 60; // 10 minutes

    private final String vetumaURL;

    @Resource
    private EncryptionService encryptionService;

    @Resource
    private VetumaLoginRequest loginRequestDefaults;

    @Resource
    private VerifiedInitiativeService verifiedInitiativeService;

    public VetumaLoginController(String baseUrl, boolean optimizeResources, String resourcesVersion, String vetumaURL) {
        super(baseUrl, optimizeResources, resourcesVersion);
        this.vetumaURL = vetumaURL;
    }

    /*
    * Login
    */
    @RequestMapping(value={VETUMA_FI, VETUMA_SV}, method=GET)
    public ModelAndView vetumaLoginGet(@RequestParam(required = false) String target, HttpServletRequest request, HttpSession session, Locale locale, Model model) {
        Urls urls = Urls.get(locale);

        target = getValidLoginTarget(target, urls);

        User user = userService.getUser(request);
        if (user.isLoggedIn() && user.isVerifiedUser()) {
            return new ModelAndView(redirect(target));
        } else {
            userService.prepareForLogin(request);
            session.setAttribute(TARGET_SESSION_PARAM, target);

            // Clone defaults
            VetumaLoginRequest vetumaRequest = loginRequestDefaults.clone();

            // Set request specific fields
            vetumaRequest.setTimestamp(new DateTime());
            vetumaRequest.setLG(locale.getLanguage());
            vetumaRequest.setRETURL(urls.vetumaLogin());
            vetumaRequest.setCANURL(urls.vetumaLogin());
            vetumaRequest.setERRURL(urls.vetumaLogin());

            // Assign MAC
            String mac = encryptionService.vetumaMAC(vetumaRequest.toMACString());
            vetumaRequest.setMAC(mac);

            model.addAttribute("vetumaRequest", vetumaRequest);
            model.addAttribute("vetumaURL", vetumaURL);

            return new ModelAndView(VETUMA_LOGIN_VIEW);
        }
    }

    @RequestMapping(value={VETUMA_FI, VETUMA_SV}, method=POST)
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

            if (!isAdult(ssn)) {
                return redirect(urls.notAdultError());
            }

            userService.login(encryptionService.registeredUserHash(ssn),
                    vtjData.getFullName(),
                    locale.equals(Locales.LOCALE_FI) ? vtjData.getAddressFi() : vtjData.getAddressSv(),
                    vtjData.getMunicipality(),
                    request);

            Maybe<PrepareInitiativeUICreateDto> prepareDataForVetuma = userService.popPrepareDataForVetuma(request);
            if (prepareDataForVetuma.isPresent()) { // User has been redirected to vetuma after starting to create initiative
                long initiativeId;
                try {
                     initiativeId = verifiedInitiativeService.prepareSafeInitiative(userService.getRequiredLoginUserHolder(request), PrepareSafeInitiativeUICreateDto.parse(prepareDataForVetuma.get()));
                } catch (InvalidHomeMunicipalityException e) {
                    return redirectWithMessageToTarget(urls.prepare(), RequestMessage.INVALID_HOME_MUNICIPALITY, request);
                }
                userService.refreshUserData(request);
                return redirect(urls.management(initiativeId));
            }

            return redirectToTarget(session);
        } else {
            if (!VetumaResponse.Status.CANCELLED.equals(status)) {   // Usually errors are REJECTED or FAILURE. Failure often has "Cannot use VTJ" and errorcode 8001
                log.error("VetumaLoginResponse:\nSTATUS = {}\nEXTRADATA = {}", status, vetumaResponse.getEXTRADATA());
                return redirect(urls.vetumaError());
            }
            return redirect(urls.frontpage());
        }
    }

    private View redirectWithMessageToTarget(String target, RequestMessage requestMessage, HttpServletRequest request) {
        addRequestMessage(requestMessage, null, request);
        return redirect(target);
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

    public static final String SSN_REGEX = "(\\d{2})(\\d{2})(\\d{2})([+-A])\\d{3}[0-9A-Z]";

    public static final Pattern SSN_PATTERN = Pattern.compile(SSN_REGEX);

    private static final int LEGAL_AGE = 18;

    private static boolean isAdult(String ssn) {
        Assert.notNull(ssn, "ssn");

        Matcher m = SSN_PATTERN.matcher(ssn);
        if (m.matches()) {
            int dd = Integer.parseInt(m.group(1));
            int mm = Integer.parseInt(m.group(2));
            int yy = Integer.parseInt(m.group(3));
            int yyyy;
            char c = ssn.charAt(6);
            switch (c) {
                case '+':
                    yyyy = 1800 + yy;
                    break;
                case '-':
                    yyyy = 1900 + yy;
                    break;
                case 'A':
                    yyyy = 2000 + yy;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid SSN");
            }
            return Years.yearsBetween(new LocalDate(yyyy, mm, dd), LocalDate.now()).getYears() >= LEGAL_AGE;
        } else {
            throw new IllegalStateException("Invalid SSN");
        }
    }
}
