package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.service.MunicipalityInitiativeService;
import fi.om.municipalityinitiative.service.MunicipalityService;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.collect.Lists;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;
import static fi.om.municipalityinitiative.web.Urls.ACTION_SAVE;
import static fi.om.municipalityinitiative.web.Urls.CREATE_FI;
import static fi.om.municipalityinitiative.web.Urls.CREATE_SV;
import static fi.om.municipalityinitiative.web.Views.CREATE_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityInitiativeCreateController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(MunicipalityInitiativeCreateController.class);

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private MunicipalityInitiativeService municipalityInitiativeService;

    @Resource
    SmartValidator validator;

    public MunicipalityInitiativeCreateController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=GET)
    public String createGet(Model model, Locale locale, HttpServletRequest request) {
        MunicipalityInitiativeUICreateDto initiative = new MunicipalityInitiativeUICreateDto();
        model.addAttribute("initiative", initiative);
        model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
        return CREATE_VIEW;
    }

    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST)
    public String createPost(@ModelAttribute("initiative") MunicipalityInitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {


        // TODO: Extract all validations to own service for encapsulation and testability
        validator.validate(initiative, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            model.addAttribute("errors", bindingResult);
            return CREATE_VIEW;
        }

        Long initiativeId = municipalityInitiativeService.addMunicipalityInitiative(initiative, false);

        Urls urls = Urls.get(locale);
        //return contextRelativeRedirect(urls.view(initiativeId));
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE_AND_SEND, request);
    }
    
    @RequestMapping(value={ CREATE_FI, CREATE_SV }, method=POST, params=ACTION_SAVE)
    public String createAndCollectPost(@ModelAttribute("initiative") MunicipalityInitiativeUICreateDto initiative,
                            BindingResult bindingResult,
                            Model model,
                            Locale locale,
                            HttpServletRequest request) {


        // TODO: Extract all validations to own service for encapsulation and testability
        validator.validate(initiative, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("initiative", initiative);
            model.addAttribute("municipalities", municipalityService.findAllMunicipalities());
            model.addAttribute("errors", bindingResult);
            return CREATE_VIEW;
        }

        Long initiativeId = municipalityInitiativeService.addMunicipalityInitiative(initiative, true);

        Urls urls = Urls.get(locale);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.SAVE, request);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, Locale locale) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    static void addRequestMessage(RequestMessage requestMessage, Model model, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        addListElement(flashMap, "requestMessages", requestMessage);
        if (model != null) {
            addListElement(model.asMap(), "requestMessages", requestMessage);
        }
    }
    
    private static <T> void addListElement(Map<? super String, ? super List<T>> map, String key, T value) {
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) map.get(key);
        if (list == null) {
            list = Lists.newArrayList();
            map.put(key, list);
        }
        list.add(value);
    }
    
    protected String redirectWithMessage(String targetUri, RequestMessage requestMessage, HttpServletRequest request) {
        addRequestMessage(requestMessage, null, request);
        return contextRelativeRedirect(targetUri);
    }


}
