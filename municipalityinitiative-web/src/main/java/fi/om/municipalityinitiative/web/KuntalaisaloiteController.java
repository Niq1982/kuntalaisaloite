package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.Random;

import static fi.om.municipalityinitiative.web.Urls.SEARCHM_FI;
import static fi.om.municipalityinitiative.web.Urls.SEARCHM_SV;
import static fi.om.municipalityinitiative.web.Views.SEARCHM_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class KuntalaisaloiteController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(KuntalaisaloiteController.class);

    public KuntalaisaloiteController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }


    /*
 * Search
 */
    @RequestMapping(value={ SEARCHM_FI, SEARCHM_SV }, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {

        String at = "omg";
        System.out.println("old: "+ request.getSession().getAttribute(at));
        request.getSession().setAttribute(at, String.valueOf(new Random().nextInt()));

        return SEARCHM_VIEW;



    }

}
