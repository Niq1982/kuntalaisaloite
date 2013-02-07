package fi.om.municipalityinitiative.newdto.email;

public class CollectableInitiativeEmailInfo extends InitiativeEmailInfo {

    private String comment;
    // TODO: Participants

    CollectableInitiativeEmailInfo() { }

    public CollectableInitiativeEmailInfo(CollectableInitiativeEmailInfo original) {
        super(original);
        this.comment = original.getComment();
    }

    public static CollectableInitiativeEmailInfo parse(InitiativeEmailInfo initiativeEmailInfo, String comment) {
        CollectableInitiativeEmailInfo collectableInitiativeEmailInfo = new CollectableInitiativeEmailInfo(initiativeEmailInfo);
        collectableInitiativeEmailInfo.comment = comment;
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
}
