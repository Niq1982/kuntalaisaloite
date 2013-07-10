package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.service.ui.AuthorService;
import fi.om.municipalityinitiative.service.ui.PublicInitiativeService;
import fi.om.municipalityinitiative.service.ui.VerifiedInitiativeService;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
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

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class PublicInitiativeController extends BaseController {

    @Resource
    private MunicipalityService municipalityService;

    @Resource
    private PublicInitiativeService publicInitiativeService;

    @Resource
    private ValidationService validationService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private AuthorService authorService;

    @Resource
    private VerifiedInitiativeService verifiedInitiativeService;

    public PublicInitiativeController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value={SEARCH_FI, SEARCH_SV}, method=GET)
    public String search(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {

        List<Municipality> municipalities = municipalityService.findAllMunicipalities(locale);
        LoginUserHolder loginUserHolder = new LoginUserHolder(userService.getUser(request));
        return ViewGenerator.searchView(publicInitiativeService.findMunicipalityInitiatives(search, loginUserHolder),
                municipalities,
                search,
                new SearchParameterQueryString(search),
                solveMunicipalityFromListById(municipalities, search.getMunicipality()),
                publicInitiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality()), loginUserHolder))
                .view(model, Urls.get(locale).alt().search());
    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=GET)
    public String view(@PathVariable("id") Long initiativeId,
                       Model model, Locale locale, HttpServletRequest request) {

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getInitiative(initiativeId, userService.getLoginUserHolder(request));

        if (initiativeInfo.isCollaborative()) {
            return ViewGenerator.collaborativeView(initiativeInfo,
                    authorService.findPublicAuthors(initiativeId),
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    new ParticipantUICreateDto(),
                    userService.hasManagementRightForInitiative(initiativeId, request),
                    new AuthorUIMessage()).view(model, Urls.get(locale).alt().view(initiativeId));
        }
        else {
            return ViewGenerator.singleView(initiativeInfo, authorService.findPublicAuthors(initiativeId))
                    .view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    @RequestMapping(value = { PREPARE_FI, PREPARE_SV }, method = GET)
    public String prepareGet(Model model, Locale locale, HttpServletRequest request) {
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

        if (InitiativeType.isVerifiable(initiative.getInitiativeType())) {
            LoginUserHolder<User> loginUserHolder = userService.getLoginUserHolder(request);
            if (loginUserHolder.isVerifiedUser()) {
                long initiativeId;
                try {
                    initiativeId = verifiedInitiativeService.prepareSafeInitiative(loginUserHolder, PrepareSafeInitiativeUICreateDto.parse(initiative));
                } catch (InvalidHomeMunicipalityException e) {
                    return redirectWithMessage(urls.prepare(), RequestMessage.INVALID_HOME_MUNICIPALITY, request);
                }
                userService.refreshUserData(request);
                return contextRelativeRedirect(urls.management(initiativeId));
            }
            else {
                userService.putPrepareDataForVetuma(initiative, request);
                return contextRelativeRedirect(urls.login());
            }
        }
        else {

            if (validationService.validationErrors(initiative, bindingResult, model)) {
                return ViewGenerator.prepareView(initiative, municipalityService.findAllMunicipalities(locale))
                        .view(model, urls.prepare());
            }

            Long initiativeId = publicInitiativeService.prepareInitiative(initiative, locale);

            addRequestAttribute(initiative.getParticipantEmail(), request); // To be shown at confirmation page
            return redirectWithMessage(urls.pendingConfirmation(initiativeId), RequestMessage.PREPARE, request);
        }

    }

    @RequestMapping(value={ VIEW_FI, VIEW_SV }, method=POST)
    public String participate(@PathVariable("id") Long initiativeId,
                              @ModelAttribute("participant") ParticipantUICreateDto participant,
                              BindingResult bindingResult, Model model, Locale locale, HttpServletRequest request) {

        if (validationService.validationSuccessful(participant, bindingResult, model)) {
            publicInitiativeService.createParticipant(participant, initiativeId, locale);
            Urls urls = Urls.get(locale);
            return redirectWithMessage(urls.view(initiativeId), RequestMessage.PARTICIPATE, request);
        }
        else {
            return ViewGenerator.collaborativeView(
                    publicInitiativeService.getPublicInitiative(initiativeId),
                    authorService.findPublicAuthors(initiativeId), municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    participant,
                    userService.hasManagementRightForInitiative(initiativeId, request),
                    new AuthorUIMessage()).view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    @RequestMapping(value={ PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV }, method=GET)
    public String participantList(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        String alternativeURL = urls.alt().view(initiativeId);

        InitiativeViewInfo initiativeInfo = publicInitiativeService.getPublicInitiative(initiativeId);

        if (!initiativeInfo.isCollaborative()) {
            throw new NotFoundException("Initiative is not collaborative",initiativeId);
        }
        else {

            String previousPageURI = urls.management(initiativeId).equals(request.getHeader("referer"))
                    ? urls.management(initiativeId)
                    : urls.view(initiativeId);

            return ViewGenerator.participantList(initiativeInfo,
                    participantService.getParticipantCount(initiativeId),
                    participantService.findPublicParticipants(initiativeId),
                    previousPageURI,
                    userService.hasManagementRightForInitiative(initiativeId, request)
            ).view(model, alternativeURL);
        }
    }

    @RequestMapping(value = {PARTICIPATING_CONFIRMATION_FI, PARTICIPATING_CONFIRMATION_SV}, method = GET)
    public String confirmParticipation(@PathVariable("id") Long participantId,
                                       @RequestParam(PARAM_CONFIRMATION_CODE) String confirmationCode,
                                       Locale locale,
                                       HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        Long initiativeId = publicInitiativeService.confirmParticipation(participantId, confirmationCode);
        return redirectWithMessage(urls.view(initiativeId), RequestMessage.CONFIRM_PARTICIPATION, request);
    }

    @RequestMapping(value={ PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV }, method=GET)
    public String pendingConfirmation(@PathVariable("id") Long initiativeId, Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);

        model.addAttribute("managementHash", RandomHashGenerator.getPrevious()); // TODO: Remove after login-link is removed from the page

        if (getRequestAttribute(request) != null) {
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
                participantService.getParticipantCount(initiativeId),
                authorInvitationUIConfirmDto
        ).view(model, Urls.get(locale).alt().getManagement(initiativeId));
    }

    @RequestMapping(value={ INVITATION_FI, INVITATION_SV }, method=POST, params = ACTION_ACCEPT_INVITATION)
    public String invitationAccept(@PathVariable("id") Long initiativeId,
                                   @ModelAttribute("authorInvitation") AuthorInvitationUIConfirmDto confirmDto,
                                   Model model, BindingResult bindingResult, Locale locale, HttpServletRequest request) {

        InitiativeViewInfo initiativeInfo = authorService.getAuthorInvitationConfirmData(initiativeId, confirmDto.getConfirmCode(), userService.getLoginUserHolder(request)).initiativeViewInfo;
        confirmDto.assignInitiativeMunicipality(initiativeInfo.getMunicipality().getId());

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

            if (validationService.validationSuccessful(confirmDto, bindingResult, model)) {
                String generatedManagementHash = authorService.confirmAuthorInvitation(initiativeId, confirmDto, locale);
                userService.authorLogin(generatedManagementHash, request);
                return redirectWithMessage(Urls.get(locale).management(initiativeId), RequestMessage.CONFIRM_INVITATION_ACCEPTED, request);
            }
            else {
                return ViewGenerator.invitationView(initiativeInfo,
                        municipalityService.findAllMunicipalities(locale),
                        authorService.findPublicAuthors(initiativeId),
                        participantService.getParticipantCount(initiativeId),
                        confirmDto
                ).view(model, Urls.get(locale).alt().manageAuthors(initiativeId));
            }
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
            publicInitiativeService.addAuthorMessage(authorUIMessage, locale);
            return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.AUTHOR_MESSAGE_ADDED, request);
        }
        else {
            return ViewGenerator.collaborativeView(publicInitiativeService.getPublicInitiative(initiativeId),
                    authorService.findPublicAuthors(initiativeId),
                    municipalityService.findAllMunicipalities(locale),
                    participantService.getParticipantCount(initiativeId),
                    new ParticipantUICreateDto(),
                    userService.hasManagementRightForInitiative(initiativeId, request),
                    authorUIMessage).view(model, Urls.get(locale).alt().view(initiativeId));
        }
    }

    @RequestMapping(value = {AUTHOR_MESSAGE_FI, AUTHOR_MESSAGE_SV}, method = GET)
    public String confirmAuthorMessage(@RequestParam(PARAM_CONFIRMATION_CODE) String confirmationCode,
                                       HttpServletRequest request, Locale locale) {
        Long initiativeId = publicInitiativeService.confirmAndSendAuthorMessage(confirmationCode);
        return redirectWithMessage(Urls.get(locale).view(initiativeId), RequestMessage.AUTHOR_MESSAGE_SENT, request);
    }

    @RequestMapping(value={IFRAME_FI, IFRAME_SV}, method=GET)
    public String iframe(InitiativeSearch search, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().search());

        List<Municipality> municipalities = municipalityService.findAllMunicipalities(locale);

        search.setShow(InitiativeSearch.Show.all);

        LoginUserHolder loginUserHolder = new LoginUserHolder(User.anonym());
        return ViewGenerator.iframeSearch(publicInitiativeService.findMunicipalityInitiatives(search, loginUserHolder),
                municipalities,
                search,
                new SearchParameterQueryString(search),
                solveMunicipalityFromListById(municipalities, search.getMunicipality()),
                publicInitiativeService.getInitiativeCounts(Maybe.fromNullable(search.getMunicipality()), loginUserHolder)
        ).view(model, urls.alt().search());
    }

    @RequestMapping(value={IFRAME_GENERATOR_FI, IFRAME_GENERATOR_SV}, method=GET)
    public String iframeGenerator(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);
        model.addAttribute(ALT_URI_ATTR, urls.alt().search());

        List<Municipality> municipalities = municipalityService.findAllMunicipalities(locale);

        return ViewGenerator.iframeGenerator(municipalities).view(model, urls.alt().iframeGenerator());
    }

    private static Maybe<Municipality> solveMunicipalityFromListById(List<Municipality> municipalities, Long municipalityId){
        for (Municipality municipality : municipalities) {
            if (municipality.getId().equals(municipalityId))
                return Maybe.of(municipality);
        }
        return Maybe.absent();
    }
}
