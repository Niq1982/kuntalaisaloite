package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.LocationService;
import fi.om.municipalityinitiative.service.ValidationService;
import fi.om.municipalityinitiative.service.ui.ModerationService;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.service.ui.Notification;
import fi.om.municipalityinitiative.service.ui.NotificationEditDto;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.ReviewHistoryDiff;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static fi.om.municipalityinitiative.web.Urls.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ModerationController extends BaseController{

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private ModerationService moderationService;

    @Resource
    private ValidationService validationService;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private LocationService locationService;

    public ModerationController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MODERATION_FI, MODERATION_SV }, method=GET)
    public String moderationView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(value = HISTORY_ITEM_PARAMETER, required = false) Long historyItemId,
                                 Model model, Locale locale, HttpServletRequest request) {

        OmLoginUserHolder loginUserHolder = userService.getRequiredOmLoginUserHolder(request);

        List<ReviewHistoryRow> reviewHistory = moderationService.findReviewHistory(loginUserHolder, initiativeId);

        Optional<ReviewHistoryDiff> reviewHistoryDiff = Optional.empty();
        if (historyItemId != null) {
            reviewHistoryDiff = Optional.of(ReviewHistoryDiff.from(reviewHistory, historyItemId));
        }
        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return ViewGenerator.moderationView(initiative,
                normalInitiativeService.getManagementSettings(initiativeId),
                moderationService.findAuthors(loginUserHolder, initiativeId),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                reviewHistory,
                reviewHistoryDiff,
                locationService.getLocations(initiativeId),
                municipalityDecisionService.getMunicipalityDecisionInfoOptional(initiative)
        ).view(model, Urls.get(locale).alt().moderation(initiativeId));
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params= ACTION_MODERATOR_ADD_COMMENT)
    public String addModeratorComment(@PathVariable("id") Long initiativeId,
                                      @RequestParam(value = HISTORY_ITEM_PARAMETER, required = false) Long historyItemId,
                                      @RequestParam(value = ACTION_MODERATOR_ADD_COMMENT, required = true) String comment,
                                      Model model, Locale locale, HttpServletRequest request) {

        moderationService.addComment(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment);
        return moderationView(initiativeId, historyItemId, model, locale, request);

    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_ACCEPT_INITIATIVE)
    public String acceptInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_SENT_COMMENT) String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.accept(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment, locale);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.ACCEPT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_REJECT_INITIATIVE)
    public String rejectInitiative(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_SENT_COMMENT) String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.reject(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.REJECT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_SEND_TO_FIX)
    public String sendInitiativeForFix(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_SENT_COMMENT) String comment,
                                   Locale locale, HttpServletRequest request) {

        moderationService.sendInitiativeBackForFixing(userService.getRequiredOmLoginUserHolder(request), initiativeId, comment);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.REJECT_INITIATIVE, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST)
    public String generateNewManagementHash(@PathVariable("id") Long initiativeId,
                                            @RequestParam("authorId") Long authorId,
                                            Locale locale, HttpServletRequest request) {

        moderationService.renewManagementHash(userService.getRequiredOmLoginUserHolder(request), authorId);
        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.MANAGEMENT_HASH_RENEWED, request);
    }

    @RequestMapping(value = {MODERATION_FI, MODERATION_SV}, method = POST, params = ACTION_RENEW_MUNICIPALITY_MANAGEMENT_HASH)
    public String renewMunicipalityManagementHash(@PathVariable("id") Long initiativeId,
                                                  HttpServletRequest request,
                                                  Locale locale) {

        moderationService.renewMunicipalityManagementHash(userService.getRequiredOmLoginUserHolder(request), initiativeId, locale);

        return redirectWithMessage(Urls.get(locale).moderation(initiativeId), RequestMessage.MANAGEMENT_HASH_RENEWED, request);
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

    @RequestMapping(value = {NOTIFICATION_MODERATION_FI, NOTIFICATION_MODERATION_SV}, method = GET)
    public String notificationModeration(Model model, HttpServletRequest request) {

        return ViewGenerator.notificationModerationView(moderationService.getNotificationStatus(userService.getRequiredOmLoginUserHolder(request)))
                .view(model, Urls.get(Locales.LOCALE_FI).notificationModeration());
    }

    @RequestMapping(value = {NOTIFICATION_MODERATION_FI, NOTIFICATION_MODERATION_SV}, method = POST)
    public String notificationModerationSave(HttpServletRequest request,
                                             NotificationEditDto notificationEditDto) {
        moderationService.saveNotificationStatus(userService.getRequiredOmLoginUserHolder(request), notificationEditDto);
        return redirectWithMessage(Urls.get(Locales.LOCALE_FI).notificationModeration(), RequestMessage.INFORMATION_SAVED, request);
    }

    @RequestMapping(value = {"/generate-notification"}, method = GET)
    public String generateNotification(@ModelAttribute("notificationEdit") Notification notification, HttpServletRequest request) {
        userService.getRequiredOmLoginUserHolder(request);
        return "components/notification";
    }

    @RequestMapping(value = {"/fi/notification", "/sv/notification"}, method = GET)
    public String checkNotification(HttpServletRequest request) {
        return "components/notification";
    }

}
