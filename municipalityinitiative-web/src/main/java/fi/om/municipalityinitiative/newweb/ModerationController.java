package fi.om.municipalityinitiative.newweb;

import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.BaseController;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ModerationController extends BaseController{

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private ModerationService moderationService;

    @Resource
    private UserService userService;

    @Resource
    private ValidationService validationService;

    public ModerationController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MODERATION_FI, MODERATION_SV }, method=GET)
    public String moderationView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        userService.getRequiredOmLoginUserHolder(request);

        return ViewGenerator.moderationView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                publicInitiativeService.getManagementSettings(initiativeId),
                moderationService.findAuthors(userService.getRequiredOmLoginUserHolder(request), initiativeId)
        ).view(model, Urls.get(locale).alt().moderation(initiativeId));
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_ACCEPT_INITIATIVE)
    public String acceptInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam("moderatorComment") String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.accept(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment, locale);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.ACCEPT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_REJECT_INITIATIVE)
    public String rejectInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam("moderatorComment") String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.reject(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.REJECT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_SEND_TO_FIX)
    public String sendInitiativeForFix(@PathVariable("id") Long initiativeId,
                                   @RequestParam("moderatorComment") String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.sendInitiativeBackForFixing(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.REJECT_INITIATIVE, request);
    }



    @RequestMapping(value = MUNICIPALITY_MODERATION, method = GET)
    public String moderateMunicipalities(Model model, HttpServletRequest request){

        return ViewGenerator.municipalityModarationView(moderationService.findMunicipalitiesForEdit(userService.getRequiredOmLoginUserHolder(request)),
                new MunicipalityUIEditDto())
                .view(model, Urls.get(Locales.LOCALE_FI).municipalityModeration());

    }

    @RequestMapping(value= MUNICIPALITY_MODERATION, method = POST)
    public String updateMunicipality(@ModelAttribute("updateData") MunicipalityUIEditDto editDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (validationService.validationSuccessful(editDto, bindingResult, model)) {
            moderationService.updateMunicipality(userService.getRequiredOmLoginUserHolder(request), editDto);
            return redirectWithMessage(Urls.get(Locales.LOCALE_FI).municipalityModeration(), RequestMessage.INFORMATION_SAVED, request);
        }

        return ViewGenerator.municipalityModarationView(
                moderationService.findMunicipalitiesForEdit(userService.getRequiredOmLoginUserHolder(request)),
                editDto
        ).view(model, Urls.get(Locales.LOCALE_FI).municipalityModeration());

    }




}
