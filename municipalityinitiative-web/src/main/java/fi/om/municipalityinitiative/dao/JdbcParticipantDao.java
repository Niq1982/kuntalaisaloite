package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.DslExpression;
import com.mysema.query.types.path.*;
import com.mysema.query.types.query.ListSubQuery;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.exceptions.InvalidParticipationConfirmationException;
import fi.om.municipalityinitiative.service.id.NormalParticipantId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;
import org.joda.time.LocalDate;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;
import static fi.om.municipalityinitiative.sql.QVerifiedParticipant.verifiedParticipant;

@SQLExceptionTranslated
public class JdbcParticipantDao implements ParticipantDao {

    static final Expression<VerifiedParticipant> verifiedParticipantMapping = new MappingProjection<VerifiedParticipant>(
            VerifiedParticipant.class,
            QVerifiedParticipant.verifiedParticipant.participateTime,
            QVerifiedParticipant.verifiedParticipant.verified,
            QVerifiedParticipant.verifiedParticipant.showName,
            QVerifiedParticipant.verifiedParticipant.membershipType,
            QVerifiedParticipant.verifiedParticipant.municipalityId,
            QVerifiedUser.verifiedUser.name,
            QVerifiedUser.verifiedUser.email,
            QVerifiedUser.verifiedUser.id,
            QMunicipality.municipality.id,
            QMunicipality.municipality.name,
            QMunicipality.municipality.nameSv,
            QMunicipality.municipality.active
    ) {
        @Override
        protected VerifiedParticipant map(Tuple row) {
            VerifiedParticipant participant = new VerifiedParticipant();

            participant.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            participant.setMunicipalityVerified(row.get(QVerifiedParticipant.verifiedParticipant.verified));
            participant.setShowName(row.get(QVerifiedParticipant.verifiedParticipant.showName));
            participant.setParticipateDate(row.get(QVerifiedParticipant.verifiedParticipant.participateTime));
            participant.setName(row.get(QVerifiedUser.verifiedUser.name));
            participant.setId(new VerifiedUserId(row.get(QVerifiedUser.verifiedUser.id)));
            participant.setMembership(row.get(QVerifiedParticipant.verifiedParticipant.membershipType));
            participant.setHomeMunicipality(
                    row.get(QMunicipality.municipality.id) != null
                            ? Mappings.parseMaybeMunicipality(row)
                            : Maybe.absent()
            );
            return participant;
        }
    };
    static final Expression<NormalParticipant> normalParticipantMapping =
            new MappingProjection<NormalParticipant>(NormalParticipant.class,
                    participant.all(), QMunicipality.municipality.all()) {
                @Override
                protected NormalParticipant map(Tuple row) {
                    NormalParticipant par = new NormalParticipant();
                    par.setParticipateDate(row.get(participant.participateTime));
                    par.setName(row.get(participant.name));
                    par.setEmail(row.get(participant.email));
                    par.setMembership(row.get(participant.membershipType));
                    par.setMunicipalityVerified(false);
                    par.setShowName(row.get(participant.showName));
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
            throw new NullPointerException("confirmationCode may not be null");
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
    public NormalParticipant confirmParticipation(Long participantId, String confirmationCode) {

         NormalParticipant participant = queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                 .leftJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
                .singleResult(normalParticipantMapping);

        if (participant == null) {
            throw new InvalidParticipationConfirmationException("Participant:" + participantId + ", code:" + confirmationCode);
        }

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .setNull(QParticipant.participant.confirmationCode)
                .where(QParticipant.participant.id.eq(participantId))
                .where(QParticipant.participant.confirmationCode.eq(confirmationCode))
                .execute());

        return participant;
    }

    @Override
    public void increaseParticipantCountFor(Long initiativeId, boolean showName, boolean citizen) {

        SQLUpdateClause updateClause = queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount,
                        QMunicipalityInitiative.municipalityInitiative.participantCount.add(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId));

        if (showName) {
            updateClause.set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic,
                    QMunicipalityInitiative.municipalityInitiative.participantCountPublic.add(1));

        }

        if (citizen) {
            updateClause.set(QMunicipalityInitiative.municipalityInitiative.participantCountCitizen, QMunicipalityInitiative.municipalityInitiative.participantCountCitizen.add(1));
        }

