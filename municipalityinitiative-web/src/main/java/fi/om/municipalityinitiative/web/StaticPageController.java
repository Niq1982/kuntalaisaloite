package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;

@Controller
public class StaticPageController extends BaseController {
    
    public StaticPageController(boolean optimizeResources, String resourcesVersion, Maybe<Integer> omPiwicId) {
        super(optimizeResources, resourcesVersion, omPiwicId);
    }
    
    /*
     * Front page
     */
    @RequestMapping(FRONT)
    public String frontpage() {
        // XXX Select locale using Accept-Language header
        return contextRelativeRedirect(Urls.FRONT_FI);
    }
    
    @RequestMapping({ FRONT_FI, FRONT_SV })
    public String frontpage(Model model, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().frontpage());
        addPiwicIdIfNotAuthenticated(model);

        return INDEX_VIEW;
    }

}
