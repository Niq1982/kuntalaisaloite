package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.service.PublicParticipant;

import java.util.List;

public class CollectableInitiativeEmailInfo extends InitiativeEmailInfo {

    private String comment;
    private List<PublicParticipant> participants;
    // TODO: Participants

    CollectableInitiativeEmailInfo() { }

    public CollectableInitiativeEmailInfo(CollectableInitiativeEmailInfo original) {
        super(original);
        this.comment = original.getComment();
        this.participants = original.participants;
    }

    public static CollectableInitiativeEmailInfo parse(InitiativeEmailInfo initiativeEmailInfo, String comment, List<PublicParticipant> participants) {
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = new CollectableInitiativeEmailInfo(initiativeEmailInfo);
        collectableInitiativeEmailInfo.comment = comment;
        collectableInitiativeEmailInfo.participants = participants;
        return collectableInitiativeEmailInfo;
    }

    private CollectableInitiativeEmailInfo(InitiativeEmailInfo initiativeEmailInfo) {
        super(initiativeEmailInfo);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PublicParticipant> getParticipants() {
        return participants;
    }
}
