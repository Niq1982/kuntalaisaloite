package fi.om.municipalityinitiative.web.controller;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.web.ErrorFilter;
import fi.om.municipalityinitiative.web.HelpPage;
import fi.om.municipalityinitiative.web.Urls;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ErrorController {

    @RequestMapping(value = {VETUMA_ERROR_FI, VETUMA_ERROR_SV}, method = GET)
    public String vetumaLoginError(HttpServletRequest request, Locale locale, Model model) {
        addModelDefaults(model, Urls.get(locale));
        return Views.ERROR_VETUMA_VIEW;
    }

    @RequestMapping(ERROR_404)
    public String notFound(Locale locale, HttpServletRequest request, HttpServletResponse response, Model model) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        Urls urls = Urls.get(locale);
        addModelDefaults(model, urls);
        return ERROR_404_VIEW;
    }

    @RequestMapping(ERROR_500)
    public String internalServerError(Locale locale, HttpServletRequest request, HttpServletResponse response, Model model) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Urls urls = Urls.get(locale);
        addModelDefaults(model, urls);
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
        addModelDefaults(model, urls);
        return ERROR_410_VIEW;

    }

    private void addModelDefaults(Model model, Urls urls) {
        model.addAttribute("urls", urls);
        model.addAttribute("locale", urls.getLang());

        Map<String, HelpPage> values = Maps.newHashMap();
        for (HelpPage value : HelpPage.values()) {
            values.put(value.name(), value);
        }
        model.addAttribute(HelpPage.class.getSimpleName(), values);
    }

}
