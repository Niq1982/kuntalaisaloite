package fi.om.municipalityinitiative.newdto.email;

public class CollectableInitiativeEmailInfo extends InitiativeEmailInfo {

    private String comment;
    // TODO: Participants

    public static CollectableInitiativeEmailInfo parse(InitiativeEmailInfo initiativeEmailInfo, String comment) {
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = new CollectableInitiativeEmailInfo();
        collectableInitiativeEmailInfo.setComment(comment);
//        return collectableInitiativeEmailInfo;
        throw new RuntimeException();

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
//
//    public InitiativeEmailInfo getInitiativeEmailInfo() {
//        return initiativeEmailInfo;
//    }
//
//    public void setInitiativeEmailInfo(InitiativeEmailInfo initiativeEmailInfo) {
//        this.initiativeEmailInfo = initiativeEmailInfo;
//    }
}
