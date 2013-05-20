package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import org.joda.time.LocalDate;

public class ParticipantListInfo {

    private Participant participant;

    public ParticipantListInfo(Participant participant) {
        this.participant = participant;
    }

    public String getName() {
        return participant.getName();
    }

    public LocalDate getParticipateDate() {
        return participant.getParticipateDate();
    }

    public Municipality getHomeMunicipality() {
        return participant.getHomeMunicipality();
    }

    public boolean isAuthor() {
        return participant.isAuthor();
    }
}
