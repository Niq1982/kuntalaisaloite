package fi.om.municipalityinitiative.dto.vetuma;

public abstract class VetumaResponse extends VetumaBase {

    public enum Status {
        SUCCESSFUL,
        CANCELLED,
        REJECTED,
        ERROR,
        FAILURE
    }

    protected Status STATUS;

    public Status getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Status STATUS) {
        this.STATUS = STATUS;
    }

}

