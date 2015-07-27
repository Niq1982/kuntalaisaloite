package fi.om.municipalityinitiative.dao;

import com.google.common.base.Strings;
import com.mysema.commons.lang.Assert;
import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.StringPath;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeListWithCount;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;
import static fi.om.municipalityinitiative.sql.QVerifiedParticipant.verifiedParticipant;

@SQLExceptionTranslated
public class JdbcInitiativeDao implements InitiativeDao {

    private static final BooleanExpression STATE_NOT_PREPARE = municipalityInitiative.name.isNotEmpty();
    private static final BooleanExpression IS_PUBLIC = municipalityInitiative.state.eq(InitiativeState.PUBLISHED)
            .and(municipalityInitiative.fixState.eq(FixState.OK));
    private static final BooleanExpression STATE_IS_DRAFT = municipalityInitiative.state.eq(InitiativeState.DRAFT)
            .and(STATE_NOT_PREPARE);
    private static final BooleanExpression STATE_IS_REVIEW = municipalityInitiative.state.eq(InitiativeState.REVIEW)
            .or(municipalityInitiative.fixState.eq(FixState.REVIEW));
    private static final BooleanExpression STATE_IS_ACCEPTED = municipalityInitiative.state.eq(InitiativeState.ACCEPTED)
            .and(municipalityInitiative.fixState.eq(FixState.OK));
    private static final BooleanExpression STATE_IS_COLLECTING = municipalityInitiative.sent.isNull().and(IS_PUBLIC);
    private static final BooleanExpression STATE_IS_SENT = municipalityInitiative.sent.isNotNull().and(IS_PUBLIC);
    private static final BooleanExpression STATE_IS_FIX = municipalityInitiative.fixState.eq(FixState.FIX);
    static final Expression<InitiativeListInfo> initiativeListInfoMapping =
            new MappingProjection<InitiativeListInfo>(InitiativeListInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(Mappings.parseMunicipality(row));
                    info.setCollaborative(InitiativeType.isCollaborative(row.get(municipalityInitiative.type)));
                    info.setSentTime(Mappings.maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setParticipantCount(Mappings.nullToZero(row.get(municipalityInitiative.participantCount)) + Mappings.nullToZero(row.get(municipalityInitiative.externalparticipantcount)));
                    info.setType(row.get(municipalityInitiative.type));
                    info.setState(row.get(municipalityInitiative.state));
                    info.setStateTime(row.get(municipalityInitiative.stateTimestamp).toLocalDate());
                    return info;
                }
            };
    static final Expression<Initiative> initiativeInfoMapping =
            new MappingProjection<Initiative>(Initiative.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all()) {
                @Override
                protected Initiative map(Tuple row) {
                    Initiative info = new Initiative();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(Mappings.parseMunicipality(row));
                    info.setType(row.get(municipalityInitiative.type));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setSentTime(Mappings.maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setState(row.get(municipalityInitiative.state));
                    info.setStateTime(row.get(municipalityInitiative.stateTimestamp).toLocalDate());
                    info.setExtraInfo(row.get(municipalityInitiative.extraInfo));
                    info.setModeratorComment(Strings.nullToEmpty(row.get(municipalityInitiative.moderatorComment)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    info.setSentComment(row.get(municipalityInitiative.sentComment));
                    info.setFixState(row.get(municipalityInitiative.fixState));
                    info.setExternalParticipantCount(Mappings.nullToZero(row.get(municipalityInitiative.externalparticipantcount)));
                    info.setParticipantCountPublic(Mappings.nullToZero(row.get(municipalityInitiative.participantCountPublic)));
                    info.setLastEmailReportTime(row.get(municipalityInitiative.lastEmailReportTime));
                    info.setLastEmailReportType(row.get(municipalityInitiative.lastEmailReportType));
                    Long maybeYouthInitiativeID = row.get(municipalityInitiative.youthInitiativeId);
                    if (maybeYouthInitiativeID != null) {
                        info.setYouthInitiativeId(maybeYouthInitiativeID);
                    }
                    Double maybeLocationLat = row.get(municipalityInitiative.locationLat);
                    Double maybeLocationLng = row.get(municipalityInitiative.locationLng);
                    String maybeLocationDescription = row.get(municipalityInitiative.locationDescription);

                    if (maybeLocationLng != null && maybeLocationLng != null && maybeLocationDescription != null) {
                        info.setLocation(new Location(maybeLocationLat, maybeLocationLng));
                        info.setLocationDescription(maybeLocationDescription);
                    }
                    return info;
                }
            };

    private final Logger log = LoggerFactory.getLogger(JdbcInitiativeDao.class);

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public InitiativeListWithCount findUnCached(InitiativeSearch search) {
        return find(search);
    }

    @Override
    @Cacheable(value = "initiativeList")
    public InitiativeListWithCount findCached(InitiativeSearch search) {
        return find(search);
    }

    private InitiativeListWithCount find(InitiativeSearch search) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality);

        filterByState(query, search);
        filterByType(query, search.getType());
        filterByTitle(query, search.getSearch());
        filterByMunicipality(query, Maybe.fromNullable(search.getMunicipalities()));
        orderBy(query, search.getOrderBy());
        long rows = query.count();
        restrictResults(query, search);


        return new InitiativeListWithCount(query.list(initiativeListInfoMapping), rows);
    }

    private static void filterByType(PostgresQuery query, InitiativeSearch.Type type) {
        switch (type) {
            case normal:
                query.where(QMunicipalityInitiative.municipalityInitiative.type.in(InitiativeType.COLLABORATIVE, InitiativeType.SINGLE, InitiativeType.UNDEFINED));
                break;
            case citizen:
                query.where(QMunicipalityInitiative.municipalityInitiative.type.eq(InitiativeType.COLLABORATIVE_CITIZEN));
                break;
            case council:
                query.where(QMunicipalityInitiative.municipalityInitiative.type.eq(InitiativeType.COLLABORATIVE_COUNCIL));
                break;
            case all:
            default:


        }
    }

    @Override
    public List<InitiativeListInfo> findInitiatives(VerifiedUserId verifiedUserId) {
        return queryFactory.from(QVerifiedUser.verifiedUser)
                .innerJoin(QVerifiedUser.verifiedUser._verifiedAuthorVerifiedUserFk, QVerifiedAuthor.verifiedAuthor)
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(QVerifiedUser.verifiedUser.id.eq(verifiedUserId.toLong()))
                .where(QMunicipalityInitiative.municipalityInitiative.name.isNotEmpty())
                .orderBy(QMunicipalityInitiative.municipalityInitiative.id.desc())
                .list(initiativeListInfoMapping);
    }

    @Override
    public List<Initiative> findAllByStateChangeBefore(InitiativeState accepted, LocalDate date) {
        DateTime dateTimeAtMidnight = date.toDateTime(new LocalTime(0, 0));
        return queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .where(QMunicipalityInitiative.municipalityInitiative.state.eq(accepted))
                .where(QMunicipalityInitiative.municipalityInitiative.stateTimestamp.before(dateTimeAtMidnight))
                .innerJoin(QMunicipalityInitiative.municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .list(initiativeInfoMapping);
    }

    @Override
    public List<Initiative> findAllPublishedNotSent() {
        return queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.sent.isNull())
                .where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED))
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .list(initiativeInfoMapping);
    }

    @Override
    public Long prepareYouthInitiative(long youthInitiativeId, String name, String proposal, String extraInfo, Long municipality) {
        return queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.youthInitiativeId, youthInitiativeId)
                .set(municipalityInitiative.name, name)
                .set(municipalityInitiative.proposal, proposal)
                .set(municipalityInitiative.extraInfo, extraInfo)
                .set(municipalityInitiative.municipalityId, municipality)
                .set(municipalityInitiative.type, InitiativeType.UNDEFINED)
                .executeWithKey(municipalityInitiative.id);
    }

    @Override
    public void markInitiativeReportSent(Long id, EmailReportType type, DateTime today) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.lastEmailReportTime, today)
                .set(municipalityInitiative.lastEmailReportType, type)
                .where(municipalityInitiative.id.eq(id))
                .execute());

    }

    @Override
    public void denormalizeParticipantCountForNormalInitiative(Long initiativeId) {

        // Querydsl: subquery?
        long publicParticipants = queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QMunicipalityInitiative.municipalityInitiative._participantMunicipalityInitiativeIdFk, QParticipant.participant)
                .where(QParticipant.participant.confirmationCode.isNull())
                .where(QParticipant.participant.showName.eq(true))
                .where(QParticipant.participant.municipalityInitiativeId.eq(initiativeId))
                .count();

        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic, (int) publicParticipants)
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void denormalizeParticipantCountForVerifiedInitiative(Long initiativeId) {
        long publicParticipants = queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QMunicipalityInitiative.municipalityInitiative._verifiedParticipantInitiativeFk, QVerifiedParticipant.verifiedParticipant)
                .where(QVerifiedParticipant.verifiedParticipant.showName.eq(true))
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .count();

        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic, (int) publicParticipants)
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute());

    }

    private static void orderBy(PostgresQuery query, InitiativeSearch.OrderBy orderBy) {
        switch (orderBy) {
            case latestSent:
                query.orderBy(municipalityInitiative.sent.desc(), municipalityInitiative.id.desc());
                break;
            case oldestSent:
                query.orderBy(municipalityInitiative.sent.asc(), municipalityInitiative.id.asc());
                break;
            case latest:
                query.orderBy(municipalityInitiative.stateTimestamp.desc(), municipalityInitiative.id.desc());
                break;
            case oldest:
                query.orderBy(municipalityInitiative.stateTimestamp.asc(), municipalityInitiative.id.asc());
                break;
            case id:
                query.orderBy(municipalityInitiative.id.desc());
                break;
            case mostParticipants:
                query.orderBy(municipalityInitiative.participantCount.add(municipalityInitiative.externalparticipantcount).desc(), municipalityInitiative.id.desc());
                break;
            case leastParticipants:
                query.orderBy(municipalityInitiative.participantCount.add(municipalityInitiative.externalparticipantcount).asc(), municipalityInitiative.id.asc());
                break;
            default:
                throw new RuntimeException("Order by not implemented:" + orderBy);
        }
    }

    private static void filterByTitle(PostgresQuery query, String search) {
        if (search != null) {
            query.where(toLikePredicate(municipalityInitiative.name, search));
        }
    }

    private static void filterByMunicipality(PostgresQuery query, Maybe<List<Long>> municipalityIds) {
        if (municipalityIds.isPresent() && !municipalityIds.getValue().isEmpty()) {
            query.where(municipalityInitiative.municipalityId.in(municipalityIds.getValue()));
        }
    }

    private static void filterByState(PostgresQuery query, InitiativeSearch search) {

        if (!search.getShow().isOmOnly()) {
            query.where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED))
                 .where(municipalityInitiative.fixState.eq(FixState.OK));
        }

        switch (search.getShow()) {

            // public

            case sent:
                query.where(STATE_IS_SENT);
                break;
            case collecting:
                query.where(STATE_IS_COLLECTING);
                break;

            // om

            case draft:
                query.where(STATE_IS_DRAFT);
                break;
            case fix:
                query.where(STATE_IS_FIX);
                break;
            case review:
                query.where(STATE_IS_REVIEW);
                break;
            case accepted:
                query.where(STATE_IS_ACCEPTED);
                break;
            case omAll:
                query.where(STATE_NOT_PREPARE);
                break;

            // default:

            case all:
                break;
            default:
                throw new RuntimeException("Unknown initiative state: " + search.getShow());
        }
    }

    private static void restrictResults(PostgresQuery query, InitiativeSearch search) {
        query.limit(search.getLimit());
        if (search.getOffset() != null) {
            query.offset(search.getOffset());
        }
    }


    private static Predicate toLikePredicate(StringPath name, String search) {
        return name.toLowerCase().like(toLikePattern(search).toLowerCase());
    }

    private static String toLikePattern(String search) {
        //TODO: Parse % and _
        return "%"+search+"%";
    }

    @Override
    public Initiative get(Long initiativeId) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(municipalityInitiative.id.eq(initiativeId));

        Initiative initiative = query.uniqueResult(initiativeInfoMapping);
        if (initiative == null) {
            throw new NotFoundException("initiative", initiativeId);
        }
        return initiative;
    }

    @Override
    @Cacheable(value = "initiativeCount")
    public InitiativeCounts getPublicInitiativeCounts(Maybe<List<Long>> municipalities, InitiativeSearch.Type initiativeType) {
        Expression<String> caseBuilder = new CaseBuilder()
                .when(municipalityInitiative.sent.isNull())
                .then(new ConstantImpl<String>(InitiativeSearch.Show.collecting.name()))
                .otherwise(new ConstantImpl<String>(InitiativeSearch.Show.sent.name()));

        SimpleExpression<String> simpleExpression = Expressions.as(caseBuilder, "showCategory");

        PostgresQuery from = queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED))
                .where(municipalityInitiative.fixState.eq(FixState.OK));

        filterByType(from, initiativeType);

        if (municipalities.isPresent() && !(municipalities.getValue()).isEmpty()) {
            from.where(municipalityInitiative.municipalityId.in(municipalities.getValue()));
        }

        MaybeHoldingHashMap<String, Long> map = new MaybeHoldingHashMap<>(from
                .groupBy(simpleExpression)
                .map(simpleExpression, municipalityInitiative.count()));

        InitiativeCounts counts = new InitiativeCounts();
        counts.sent = map.get(InitiativeSearch.Show.sent.name()).or(0L);
        counts.collecting = map.get(InitiativeSearch.Show.collecting.name()).or(0L);
        return counts;
    }

    @Override
    public InitiativeCounts getAllInitiativeCounts(Maybe<List<Long>> municipalities, InitiativeSearch.Type initiativeType) {
        String unknownStateFound = "unknownStateFound";
        Expression<String> caseBuilder = new CaseBuilder()
                .when(STATE_IS_COLLECTING)
                .then(new ConstantImpl<>(InitiativeSearch.Show.collecting.name()))
                .when(STATE_IS_SENT)
                .then(new ConstantImpl<>(InitiativeSearch.Show.sent.name()))
                .when(STATE_IS_DRAFT)
                .then(new ConstantImpl<>(InitiativeState.DRAFT.name()))
                .when(STATE_IS_ACCEPTED)
                .then(new ConstantImpl<>(InitiativeState.ACCEPTED.name()))
                .when(STATE_IS_REVIEW)
                .then(new ConstantImpl<>(InitiativeState.REVIEW.name()))
                .when(STATE_IS_FIX)
                .then(new ConstantImpl<>(FixState.FIX.name()))
                .otherwise(new ConstantImpl<>(unknownStateFound));

        SimpleExpression<String> showCategory = Expressions.as(caseBuilder, "showCategory");

        PostgresQuery from = queryFactory.from(municipalityInitiative).where(STATE_NOT_PREPARE);

        filterByType(from, initiativeType);

        if (municipalities.isPresent() && !(municipalities.getValue()).isEmpty()) {
            from.where(municipalityInitiative.municipalityId.in(municipalities.getValue()));
        }

        MaybeHoldingHashMap<String, Long> map = new MaybeHoldingHashMap<>(from
                .groupBy(showCategory)
                .map(showCategory, municipalityInitiative.count()));

        InitiativeCounts counts = new InitiativeCounts();
        counts.sent = map.get(InitiativeSearch.Show.sent.name()).or(0L);
        counts.collecting = map.get(InitiativeSearch.Show.collecting.name()).or(0L);
        counts.draft = map.get(InitiativeState.DRAFT.name()).or(0L);
        counts.accepted = map.get(InitiativeState.ACCEPTED.name()).or(0L);
        counts.review = map.get(InitiativeState.REVIEW.name()).or(0L);
        counts.fix = map.get(FixState.FIX.name()).or(0L);

        if (map.get(unknownStateFound).isPresent()) {
            log.error("Initiatives found with unknown state: " + map.get(unknownStateFound).get());
        }
        return counts;
    }

    @Override
    public Long prepareInitiative(Long municipalityId) {
        return queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.municipalityId, municipalityId)
                .set(municipalityInitiative.type, InitiativeType.UNDEFINED)
                .executeWithKey(municipalityInitiative.id);
    }

    @Override
    public void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto) {
        SQLUpdateClause query = queryFactory.update(municipalityInitiative);
        query.set(municipalityInitiative.name, editDto.getName())
                .set(municipalityInitiative.proposal, editDto.getProposal())
                .set(municipalityInitiative.modified, CURRENT_TIME)
                .set(municipalityInitiative.extraInfo, editDto.getExtraInfo())
                .set(municipalityInitiative.externalparticipantcount, editDto.getExternalParticipantCount());

        if (editDto.getLocation() != null) {
            query.set(municipalityInitiative.locationLat, editDto.getLocation().getLat());
            query.set(municipalityInitiative.locationLng, editDto.getLocation().getLng());
            query.set(municipalityInitiative.locationDescription, editDto.getLocationDescription());
        } else {
            query.setNull(municipalityInitiative.locationLat);
            query.setNull(municipalityInitiative.locationLng);
            query.setNull(municipalityInitiative.locationDescription);
        }
        assertSingleAffection(query
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());

    }

    @Override
    public void updateInitiativeState(Long initiativeId, InitiativeState state) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.state, state)
                .set(municipalityInitiative.stateTimestamp, CURRENT_TIME)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void updateInitiativeFixState(Long initiativeId, FixState fixState) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.fixState, fixState)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void updateInitiativeType(Long initiativeId, InitiativeType initiativeType) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.type, initiativeType)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void markInitiativeAsSent(Long initiativeId) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sent, CURRENT_TIME)
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(municipalityInitiative.sent.isNull())
                .execute());
    }

    @Override
    public void updateModeratorComment(Long initiativeId, String moderatorComment) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.moderatorComment, moderatorComment)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void updateSentComment(Long initiativeId, String sentComment) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sentComment, sentComment)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public void updateExtraInfo(Long initiativeId, String extraInfo, Integer externalParticipantCount) {

        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.extraInfo, extraInfo)
                .set(municipalityInitiative.externalparticipantcount, externalParticipantCount)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    public Long prepareVerifiedInitiative(Long municipalityId, InitiativeType initiativeType) {
        return queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.municipalityId, municipalityId)
                .set(municipalityInitiative.type, initiativeType)
                .executeWithKey(municipalityInitiative.id);
    }

    @Override
    public boolean isVerifiableInitiative(Long initiativeId) {
        InitiativeType initiativeType = queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.id.eq(initiativeId))
                .uniqueResult(municipalityInitiative.type);

        return initiativeType != null && initiativeType.isVerifiable();
    }

    @Override
    public List<Long> getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate date) {

        return queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.state.in(InitiativeState.PUBLISHED),
                        municipalityInitiative.sent.goe(date.toLocalDateTime(new LocalTime(0, 0)).toDateTime()).or(municipalityInitiative.sent.isNull())
                        .or(municipalityInitiative.supportCountData.isNull()))
                .list(municipalityInitiative.id);
    }


    @Override
    public Map<LocalDate,Long> getSupportVoteCountByDateUntil(Long initiativeId, LocalDate tillDay){

        if (get(initiativeId).getType().isVerifiable()) {
            return queryFactory.from(verifiedParticipant)
                    .where(verifiedParticipant.initiativeId.eq(initiativeId))
                    .where(verifiedParticipant.participateTime.loe(tillDay))
                    .groupBy(verifiedParticipant.participateTime)
                    .map(verifiedParticipant.participateTime, verifiedParticipant.participateTime.count());

        }
        else {
            return queryFactory.from(participant)
                    .where(participant.municipalityInitiativeId.eq(initiativeId))
                    .where(participant.participateTime.loe(tillDay))
                    .where(participant.confirmationCode.isNull())
                    .groupBy(participant.participateTime)
                    .map(participant.participateTime, participant.participateTime.count());
        }


    }

    public static void assertSingleAffection(long affectedRows) {
        Assert.isTrue(affectedRows == 1, "Should have affected only one row. Affected: " + affectedRows);
    }




}
