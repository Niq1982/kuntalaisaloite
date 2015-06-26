package fi.om.municipalityinitiative.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.AttachmentFile;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.service.ui.AuthorService;
import fi.om.municipalityinitiative.service.ui.NormalInitiativeService;
import fi.om.municipalityinitiative.service.ui.PublicInitiativeService;
import fi.om.municipalityinitiative.service.ui.VerifiedInitiativeService;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
import fi.om.municipalityinitiative.web.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static fi.om.municipalityinitiative.web.WebConstants.JSON;

@Controller
public class PublicInitiativeController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private ValidationService validationService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private AuthorService authorService;

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private NormalInitiativeService normalInitiativeService;

    @Resource
    private VerifiedInitiativeService verifiedInitiativeService;

    @Resource
    private AttachmentService attachmentService;


    @Resource
    private SupportCountService supportCountService;

    private static final Logger log = LoggerFactory.getLogger(PublicInitiativeController.class);

    private ObjectMapper objectMapper = new ObjectMapper();


    public PublicInitiativeController(boolean optimizeResources, String resourcesVersion, Maybe<Integer> piwikId) {
        super(optimizeResources, resourcesVersion, piwikId);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = new LoginUserHolder<>(userService.getUser(request));
        SearchParameterQueryString queryString = new SearchParameterQueryString(search);

        addPiwicIdIfNotAuthenticated(model, request);

        InitiativeListPageInfo pageInfo = publicInitiativeService.getInitiativeListPageInfo(search, loginUserHolder, locale);

        return ViewGenerator.searchView(pageInfo,
                search,
                queryString,
                solveMunicipalityFromListById(pageInfo.municipalities, Maybe.fromNullable(search.getMunicipalities())))
                .view(model, Urls.get(locale).alt().search() + queryString.get());
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId,
                       Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getLoginUserHolder(request);

        addPiwicIdIfNotAuthenticated(model, request);

        InitiativePageInfo initiativePageView = publicInitiativeService.getInitiativePageDto(initiativeId, loginUserHolder);
            if (initiativePageView.isCollaborative()) {

            addVotingInfo(initiativeId, model);
            return ViewGenerator.collaborativeView(initiativePageView,
                    municipalityService.findAllMunicipalities(locale),
                    new ParticipantUICreateDto(),
                    new AuthorUIMessage()).view(model, Urls.get(locale).alt().view(initiativeId));
        }
        else {
            return ViewGenerator.singleView(initiativePageView).view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    private void addVotingInfo(Long initiativeId, Model model) {
        model.addAttribute("supportCountData", supportCountService.getSupportVotesPerDateJson(initiativeId));
    }

    @RequestMapping(value = { PREPARE_FI, PREPARE_SV }, method = GET)
    public String prepareGet(Model model, Locale locale, HttpServletRequest request) {

        addPiwicIdIfNotAuthenticated(model, request);

        return ViewGenerator.prepareView(
                new PrepareInitiativeUICreateDto(),
                municipalityService.findAllMunicipalities(locale)
        )
                .view(model, Urls.get(locale).alt().prepare());
    }

    @RequestMapping(value={ PREPARE_FI, PREPARE_SV }, method=POST)
    // XXX: Cyclomatic complexity too high.
    public String preparePost(@ModelAttribute("initiative") PrepareInitiativeUICreateDto initiative,
                              BindingResult bindingResult,
                              Model model,
                              Locale locale,
                              HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        if (validationService.validationErrors(initiative, bindingResult, model, solveValidationGroup(initiative.getInitiativeType()))) {
            return ViewGenerator.prepareView(initiative, municipalityService.findAllMunicipalities(locale))
                    .view(model, urls.alt().prepare());
        }

        if (InitiativeType.isVerifiable(initiative.getInitiativeType())) {
            LoginUserHolder loginUserHolder = userService.getLoginUserHolder(request);
            if (loginUserHolder.isVerifiedUser()) {
                long initiativeId;
                try {
                    initiativeId = verifiedInitiativeService.prepareVerifiedInitiative(loginUserHolder, PrepareSafeInitiativeUICreateDto.parse(initiative));
                } catch (InvalidHomeMunicipalityException e) {
                    return redirectWithMessage(urls.prepare(), RequestMessage.INVALID_HOME_MUNICIPALITY, request);
                }
                userService.refreshUserData(request);
                return contextRelativeRedirect(urls.management(initiativeId));
            }
            else {
                userService.putPrepareDataForVetuma(initiative, request);
                return contextRelativeRedirect(urls.vetumaLogin());
            }
        }
        else {

            Long initiativeId = normalInitiativeService.prepareInitiative(initiative, locale);

            addRequestAttribute(initiative.getParticipantEmail(), request); // To be shown at confirmation page
            return redirectWithMessage(urls.pendingConfirmation(initiativeId), RequestMessage.PREPARE, request);
        }

    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=POST)
    public String participate(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("participant") ParticipantUICreateDto participant,
                              BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {

        LoginUserHolder loginUserHolder = userService.getLoginUserHolder(request);
        InitiativePageInfo initiativePageInfo = publicInitiativeService.getInitiativePageInfo(initiativeId);
        participant.assignInitiativeMunicipality(initiativePageInfo.initiative.getMunicipality().getId());

        if (initiativePageInfo.isVerifiable()) {
            try {
                verifiedInitiativeService.createParticipant(participant, initiativeId, loginUserHolder);
                userService.refreshUserData(request);
            } catch (InvalidHomeMunicipalityException e) {
                return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.INVALID_HOME_MUNICIPALITY, request);
            }
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.PARTICIPATE_VERIFIABLE, request);
        }
        else {
            if (validationService.validationSuccessful(participant, bindingResult, model, NormalInitiative.class)) {
                participantService.createParticipant(participant, initiativeId, locale);
                Urls urls = Urls.get(locale);
                return redirectWithMessage(urls.view(initiativeId), RequestMessage.PARTICIPATE, request);
            } else {
                addVotingInfo(initiativeId, model);
                return ViewGenerator.collaborativeView(initiativePageInfo,
                        municipalityService.findAllMunicipalities(locale),
                        participant,
                        new AuthorUIMessage()).view(model, Urls.get(locale).alt().view(initiativeId));
            }
        }
    }

    @RequestMapping(value={ PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV }, method=GET)
    public String participantList(@PathVariable("id") Long initiativeId, @RequestParam(defaultValue = "0", value = "offset") int offset,
                                  Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        String alternativeURL = urls.alt().participantList(initiativeId);

        LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(request);
        ParticipantsPageInfo initiativeInfo = publicInitiativeService.getInitiativePageInfoWithParticipants(initiativeId, loginUserHolder, offset);

        addPiwicIdIfNotAuthenticated(model, request);

        if (!initiativeInfo.initiative.isCollaborative()) {
            throw new NotFoundException("Initiative is not collaborative",initiativeId);
        }
        else {
            String previousPageURI = urls.management(initiativeId).equals(request.getHeader("referer"))
                    ? urls.management(initiativeId)
                    : urls.view(initiativeId);

            return ViewGenerator.participantList(initiativeInfo,
                    previousPageURI,
                    loginUserHolder.hasManagementRightsForInitiative(initiativeId),
                    offset
            ).view(model, alternativeURL);
        }
    }

    @RequestMapping(value = {PARTICIPATING_CONFIRMATION_FI, PARTICIPATING_CONFIRMATION_SV}, method = GET)
    public String confirmParticipation(@PathVariable("id") Long participantId,
                                       @RequestParam(PARAM_CONFIRMATION_CODE) String confirmationCode,
                                       Locale locale,
                                       HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        Long initiativeId = participantService.confirmParticipation(participantId, confirmationCode);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.CONFIRM_PARTICIPATION, request);
    }

    @RequestMapping(value={ PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV }, method=GET)
    public String pendingConfirmation(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        if (getRequestAttribute(request) != null) {
            model.addAttribute(ALT_URI_ATTR, urls.alt().pendingConfirmation(initiativeId));
            return PENDING_CONFIRMATION;
        } else {
            return redirectWithMessage(urls.prepare(), RequestMessage.PREPARE_CONFIRM_EXPIRED, request);
        }
    }

    @RequestMapping(value={ INVITATION_FI, INVITATION_SV }, method=GET)
    public String invitationView(@PathVariable("id") Long initiativeId,
                                 @RequestParam(PARAM_INVITATION_CODE) String confirmCode,
                                 Model model, Locale locale, HttpServletRequest request) {


        AuthorService.AuthorInvitationConfirmViewData data = authorService.getAuthorInvitationConfirmData(initiativeId, confirmCode, userService.getLoginUserHolder(request));
        AuthorInvitationUIConfirmDto authorInvitationUIConfirmDto = data.authorInvitationUIConfirmDto;
        InitiativeViewInfo initiativeInfo = data.initiativeViewInfo;

        return ViewGenerator.invitationView(initiativeInfo,
                municipalityService.findAllMunicipalities(locale),
                authorService.findPublicAuthors(initiativeId),
                initiativeInfo.getParticipantCount(),
                authorInvitationUIConfirmDto
        ).view(model, Urls.get(locale).alt().invitation(initiativeId, confirmCode));
    }

    @RequestMapping(value={ INVITATION_FI, INVITATION_SV }, method=POST, params = ACTION_ACCEPT_INVITATION)
    public String invitationAccept(@PathVariable("id") Long initiativeId,
                                   @ModelAttribute("authorInvitation") AuthorInvitationUIConfirmDto confirmDto,
                                   Model model, BindingResult bindingResult, Locale locale, HttpServletRequest request) {

        InitiativeViewInfo initiativeInfo = authorService.getAuthorInvitationConfirmData(initiativeId, confirmDto.getConfirmCode(), userService.getLoginUserHolder(request)).initiativeViewInfo;
        confirmDto.assignInitiativeMunicipality(initiativeInfo.getMunicipality().getId());

        if (validationService.validationErrors(confirmDto, bindingResult, model, solveValidationGroup(initiativeInfo))) {
            return ViewGenerator.invitationView(initiativeInfo,
                    municipalityService.findAllMunicipalities(locale),
                    authorService.findPublicAuthors(initiativeId),
                    initiativeInfo.getParticipantCount(),
                    confirmDto
            ).view(model, Urls.get(locale).alt().invitation(initiativeId, confirmDto.getConfirmCode()));

        }

        if (initiativeInfo.isVerifiable()) {
            try {
                verifiedInitiativeService.confirmVerifiedAuthorInvitation(userService.getLoginUserHolder(request), initiativeId, confirmDto, locale);
                userService.refreshUserData(request);
                return redirectWithMessage(Urls.get(locale).management(initiativeId), RequestMessage.CONFIRM_INVITATION_ACCEPTED, request);
            } catch (InvalidHomeMunicipalityException e) {
                return redirectWithMessage(Urls.get(locale).invitation(initiativeId, confirmDto.getConfirmCode()), RequestMessage.INVALID_HOME_MUNICIPALITY, request);
            }
        }
        else {
            String generatedManagementHash = authorService.confirmAuthorInvitation(initiativeId, confirmDto, locale);
            userService.authorLogin(generatedManagementHash, request);
            return redirectWithMessage(Urls.get(locale).management(initiativeId), RequestMessage.CONFIRM_INVITATION_ACCEPTED, request);
        }
    }

    @RequestMapping(value={ INVITATION_FI, INVITATION_SV }, method=POST)
    public String invitationReject(@PathVariable("id") Long initiativeId,
                                   @RequestParam(PARAM_INVITATION_CODE) String confirmCode,
                                   Locale locale, HttpServletRequest request) {

        String initiativeName = authorService.getAuthorInvitationConfirmData(initiativeId, confirmCode, userService.getLoginUserHolder(request)).initiativeViewInfo.getName();

        authorService.rejectInvitation(initiativeId, confirmCode);

        addRequestAttribute(initiativeName, request); // To be shown at invitation rejected page
        return redirectWithMessage(Urls.get(locale).invitationRejected(initiativeId), RequestMessage.CONFIRM_INVITATION_REJECTED, request);
    }


    @RequestMapping(value={ INVITATION_REJECTED_FI, INVITATION_REJECTED_SV }, method=GET)
    public String invitationRejected(Model model, Locale locale, HttpServletRequest request) {

        if (getRequestAttribute(request) != null) {
            return INVITATION_REJECTED;
        } else {
            return contextRelativeRedirect(Urls.get(locale).frontpage());
        }
    }

    @RequestMapping(value = {VIEW_FI, VIEW_SV}, method = POST, params = ACTION_CONTACT_AUTHOR)
    public String addAuthorMessage(@PathVariable("id") Long initiativeId,
                                   @ModelAttribute("authorMessage") AuthorUIMessage authorUIMessage,
                                   Model model, BindingResult bindingResult, Locale locale, HttpServletRequest request) {

        authorUIMessage.setInitiativeId(initiativeId);
        if (validationService.validationSuccessful(authorUIMessage, bindingResult, model)) {
            normalInitiativeService.addAuthorMessage(authorUIMessage, locale);
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.AUTHOR_MESSAGE_ADDED, request);
        }
        else {
            addVotingInfo(initiativeId, model);
            return ViewGenerator.collaborativeView(publicInitiativeService.getInitiativePageInfo(initiativeId),
                    municipalityService.findAllMunicipalities(locale),
                    new ParticipantUICreateDto(),
                    authorUIMessage).view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    @RequestMapping(value = {AUTHOR_MESSAGE_FI, AUTHOR_MESSAGE_SV}, method = GET)
    public String confirmAuthorMessage(@RequestParam(PARAM_CONFIRMATION_CODE) String confirmationCode,
                                       HttpServletRequest request, Locale locale) {
        Long initiativeId = normalInitiativeService.confirmAndSendAuthorMessage(confirmationCode);
        return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.AUTHOR_MESSAGE_SENT, request);
    }

    @RequestMapping(value = Urls.ATTACHMENT)
    public void getImage(@PathVariable Long id,
                         @PathVariable String fileName,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            AttachmentFile attachment = attachmentService.getAttachment(id, fileName, userService.getLoginUserHolder(request));
            attachmentFileResponse(response, attachment);
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Throwable t) {
            log.error("Attachment not found: " + id + "," + fileName, t);
            throw new AccessDeniedException("Attachment not found: " + id + ", "+fileName);
        }
    }

    @RequestMapping(value = Urls.ATTACHMENT_THUMBNAIL)
    public void getThumbnail(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            AttachmentFile thumbnail = attachmentService.getThumbnail(id, userService.getLoginUserHolder(request));
            attachmentFileResponse(response, thumbnail);
        } catch (Throwable t) {
            log.error("Thumbnail not found: " + id, t);
            throw new AccessDeniedException("Thumbnail not found: " + id);
        }
    }

    @RequestMapping(value = SUPPORTS_BY_DATE, method=GET, produces=JSON)
    public @ResponseBody JsonNode jsonSupportsByDate(@PathVariable Long id) throws IOException {
        return objectMapper.readTree(supportCountService.getSupportVotesPerDateJson(id));
    }

    private static void attachmentFileResponse(HttpServletResponse response, AttachmentFile file) throws IOException {
        response.setContentType(MediaType.parseMediaType(file.getContentType()).toString());
        response.setContentLength(file.getBytes().length);
        response.setHeader("Last-Modified", file.getCreateTime().toString("E, dd MMM yyyy HH:mm:ss z"));
        response.setHeader("Cache-Control", "public, max-age=3153600");
        response.getOutputStream().write(file.getBytes());
    }

    private static Maybe<ArrayList<Municipality>> solveMunicipalityFromListById(List<Municipality> municipalities, Maybe<ArrayList<Long>> municipalityIds){
        if (municipalityIds.isNotPresent()) {
            return Maybe.absent();
        }
        ArrayList<Municipality> currentMunicipalities =  new ArrayList<Municipality>();
        for (Municipality municipality : municipalities) {
            if ((municipalityIds.get().contains(municipality.getId())))
                currentMunicipalities.add(municipality);
        }
        return Maybe.of(currentMunicipalities);
    }
}
