package fi.om.municipalityinitiative.web.controller;

import fi.om.municipalityinitiative.service.StatusService;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.InfoRibbon;
import fi.om.municipalityinitiative.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.web.Urls.STATUS;
import static fi.om.municipalityinitiative.web.Views.STATUS_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class StatusPageController extends BaseController {

    @Resource
    StatusService statusService;

    public StatusPageController(boolean optimizeResources, String resourcesVersion) {
        super(optimizeResources, resourcesVersion);
    }

    @RequestMapping(value=STATUS, method=GET)
    public String statusGet(Model model, @RequestParam(value="ribbon", required=false) String ribbon) {

        model.addAttribute("applicationInfoRows", statusService.getApplicationInfo());
        model.addAttribute("schemaVersionInfoRows", statusService.getSchemaVersionInfo());
        model.addAttribute("configurationInfoRows", statusService.getConfigurationInfo());
        model.addAttribute("configurationTestInfoRows", statusService.getConfigurationTestInfo());
        model.addAttribute("systemInfoRows", statusService.getSystemInfo());

        if ("refresh".equals(ribbon)) {
            InfoRibbon.refreshInfoRibbonTexts();
            model.addAttribute("infoRibbon", InfoRibbon.getInfoRibbonText(Locales.LOCALE_FI));
        }

        return STATUS_VIEW;
    }
}
