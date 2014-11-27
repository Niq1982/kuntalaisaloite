package fi.om.municipalityinitiative.web.controller;


import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.service.YouthInitiativeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class YouthInitiativeController {

    @Resource
    private YouthInitiativeService youthInitiativeService;

    @RequestMapping(value="/services/nua/1.0/create", method=RequestMethod.POST)
    public @ResponseBody YouthInitiativeService.YouthInitiativeCreateResult createYouthInitiative(@RequestBody YouthInitiativeCreateDto youthInitiative) {
        return youthInitiativeService.prepareYouthInitiative(youthInitiative);
    }
}
