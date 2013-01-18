package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;

public interface ParticipantDao {

    Long create(ParticipantCreateDto createDto);

    ParticipantCount countSupports(Long municipalityId);
}
