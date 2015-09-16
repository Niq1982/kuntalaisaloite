package fi.om.municipalityinitiative.web.controller;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.service.AttachmentService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReviewHistoryDiff;
import fi.om.municipalityinitiative.web.SearchParameterQueryString;
import fi.om.municipalityinitiative.web.Views;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fi.om.municipalityinitiative.web.Views.*;

public final class ViewGenerator {

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

    public static ViewGenerator collaborativeView(InitiativePageInfo initiative,
                                                  List<Municipality> municipalities,
                                                  ParticipantUICreateDto participantUICreateDto,
                                                  AuthorUIMessage authorUIMessage,
                                                  String supportCountData) {
        return new ViewGenerator(Views.PUBLIC_COLLECT_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative.initiative)
                        .add("authors", initiative.authors)
                        .add("municipalities", municipalities)
                        .add("participantCount", initiative.initiative.getParticipantCount())
                        .add("attachments", initiative.attachments)
                        .add("participant", participantUICreateDto)
                        .add("authorMessage", authorUIMessage)
                        .add("supportCountData", supportCountData)
                        .add("locations", initiative.locations)
                        .build()
        );
    }

    public static ViewGenerator searchView(InitiativeListPageInfo info,
                                           InitiativeSearch currentSearch,
                                           SearchParameterQueryString queryString,
                                           Maybe<ArrayList<Municipality>> currentMunicipalities) {
        return new ViewGenerator(SEARCH_VIEW,
                new AttributeBuilder()
                        .add("initiatives", info.initiatives)
                        .add("municipalities", info.municipalities)
                        .add("initiativeCounts", info.initiativeCounts)
                        .add("currentSearch", currentSearch)
                        .add("queryString", queryString)
                        .add("currentMunicipalities", currentMunicipalities)
                        .build()
        );
    }

    public static ViewGenerator participantList(ParticipantsPageInfo initiativeInfo,
                                                String previousPageURI,
                                                boolean hasManagementRightForInitiative,
                                                int offset) {
        return new ViewGenerator(PARTICIPANT_LIST,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo.initiative)
                        .add("participants", initiativeInfo.participants)
                        .add("participantCount", initiativeInfo.initiative.getParticipantCount())
                        .add("previousPageURI", previousPageURI)
                        .add("hasManagementRightForInitiative", hasManagementRightForInitiative)
                        .add("offset", offset)
                        .build());
    }
    
    public static ViewGenerator participantListManage(InitiativeViewInfo initiativeInfo,
                                                      ParticipantCount participantCount,
                                                      List<ParticipantListInfo> publicParticipants,
                                                      int offset,
                                                      String previousPageURI) {
        return new ViewGenerator(PARTICIPANT_LIST_MANAGE,
                new AttributeBuilder()
                .add("initiative", initiativeInfo)
                .add("participants", publicParticipants)
                .add("participantCount", participantCount)
                .add("offset", offset)
                .add("previousPageURI", previousPageURI)
                .build());
    }

    public static ViewGenerator iframeSearch(List<InitiativeListInfo> initiatives, Maybe<List<Municipality>> municipalities, SearchParameterQueryString searchParameterQueryString) {
        return new ViewGenerator(IFRAME_VIEW,
                new AttributeBuilder()
                        .add("initiatives", initiatives)
                        .add("municipalities", municipalities)
                        .add("municipalitiesSize", municipalities.isPresent() ? municipalities.getValue().size() : 0)
                        .add("queryString", searchParameterQueryString)
                        .build()
        );
    }
    
    public static ViewGenerator iframeGenerator(List<Municipality> municipalities) {
        return new ViewGenerator(IFRAME_GENERATOR_VIEW,
                new AttributeBuilder()
                        .add("municipalities", municipalities)
                        .build());
    }

    public static ViewGenerator graphIFrameView() {
        return new ViewGenerator(GRAPH_IFRAME_VIEW, new AttributeBuilder().build());
    }

    public static ViewGenerator graphIFrameGeneratorView() {
        return new ViewGenerator(GRAPH_IFRAME_GENERATOR_VIEW, new AttributeBuilder().build());
    }

    public static ViewGenerator singleView(InitiativePageInfo initiativePageView) {
        return new ViewGenerator(Views.PUBLIC_SINGLE_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativePageView.initiative)
                        .add("authors", initiativePageView.authors)
                        .add("attachments", initiativePageView.attachments)
                        .build());
    }

    public static ViewGenerator moderationView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, List<? extends Author> authors, AttachmentService.Attachments allAttachments, List<ReviewHistoryRow> reviewHistory, Maybe<ReviewHistoryDiff> reviewHistoryDiff, List<Location> locations) {
        return new ViewGenerator(MODERATION_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("authors", authors)
                        .add("attachments", allAttachments)
                        .add("reviewHistories", reviewHistory)
                        .add("reviewHistoryDiff", reviewHistoryDiff)
                        .add("locations", locations)
                        .build()
        );
    }

    public static ViewGenerator municipalityDecisionView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, List<? extends Author> authors,  AttachmentService.Attachments allAttachments){
        return new ViewGenerator("municipality-decision-view",
                new AttributeBuilder()
                    .add("initiative", initiativeInfo)
                    .add("managementSettings", managementSettings)
                    .add("authors", authors)
                    .add("attachments", allAttachments)
                    .build()
                );
        }

    public static ViewGenerator managementView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, List<? extends Author> authors, AttachmentService.Attachments attachments, ParticipantCount participantCount, List<Location> locations, CommentUIDto commentUIDto) {
        return new ViewGenerator(MANAGEMENT_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("authors", authors)
                        .add("participantCount", participantCount)
                        .add("comment", commentUIDto)
                        .add("attachments", attachments)
                        .add("locations", locations)
                        .build()
        );
    }
    
    public static ViewGenerator manageAttachmentsView(InitiativeViewInfo initiativeInfo, ManagementSettings managementSettings, AttachmentService.Attachments attachments, AttachmentCreateDto attachmentCreateDto, AttachmentService.ImageProperties imageProperties) {
        return new ViewGenerator(MANAGE_ATTACHMENTS_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiativeInfo)
                        .add("managementSettings", managementSettings)
                        .add("attachments", attachments)
                        .add("attachment", attachmentCreateDto)
                        .add("imageProperties", imageProperties)
                        .build()
        );
    }

    public static ViewGenerator updateView(InitiativeViewInfo initiative, InitiativeUIUpdateDto initiativeForUpdate, Author authorInformation, List<? extends Author> authors, List<Location> locations, String previousPageURI) {
        return new ViewGenerator(UPDATE_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative)
                        .add("updateData", initiativeForUpdate)
                        .add("author", authorInformation)
                        .add("authors", authors)
                        .add("previousPageURI", previousPageURI)
                        .add("locations", locations)
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

    public static ViewGenerator editView(InitiativeViewInfo initiative, boolean hasNeverBeenSaved, InitiativeDraftUIEditDto editDto, Author authorInformation, String previousPageURI) {
        return new ViewGenerator(EDIT_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative)
                        .add("updateData", editDto)
                        .add("author", authorInformation)
                        .add("previousPageURI", previousPageURI)
                        .add("hasNeverBeenSaved", hasNeverBeenSaved)
                        .build());
    }

    public static ViewGenerator manageAuthorsView(InitiativeViewInfo initiativeInfo,
                                                  ManagementSettings managementSettings,
                                                  List<? extends Author> authors,
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

    public static ViewGenerator invitationView(InitiativeViewInfo initiative,
                                               List<Municipality> allMunicipalities,
                                               PublicAuthors publicAuthors,
                                               ParticipantCount participantCount,
                                               AuthorInvitationUIConfirmDto authorInvitationUIConfirmDto) {
        return new ViewGenerator(Views.INVITATION_VIEW,
                new AttributeBuilder()
                        .add("initiative", initiative)
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

    public static ViewGenerator ownInitiatives(List<InitiativeListInfo> initiatives) {
        return new ViewGenerator(Views.OWN_INITIATIVES,
                new AttributeBuilder()
                        .add("initiatives", initiatives).build()
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
