package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Participant;

public class ParticipantListInfo<E extends Participant> {

    private E participant;
    private boolean author;

    public ParticipantListInfo(E participant, boolean isAuthor) {
        this.participant = participant;
        author = isAuthor;
    }

    public E getParticipant() {
        return participant;
    }

    public boolean isAuthor() {
        return author;
    }

}
