package fi.om.municipalityinitiative.dto.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

public class InitiativeListJson {

    private InitiativeListInfo initiative;

    public InitiativeListJson(InitiativeListInfo initiative) {
        this.initiative = initiative;
    }

    @JsonId(path= Urls.INITIATIVE)
    public Long getId() {
        return initiative.getId();
    }

    public String getName() {
        return initiative.getName();
    }

    public Municipality getMunicipality() {
        return initiative.getMunicipality();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getPublishDate() {
        return initiative.getStateTime();
    }

    public boolean isCollaborative() {
        return initiative.isCollaborative();
    }

    public InitiativeType getType() {
        return initiative.getType();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getSentTime() {
        return initiative.getSentTime().isPresent() ? initiative.getSentTime().get() : null;
    }

    public long getParticipantCount() {
        return initiative.getParticipantCount();
    }

}
