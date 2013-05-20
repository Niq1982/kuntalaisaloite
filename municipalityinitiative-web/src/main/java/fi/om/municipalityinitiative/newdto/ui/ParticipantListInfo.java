package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import org.joda.time.LocalDate;

public class ParticipantListInfo {

    private Participant participant;
    private boolean author;

    public ParticipantListInfo(Participant participant, boolean isAuthor) {
        this.participant = participant;
        author = isAuthor;
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
        return author;
    }

    public Long getId() {
        return participant.getId();
    }

}
