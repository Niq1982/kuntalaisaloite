package fi.om.municipalityinitiative.newdto.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.Participants;
import fi.om.municipalityinitiative.service.ParticipantService;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

import java.util.List;

public class InitiativeJson {

    final InitiativeViewInfo initiative;
    final List<Participant> participants;

    private InitiativeJson(InitiativeViewInfo initiative, List<Participant> participants) {
        this.initiative = initiative;
        this.participants = participants;
    }

    @JsonId(path= Urls.INITIATIVE)
    public Long getId() {
        return initiative.getId();
    }

    public String getName() {
        return initiative.getName();
    }

    public String getProposal() {
        return initiative.getProposal();
    }

    public Municipality getMunicipality() {
        return new Municipality(initiative.getMunicipalityName(), initiative.getMunicipalityId());
    }

    public String getAuthorName() {
        return initiative.isShowName() ? initiative.getAuthorName() : null;
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getCreateTime() {
        return initiative.getCreateTime();
    }

    public boolean isCollectable() {
        return initiative.isCollectable();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getSentTime() {
        return initiative.isSent() ? initiative.getSentTime().get() : null;
    }

    public static InitiativeJson from(InitiativeViewInfo initiativeInfo, List<Participant> participantCount) {
        if (initiativeInfo.isCollectable()) {
            return new CollectableInitiativeJson(initiativeInfo, participantCount);
        }
        else {
            return new InitiativeJson(initiativeInfo, participantCount);
        }
    }

    private static class CollectableInitiativeJson extends InitiativeJson {
        private CollectableInitiativeJson(InitiativeViewInfo initiative, List<Participant> participants) {
            super(initiative, participants);
        }

        public Participants getPublicParticipants() {
            return ParticipantService.toParticipantNames(participants);
        }

    }
}
