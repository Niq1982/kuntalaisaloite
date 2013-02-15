package fi.om.municipalityinitiative.newdto.service;

import org.joda.time.LocalDate;

public class Participant extends PublicParticipant {

    private final String homeMunicipality;

    public Participant(LocalDate participateDate, String name, boolean franchise, String homeMunicipality) {
        super(participateDate, name, franchise);
        this.homeMunicipality =  homeMunicipality;
    }

    public String getHomeMunicipality() {
        return homeMunicipality;
    }
}
