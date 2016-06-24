package fi.om.municipalityinitiative.web.controller;


import fi.om.municipalityinitiative.dto.json.InitiativeListJson;
import fi.om.municipalityinitiative.service.CachedInitiativeFinder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static fi.om.municipalityinitiative.web.WebConstants.JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class KapaController {

    @Resource
    private CachedInitiativeFinder initiativeFinder;

    @RequestMapping(value="/services/kapa/v1/initiatives/{ssn}", method=GET, produces=JSON)
    public @ResponseBody
    List<InitiativeListJson> getInitiatives(@PathVariable("ssn") String ssn,
                                            @RequestHeader(value="secure", required = true) String secure) {

        // TODO: Get real initiatives
        // TODO: Assert secure
        return initiativeFinder.frontPageInitiatives().stream()
                .map(InitiativeListJson::new)
                .collect(Collectors.toList());

    }
}
