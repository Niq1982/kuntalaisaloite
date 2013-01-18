package fi.om.municipalityinitiative.newdto;

public class Participant {
    private String name;
    private boolean franchise;

    public Participant(String name, boolean franchise) {
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
