package fi.om.municipalityinitiative.dto.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

import java.util.List;

@JsonPropertyOrder(alphabetic = true)
public class InitiativeJson {

    final Initiative initiative;

    private InitiativeJson(Initiative initiative) {
        this.initiative = initiative;
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
        return initiative.getMunicipality();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getCreateTime() {
        return initiative.getCreateTime();
    }

    public boolean isCollaborative() {
        return initiative.isCollaborative();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getSentTime() {
        return initiative.isSent() ? initiative.getSentTime().get() : null;
    }

    public static InitiativeJson from(Initiative initiativeInfo, List<Participant> publicParticipants, ParticipantCount participantCount) {
        if (initiativeInfo.isCollaborative()) {
            return new CollaborativeInitiativeJson(initiativeInfo, publicParticipants, participantCount);
        }
        else {
//            return new InitiativeJson(initiativeInfo);
            // XXX: MMh... Whould it be better if non-collaborative initiative would not return any participantinformation at all or not?
            return new CollaborativeInitiativeJson(initiativeInfo, Lists.<Participant>newArrayList(), new ParticipantCount());
        }
    }

    private static class CollaborativeInitiativeJson extends InitiativeJson {
        private List<Participant> participants;
        private ParticipantCount participantCount;

        private CollaborativeInitiativeJson(Initiative initiative,
                                          List<Participant> participants,
                                          ParticipantCount participantCount) {
            super(initiative);
            this.participants = participants;
            this.participantCount = participantCount;
        }

        public ParticipantCount getParticipantCount() {
            return participantCount;
        }

        public List<Participant> getPublicParticipants() {
            return participants;
        }

    }
}
