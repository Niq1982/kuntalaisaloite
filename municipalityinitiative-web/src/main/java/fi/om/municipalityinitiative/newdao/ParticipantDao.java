package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.Participant;
import fi.om.municipalityinitiative.newdto.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;

import java.util.List;

public interface ParticipantDao {

    Long create(ParticipantCreateDto createDto);

    ParticipantCount getParticipantCount(Long initiativeId);

    List<Participant> findPublicParticipants(Long initiativeId);
}
