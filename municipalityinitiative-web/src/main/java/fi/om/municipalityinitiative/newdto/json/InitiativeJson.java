package fi.om.municipalityinitiative.newdto.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
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

    public String getAuthorName() {
        return Boolean.TRUE.equals(initiative.getAuthor().getContactInfo().isShowName()) ? initiative.getAuthor().getContactInfo().getName() : null;
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

    public static InitiativeJson from(Initiative initiativeInfo, List<Participant> publicParticipants, ParticipantCount participantCount) {
        if (initiativeInfo.isCollectable()) {
            return new CollectableInitiativeJson(initiativeInfo, publicParticipants, participantCount);
        }
        else {
//            return new InitiativeJson(initiativeInfo);
            // XXX: MMh... Whould it be better if non-collectable initiative would not return any participantinformation at all or not?
            return new CollectableInitiativeJson(initiativeInfo, Lists.<Participant>newArrayList(), new ParticipantCount());
        }
    }

    private static class CollectableInitiativeJson extends InitiativeJson {
        private List<Participant> participants;
        private ParticipantCount participantCount;

        private CollectableInitiativeJson(Initiative initiative,
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
