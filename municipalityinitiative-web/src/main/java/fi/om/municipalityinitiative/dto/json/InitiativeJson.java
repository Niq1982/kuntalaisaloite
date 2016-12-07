package fi.om.municipalityinitiative.dto.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.dto.ui.PublicAuthors;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

@JsonPropertyOrder(alphabetic = true)
public class InitiativeJson {

    private final Initiative initiative;
    private final PublicAuthorsJson authors;

    private InitiativeJson(Initiative initiative, PublicAuthors authors) {
        this.initiative = initiative;
        this.authors = new PublicAuthorsJson(authors);
    }

    @JsonId(path= Urls.INITIATIVE, useApiUrl = true)
    public Long getId() {
        return initiative.getId();
    }

    public String getName() {
        return initiative.getName();
    }

    public ViewUrlJson getUrl() {
        return new ViewUrlJson(initiative.getId());
    }

    public String getProposal() {
        return initiative.getProposal();
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
        return initiative.isSent() ? initiative.getSentTime().get() : null;
    }

    public PublicAuthorsJson getAuthors() {
        return authors;
    }

    public static InitiativeJson from(Initiative initiativeInfo, ParticipantCount participantCount, PublicAuthors publicAuthors) {
        if (initiativeInfo.isCollaborative()) {
            return new CollaborativeInitiativeJson(initiativeInfo, participantCount, publicAuthors);
        }
        else {
//            return new InitiativeJson(initiativeInfo);
            // XXX: MMh... Whould it be better if non-collaborative initiative would not return any participantinformation at all or not?
            return new CollaborativeInitiativeJson(initiativeInfo, new ParticipantCount(), publicAuthors);
        }
    }

    private static final class CollaborativeInitiativeJson extends InitiativeJson {
        private ParticipantCountJson participantCount;

        private CollaborativeInitiativeJson(Initiative initiative,
                                            ParticipantCount participantCount,
                                            PublicAuthors authors) {
            super(initiative, authors);
            this.participantCount = new ParticipantCountJson(participantCount, initiative.getExternalParticipantCount());
        }

        public ParticipantCountJson getParticipantCount() {
            return participantCount;
        }

    }
}
