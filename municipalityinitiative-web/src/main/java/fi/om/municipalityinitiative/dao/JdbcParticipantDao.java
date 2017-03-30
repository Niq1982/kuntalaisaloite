package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.exceptions.InvalidParticipationConfirmationException;
import fi.om.municipalityinitiative.service.id.NormalParticipantId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;
import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;
import static fi.om.municipalityinitiative.sql.QVerifiedParticipant.verifiedParticipant;
import static fi.om.municipalityinitiative.sql.QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives;

@SQLExceptionTranslated
public class JdbcParticipantDao implements ParticipantDao {

    static final Expression<VerifiedParticipant> verifiedParticipantMapping = new MappingProjection<VerifiedParticipant>(
            VerifiedParticipant.class,
            QVerifiedParticipant.verifiedParticipant.participateTime,
            QVerifiedParticipant.verifiedParticipant.verified,
            QVerifiedUser.verifiedUser.name,
            QVerifiedUser.verifiedUser.email,
            QVerifiedUser.verifiedUser.id
    ) {
        @Override
        protected VerifiedParticipant map(Tuple row) {
            VerifiedParticipant participant = new VerifiedParticipant();

            participant.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            participant.setVerified(row.get(QVerifiedParticipant.verifiedParticipant.verified));
            participant.setParticipateDate(row.get(QVerifiedParticipant.verifiedParticipant.participateTime));
            participant.setName(row.get(QVerifiedUser.verifiedUser.name));
            participant.setId(new VerifiedUserId(row.get(QVerifiedUser.verifiedUser.id)));

            return participant;
        }
    };
    static final Expression<NormalParticipant> normalParticipantMapping =
            new MappingProjection<NormalParticipant>(NormalParticipant.class,
                    participant.all(), QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives.all(), QMunicipality.municipality.all()) {
                @Override
                protected NormalParticipant map(Tuple row) {
                    NormalParticipant par = new NormalParticipant();
                    par.setParticipateDate(row.get(participant.participateTime));
                    par.setName(row.get(participant.name));
                    par.setEmail(row.get(participant.email));
                    par.setMembership(row.get(participant.membershipType));
                    par.setVerified(Boolean.TRUE.equals(row.get(QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives.verified)));
                    if (row.get(QMunicipality.municipality.id) != null) {
                        par.setHomeMunicipality(Maybe.of(Mappings.parseMunicipality(row)));
                    }
                    else {
                        par.setHomeMunicipality(Maybe.<Municipality>absent());
                    }
                    par.setId(new NormalParticipantId(row.get(participant.id)));
                    return par;

                }
            };
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

        Tuple columns = queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .singleResult(QParticipant.participant.showName, QParticipant.participant.municipalityInitiativeId);

        if (columns == null || columns.size() == 0) {
            throw new InvalidParticipationConfirmationException("Participant:" + participantId + ", code:" + confirmationCode);
        }

        Boolean showName = columns.get(QParticipant.participant.showName);
        long initiativeIdByParticipant = columns.get(QParticipant.participant.municipalityInitiativeId);

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .setNull(QParticipant.participant.confirmationCode)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .execute());

        if (Boolean.TRUE.equals(showName)) {
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
    public void verifiedUserParticipatesNormalInitiative(Long participantId, VerifiedUserId userId, boolean verified) {
        assertSingleAffection(queryFactory.insert(verifiedUserNormalInitiatives)
                .set(verifiedUserNormalInitiatives.participant, participantId)
                .set(verifiedUserNormalInitiatives.verified, verified)
                .set(verifiedUserNormalInitiatives.verifiedUser, userId.toLong()).execute());
    }

    @Override
    public Collection<Long> getNormalInitiativesVerifiedUserHasParticipated(VerifiedUserId userId) {
        return queryFactory.from(verifiedUserNormalInitiatives)
                .where(verifiedUserNormalInitiatives.verifiedUser
                        .eq(userId.toLong()))
                .innerJoin(participant)
                    .on(verifiedUserNormalInitiatives.participant.eq(participant.id))
                .innerJoin(municipalityInitiative)
                    .on(participant.municipalityInitiativeId.eq(municipalityInitiative.id))
                .list(municipalityInitiative.id);
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
                .leftJoin(participant._verifiedUserNormalInitiativesParticipantId, QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives)
                .where(participant.showName.eq(true))
                .where(participant.confirmationCode.isNull())
                .orderBy(participant.id.desc())
                .list(normalParticipantMapping);
    }

    @Override
    public List<NormalParticipant> findNormalPublicParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .leftJoin(participant._verifiedUserNormalInitiativesParticipantId, QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives)
                .where(participant.showName.eq(true))
                .where(participant.confirmationCode.isNull())
                .orderBy(participant.id.desc())
                .offset(offset)
                .limit(limit)
                .list(normalParticipantMapping);
    }

    @Override
    public List<NormalParticipant> findNormalAllParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .leftJoin(participant._verifiedUserNormalInitiativesParticipantId, QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives)
                .orderBy(participant.id.desc())
                .limit(limit)
                .offset(offset)
                .list(normalParticipantMapping);
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
                .list(verifiedParticipantMapping);
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
                .list(verifiedParticipantMapping);
    }

    @Override
    public Maybe<Long> getInitiativeIdByParticipant(Long participantId) {
        return Maybe.fromNullable(queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .singleResult(QParticipant.participant.municipalityInitiativeId));
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
    public void deleteVerifiedParticipant(Long initiativeId, Long verifiedparticipantId) {
        assertSingleAffection(queryFactory.delete(verifiedParticipant)
                .where(verifiedParticipant.verifiedUserId.eq(verifiedparticipantId))
                .where(verifiedParticipant.initiativeId.eq(initiativeId))
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
