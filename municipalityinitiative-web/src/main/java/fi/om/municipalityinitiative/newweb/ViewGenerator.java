package fi.om.municipalityinitiative.newweb;

import com.apple.jobjc.appkit.NSMenuItem;
import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Views.SEARCH_VIEW;

public class ViewGenerator {

    private static final String ALT_URI_ATTR = "altUri";

    private final String viewName;
    private final Map<String, Object> modelAttributes;

    private ViewGenerator(String viewName, Map<String, Object> modelAttributes) {
        this.viewName = viewName;
        this.modelAttributes = modelAttributes;
    }

    public static ViewGenerator collaborativeView(InitiativeViewInfo municipalityInitiative,
                                                  List<Municipality> allMunicipalities,
                                                  ParticipantCount participantCount,
                                                  Participants participants,
                                                  ParticipantUICreateDto participantUICreateDto) {
        return new ViewGenerator(Views.PUBLIC_COLLECT_VIEW,
                new AttributeBuilder()
                        .add("initiative", municipalityInitiative)
                        .add("municipalities", allMunicipalities)
                        .add("participantCount", participantCount)
                        .add("participants", participants)
                        .add("participant", participantUICreateDto)
                        .build()
        );
    }

    public String view(Model model, String alternativeURL) {
        for (Map.Entry<String, Object> stringObjectEntry : modelAttributes.entrySet()) {
            model.addAttribute(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        model.addAttribute(ALT_URI_ATTR, alternativeURL);
        return viewName;
    }

    public static ViewGenerator singleView(InitiativeViewInfo initiativeInfo) {
        return new ViewGenerator(Views.PUBLIC_SINGLE_VIEW,new AttributeBuilder().add("initiative", initiativeInfo).build());
    }

    public static ViewGenerator searchView(List<InitiativeListInfo> initiatives,
                                           List<Municipality> municipalities,
                                           InitiativeSearch currentSearch,
                                           SearchParameterQueryString queryString,
                                           Maybe<Municipality> currentMunicipality,
                                           InitiativeCounts initiativeCounts) {
        return new ViewGenerator(SEARCH_VIEW,
                new AttributeBuilder()
                        .add("initiatives", initiatives)
                        .add("municipalities", municipalities)
                        .add("currentSearch", currentSearch)
                        .add("queryString", queryString)
                        .add("currentMunicipality", currentMunicipality)
                        .add("initiativeCounts", initiativeCounts)
                        .build()
        );
    }

    private static class AttributeBuilder {
        private Map<String, Object> attributes = Maps.newHashMap();

        public AttributeBuilder add(String name, Object value) {
            attributes.put(name, value);
            return this;
        }
        public Map<String, Object> build() {
            return attributes;
        }
    }
}
