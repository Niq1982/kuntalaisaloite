package fi.om.municipalityinitiative.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ui.Participants;
import fi.om.municipalityinitiative.service.UserService;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;

public class BaseController {

    static final String REQUEST_MESSAGES_KEY = "requestMessages";

    static final String REQUEST_ATTRIBUTE_KEY = "requestAttribute";
    
    public final String ALT_URI_ATTR = "altUri";
    public final String CURRENT_URI_ATTR = "currentUri";

    public final String OM_PICIW_ID = "omPiwicId";

    @Resource
    BeansWrapper freemarkerObjectWrapper;

    @Resource
    UserService userService;
    
    private final boolean optimizeResources;
    
    private final String resourcesVersion;

    private final Maybe<Integer> omPiwicId;
    
    public BaseController(boolean optimizeResources, String resourcesVersion) {
        this(optimizeResources, resourcesVersion, Maybe.<Integer>absent());
    }
    
    public BaseController(boolean optimizeResources, String resourcesVersion, Maybe<Integer> omPiwicId) {
        this.optimizeResources = optimizeResources;
        this.resourcesVersion = resourcesVersion;
        this.omPiwicId = omPiwicId;
        InfoRibbon.refreshInfoRibbonTexts();
    }

    static void addRequestMessage(RequestMessage requestMessage, Model model, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        addListElement(flashMap, REQUEST_MESSAGES_KEY, requestMessage);
        if (model != null) {
            addListElement(model.asMap(), REQUEST_MESSAGES_KEY, requestMessage);
        }
    }

    protected void addRequestAttribute(String attributeValue, Model model, HttpServletRequest request) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(REQUEST_ATTRIBUTE_KEY, attributeValue);
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
    
    

    @SuppressWarnings("unchecked")
    private List<RequestMessage> getRequestMessages(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            return (List<RequestMessage>) flashMap.get(REQUEST_MESSAGES_KEY);
        } else {
            return Lists.newArrayList();
        }
    }

    protected String getRequestAttribute(HttpServletRequest request) {

        if (RequestContextUtils.getInputFlashMap(request) == null) {
            return null;
        }

        Object object = RequestContextUtils.getInputFlashMap(request).get(REQUEST_ATTRIBUTE_KEY);
        return object == null ? null : object.toString();
    }

    private <T extends Enum<?>> void addEnum(Class<T> enumType, Model model) {
        Map<String, T> values = Maps.newHashMap();
        for (T value : enumType.getEnumConstants()) {
            values.put(value.name(), value);
        }
        model.addAttribute(enumType.getSimpleName(), values);
    }

    protected void addPiwicIdIfNotAuthenticated(Model model) {
//        boolean isAuthenticated = userService.getCurrentUser(false).isAuthenticated();
//        if (!isAuthenticated) {
//            model.addAttribute(OM_PICIW_ID, omPiwicId.orNull());
//        }
    }


    @Deprecated
    /**
     *  XXX: Bad code.
     *  Not yet fully deprecated, but will be replaced with something nicer when new collect-view is implemented
     */
    protected void addModelAttributesToCollectView(Model model, InitiativeViewInfo municipalityInitiative, List<MunicipalityInfo> allMunicipalities, ParticipantCount participantCount, Participants participants) {
        model.addAttribute("initiative", municipalityInitiative);
        model.addAttribute("municipalities", allMunicipalities);
        model.addAttribute("participantCount", participantCount);
        model.addAttribute("participants", participants);
    }

    @ModelAttribute
    public void addModelDefaults(Locale locale, HttpServletRequest request, Model model) {
        Urls urls = Urls.get(locale);
        model.addAttribute("locale", urls.getLang());
        model.addAttribute("user", userService.getOptionalUser());
        model.addAttribute("altLocale", urls.getAltLang());
        model.addAttribute("urls", urls);
        model.addAttribute("fieldLabelKey", FieldLabelKeyMethod.INSTANCE);
        model.addAttribute(REQUEST_MESSAGES_KEY, getRequestMessages(request));
        model.addAttribute(REQUEST_ATTRIBUTE_KEY, getRequestAttribute(request));
        model.addAttribute("summaryMethod", SummaryMethod.INSTANCE);
        model.addAttribute("optimizeResources", optimizeResources);
        model.addAttribute("resourcesVersion", resourcesVersion);
        model.addAttribute(CURRENT_URI_ATTR, urls.getBaseUrl() + request.getRequestURI());
        model.addAttribute("infoRibbon", InfoRibbon.getInfoRibbonText(locale));

        try {
            model.addAttribute("UrlConstants", freemarkerObjectWrapper.getStaticModels().get(Urls.class.getName()));
            model.addAttribute("InitiativeConstants", freemarkerObjectWrapper.getStaticModels().get(InitiativeConstants.class.getName()));
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }

        addEnum(RequestMessage.class, model);
        addEnum(RequestMessageType.class, model);
        addEnum(HelpPage.class, model);
        addEnum(InfoPage.class, model);
        addEnum(InitiativeType.class, model);
        addEnum(InitiativeState.class, model);
    }

}
