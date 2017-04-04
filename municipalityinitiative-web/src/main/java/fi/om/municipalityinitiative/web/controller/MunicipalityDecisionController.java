package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.service.MunicipalityDecisionService;
import fi.om.municipalityinitiative.service.ui.MunicipalityDecisionInfo;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SecurityFilter;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MunicipalityDecisionController extends BaseController{

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private MunicipalityDecisionService municipalityDecisionService;


    public MunicipalityDecisionController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = {MUNICIPALITY_LOGIN_FI, MUNICIPALITY_LOGIN_SV}, method = RequestMethod.GET, params = {PARAM_MANAGEMENT_CODE})
    public RedirectView municipalityLoginLinkCreation(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                                      Model model, Locale locale, HttpServletRequest request) {

        municipalityDecisionService.createAndSendMunicipalityLoginLink(managementHash);

        return new RedirectView(Urls.get(locale).municipalityLoginSent(), false, true, false);

    }

    @RequestMapping(value = {MUNICIPALITY_LOGIN_SENT_FI, MUNICIPALITY_LOGIN_SENT_SV})
    public String municipalityLoginSent() {
        return "municipality-login-sent";
    }

    @RequestMapping(value = {MUNICIPALITY_LOGIN_FI, MUNICIPALITY_LOGIN_SV}, method = RequestMethod.GET, params = {PARAM_MANAGEMENT_CODE, PARAM_MANAGEMENT_LOGIN_CODE})
    public RedirectView municipalityLoginPost(@RequestParam(PARAM_MANAGEMENT_CODE) String managementHash,
                                              @RequestParam(PARAM_MANAGEMENT_LOGIN_CODE) String managementLoginHash,
                                              Model model, Locale locale, HttpServletRequest request) {
        Long initiativeId = userService.municipalityUserLogin(managementHash, managementLoginHash, request);
        return new RedirectView(Urls.get(locale).getMunicipalityDecisionView(initiativeId), false, true, false);
    }



    @RequestMapping(value = {MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV}, method = GET)
    public String municipalityModerationView(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return showMunicipalityDecisionView(initiativeId, new MunicipalityDecisionDto(), model, locale, loginUserHolder, false, initiative.getDecisionDate().isNotPresent());

    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_FI, EDIT_MUNICIPALITY_DECISION_SV}, method = GET)
    public String editDecisionText(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return showMunicipalityDecisionView(initiativeId, MunicipalityDecisionDto.build(initiative.getDecisionText()), model, locale, loginUserHolder, false, true);
    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI, EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV}, method = GET)
    public String editDecisionAttachments(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        return showMunicipalityDecisionView(initiativeId, MunicipalityDecisionDto.build(initiative.getDecisionText()), model, locale, loginUserHolder, true, false);
    }

    @RequestMapping(value = {MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV}, method = POST)
    public String addDecision(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("decision") MunicipalityDecisionDto decision,
                              Model model,
                              BindingResult bindingResult,
                              DefaultMultipartHttpServletRequest request,
                              Locale locale) {

        // CSRF Must be validated here because SecurityFilter is not able to handle MultipartHttpServletRequest.
        SecurityFilter.verifyAndGetCurrentCSRFToken(request);

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);

        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        boolean updating = initiative.getDecisionDate().isPresent();
        if (!municipalityDecisionService.validationSuccessful(decision, updating, bindingResult, model)) {
            return showMunicipalityDecisionView(initiativeId,  decision,  model,  locale,  loginUserHolder,  false,  true);
        }

        try {
            municipalityDecisionService.setDecision(decision, initiativeId, loginUserHolder, locale);

        } catch (InvalidAttachmentException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_INVALID, request);

        } catch (FileUploadException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_FAILURE, request);
        }
        if (updating) {
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.DECISION_UPDATED, request);
        } else{
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.DECISION_ADDED, request);

        }
    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI, EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV}, method = POST)
    public String editDecisionAttachments(@PathVariable("id") Long initiativeId,
                                          @ModelAttribute("decision") MunicipalityDecisionDto decision,
                                          Model model,
                                          BindingResult bindingResult,
                                          Locale locale,
                                          DefaultMultipartHttpServletRequest request) {

        // CSRF Must be validated here because SecurityFilter is not able to handle MultipartHttpServletRequest.
        SecurityFilter.verifyAndGetCurrentCSRFToken(request);

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);

        decision.setFiles(municipalityDecisionService.clearEmptyFiles(decision.getFiles()));

        if(!municipalityDecisionService.validationSuccessful(decision.getFiles(), bindingResult, model)) {

            return showMunicipalityDecisionView(initiativeId, decision, model, locale, loginUserHolder, true, false);
        }
        try {
            municipalityDecisionService.updateAttachments(initiativeId, decision.getFiles(), loginUserHolder);
        } catch (FileUploadException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_FAILURE, request);
        } catch (InvalidAttachmentException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_INVALID, request);
        }

        return redirectWithMessage(Urls.get(locale).openDecisionAttachmentsForEdit(initiativeId), RequestMessage.ATTACHMENT_ADDED, request);
    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI, EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV}, method = POST, params = ACTION_DELETE_ATTACHMENT)
    public String deleteAttachment(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_ATTACHMENT_ID) Long attachmentId,
                                   HttpServletRequest request,
                                   Locale locale) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request, initiativeId);
        municipalityDecisionService.removeAttachmentFromDecision(attachmentId, loginUserHolder);
        return redirectWithMessage(Urls.get(locale).openDecisionAttachmentsForEdit(initiativeId), RequestMessage.ATTACHMENT_DELETED, request);
    }



    // http://stackoverflow.com/questions/22391064/why-is-spring-mvc-inserting-an-empty-object-into-what-should-be-an-empty-list
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.setBindEmptyMultipartFiles(false);
    }



    private String showMunicipalityDecisionView(Long initiativeId,  MunicipalityDecisionDto decision, Model model, Locale locale, MunicipalityUserHolder loginUserHolder, boolean editAttachments, boolean showDecisionForm) {
        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);
        Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();
        if (initiative.getDecisionDate().isPresent()) {
            decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(), initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
        }

        return ViewGenerator.municipalityDecisionView(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                normalInitiativeService.getManagementSettings(initiativeId),
                decision,
                decisionInfo,
                showDecisionForm,
                editAttachments
        ).view(model, Urls.get(locale).alt().getMunicipalityDecisionView(initiativeId));
    }

}