        assertSingleAffection(updateClause.execute());

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

        return participantId;
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
                .list(normalParticipantMapping);
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
                .list(normalParticipantMapping);
    }

    @Override
    public List<VerifiedParticipant> findVerifiedPublicParticipants(Long initiativeId, int offset, int limit) {
        return queryFactory.from(QVerifiedParticipant.verifiedParticipant)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantVerifiedUserFk, QVerifiedUser.verifiedUser)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.showName.eq(true))
                .leftJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantMunicipalityIdFk, QMunicipality.municipality)
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
                .leftJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantMunicipalityIdFk, QMunicipality.municipality)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .orderBy(QVerifiedParticipant.verifiedParticipant.participateTime.desc(), QVerifiedUser.verifiedUser.id.desc())
                .limit(limit)
                .offset(offset)
                .list(verifiedParticipantMapping);
    }

    @Override
    public List<Participant> findAllParticipants(Long initiativeId, boolean requireShowName, int offset, int limit) {

        ListSubQuery verifiedParticipants = new SQLSubQuery()
                .from(QVerifiedParticipant.verifiedParticipant)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantVerifiedUserFk, QVerifiedUser.verifiedUser)
                .innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .leftJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantMunicipalityIdFk, QMunicipality.municipality)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .list(QVerifiedUser.verifiedUser.id.as(ParticipateUnionRow.id.getMetadata().getName()),
                        QVerifiedUser.verifiedUser.id.isNotNull().as(ParticipateUnionRow.verified_user.getMetadata().getName()),
                        QVerifiedUser.verifiedUser.name.as(ParticipateUnionRow.name.getMetadata().getName()),
                        QVerifiedUser.verifiedUser.email.as(ParticipateUnionRow.email.getMetadata().getName()), // XXX: This might be null for verified participants...
                        QVerifiedParticipant.verifiedParticipant.showName.as(ParticipateUnionRow.show_name.getMetadata().getName()),
                        QVerifiedParticipant.verifiedParticipant.verified.as(ParticipateUnionRow.verified.getMetadata().getName()),
                        QVerifiedParticipant.verifiedParticipant.membershipType.as(ParticipateUnionRow.membership_type.getMetadata().getName()),
                        QVerifiedParticipant.verifiedParticipant.participateTime.as(ParticipateUnionRow.participate_date.getMetadata().getName()),
                        QMunicipality.municipality.id.as(ParticipateUnionRow.municipality_id.getMetadata().getName()),
                        QMunicipality.municipality.name.as(ParticipateUnionRow.municipality_name_fi.getMetadata().getName()),
                        QMunicipality.municipality.nameSv.as(ParticipateUnionRow.municipality_name_sv.getMetadata().getName()));

        ListSubQuery normalParticipants = new SQLSubQuery()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .where(participant.confirmationCode.isNull())
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .list(participant.id.as(ParticipateUnionRow.id.getMetadata().getName()),
                        participant.id.isNull().as(ParticipateUnionRow.verified_user.getMetadata().getName()),
                        participant.name.as(ParticipateUnionRow.name.getMetadata().getName()),
                        participant.email.as(ParticipateUnionRow.email.getMetadata().getName()),
                        participant.showName.as(ParticipateUnionRow.show_name.getMetadata().getName()),
                        participant.id.isNull().as(ParticipateUnionRow.verified.getMetadata().getName()),
                        participant.membershipType.as(ParticipateUnionRow.membership_type.getMetadata().getName()),
                        participant.participateTime.as(ParticipateUnionRow.participate_date.getMetadata().getName()),
                        QMunicipality.municipality.id.as(ParticipateUnionRow.municipality_id.getMetadata().getName()),
                        QMunicipality.municipality.name.as(ParticipateUnionRow.municipality_name_fi.getMetadata().getName()),
                        QMunicipality.municipality.nameSv.as(ParticipateUnionRow.municipality_name_sv.getMetadata().getName()));


        DslExpression unionExpression = new SQLSubQuery().union(normalParticipants, verifiedParticipants).as(ParticipateUnionRow.path);
        Expression<Participant> mapping = new MappingProjection<Participant>(Participant.class,
                ParticipateUnionRow.id,
                ParticipateUnionRow.name,
                ParticipateUnionRow.email,
                ParticipateUnionRow.show_name,
                ParticipateUnionRow.verified,
                ParticipateUnionRow.verified_user,
                ParticipateUnionRow.participate_date,
                ParticipateUnionRow.membership_type,
                ParticipateUnionRow.municipality_id,
                ParticipateUnionRow.municipality_name_fi,
                ParticipateUnionRow.municipality_name_sv) {
            @Override
            protected Participant map(Tuple row) {

                Participant participant;

                if (row.get(ParticipateUnionRow.verified_user)) {
                    participant = new VerifiedParticipant() {{
                        setId(new VerifiedUserId(row.get(ParticipateUnionRow.id)));
                    }};

                }
                else {
                    participant = new NormalParticipant() {{
                        setId(new NormalParticipantId(row.get(ParticipateUnionRow.id)));
                    }};
                }

                participant.setName(row.get(ParticipateUnionRow.name));
                participant.setEmail(row.get(ParticipateUnionRow.email));
                participant.setHomeMunicipality(Maybe.fromNullable(
                         (row.get(ParticipateUnionRow.municipality_id) != null)
                            ? new Municipality(row.get(ParticipateUnionRow.municipality_id),
                                 row.get(ParticipateUnionRow.municipality_name_fi),
                                 row.get(ParticipateUnionRow.municipality_name_sv),
                                 false)
                                 : null
                ));
                participant.setMembership(Membership.valueOf(row.get(ParticipateUnionRow.membership_type)));
                participant.setMunicipalityVerified(row.get(ParticipateUnionRow.verified));
                participant.setShowName(row.get(ParticipateUnionRow.show_name));
                participant.setParticipateDate(row.get(ParticipateUnionRow.participate_date));
                return participant;
            }
        };
        PostgresQuery unionQuery = queryFactory.from(unionExpression)
                 .offset(offset)
                 .limit(limit);

        if (requireShowName) {
            unionQuery.where(ParticipateUnionRow.show_name.isTrue());
        }

        List<Participant> list = unionQuery
                // XXX: This ordering does not use any indices. Ordering by date only is not enough, because the field does not have time attributes :E
                .orderBy(ParticipateUnionRow.participate_date.desc(), ParticipateUnionRow.name.asc())
                .list(mapping);


        return list;


    }

    private static class ParticipateUnionRow {

        public static final Path path = Expressions.path(Void.class, "innernamequery");

        public static final NumberPath<Long> id = new NumberPath<>(Long.class, path, "id");
        public static final BooleanPath verified_user = new BooleanPath(path, "verified_user");
        public static final StringPath name = new StringPath(path, "name");
        public static final BooleanPath show_name = new BooleanPath(path, "show_name");
        public static final BooleanPath verified = new BooleanPath(path, "verified");
        public static final StringPath membership_type = new StringPath(path, "membership_type");
        public static final DatePath<LocalDate> participate_date = new DatePath<>(LocalDate.class, path, "participate_date");
        public static final NumberPath<Long> municipality_id = new NumberPath<>(Long.class, path, "municipality_id");
        public static final StringPath municipality_name_fi = new StringPath(path, "municipality_name_fi");
        public static final StringPath municipality_name_sv = new StringPath(path, "municipality_name_sv");
        public static final StringPath email = new StringPath(path, "email");
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
    }

    @Override
    public void deleteVerifiedParticipant(Long initiativeId, Long verifiedparticipantId) {
        assertSingleAffection(queryFactory.delete(verifiedParticipant)
                .where(verifiedParticipant.verifiedUserId.eq(verifiedparticipantId))
                .where(verifiedParticipant.initiativeId.eq(initiativeId))
                .execute());
    }


    @Override
    public void addVerifiedParticipant(Long initiativeId, VerifiedUserId userId, boolean showName, boolean verifiedMunicipality, Long homeMunicipalityId, Membership municipalMembership) {

        assertSingleAffection(queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, userId.toLong())
                .set(QVerifiedParticipant.verifiedParticipant.showName, showName)
                .set(QVerifiedParticipant.verifiedParticipant.verified, verifiedMunicipality)
                .set(QVerifiedParticipant.verifiedParticipant.municipalityId, homeMunicipalityId)
                .set(QVerifiedParticipant.verifiedParticipant.membershipType, municipalMembership == null ? Membership.none : municipalMembership)
                .execute());

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
