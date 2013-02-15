package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.service.PublicParticipant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;

import java.util.List;

public interface ParticipantDao {

    Long create(ParticipantCreateDto createDto);

    ParticipantCount getParticipantCount(Long initiativeId);

    List<PublicParticipant> findPublicParticipants(Long initiativeId);

    List<PublicParticipant> findAllParticipants(Long initiativeId);
}
