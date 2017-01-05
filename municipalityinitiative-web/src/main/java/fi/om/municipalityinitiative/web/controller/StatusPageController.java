package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.service.StatusService;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.InfoRibbon;
import fi.om.municipalityinitiative.web.Urls;
import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.web.Urls.STATUS;
import static fi.om.municipalityinitiative.web.Views.STATUS_VIEW;
import static fi.om.municipalityinitiative.web.Views.contextRelativeRedirect;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class StatusPageController extends BaseController {

    @Resource
    StatusService statusService;

    @Resource
    AbstractReloadingMetadataProvider idpMetadataProvider;

    public StatusPageController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value=STATUS, method=GET)
    public String statusGet(Model model,
                            @RequestParam(value="saml", required = false) Boolean saml,
                            @RequestParam(value="ribbon", required=false) String ribbon,
                            @RequestParam(value = "emails", required = false) Long emailOffset) throws MetadataProviderException {

        model.addAttribute("applicationInfoRows", statusService.getApplicationInfo());
        model.addAttribute("schemaVersionInfoRows", statusService.getSchemaVersionInfo());
        model.addAttribute("configurationInfoRows", statusService.getConfigurationInfo());
        model.addAttribute("systemInfoRows", statusService.getSystemInfo());
        model.addAttribute("hardCodedUris", statusService.getInvalidHelpUris());

        List<EmailDto> failedEmails = statusService.findTriedNotSucceededEmails();
        model.addAttribute("hasFailedEmails", !failedEmails.isEmpty());

        model.addAttribute("untriedEmails", statusService.findUntriedEmails());

        if (emailOffset != null) {
            model.addAttribute("showEmails", emailOffset);
            model.addAttribute("notSucceededEmails", statusService.findNotSucceededEmails());
            model.addAttribute("succeededEmails", statusService.findSucceededEmails(emailOffset));
        }

        if ("refresh".equals(ribbon)) {
            InfoRibbon.refreshInfoRibbonTexts();
            idpMetadataProvider.refresh();
            model.addAttribute("infoRibbon", InfoRibbon.getInfoRibbonText(Locales.LOCALE_FI));
        }
        if (saml != null) {
            environmentSettings.enableSaml(saml);
        }

        return STATUS_VIEW;
    }

    @RequestMapping(value = STATUS, method = POST)
    public String statusPost(Model model, @RequestParam(value = "emails", required = false, defaultValue = "false") Boolean startEmails) {
        if (startEmails) {
            statusService.resendFailedEmailsAndContinueScheduledMailSender();
        }
        return contextRelativeRedirect(Urls.get(Locales.LOCALE_FI).getStatusPage() + "?emails=0");
    }
}
