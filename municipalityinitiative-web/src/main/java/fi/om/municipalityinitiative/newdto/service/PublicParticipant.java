package fi.om.municipalityinitiative.newdto.service;

public class PublicParticipant {
    private String name;
    private boolean franchise;

    public PublicParticipant(String name, boolean franchise) {
        this.name = name;
        this.franchise = franchise;
    }

    public String getName() {
        return name;
    }

    public boolean isFranchise() {
        return franchise;
    }
}
