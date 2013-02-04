package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.util.Locales.LOCALE_FI;
import static fi.om.municipalityinitiative.util.Locales.LOCALE_SV;
import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class StaticPageController extends BaseController {
    
    @Resource MessageSource messageSource;

    public final String HELP_PAGE_ATTR = "helpPage";
    public final String INFO_PAGE_ATTR = "infoPage";
    
    private final Map<String, HelpPage> helpPageEnumsFi = new HashMap<>();
    private final Map<String, HelpPage> helpPageEnumsSv = new HashMap<>();
    private final HelpPage defaultHelpPage = HelpPage.GENERAL;

    private final Map<String, InfoPage> infoPageEnumsFi = new HashMap<>();
    private final Map<String, InfoPage> infoPageEnumsSv = new HashMap<>();
    private final InfoPage defaultInfoPage = InfoPage.OM;
    
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

    @RequestMapping(API)
    public String api() {
        return Views.API_VIEW;
    }

    @RequestMapping({ HELP_INDEX_FI, HELP_INDEX_SV })
    public String helpIndex(Model model, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().helpIndex());
        addPiwicIdIfNotAuthenticated(model);

        model.addAttribute(HELP_PAGE_ATTR, defaultHelpPage);
        
        return HELP_VIEW;
    }
    
    @RequestMapping(value={ HELP_FI, HELP_SV }, method=GET)
    public String help(@PathVariable("helpPage") String localizedPageName, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().help(getAltHelpPageName(localizedPageName, locale)));
        addPiwicIdIfNotAuthenticated(model);

        HelpPage helpPage = getHelpPageId(localizedPageName, locale);
        if (helpPage == null) {
            throw new NotFoundException("helpPage", localizedPageName);
        }
        else {
            model.addAttribute(HELP_PAGE_ATTR, helpPage);
            return HELP_VIEW;
        }
    }

    @RequestMapping({ INFO_INDEX_FI, INFO_INDEX_SV })
    public String infoIndex(Model model, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().infoIndex());
        addPiwicIdIfNotAuthenticated(model);

        model.addAttribute(INFO_PAGE_ATTR, defaultInfoPage);
        
        return INFO_VIEW;
    }
    
    @RequestMapping(value={ INFO_FI, INFO_SV }, method=GET)
    public String info(@PathVariable("infoPage") String localizedPageName, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().info(getAltInfoPageName(localizedPageName, locale)));
        addPiwicIdIfNotAuthenticated(model);

        InfoPage infoPage = getInfoPageId(localizedPageName, locale);
        if (infoPage == null) {
            throw new NotFoundException("infoPage", localizedPageName);
        }
        else {
            model.addAttribute(INFO_PAGE_ATTR, infoPage);
            return INFO_VIEW;
        }
    }
    
    
    /*
     * News
     */
    @RequestMapping({ NEWS_FI, NEWS_SV })
    public String news(Model model, Locale locale) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().news());
        addPiwicIdIfNotAuthenticated(model);

        return NEWS_VIEW;
    }

    
    /*
     * Misc helpers, these can be moved later to separate StaticPageService: 
     */
    @PostConstruct
    public void initEnumTranslations() {
        for (HelpPage value : HelpPage.values()) {
            helpPageEnumsFi.put(getEnumDesc(value, LOCALE_FI), value);
            helpPageEnumsSv.put(getEnumDesc(value, LOCALE_SV), value);
        }
        for (InfoPage value : InfoPage.values()) {
            infoPageEnumsFi.put(getEnumDesc(value, LOCALE_FI), value);
            infoPageEnumsSv.put(getEnumDesc(value, LOCALE_SV), value);
        }
    }
    
    private String getEnumDesc(HelpPage value, Locale locale) {
        return getMessageForKey(HelpPage.class.getSimpleName() + "." + value.name(), locale);
    }

    private String getEnumDesc(InfoPage value, Locale locale) {
        return getMessageForKey(InfoPage.class.getSimpleName() + "." + value.name(), locale);
    }
    
    private String getMessageForKey(String key, Locale locale) {
        String ret;
        try {
            ret = messageSource.getMessage(key, null, locale);
        } catch (Exception ex) {
            ret = "[" + key + "]";
        }
//        System.out.println("[@@@] " + messageSource + " "+ locale + ": '" + key + "' --> " + ret); //TODO: remove
        return ret;
    }
    
    private HelpPage getHelpPageId(String localizedName, Locale locale) {
        if (LOCALE_SV.equals(locale)) {
            return helpPageEnumsSv.get(localizedName);
        }
        else {
            return helpPageEnumsFi.get(localizedName);
        }
    }

    private InfoPage getInfoPageId(String localizedName, Locale locale) {
        if (LOCALE_SV.equals(locale)) {
            return infoPageEnumsSv.get(localizedName);
        }
        else {
            return infoPageEnumsFi.get(localizedName);
        }
    }

    private String getAltHelpPageName(String localizedName, Locale sourceLocale) {
        HelpPage helpPage = getHelpPageId(localizedName, sourceLocale);
        if (helpPage == null) {
            helpPage = defaultHelpPage;
        }
        return getEnumDesc(helpPage, Locales.getAltLocale(sourceLocale));
    }

    private String getAltInfoPageName(String localizedName, Locale sourceLocale) {
        InfoPage infoPage = getInfoPageId(localizedName, sourceLocale);
        if (infoPage == null) {
            infoPage = defaultInfoPage;
        }
        return getEnumDesc(infoPage, Locales.getAltLocale(sourceLocale));
    }

}
