package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.util.Membership;

import java.util.List;

public interface ParticipantDao {

    Long prepareParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership);

    Long create(ParticipantCreateDto createDto, String confirmationCode);

    void confirmParticipation(Long participantId, String confirmationCode);

    ParticipantCount getParticipantCount(Long initiativeId);

    List<Participant> findPublicParticipants(Long initiativeId);

    List<Participant> findAllParticipants(Long initiativeId);

    Long getInitiativeIdByParticipant(Long participantId);

    void deleteParticipant(Long initiativeId, Long participantId);
}
