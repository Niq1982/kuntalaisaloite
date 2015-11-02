package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.service.MunicipalityDecisionService;
import fi.om.municipalityinitiative.service.ui.AuthorService;
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
    private AuthorService authorService;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private MunicipalityDecisionService municipalityDecisionService;




    public MunicipalityDecisionController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = {MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV}, method = GET)
    public String municipalityModerationView(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();

        if (initiative.getDecisionDate().isPresent()) {
            decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(),initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
        }

        boolean ediDecision = decisionInfo.isNotPresent();
        boolean editAttachments = false;
        return ViewGenerator.municipalityDecisionView(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                normalInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                new MunicipalityDecisionDto(),
                decisionInfo,
                ediDecision,
                editAttachments
        ).view(model, Urls.get(locale).alt().municipalityModeration());

    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_FI, EDIT_MUNICIPALITY_DECISION_SV}, method = GET)
    public String editDecision(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();
        if (initiative.getDecisionDate().isPresent()) {
            decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(), initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
        }

        boolean ediDecision = true;
        boolean editAttachments = false;
        return ViewGenerator.municipalityDecisionView(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                normalInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                MunicipalityDecisionDto.build(initiative.getDecisionText().getValue()),
                decisionInfo,
                ediDecision,
                editAttachments
        ).view(model, Urls.get(locale).alt().municipalityModeration());
    }

    @RequestMapping(value = {EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI, EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV}, method = GET)
    public String editDecisionAttachments(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);

        InitiativeViewInfo initiative =  normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();
        if (initiative.getDecisionDate().isPresent()) {
            decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(), initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
        }


        boolean ediDecision = false;
        boolean editAttachments = true;
        return ViewGenerator.municipalityDecisionView(
                normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                normalInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                MunicipalityDecisionDto.build(initiative.getDecisionText().getValue()),
                decisionInfo,
                ediDecision,
                editAttachments
        ).view(model, Urls.get(locale).alt().municipalityModeration());
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

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);
        if(!municipalityDecisionService.validationSuccessful(decision.getFiles(), bindingResult, model)) {
            boolean editAttachments = true;
            boolean showDecisionForm = false;

            InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);
            Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();
            if (initiative.getDecisionDate().isPresent()) {
                decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(), initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
            }

            return ViewGenerator.municipalityDecisionView(
                    normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                    normalInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                    decision,
                    decisionInfo,
                    showDecisionForm,
                    editAttachments
            ).view(model, Urls.get(locale).alt().municipalityModeration());
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

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);
        municipalityDecisionService.removeAttachmentFromDecision(attachmentId, loginUserHolder);
        return redirectWithMessage(Urls.get(locale).openDecisionAttachmentsForEdit(initiativeId), RequestMessage.ATTACHMENT_DELETED, request);
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

        MunicipalityUserHolder loginUserHolder = userService.getRequiredMunicipalityUserHolder(request);

        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);
        Maybe<MunicipalityDecisionInfo> decisionInfo = Maybe.absent();
        if (initiative.getDecisionDate().isPresent()) {
            decisionInfo = Maybe.of(MunicipalityDecisionInfo.build(initiative.getDecisionText(), initiative.getDecisionDate().getValue(), initiative.getDecisionModifiedDate(), municipalityDecisionService.getDecisionAttachments(initiativeId)));
        }

        if (!municipalityDecisionService.validationSuccessful(decision,decisionInfo.isPresent(), bindingResult, model)) {

            boolean editAttachments = false;
            boolean showDecisionForm = true;

            return ViewGenerator.municipalityDecisionView(
                    normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                    normalInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                    decision,
                    decisionInfo,
                    showDecisionForm,
                    editAttachments
            ).view(model, Urls.get(locale).alt().municipalityModeration());
        }

        try {
            municipalityDecisionService.setDecision(decision, initiativeId, loginUserHolder);

        } catch (InvalidAttachmentException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_INVALID, request);

        } catch (FileUploadException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.ATTACHMENT_FAILURE, request);
        }

        return redirectWithMessage(Urls.get(locale).getMunicipalityDecisionView(initiativeId), RequestMessage.DECISION_UPDATED, request);

    }

    // http://stackoverflow.com/questions/22391064/why-is-spring-mvc-inserting-an-empty-object-into-what-should-be-an-empty-list
    @InitBinder
    public void init(WebDataBinder binder) {
        binder.setBindEmptyMultipartFiles(false);
    }

}
