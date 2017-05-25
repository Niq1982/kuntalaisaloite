package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.InfoTextSubject;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.service.FileImageFinder;
import fi.om.municipalityinitiative.service.ImageFinder;
import fi.om.municipalityinitiative.service.InfoTextService;
import fi.om.municipalityinitiative.util.InfoTextCategory;
import fi.om.municipalityinitiative.web.HelpPage;
import fi.om.municipalityinitiative.web.RequestMessage;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static fi.om.municipalityinitiative.web.Urls.*;
import static fi.om.municipalityinitiative.web.Views.*;
import static fi.om.municipalityinitiative.web.WebConstants.JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class InfoTextController extends BaseController {

    @Resource
    public InfoTextService infoTextService;

    @Resource
    ImageFinder imageFinder;

    public InfoTextController(boolean optimizeResources, String resourcesVersion, Optional<Integer> omPiwicId) {
        super(optimizeResources, resourcesVersion, omPiwicId);
    }

    @RequestMapping(value={ HELP_INDEX_FI, HELP_INDEX_SV }, method=GET)
    public String help(Model model, Locale locale, HttpServletRequest request) {

        Urls urls = Urls.get(locale);
        Map<String, List<InfoTextSubject>> publicSubjectList = infoTextService.getPublicSubjectList(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().help(""));

        model.addAttribute("categoryLinksMap", publicSubjectList);

        InfoTextSubject firstUnderMain = publicSubjectList.get("MAIN").get(0);

        if (firstUnderMain != null) {
            model.addAttribute("content", infoTextService.getPublished(firstUnderMain.getUri()));
            model.addAttribute("helpPage", firstUnderMain.getUri());
        }
        else {
            model.addAttribute("content", null);
            model.addAttribute("helpPage", "");
        }

        model.addAttribute("omUser", userService.getUser(request).isOmUser());

        addPiwicIdIfNotAuthenticated(model, request);

        return HELP_VIEW;

    }

    private String getUriForFirstSubject(Locale locale) {
        Map<String,List<InfoTextSubject>> allSubjects = infoTextService.getPublicSubjectList(locale);
        return allSubjects.get(InfoTextCategory.MAIN.name()).get(0).getUri();
    }

    @RequestMapping(value={ HELP_FI, HELP_SV }, method=GET)
    public String help(@PathVariable("helpPage") String localizedPageName, Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().help(""));
        model.addAttribute("helpPage", localizedPageName);
        model.addAttribute("categoryLinksMap", infoTextService.getPublicSubjectList(locale));
        model.addAttribute("content", infoTextService.getPublished(localizedPageName));

        model.addAttribute("omUser", userService.getUser(request).isOmUser());

        addPiwicIdIfNotAuthenticated(model, request);

        return HELP_VIEW;
    }

    @RequestMapping(value = { NEWS_FI, NEWS_SV }, method=GET)
    public String news(Model model, Locale locale, HttpServletRequest request) {
        Urls urls = Urls.get(locale);

        String pageUri = HelpPage.NEWS.getUri(locale.toLanguageTag());

        model.addAttribute(ALT_URI_ATTR, urls.alt().news());
        model.addAttribute("content", infoTextService.getPublished(pageUri));
        model.addAttribute("pageUri", pageUri);
        model.addAttribute("omUser", userService.getUser(request).isOmUser());

        addPiwicIdIfNotAuthenticated(model, request);

        return NEWS_VIEW;
    }

    @RequestMapping(value = {HELP_EDIT_INDEX_FI, HELP_EDIT_INDEX_SV}, method = GET)
    public String helpEdit(Model model, Locale locale, HttpServletRequest request) {
        return helpEdit(getUriForFirstSubject(locale), model, locale, request);
    }

    @RequestMapping(value={ HELP_EDIT_FI, HELP_EDIT_SV }, method=GET)
    public String helpEdit(@PathVariable("helpPage") String localizedPageName, Model model, Locale locale, HttpServletRequest request) {

        OmLoginUserHolder requiredOmLoginUserHolder = userService.getRequiredOmLoginUserHolder(request);

        Urls urls = Urls.get(locale);

        model.addAttribute(ALT_URI_ATTR, urls.alt().help(""));
        model.addAttribute("helpPage", localizedPageName);
        model.addAttribute("categoryLinksMap", infoTextService.getOmSubjectList(locale, requiredOmLoginUserHolder));
        model.addAttribute("content", infoTextService.getDraft(localizedPageName, requiredOmLoginUserHolder));
        model.addAttribute("urls", urls);

        return HELP_EDIT_VIEW;
    }

    @RequestMapping(value={ HELP_EDIT_FI, HELP_EDIT_SV }, method=POST, params=ACTION_EDITOR_SAVE_DRAFT)
    public String helpEdit(@PathVariable("helpPage") String localizedPageName,
                           @RequestParam(value = "content", required = true) String content,
                           @RequestParam(value = "subject", required = true) String subject,
                           Model model, Locale locale, HttpServletRequest request) {

        infoTextService.updateDraft(userService.getRequiredOmLoginUserHolder(request), localizedPageName, content, subject);

        return redirectWithMessage(Urls.get(locale).helpEdit(localizedPageName), RequestMessage.EDITOR_SAVE_DRAFT, request);
    }

    @RequestMapping(value={ HELP_EDIT_FI, HELP_EDIT_SV }, method=POST, params=ACTION_EDITOR_PUBLISH_DRAFT)
    public String publishDraft(@PathVariable("helpPage") String localizedPageName, Locale locale, HttpServletRequest request) {

        infoTextService.publishDraft(localizedPageName, userService.getRequiredOmLoginUserHolder(request));

        return redirectWithMessage(Urls.get(locale).helpEdit(localizedPageName), RequestMessage.EDITOR_PUBLISH_DRAFT, request);
    }


    @RequestMapping(value={ HELP_EDIT_FI, HELP_EDIT_SV }, method=POST, params=ACTION_EDITOR_RESTORE_PUBLISHED)
    public String restoreDraftFromPublished(@PathVariable("helpPage") String localizedPageName, Locale locale, HttpServletRequest request) {
        infoTextService.restoreDraftFromPublished(localizedPageName, userService.getRequiredOmLoginUserHolder(request));
        return redirectWithMessage(Urls.get(locale).helpEdit(localizedPageName), RequestMessage.EDITOR_RESTORE_PUBLISHED, request);
    }

    @RequestMapping(value={ CONTENT_EDITOR_HELP }, method=GET)
    public String contentEditorHelp() {
        return CONTENT_EDITOR_HELP_VIEW;
    }

    @RequestMapping(value = IMAGE_JSON, produces = JSON)
    public @ResponseBody List<FileImageFinder.FileJson> files() {
        return imageFinder.getImages();
    }

    @RequestMapping(value = IMAGES +"/{fileName}.{fileType}")
    public void getImage(@PathVariable String fileName,
                         @PathVariable String fileType,
                         @RequestParam(required = false) String version,
                         HttpServletResponse response) throws IOException {

        FileImageFinder.FileInfo file = imageFinder.getFile(fileName + "." + fileType, version);

        response.setContentType(MediaType.parseMediaType("image/" + fileType).toString());
        response.setContentLength(file.bytes.length);
        response.setHeader("Last-Modified", file.modifyTime);
        response.setHeader("Cache-Control", "public, max-age=3153600");
        response.getOutputStream().write(file.bytes);
    }

    @RequestMapping(value = IMAGES, method = POST)
    public String addImage(@RequestParam("image") MultipartFile file, DefaultMultipartHttpServletRequest request, Locale locale) throws IOException {

        // NOTE: Checking user rights and CSRF-Token needs to be done here because HttpUserService gains
        // NOTE org.eclipse.jetty.server.Request which is not able to handle parameters at multipartrequests

        userService.getRequiredLoginUserHolder(request);
//        User currentUser = userService.getUser(request); // TODO: Hmm.. Kansalaisalotteen puolella tässä checkattiin csrf-tokeni?
//        if (currentUser.isNotOmUser()) {
//            throw new AccessDeniedException("Om rights required");
//        }

        // userService.verifyCSRFToken(request);

        imageFinder.validateAndSaveFile(file);
        return redirectWithMessage(request.getHeader("Referer"), RequestMessage.EDITOR_UPLOAD_IMAGE, request);
    }
}
