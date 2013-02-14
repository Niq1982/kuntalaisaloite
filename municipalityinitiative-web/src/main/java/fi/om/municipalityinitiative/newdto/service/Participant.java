package fi.om.municipalityinitiative.newdto.service;

public class Participant extends PublicParticipant {

    private final String homeMunicipality;

    public Participant(String name, boolean franchise, String homeMunicipality) {
        super(name, franchise);
        this.homeMunicipality =  homeMunicipality;
    }

    public String getHomeMunicipality() {
        return homeMunicipality;
    }
}
