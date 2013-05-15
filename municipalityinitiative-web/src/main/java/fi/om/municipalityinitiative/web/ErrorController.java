package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.web.Urls.ERROR_404;
import static fi.om.municipalityinitiative.web.Urls.ERROR_410;
import static fi.om.municipalityinitiative.web.Urls.ERROR_500;
import static fi.om.municipalityinitiative.web.Views.ERROR_404_VIEW;
import static fi.om.municipalityinitiative.web.Views.ERROR_500_VIEW;
import static fi.om.municipalityinitiative.web.Views.ERROR_410_VIEW;
import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping(ERROR_404)
    public String notFound(Locale locale, HttpServletRequest request, HttpServletResponse response, Model model) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        Urls urls = Urls.get(locale);
        model.addAttribute("urls", urls);
        model.addAttribute("locale", urls.getLang());
        return ERROR_404_VIEW;
    }

    @RequestMapping(ERROR_500)
    public String internalServerError(Locale locale, HttpServletRequest request, HttpServletResponse response, Model model) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Urls urls = Urls.get(locale);
        model.addAttribute("urls", urls);
        model.addAttribute("locale", urls.getLang());
        if (request.getAttribute(ErrorFilter.ATTR_ERROR_CASE_ID) != null) {
            // this request came through ErrorFilter
            return ERROR_500_VIEW;
        } 
        else {
            // not real error, no need to show error page
            return contextRelativeRedirect(Urls.FI.frontpage());
        }
    }

    @RequestMapping(ERROR_410)
    public String resourceGone(Locale locale, HttpServletRequest request, HttpServletResponse response, Model model) {
        response.setStatus(HttpStatus.GONE.value());
        Urls urls = Urls.get(locale);
        model.addAttribute("urls", urls);
        model.addAttribute("locale", urls.getLang());
        return ERROR_410_VIEW;

    }

}
