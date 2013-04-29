package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.SimpleExpression;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.MaybeHoldingHashMap;
import fi.om.municipalityinitiative.util.Membership;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.newdao.JdbcInitiativeDao.assertSingleAffection;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;

@Transactional(readOnly = true)
@SQLExceptionTranslated
public class JdbcParticipantDao implements ParticipantDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = false)
    public Long create(ParticipantCreateDto createDto, String confirmationCode) {
        Long participantId = queryFactory.insert(participant)
                .set(participant.franchise, createDto.isFranchise())
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = true)
    // Preparing because we do not know participants name
    public Long prepareParticipant(Long initiativeId, Long homeMunicipality, String email, Membership membership, Boolean franchise) {
        Long participantId = queryFactory.insert(participant)
                .set(participant.franchise, franchise)
                .set(participant.municipalityId, homeMunicipality)
                .set(participant.municipalityInitiativeId, initiativeId)
                .set(participant.email, email)
                .set(participant.membershipType, membership)
                .set(participant.showName, true) // Default is true
                .executeWithKey(participant.id);

        // Increase denormalized participantCount if collectable initiative.
        // DB constraint will also fail if trying to increase count for non-collectable initiative
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

        Expression<String> caseBuilder = new CaseBuilder()
                .when(participant.franchise.isTrue().and(participant.showName.isTrue())).then(new ConstantImpl<String>("11"))
                .when(participant.franchise.isTrue().and(participant.showName.isFalse())).then(new ConstantImpl<String>("10"))
                .when(participant.franchise.isFalse().and(participant.showName.isTrue())).then(new ConstantImpl<String>("01"))
                .when(participant.franchise.isFalse().and(participant.showName.isFalse())).then(new ConstantImpl<String>("00"))
                .otherwise(new ConstantImpl<String>("XX"));


        SimpleExpression<String> simpleExpression = Expressions.as(caseBuilder, "testi");
        MaybeHoldingHashMap<String, Long> map = new MaybeHoldingHashMap<String, Long>(queryFactory
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .groupBy(simpleExpression)
                .map(simpleExpression, participant.count()));

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.getFranchise().setPublicNames(map.get("11").or(0L));
        participantCount.getFranchise().setPrivateNames(map.get("10").or(0L));
        participantCount.getNoFranchise().setPublicNames(map.get("01").or(0L));
        participantCount.getNoFranchise().setPrivateNames(map.get("00").or(0L));
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
                .list(participantMapping);
    }

    @Override
    public List<Participant> findAllParticipants(Long initiativeId) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .orderBy(participant.id.desc())
                .list(participantMapping);
    }

    @Override
    public Long getInitiativeIdByParticipant(Long participantId) {
        return queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .singleResult(QParticipant.participant.municipalityInitiativeId);
    }

    Expression<Participant> participantMapping =
            new MappingProjection<Participant>(Participant.class,
                    participant.all(), QMunicipality.municipality.all()) {
                @Override
                protected Participant map(Tuple row) {
                    return new Participant(
                            row.get(participant.participateTime),
                            row.get(participant.name),
                            row.get(participant.franchise),
                            new Municipality(
                                    row.get(QMunicipality.municipality.id),
                                    row.get(QMunicipality.municipality.name),
                                    row.get(QMunicipality.municipality.nameSv)
                            ),
                            row.get(participant.email),
                            row.get(participant.membershipType)
                    );

                }
            };

}
