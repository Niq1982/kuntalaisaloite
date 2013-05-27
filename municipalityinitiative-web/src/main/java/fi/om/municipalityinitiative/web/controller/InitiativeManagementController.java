package fi.om.municipalityinitiative.web.controller;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.service.*;
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
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InitiativeManagementController extends BaseController {

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private InitiativeManagementService initiativeManagementService;

    @Resource
    private ValidationService validationService;

    @Resource
    private AuthorService authorService;
    
    @Resource ParticipantService participantService;

    public InitiativeManagementController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={ MANAGEMENT_FI, MANAGEMENT_SV }, method=GET)
    public String managementView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        if (hasNeverBeenSaved(initiativeInfo)) {
            return contextRelativeRedirect(Urls.get(locale).edit(initiativeId));
        }

        return ViewGenerator.managementView(initiativeInfo,
                publicInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                participantService.getParticipantCount(initiativeId)
        ).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }

    private static boolean hasNeverBeenSaved(InitiativeViewInfo initiativeInfo) {
        return Strings.isNullOrEmpty(initiativeInfo.getName());
    }

    @RequestMapping(value={ UPDATE_FI, UPDATE_SV }, method=GET)
    public String updateView(@PathVariable("id") Long initiativeId,
                             Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Urls urls = Urls.get(locale);
        ManagementSettings managementSettings = publicInitiativeService.getManagementSettings(initiativeId);

        if (managementSettings.isAllowUpdate()) {

            return ViewGenerator.updateView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    initiativeManagementService.getInitiativeForUpdate(initiativeId, loginUserHolder),
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    authorService.findAuthors(initiativeId, loginUserHolder),
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

        if (validationService.validationErrors(updateDto, bindingResult, model)) {

            return ViewGenerator.updateView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    updateDto,
                    initiativeManagementService.getAuthorInformation(initiativeId, loginUserHolder),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    urls.getManagement(initiativeId)
            ).view(model, urls.alt().update(initiativeId));
        }

        initiativeManagementService.updateInitiative(initiativeId, loginUserHolder, updateDto);
        return redirectWithMessage(urls.management(initiativeId), RequestMessage.UPDATE_INITIATIVE, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW)
    public String sendToReview(@PathVariable("id") Long initiativeId,
                               @RequestParam(PARAM_SENT_COMMENT) String sentComment,
                               Locale locale, HttpServletRequest request) {
        initiativeManagementService.sendReviewAndStraightToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), sentComment, locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_TO_REVIEW_COLLECT)
    public String sendToReviewForCollecting(@PathVariable("id") Long initiativeId,
                                            Locale locale, HttpServletRequest request) {
        initiativeManagementService.sendReviewOnlyForAcceptance(initiativeId, userService.getRequiredLoginUserHolder(request), locale);
        return redirectWithMessage(Urls.get(locale).management(initiativeId),RequestMessage.SEND_TO_REVIEW, request);
    }

    @RequestMapping(value = {MANAGEMENT_FI, MANAGEMENT_SV}, method = POST, params = ACTION_SEND_FIX_TO_REVIEW)
    public String sendFixToReview(@PathVariable("id") Long initiativeId,
                                  Locale locale, HttpServletRequest request) {
        LoginUserHolder requiredLoginUserHolder = userService.getRequiredLoginUserHolder(request);
        initiativeManagementService.sendFixToReview(initiativeId, requiredLoginUserHolder, locale);
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
                                     @RequestParam(PARAM_SENT_COMMENT) String sentComment,
                                     Locale locale, HttpServletRequest request) {
        initiativeManagementService.sendToMunicipality(initiativeId, userService.getRequiredLoginUserHolder(request), sentComment, locale);
        return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.PUBLISH_AND_SEND, request);
    }
    
    @RequestMapping(value={ MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV }, method=GET)
    public String manageAuthorsView(@PathVariable("id") Long initiativeId,
                                 Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(request);
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (initiativeInfo.isSent()) {
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.ALREADY_SENT, request);
        }

        return ViewGenerator.manageAuthorsView(initiativeInfo,
                publicInitiativeService.getManagementSettings(initiativeId),
                authorService.findAuthors(initiativeId, loginUserHolder),
                authorService.findAuthorInvitations(initiativeId, loginUserHolder),
                new AuthorInvitationUICreateDto()
        ).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }

    @RequestMapping(value={ PARITICIPANT_LIST_MANAGE_FI, PARITICIPANT_LIST_MANAGE_SV }, method=GET)
    public String participantListManage(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        String alternativeURL = urls.alt().view(initiativeId);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getMunicipalityInitiative(initiativeId);

        if (!initiativeInfo.isCollaborative()) {
            return ViewGenerator.singleView(initiativeInfo, authorService.findPublicAuthors(initiativeId)).view(model, alternativeURL);
        }
        else {

            String previousPageURI = urls.management(initiativeId).equals(request.getHeader("referer"))
                    ? urls.management(initiativeId)
                    : urls.view(initiativeId);

            return ViewGenerator.participantListManage(initiativeInfo,
                    participantService.getParticipantCount(initiativeId),
                    participantService.findAllParticipants(initiativeId, userService.getRequiredLoginUserHolder(request)),
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
            return ViewGenerator.manageAuthorsView(publicInitiativeService.getMunicipalityInitiative(initiativeId),
                    publicInitiativeService.getManagementSettings(initiativeId),
                    authorService.findAuthors(initiativeId, loginUserHolder),
                    authorService.findAuthorInvitations(initiativeId, loginUserHolder),
                    authorInvitationUICreateDto).view(model, Urls.get(locale).alt().getManagement(initiativeId));
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
    
    @RequestMapping(value = {PARITICIPANT_LIST_MANAGE_FI, PARITICIPANT_LIST_MANAGE_SV}, method = POST)
    public String deleteParticipant(@PathVariable("id") Long initiativeId,
                                     @RequestParam(PARAM_PARTICIPANT_ID) Long participantId,
                                     Locale locale, HttpServletRequest request) {
        
        participantService.deleteParticipant(initiativeId, userService.getRequiredLoginUserHolder(request), participantId);
        
        return redirectWithMessage(Urls.get(locale).participantListManage(initiativeId), RequestMessage.PARTICIPANT_DELETED, request);
    }

    
}
