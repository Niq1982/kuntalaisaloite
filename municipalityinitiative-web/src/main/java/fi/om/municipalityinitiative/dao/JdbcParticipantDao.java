package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.exceptions.InvalidParticipationConfirmationException;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.Membership;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.dao.JdbcInitiativeDao.assertSingleAffection;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;

@SQLExceptionTranslated
public class JdbcParticipantDao implements ParticipantDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long create(ParticipantCreateDto createDto, String confirmationCode) {

        if (confirmationCode == null) {
            throw new NullPointerException("confirmationCode may not be null: Participation would be accepted without participantCount increasing.");
        }

        return queryFactory.insert(participant)
                .set(participant.municipalityId, createDto.getHomeMunicipality())
                .set(participant.municipalityInitiativeId, createDto.getMunicipalityInitiativeId())
                .set(participant.name, createDto.getParticipantName())
                .set(participant.showName, createDto.isShowName())
                .set(participant.email, createDto.getEmail())
                .set(participant.confirmationCode, confirmationCode)
                .set(participant.membershipType, createDto.getMunicipalMembership())
                .executeWithKey(participant.id);
    }

    @Override
    public void confirmParticipation(Long participantId, String confirmationCode) {

        Object[] columns = queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .singleResult(QParticipant.participant.showName, QParticipant.participant.municipalityInitiativeId);

        if (columns == null || columns.length == 0) {
            throw new InvalidParticipationConfirmationException("Participant:" + participantId + ", code:" + confirmationCode);
        }

        Boolean showName = (boolean) columns[0];
        long initiativeIdByParticipant = (long) columns[1];

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .setNull(QParticipant.participant.confirmationCode)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .execute());

        if (showName) {
            assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic,
                            QMunicipalityInitiative.municipalityInitiative.participantCountPublic.add(1))
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                            QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                    .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeIdByParticipant))
                    .execute());
        } else {
            assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                            QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                    .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeIdByParticipant))
                    .execute());
        }
    }

    @Override
    // Preparing because we do not know participants name
    public Long prepareConfirmedParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership, boolean showName) {
        Long participantId = queryFactory.insert(participant)
                .set(participant.municipalityId, homeMunicipality)
                .set(participant.municipalityInitiativeId, initiativeId)
                .set(participant.email, email)
                .set(participant.membershipType, membership)
                .set(participant.showName, showName) // Default is true
                .executeWithKey(participant.id);

        // Increase denormalized participantCount if collaborative initiative.
        SQLUpdateClause updateParticipantCount = queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                        QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId));
        if (showName) {
            updateParticipantCount.set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic, QMunicipalityInitiative.municipalityInitiative.participantCountPublic.add(1));
        }
        updateParticipantCount.execute();

        return participantId;
    }

    @Override
    public List<NormalParticipant> findNormalPublicParticipants(Long initiativeId) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .where(participant.showName.eq(true))
                .where(participant.confirmationCode.isNull())
                .orderBy(participant.id.desc())
                .list(Mappings.normalParticipantMapping);
    }

    @Override
    public List<NormalParticipant> findNormalPublicParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .where(participant.showName.eq(true))
                .where(participant.confirmationCode.isNull())
                .orderBy(participant.id.desc())
                .offset(offset)
                .limit(limit)
                .list(Mappings.normalParticipantMapping);
    }

    @Override
    public List<NormalParticipant> findNormalAllParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .orderBy(participant.id.desc())
                .limit(limit)
                .offset(offset)
                .list(Mappings.normalParticipantMapping);
    }

    @Override
    public List<VerifiedParticipant> findVerifiedPublicParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.from(QVerifiedParticipant.verifiedParticipant)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantVerifiedUserFk, QVerifiedUser.verifiedUser)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.showName.eq(true))
                .orderBy(QVerifiedParticipant.verifiedParticipant.participateTime.desc(), QVerifiedUser.verifiedUser.id.desc())
                .limit(limit)
                .offset(offset)
                .list(Mappings.verifiedParticipantMapping);
    }

    @Override
    public List<VerifiedParticipant> findVerifiedAllParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.from(QVerifiedParticipant.verifiedParticipant)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantVerifiedUserFk, QVerifiedUser.verifiedUser)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .leftJoin(QMunicipalityInitiative.municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .orderBy(QVerifiedParticipant.verifiedParticipant.participateTime.desc(), QVerifiedUser.verifiedUser.id.desc())
                .limit(limit)
                .offset(offset)
                .list(Mappings.verifiedParticipantMapping);
    }

    @Override
    public Long getInitiativeIdByParticipant(Long participantId) {
        return queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .singleResult(QParticipant.participant.municipalityInitiativeId);
    }

    @Override
    public void deleteParticipant(Long initiativeId, Long participantId) {
        assertSingleAffection(queryFactory.delete(participant)
                .where(participant.id.eq(participantId))
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .execute());
        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.subtract(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void addVerifiedParticipant(Long initiativeId, VerifiedUserId userId, boolean showName, boolean verifiedMunicipality) {
        assertSingleAffection(queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, userId.toLong())
                .set(QVerifiedParticipant.verifiedParticipant.showName, showName)
                .set(QVerifiedParticipant.verifiedParticipant.verified, verifiedMunicipality)
                .execute());

        if (showName) {
            assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic, QMunicipalityInitiative.municipalityInitiative.participantCountPublic.add(1))
                    .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId)).execute());
        }
        else {
            assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                    .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                    .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                    .execute());
        }

    }

    @Override
    // TODO: Learn better usage of querydsl so this could be done in single query
    public void updateVerifiedParticipantShowName(Long initiativeId, String hash, boolean showName) {
        Long verifiedUserId = queryFactory.from(QVerifiedUser.verifiedUser)
                .where(QVerifiedUser.verifiedUser.hash.eq(hash))
                .uniqueResult(QVerifiedUser.verifiedUser.id);

        assertSingleAffection(queryFactory.update(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.showName, showName)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.verifiedUserId.eq(verifiedUserId))
                .execute());
    }
}
