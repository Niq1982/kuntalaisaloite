package fi.om.municipalityinitiative.newweb;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.*;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Views.*;

public class ViewGenerator {

    private static final String ALT_URI_ATTR = "altUri";

    private final String viewName;
    private final Map<String, Object> modelAttributes;

    private ViewGenerator(String viewName, Map<String, Object> modelAttributes) {
        this.viewName = viewName;
        this.modelAttributes = modelAttributes;
    }

    public String view(Model model, String alternativeURL) {
        for (Map.Entry<String, Object> stringObjectEntry : modelAttributes.entrySet()) {
            model.addAttribute(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        model.addAttribute(ALT_URI_ATTR, alternativeURL);
        return viewName;
    }

    public static ViewGenerator collaborativeView(InitiativeViewInfo municipalityInitiative,
                                                  PublicAuthors publicAuthors,
                                                  List<Municipality> allMunicipalities,
                                                  ParticipantCount participantCount,
                                                  ParticipantUICreateDto participantUICreateDto,
                                                  boolean hasManagementRightForInitiative) {
        return new ViewGenerator(Views.PUBLIC_COLLECT_VIEW,
                new AttributeBuilder()
                        .add("initiative", municipalityInitiative)
                        .add("authors", publicAuthors)
                        .add("municipalities", allMunicipalities)
                        .add("participantCount", participantCount)
                        .add("participant", participantUICreateDto)
                        .add("hasManagementRightForInitiative", hasManagementRightForInitiative)
                        .build()
        );
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

    public static ViewGenerator participantList(InitiativeViewInfo initiativeInfo,
                                                ParticipantCount participantCount,
                                                List<ParticipantListInfo> publicParticipants,
                                                String previousPageURI,
                                                boolean hasManagementRightForInitiative) {
        return new ViewGenerator(PARTICIPANT_LIST,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("participants", publicParticipants)
                        .add("participantCount", participantCount)
                        .add("previousPageURI", previousPageURI)
                        .add("hasManagementRightForInitiative", hasManagementRightForInitiative)
                        .build());
    }
    
    public static ViewGenerator participantListManage(InitiativeViewInfo initiativeInfo,
                                                      ParticipantCount participantCount,
                                                      List<ParticipantListInfo> publicParticipants,
                                                      String previousPageURI) {
        return new ViewGenerator(PARTICIPANT_LIST_MANAGE,
                new AttributeBuilder()
                .add("initiative", initiativeInfo)
                .add("participants", publicParticipants)
                .add("participantCount", participantCount)
                .add("previousPageURI", previousPageURI)
                .build());
    }

    public static ViewGenerator iframeSearch(List<InitiativeListInfo> initiatives,
                                             List<Municipality> municipalities,
                                             InitiativeSearch currentSearch,
                                             SearchParameterQueryString queryString,
                                             Maybe<Municipality> currentMunicipality,
                                             InitiativeCounts initiativeCounts) {
        return new ViewGenerator(IFRAME_VIEW,
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
    
    public static ViewGenerator iframeGenerator(List<Municipality> municipalities) {
        return new ViewGenerator(IFRAME_GENERATOR_VIEW,
                new AttributeBuilder()
                        .add("municipalities", municipalities)
                        .build());
    }

    public static ViewGenerator singleView(InitiativeViewInfo initiativeInfo, PublicAuthors publicAuthors) {
        return new ViewGenerator(Views.PUBLIC_SINGLE_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("authors", publicAuthors)
                        .build());
    }

    public static ViewGenerator moderationView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, List<Author> authors) {
        return new ViewGenerator(MODERATION_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("authors", authors)
                        .build()
        );
    }

    public static ViewGenerator managementView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, List<Author> authors) {
        return new ViewGenerator(MANAGEMENT_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("authors", authors)
                        .build()
        );
    }

    public static ViewGenerator updateView(InitiativeViewInfo initiative, InitiativeUIUpdateDto initiativeForUpdate, Author authorInformation, List<Author> authors, String previousPageURI) {
        return new ViewGenerator(UPDATE_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative)
                        .add("updateData", initiativeForUpdate)
                        .add("author", authorInformation)
                        .add("authors", authors)
                        .add("previousPageURI", previousPageURI)
                        .build()
        );
    }

    public static ViewGenerator prepareView(PrepareInitiativeUICreateDto prepareInitiativeUICreateDto, List<Municipality> allMunicipalities) {
        return new ViewGenerator(PREPARE_VIEW,
                new AttributeBuilder()
                        .add("initiative", prepareInitiativeUICreateDto)
                        .add("municipalities", allMunicipalities).build()
        );
    }

    public static ViewGenerator editView(InitiativeDraftUIEditDto initiative, Author authorInformation, String previousPageURI) {
        return new ViewGenerator(EDIT_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative)
                        .add("author", authorInformation)
                        .add("previousPageURI", previousPageURI)
                        .build());
    }

    public static ViewGenerator manageAuthorsView(InitiativeViewInfo initiativeInfo,
                                                  ManagementSettings managementSettings,
                                                  List<Author> authors,
                                                  List<AuthorInvitation> invitations, AuthorInvitationUICreateDto invitationUiCreate) {
        return new ViewGenerator(MANAGE_AUTHORS_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("authors", authors)
                        .add("invitations", invitations)
                        .add("newInvitation", invitationUiCreate)
                        .build()
        );
    }

    public static ViewGenerator invitationView(InitiativeViewInfo municipalityInitiative,
                                               List<Municipality> allMunicipalities,
                                               PublicAuthors publicAuthors,
                                               ParticipantCount participantCount,
                                               AuthorInvitationUIConfirmDto authorInvitationUIConfirmDto) {
        return new ViewGenerator(Views.INVITATION_VIEW,
                new AttributeBuilder()
                        .add("initiative", municipalityInitiative)
                        .add("municipalities", allMunicipalities)
                        .add("participantCount", participantCount)
                        .add("publicAuthors", publicAuthors)
                        .add("authorInvitation", authorInvitationUIConfirmDto)
                        .build()
        );
    }

    public static ViewGenerator municipalityModarationView(List<MunicipalityEditDto> municipalitiesForEdit, MunicipalityUIEditDto municipalityUIEditDto) {
        return new ViewGenerator(Views.MUNICIPALITY_MODERATION,
                new AttributeBuilder()
                        .add("municipalities", municipalitiesForEdit)
                        .add("updateData", municipalityUIEditDto)
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
