package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Membership;

import java.util.List;
import java.util.Optional;

public interface ParticipantDao {

    Long prepareConfirmedParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership, boolean showName);

    Long create(ParticipantCreateDto createDto, String confirmationCode);

    NormalParticipant confirmParticipation(Long participantId, String confirmationCode);

    void increaseParticipantCountFor(Long initiativeId, boolean showName, boolean citizen);

    List<NormalParticipant> findNormalPublicParticipants(Long initiativeId, int offset, int limit);

    List<NormalParticipant> findNormalAllParticipants(Long initiativeId, int offset, int limit);

    List<VerifiedParticipant> findVerifiedPublicParticipants(Long initiativeId, int offset, int limit);

    List<VerifiedParticipant> findVerifiedAllParticipants(Long initiativeId, int offset, int limit);

    Optional<Long> getInitiativeIdByParticipant(Long participantId);

    void deleteParticipant(Long initiativeId, Long participantId);

    void deleteVerifiedParticipant(Long initiativeId, Long participantId);

    void updateVerifiedParticipantShowName(Long initiativeId, String hash, boolean showName);

    void addVerifiedParticipant(Long initiativeId, VerifiedUserId verifiedUserId, boolean showName, boolean verifiedMunicipality, Long homeMunicipality, Membership municipalMembership);

    List<Participant> findAllParticipants(Long initiativeId, boolean requireShowName, int offset, int limit);
}
