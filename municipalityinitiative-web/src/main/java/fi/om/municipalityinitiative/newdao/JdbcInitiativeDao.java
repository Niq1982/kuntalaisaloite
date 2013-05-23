package fi.om.municipalityinitiative.newdao;

import com.mysema.commons.lang.Assert;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.StringPath;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcInitiativeDao implements InitiativeDao {

    private static final BooleanExpression IS_PUBLIC = municipalityInitiative.state.eq(InitiativeState.PUBLISHED)
            .and(municipalityInitiative.fixState.eq(FixState.OK));
    private static final BooleanExpression STATE_IS_DRAFT = municipalityInitiative.state.eq(InitiativeState.DRAFT)
            .or(municipalityInitiative.fixState.eq(FixState.FIX));
    private static final BooleanExpression STATE_IS_REVIEW = municipalityInitiative.state.eq(InitiativeState.REVIEW)
            .or(municipalityInitiative.fixState.eq(FixState.REVIEW));
    private static final BooleanExpression STATE_IS_ACCEPTED = municipalityInitiative.state.eq(InitiativeState.ACCEPTED)
            .and(municipalityInitiative.fixState.eq(FixState.OK));
    private static final BooleanExpression STATE_IS_COLLECTING = municipalityInitiative.sent.isNull().and(IS_PUBLIC);
    private static final BooleanExpression STATE_IS_SENT = municipalityInitiative.sent.isNotNull().and(IS_PUBLIC);

    private final Logger log = LoggerFactory.getLogger(JdbcInitiativeDao.class);

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<InitiativeListInfo> find(InitiativeSearch search) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                ;

        filterByState(query, search);
        filterByTitle(query, search.getSearch());
        filterByMunicipality(query, search.getMunicipality());
        orderBy(query, search.getOrderBy());
        restrictResults(query, search);

        return query.list(Mappings.initiativeListInfoMapping);
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
                query.orderBy(municipalityInitiative.modified.desc(), municipalityInitiative.id.desc());
                break;
            case oldest:
                query.orderBy(municipalityInitiative.modified.asc(), municipalityInitiative.id.asc());
                break;
            case id:
                query.orderBy(municipalityInitiative.id.desc());
                break;
            case mostParticipants:
                query.orderBy(municipalityInitiative.participantCount.desc(), municipalityInitiative.id.desc());
                break;
            case leastParticipants:
                query.orderBy(municipalityInitiative.participantCount.asc(), municipalityInitiative.id.asc());
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

    private static void filterByMunicipality(PostgresQuery query, Long municipalityId) {
        if (municipalityId != null) {
            query.where(municipalityInitiative.municipalityId.eq(municipalityId));
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
            case review:
                query.where(STATE_IS_REVIEW);
                break;
            case accepted:
                query.where(STATE_IS_ACCEPTED);
                break;
            case omAll:
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

        Initiative initiative = query.uniqueResult(Mappings.initiativeInfoMapping);
        if (initiative == null) {
            throw new NotFoundException("initiative", initiativeId);
        }
        return initiative;
    }

    @Override
    public InitiativeCounts getPublicInitiativeCounts(Maybe<Long> municipality) {
        Expression<String> caseBuilder = new CaseBuilder()
                .when(municipalityInitiative.sent.isNull())
                .then(new ConstantImpl<String>(InitiativeSearch.Show.collecting.name()))
                .otherwise(new ConstantImpl<String>(InitiativeSearch.Show.sent.name()));

        SimpleExpression<String> simpleExpression = Expressions.as(caseBuilder, "showCategory");

        PostgresQuery from = queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED))
                .where(municipalityInitiative.fixState.eq(FixState.OK));

        if (municipality.isPresent()) {
            from.where(municipalityInitiative.municipalityId.eq(municipality.get()));
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
    @Transactional(readOnly = true)
    public InitiativeCounts getAllInitiativeCounts(Maybe<Long> municipality) {
        String unknownStateFound = "unknownStateFound";
        Expression<String> caseBuilder = new CaseBuilder()
                .when(STATE_IS_COLLECTING)
                .then(new ConstantImpl<String>(InitiativeSearch.Show.collecting.name()))
                .when(STATE_IS_SENT)
                .then(new ConstantImpl<String>(InitiativeSearch.Show.sent.name()))
                .when(STATE_IS_DRAFT)
                .then(new ConstantImpl<String>(InitiativeState.DRAFT.name()))
                .when(STATE_IS_ACCEPTED)
                .then(new ConstantImpl<String>(InitiativeState.ACCEPTED.name()))
                .when(STATE_IS_REVIEW)
                .then(new ConstantImpl<String>(InitiativeState.REVIEW.name()))
                .otherwise(new ConstantImpl<String>(unknownStateFound));

        SimpleExpression<String> simpleExpression = Expressions.as(caseBuilder, "showCategory");

        PostgresQuery from = queryFactory.from(municipalityInitiative);

        if (municipality.isPresent()) {
            from.where(municipalityInitiative.municipalityId.eq(municipality.get()));
        }

        MaybeHoldingHashMap<String, Long> map = new MaybeHoldingHashMap<>(from
                .groupBy(simpleExpression)
                .map(simpleExpression, municipalityInitiative.count()));

        InitiativeCounts counts = new InitiativeCounts();
        counts.sent = map.get(InitiativeSearch.Show.sent.name()).or(0L);
        counts.collecting = map.get(InitiativeSearch.Show.collecting.name()).or(0L);
        counts.draft = map.get(InitiativeState.DRAFT.name()).or(0L);
        counts.accepted = map.get(InitiativeState.ACCEPTED.name()).or(0L);
        counts.review = map.get(InitiativeState.REVIEW.name()).or(0L);

        if (map.get(unknownStateFound).isPresent()) {
            log.error("Initiatives found with unknown state: " + map.get(unknownStateFound).get());
        }
        return counts;
    }

    @Override
    @Transactional(readOnly = false)
    public Long prepareInitiative(Long municipalityId) {
        return queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.municipalityId, municipalityId)
                .set(municipalityInitiative.type, InitiativeType.UNDEFINED)
                .executeWithKey(municipalityInitiative.id);
    }

    @Override
    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, InitiativeDraftUIEditDto editDto) {

        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.name, editDto.getName())
                .set(municipalityInitiative.proposal, editDto.getProposal())
                .set(municipalityInitiative.modified, CURRENT_TIME)
                .set(municipalityInitiative.extraInfo, editDto.getExtraInfo())
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());

    }

    @Override
    @Transactional(readOnly = false)
    public void updateInitiativeState(Long initiativeId, InitiativeState state) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.state, state)
                .set(municipalityInitiative.stateTimestamp, CURRENT_TIME)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void updateInitiativeFixState(Long initiativeId, FixState fixState) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.fixState, fixState)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void updateInitiativeType(Long initiativeId, InitiativeType initiativeType) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.type, initiativeType)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void markInitiativeAsSent(Long initiativeId) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sent, CURRENT_TIME)
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(municipalityInitiative.sent.isNull())
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void updateModeratorComment(Long initiativeId, String moderatorComment) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.moderatorComment, moderatorComment)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void updateSentComment(Long initiativeId, String sentComment) {
        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sentComment, sentComment)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    @Override
    @Transactional(readOnly = false)
    public void updateExtraInfo(Long initiativeId, String extraInfo) {

        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.extraInfo, extraInfo)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());
    }

    public static void assertSingleAffection(long affectedRows) {
        Assert.isTrue(affectedRows == 1, "Should have affected only one row. Affected: " + affectedRows);
    }

    // Mappings:


}
