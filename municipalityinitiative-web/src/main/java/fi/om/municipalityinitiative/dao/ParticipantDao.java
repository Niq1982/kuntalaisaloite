package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Membership;

import java.util.List;

public interface ParticipantDao {

    Long prepareParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership);

    Long create(ParticipantCreateDto createDto, String confirmationCode);

    void confirmParticipation(Long participantId, String confirmationCode);

    // FIXME: Remove
    ParticipantCount getNormalParticipantCount(Long initiativeId);

    // FIXME: Remove
    ParticipantCount getVerifiedParticipantCount(Long initiativeId);

    List<NormalParticipant> findNormalPublicParticipants(Long initiativeId);

    List<NormalParticipant> findNormalAllParticipants(Long initiativeId);

    List<VerifiedParticipant> findVerifiedPublicParticipants(Long initiativeId, int offset, int maxParticipantListLimit);

    List<VerifiedParticipant> findVerifiedAllParticipants(Long initiativeId);

    Long getInitiativeIdByParticipant(Long participantId);

    void deleteParticipant(Long initiativeId, Long participantId);

    void updateVerifiedParticipantShowName(Long initiativeId, String hash, boolean showName);

    void addVerifiedParticipant(Long initiativeId, VerifiedUserId userId, boolean showName, boolean verifiedMunicipality);

    List<NormalParticipant> findNormalPublicParticipants(Long initiativeId, int offset, int limit);
}
