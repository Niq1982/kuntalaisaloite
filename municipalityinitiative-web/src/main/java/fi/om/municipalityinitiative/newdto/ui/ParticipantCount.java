package fi.om.municipalityinitiative.newdto.ui;

public class ParticipantCount {

    private long publicNames = 0;
    private long privateNames = 0;

    public long getPublicNames() {
        return publicNames;
    }

    public long getPrivateNames() {
        return privateNames;
    }

    public void setPublicNames(long publicNames) {
        this.publicNames = publicNames;
    }

    public void setPrivateNames(long privateNames) {
        this.privateNames = privateNames;
    }

    public long getTotal() {
        return publicNames+privateNames;
    }

}
