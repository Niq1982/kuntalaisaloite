package fi.om.municipalityinitiative.newdto.email;

public class CollectableInitiativeEmailInfo extends InitiativeEmailInfo {

    private String comment;
    // TODO: Participants

    CollectableInitiativeEmailInfo() { }

    public CollectableInitiativeEmailInfo(InitiativeEmailInfo initiativeEmailInfo, String comment) {
        super(initiativeEmailInfo);
        this.comment = comment;
    }

    public CollectableInitiativeEmailInfo(CollectableInitiativeEmailInfo original) {
        super(original);
        this.comment = original.getComment();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
