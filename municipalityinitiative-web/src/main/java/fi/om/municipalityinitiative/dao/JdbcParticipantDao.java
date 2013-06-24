package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.SimpleExpression;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.MaybeHoldingHashMap;
import fi.om.municipalityinitiative.util.Membership;
import org.springframework.transaction.annotation.Transactional;

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

        Long participantId = queryFactory.insert(participant)
                .set(participant.municipalityId, createDto.getHomeMunicipality())
                .set(participant.municipalityInitiativeId, createDto.getMunicipalityInitiativeId())
                .set(participant.name, createDto.getParticipantName())
                .set(participant.showName, createDto.isShowName())
                .set(participant.email, createDto.getEmail())
                .set(participant.confirmationCode, confirmationCode)
                .set(participant.membershipType, createDto.getMunicipalMembership())
                .executeWithKey(participant.id);

        return participantId;
    }

    @Override
    public void confirmParticipation(Long participantId, String confirmationCode) {
        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .setNull(QParticipant.participant.confirmationCode)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .execute());

        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                        QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(getInitiativeIdByParticipant(participantId)))
                .execute());
    }

    @Override
    // Preparing because we do not know participants name
    public Long prepareParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership) {
        Long participantId = queryFactory.insert(participant)
                .set(participant.municipalityId, homeMunicipality)
                .set(participant.municipalityInitiativeId, initiativeId)
                .set(participant.email, email)
                .set(participant.membershipType, membership)
                .set(participant.showName, true) // Default is true
                .executeWithKey(participant.id);

        // Increase denormalized participantCount if collaborative initiative.
        // DB constraint will also fail if trying to increase count for non-collaborative initiative
        queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                        QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute();

        return participantId;
    }

    // TODO: Fix magic strings to enum constants or something.
    @Override
    public ParticipantCount getParticipantCount(Long initiativeId) {

        MaybeHoldingHashMap<Boolean, Long> map = new MaybeHoldingHashMap<>(queryFactory
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .groupBy(participant.showName)
                .map(participant.showName, participant.count()));

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.setPublicNames(map.get(true).or(0L));
        participantCount.setPrivateNames(map.get(false).or(0L));
        return participantCount;

    }

    @Override
    public List<Participant> findPublicParticipants(Long initiativeId) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .where(participant.showName.eq(true))
                .where(participant.confirmationCode.isNull())
                .orderBy(participant.id.desc())
                .list(Mappings.participantMapping);
    }

    @Override
    public List<Participant> findAllParticipants(Long initiativeId) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .orderBy(participant.id.desc())
                .list(Mappings.participantMapping);
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
}
