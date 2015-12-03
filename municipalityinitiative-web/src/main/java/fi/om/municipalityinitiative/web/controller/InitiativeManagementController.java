package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.FileUploadException;
import fi.om.municipalityinitiative.exceptions.InvalidAttachmentException;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.service.ui.AuthorService;
import fi.om.municipalityinitiative.service.ui.InitiativeManagementService;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SecurityFilter;
import fi.om.municipalityinitiative.web.Urls;
import org.im4java.core.InfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.ERROR_404_VIEW;
import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeManagementController extends BaseController {


    @Resource
    private InitiativeManagementService initiativeManagementService;
    
    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private ValidationService validationService;

    @Resource
    private AuthorService authorService;
    
    @Resource
    ParticipantService participantService;

    @Resource
    AttachmentService attachmentService;

    @Resource
    LocationService locationService;

    @Resource
    VideoService videoService;

    private static final Logger log = LoggerFactory.getLogger(InitiativeManagementController.class);

    public InitiativeManagementController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value = {OWN_INITIATIVES_FI, OWN_INITIATIVES_SV}, method = GET)
    public String ownInitiatives(Model model, Locale locale, HttpServletRequest request) {
        LoginUserHolder loginUserHolder = userService.getLoginUserHolder(request);

        List<InitiativeListInfo> initiatives = initiativeManagementService.findOwnInitiatives(loginUserHolder);

        return ViewGenerator.ownInitiatives(initiatives).view(model,Urls.get(locale).alt().ownInitiatives());
    }

    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (initiativeInfo.hasNeverBeenSaved()) {
            return contextRelativeRedirect(Urls.get(locale).edit(initiativeId));
        }

        return ViewGenerator.managementView(initiativeInfo,
                normalInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                initiativeInfo.getParticipantCount(),
                locationService.getLocations(initiativeId),
                new CommentUIDto()
        ).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }
    
    @RequestMapping(value={ MANAGE_ATTACHMENTS_FI+ID_PARAMETER, MANAGE_ATTACHMENTS_SV+ID_PARAMETER }, method=GET)
    public String manageAttachmentsView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);

        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (initiativeInfo.hasNeverBeenSaved()) {
            return contextRelativeRedirect(Urls.get(locale).edit(initiativeId));
        }

        return ViewGenerator.manageAttachmentsView(initiativeInfo,
                normalInitiativeService.getManagementSettings(initiativeId),
                attachmentService.findAllAttachments(initiativeId, loginUserHolder),
                new AttachmentCreateDto(),
                AttachmentUtil.ImageProperties.instance()).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }
    
    @RequestMapping(value={ MANAGE_VIDEOS_FI+ID_PARAMETER, MANAGE_VIDEOS_SV+ID_PARAMETER }, method=GET)
    public String manageVideos(@PathVariable("id") Long initiativeId,
                                        Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);

        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (!environmentSettings.getVideoEnabled()) {
            throw new AccessDeniedException("Feature not enabled");
        }
        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (initiativeInfo.hasNeverBeenSaved()) {
            return contextRelativeRedirect(Urls.get(locale).edit(initiativeId));
        }

        return ViewGenerator.manageVideosView(initiativeInfo,
                normalInitiativeService.getManagementSettings(initiativeId),
                new VideoCreateDto(initiativeInfo.getVideoUrl(), initiativeInfo.getVideoName()),
                new VideoCreateDto())
                .view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }

    @RequestMapping(value= {MANAGE_VIDEOS_FI+ID_PARAMETER, MANAGE_VIDEOS_SV+ID_PARAMETER }, method = POST, params = ACTION_ADD_VIDEO)
    public String addVideo(@PathVariable("id") Long initiativeId, @ModelAttribute("video") VideoCreateDto video, BindingResult bindingResult,Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (!environmentSettings.getVideoEnabled()) {
            throw new AccessDeniedException("Feature not enabled");
        }
        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }
        if (!validationService.validationSuccessful(video, bindingResult, model)) {
            return ViewGenerator.manageVideosView(initiativeInfo,
                    normalInitiativeService.getManagementSettings(initiativeId),
                    video,
                    new VideoCreateDto())
                    .view(model, Urls.get(locale).alt().getManagement(initiativeId));
        }

        try {
            videoService.addVideoUrl(video, initiativeId);
        } catch (InvalidVideoUrlException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getManageVideoUrl(initiativeId), RequestMessage.VIDEO_FAILURE, request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return redirectWithMessage(Urls.get(locale).getManageVideoUrl(initiativeId), RequestMessage.VIDEO_FAILURE, request);
        }
        return redirectWithMessage(Urls.get(locale).getManageVideoUrl(initiativeId), RequestMessage.VIDEO_ADDED, request);

    }

    @RequestMapping(value= {MANAGE_VIDEOS_FI+ID_PARAMETER, MANAGE_VIDEOS_SV+ID_PARAMETER }, method = POST, params = ACTION_REMOVE_VIDEO)
    public String removeVideo(@PathVariable("id") Long initiativeId, @ModelAttribute("video") VideoCreateDto video,  Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (!environmentSettings.getVideoEnabled()) {
            throw new AccessDeniedException("Feature not enabled");
        }
        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        videoService.removeVideoUrl(initiativeId);
        return redirectWithMessage(Urls.get(locale).getManageVideoUrl(initiativeId), RequestMessage.VIDEO_DELETED, request);
    }



    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=GET)
    public String editView(@PathVariable("id") Long initiativeId,
                           Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        ManagementSettings managementSettings = normalInitiativeService.getManagementSettings(initiativeId);

        Urls urls = Urls.get(locale);
        if (managementSettings.isAllowEdit()) {
            InitiativeDraftUIEditDto initiativeDraft = initiativeManagementService.getInitiativeDraftForEdit(initiativeId, loginUserHolder);
            return ViewGenerator.editView(
                    normalInitiativeService.getInitiative(initiativeId, loginUserHolder),
                    Strings.isNullOrEmpty(initiativeDraft.getName()),
                    initiativeDraft,
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.management(initiativeId)
            ).view(model, urls.alt().edit(initiativeId));
        }
        else if (managementSettings.isAllowUpdate()) {
            return contextRelativeRedirect(urls.update(initiativeId));
        }
        else {
            return ERROR_500; // TODO: Custom error page or some message that operation is not allowed
        }
    }

    @RequestMapping(value={ EDIT_FI, EDIT_SV }, method=POST)
    public String editPost(@PathVariable("id") Long initiativeId,
                           @ModelAttribute("updateData") InitiativeDraftUIEditDto editDto,
                           BindingResult bindingResult,
                           Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiative = normalInitiativeService.getInitiative(initiativeId, loginUserHolder);

        if (validationService.validationErrors(editDto, bindingResult, model, solveValidationGroup(initiative))) {

            return ViewGenerator.editView(
                    initiative,
                    Strings.isNullOrEmpty(initiativeManagementService.getInitiativeDraftForEdit(initiativeId, loginUserHolder).getName()),
                    editDto,
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    urls.management(initiativeId)
            ).view(model, urls.alt().edit(initiativeId));
        }

        initiativeManagementService.editInitiativeDraft(initiativeId, loginUserHolder, editDto, locale);
        userService.refreshUserData(request);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.SAVE_DRAFT, request);
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=GET)
    public String updateView(@PathVariable("id") Long initiativeId,
                             Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = normalInitiativeService.getManagementSettings(initiativeId);

        if (managementSettings.isAllowUpdate()) {

            return ViewGenerator.updateView(
                    initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder),
                    initiativeManagementService.getInitiativeForUpdate(initiativeId, loginUserHolder),
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    locationService.getLocations(initiativeId),
                    urls.getManagement(initiativeId)
            ).view(model, urls.alt().update(initiativeId));

        } else {
            return ERROR_500; // TODO: Custom error page or some message that operation is not allowed
        }
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=POST)
    public String updatePost(@PathVariable("id") Long initiativeId,
                             @ModelAttribute("updateData") InitiativeUIUpdateDto updateDto,
                             BindingResult bindingResult,
                             Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        if (validationService.validationErrors(updateDto, bindingResult, model, solveValidationGroup(normalInitiativeService.getInitiative(initiativeId, loginUserHolder)))) {

            return ViewGenerator.updateView(initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder),
                    updateDto,
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    locationService.getLocations(initiativeId),
                    urls.getManagement(initiativeId)
            ).view(model, urls.alt().update(initiativeId));
        }

        initiativeManagementService.updateInitiative(initiativeId, loginUserHolder, updateDto);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.UPDATE_INITIATIVE, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReviewAndMunicipality(@PathVariable("id") Long initiativeId,
                                              @ModelAttribute("comment") CommentUIDto commentUIDto,
                                              BindingResult bindingResult,
                                              Model model,
                                              Locale locale, HttpServletRequest request) {
        if (validationService.validationErrors(commentUIDto, bindingResult, model)) {
            LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(request);
            InitiativeViewInfo municipalityInitiative = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);
            return ViewGenerator.managementView(municipalityInitiative,
                    normalInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    attachmentService.findAllAttachments(initiativeId, loginUserHolder), municipalityInitiative.getParticipantCount(), locationService.getLocations(initiativeId),
                    commentUIDto)
                    .view(model, Urls.get(locale).alt().moderation(initiativeId));
        }
        initiativeManagementService.sendReviewAndStraightToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), commentUIDto.getComment());
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            Locale locale, HttpServletRequest request) {
        initiativeManagementService.sendReviewWithUndefinedType(initiativeId, userService.getRequiredLoginUserHolder(request));
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_FIX_TO_REVIEW)
    public String sendFixToReview(@PathVariable("id") Long initiativeId,
                                  Locale locale, HttpServletRequest request) {
        LoginUserHolder requiredLoginUserHolder = userService.getRequiredLoginUserHolder(request);
        initiativeManagementService.sendFixToReview(initiativeId, requiredLoginUserHolder);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_START_COLLECTING)
    public String publishAndStartCollecting(@PathVariable("id") Long initiativeId,
                               Locale locale, HttpServletRequest request) {
        initiativeManagementService.publishAndStartCollecting(initiativeId, userService.getRequiredLoginUserHolder(request));
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.START_COLLECTING, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_MUNICIPALITY)
    public String sendToMunicipality(@PathVariable("id") Long initiativeId,
                                     @ModelAttribute("comment") CommentUIDto commentUIDto,
                                     BindingResult bindingResult,
                                     Model model,
                                     Locale locale, HttpServletRequest request) {
        if (validationService.validationErrors(commentUIDto, bindingResult, model)) {
            LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(request);
            InitiativeViewInfo municipalityInitiative = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);
            return ViewGenerator.managementView(municipalityInitiative,
                    normalInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    attachmentService.findAllAttachments(initiativeId, loginUserHolder), municipalityInitiative.getParticipantCount(),
                    locationService.getLocations(initiativeId),
                    commentUIDto)
                    .view(model, Urls.get(locale).alt().moderation(initiativeId));
        }

        initiativeManagementService.sendToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), commentUIDto.getComment(), locale);
        return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.PUBLISH_AND_SEND, request);
    }
    
    @RequestMapping(value={ MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV }, method=GET)
    public String manageAuthorsView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        return ViewGenerator.manageAuthorsView(initiativeInfo,
                normalInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                authorService.findAuthorInvitations(initiativeId, loginUserHolder),
                new AuthorInvitationUICreateDto()
        ).view(model, Urls.get(locale).alt().manageAuthors(initiativeId));
    }

    @RequestMapping(value={PARTICIPANT_LIST_MANAGE_FI, PARTICIPANT_LIST_MANAGE_SV}, method=GET)
    public String participantListManage(@PathVariable("id") Long initiativeId,
                                        @RequestParam(value = "offset", defaultValue = "0") int offset,
                                        Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        String alternativeURL = urls.alt().participantListManage(initiativeId);

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);

        InitiativeViewInfo initiativeInfo = initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder);

        if (!initiativeInfo.isCollaborative()) {
            return ERROR_404_VIEW;
        }
        else {

            String previousPageURI = urls.management(initiativeId).equals(request.getHeader("referer"))
                    ? urls.management(initiativeId)
                    : urls.view(initiativeId);

            return ViewGenerator.participantListManage(initiativeInfo,
                    initiativeInfo.getParticipantCount(),
                    participantService.findAllParticipants(initiativeId, loginUserHolder, offset),
                    offset,
                    previousPageURI
            ).view(model, alternativeURL);
        }
    }

    @RequestMapping(value = {MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV}, method = POST)
    public String inviteAuthor(@PathVariable("id") Long initiativeId,
                               @ModelAttribute("newInvitation") AuthorInvitationUICreateDto authorInvitationUICreateDto,
                               Model model,
                               Locale locale,
                               BindingResult bindingResult,
                               HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);

        if (validationService.validationSuccessful(authorInvitationUICreateDto, bindingResult, model)) {
            authorService.createAuthorInvitation(initiativeId, loginUserHolder, authorInvitationUICreateDto);
            return redirectWithMessage(Urls.get(locale).manageAuthors(initiativeId), RequestMessage.INVITATION_SENT, request);
        }
        else {
            return ViewGenerator.manageAuthorsView(initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder),
                    normalInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    authorService.findAuthorInvitations(initiativeId, loginUserHolder),
                    authorInvitationUICreateDto).view(model, Urls.get(locale).alt().manageAuthors(initiativeId));
        }
    }

    @RequestMapping(value = {MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV}, method = POST, params = PARAM_INVITATION_CODE)
    public String resendInvitation(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_INVITATION_CODE) String confirmationCode,
                                   Locale locale,
                                   HttpServletRequest request) {
            authorService.resendInvitation(initiativeId, userService.getRequiredLoginUserHolder(request), confirmationCode);
            return redirectWithMessage(Urls.get(locale).manageAuthors(initiativeId), RequestMessage.INVITATION_SENT, request);
    }
    
    @RequestMapping(value = {MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV}, method = POST, params = ACTION_DELETE_AUTHOR)
    public String deleteAuthor(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_AUTHOR_ID) Long authorId,
                               Model model,
                               Locale locale,
                               HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);

        authorService.deleteAuthor(initiativeId, loginUserHolder, authorId);
        return redirectWithMessage(Urls.get(locale).manageAuthors(initiativeId), RequestMessage.AUTHOR_DELETED, request);
    }
    
    @RequestMapping(value = {PARTICIPANT_LIST_MANAGE_FI, PARTICIPANT_LIST_MANAGE_SV}, method = POST)
    public String deleteParticipant(@PathVariable("id") Long initiativeId,
                                     @RequestParam(PARAM_PARTICIPANT_ID) Long participantId,
                                     Locale locale, HttpServletRequest request) {
        
        participantService.deleteParticipant(initiativeId, userService.getRequiredLoginUserHolder(request), participantId);
        
        return redirectWithMessage(Urls.get(locale).participantListManage(initiativeId), RequestMessage.PARTICIPANT_DELETED, request);
    }

    @RequestMapping(value = {MANAGE_ATTACHMENTS_FI+ID_PARAMETER, MANAGE_ATTACHMENTS_SV+ID_PARAMETER}, method = POST, params = ACTION_ADD_ATTACHMENT)
    public String addAttachment(@PathVariable("id") Long initiativeId,
                                @ModelAttribute("attachment") AttachmentCreateDto attachmentCreateDto,
                                Model model,
                                BindingResult bindingResult,
                                DefaultMultipartHttpServletRequest request,
                                Locale locale) throws IOException, InfoException {

        // CSRF Must be validated here because SecurityFilter is not able to handle MultipartHttpServletRequest.
        SecurityFilter.verifyAndGetCurrentCSRFToken(request);

        // TODO: Validate whole world.

        if (!attachmentService.validationSuccessful(initiativeId, attachmentCreateDto, bindingResult, model)) {
            LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(request);
            return ViewGenerator.manageAttachmentsView(
                    initiativeManagementService.getMunicipalityInitiative(initiativeId, loginUserHolder),
                    normalInitiativeService.getManagementSettings(initiativeId),
                    attachmentService.findAttachments(initiativeId, loginUserHolder),
                    attachmentCreateDto,
                    AttachmentUtil.ImageProperties.instance())
                    .view(model, Urls.get(locale).alt().manageAttachments(initiativeId));

        }

        try {
            attachmentService.addAttachment(initiativeId, userService.getLoginUserHolder(request), attachmentCreateDto.getImage(), attachmentCreateDto.getDescription());
        } catch (FileUploadException e) {
            log.warn("FileUploadException: ", e);
            return redirectWithMessage(Urls.get(locale).manageAttachments(initiativeId), RequestMessage.ATTACHMENT_FAILURE, request);
        } catch (InvalidAttachmentException e) {
            log.warn("InvalidAttachmentException: ", e);
            return redirectWithMessage(Urls.get(locale).manageAttachments(initiativeId), RequestMessage.ATTACHMENT_INVALID, request);
        }
        return redirectWithMessage(Urls.get(locale).manageAttachments(initiativeId), RequestMessage.ATTACHMENT_ADDED, request);
    }

    @RequestMapping(value = {MANAGE_ATTACHMENTS_FI+ID_PARAMETER, MANAGE_ATTACHMENTS_SV+ID_PARAMETER}, method = POST, params = ACTION_DELETE_ATTACHMENT)
    public String deleteAttachment(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_ATTACHMENT_ID) Long attachmentId,
                                   HttpServletRequest request,
                                   Locale locale) {

        // CSRF Must be validated here because SecurityFilter is not able to handle MultipartHttpServletRequest.
        SecurityFilter.verifyAndGetCurrentCSRFToken(request);
        attachmentService.deleteAttachment(attachmentId, userService.getLoginUserHolder(request));
        return redirectWithMessage(Urls.get(locale).manageAttachments(initiativeId), RequestMessage.ATTACHMENT_DELETED, request);
    }

    
}
