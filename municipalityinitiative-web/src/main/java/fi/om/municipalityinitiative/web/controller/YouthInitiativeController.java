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
    public @ResponseBody Failable<YouthInitiativeService.YouthInitiativeCreateResult> createYouthInitiative(@RequestBody YouthInitiativeCreateDto youthInitiative) {

        try {
            return new Failable<>(youthInitiativeService.prepareYouthInitiative(youthInitiative));
        } catch (Throwable t) {
            return new Failable<>(t.getMessage());
        }

    }

    public class Failable<T> {

        public final String failure;
        public final T result;

        public Failable(T youthInitiativeCreateResult) {
            failure = null;
            result = youthInitiativeCreateResult;
        }

        public Failable(String error) {
            failure = error;
            result = null;
        }
    }
}
