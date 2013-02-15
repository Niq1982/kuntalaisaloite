package fi.om.municipalityinitiative.newdto.service;

import org.joda.time.LocalDate;

public class Participant extends PublicParticipant {

    public Participant(LocalDate participateDate, String name, boolean franchise, String homeMunicipality) {
        super(participateDate, name, franchise, homeMunicipality);
    }
}
