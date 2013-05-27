package fi.om.municipalityinitiative.dto.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import org.joda.time.LocalDate;

public class ParticipantJson {

    private Participant participant;

    public ParticipantJson(Participant participant) {
        this.participant = participant;
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getParticipateDate() {
        return participant.getParticipateDate();
    }

    public String getName() {
        return participant.getName();
    }

    public Municipality getMunicipality() {
        return participant.getHomeMunicipality();
    }
}
