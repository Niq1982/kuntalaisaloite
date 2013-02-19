package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.service.Participant;

import java.util.List;

import com.google.common.collect.Lists;

public class CollectableInitiativeEmailInfo extends InitiativeEmailInfo {

    private String comment;
    private List<Participant> participantsFranchise = Lists.newArrayList();
    private List<Participant> participantsNoFranchise = Lists.newArrayList();

    CollectableInitiativeEmailInfo() { }

    public CollectableInitiativeEmailInfo(CollectableInitiativeEmailInfo original) {
        super(original);
        this.comment = original.getComment();
        this.participantsFranchise = original.participantsFranchise;
    }

    public static CollectableInitiativeEmailInfo parse(InitiativeEmailInfo initiativeEmailInfo, String comment, List<Participant> participants) {
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = new CollectableInitiativeEmailInfo(initiativeEmailInfo);
        collectableInitiativeEmailInfo.comment = comment;

        for (Participant participant : participants) {
            if (participant.isFranchise()) {
               collectableInitiativeEmailInfo.participantsFranchise.add(participant);
            }
            else {
                collectableInitiativeEmailInfo.participantsNoFranchise.add(participant);
            }
        }
        
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

    public List<Participant> getParticipantsFranchise() {
        return participantsFranchise;
    }
    
    public List<Participant> getParticipantsNoFranchise() {
        return participantsNoFranchise;
    }
    
    public long getParticipantNoFranchiseCount(){
        return participantsNoFranchise.size();
    }
    
    public long getParticipantFranchiseCount() {
        return participantsFranchise.size();
    }
    
    public long getParticipantCount() {
        return getParticipantNoFranchiseCount() + getParticipantFranchiseCount();
    }
}
