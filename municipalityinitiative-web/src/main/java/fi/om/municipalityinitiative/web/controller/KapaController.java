package fi.om.municipalityinitiative.web.controller;


import fi.om.municipalityinitiative.dto.json.InitiativeListJson;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.HashCreator;
import fi.om.municipalityinitiative.service.KapaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class KapaController{

    private final Logger log = LoggerFactory.getLogger(KapaController.class);

    @Resource
    private KapaService kapaService;

    @Resource(name = "kapaHashCreator")
    private HashCreator hashCreator;

    @RequestMapping(value="/services/kapa/v1/initiatives/{ssn}", method=GET, produces=JSON)
    public @ResponseBody List<InitiativeListJson> getInitiatives(@PathVariable("ssn") String ssn,
                                            @RequestHeader(value="secure", required = true) String secure) {

        if (hashCreator.isHash(ssn, secure)) {

            return kapaService.findInitiativesForUser(ssn)
                    .stream()
                    .map(InitiativeListJson::new)
                    .collect(Collectors.toList());
        }
        else {
            log.warn("Checking given hash " + secure + " against " + hashCreator.hash(ssn) + " failed.");
            throw new AccessDeniedException();
        }

    }
}
