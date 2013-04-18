package fi.om.municipalityinitiative.newdao;

import com.google.common.base.Strings;
import com.mysema.commons.lang.Assert;
import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.CaseBuilder;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.StringPath;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.MaybeHoldingHashMap;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcInitiativeDao implements InitiativeDao {

    // This is for querydsl for not being able to create a row with DEFERRED not-null-check value being null..
    // Querydsl always assigned some value to it and setting it to null was not an option.
    private static final Long PREPARATION_ID = -1L;

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);
    public static final QMunicipality INITIATIVE_MUNICIPALITY = new QMunicipality("initiativeMunicipality");
    public static final QMunicipality AUTHOR_MUNICIPALITY = new QMunicipality("authorMunicipality");

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<InitiativeListInfo> find(InitiativeSearch search) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .innerJoin(municipalityInitiative.initiativeAuthorFk, QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED))
                ;

        filterByTitle(query, search.getSearch());
        filterByMunicipality(query, search.getMunicipality());
        filterByState(query, search);
        restrictResults(query, search);
        orderBy(query, search.getOrderBy());

        return query.list(initiativeListInfoMapping);

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
        switch (search.getShow()) {
            case sent:
                query.where(municipalityInitiative.sent.isNotNull());
                break;
            case collecting:
                query.where(municipalityInitiative.sent.isNull());
                break;
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
    public Initiative getByIdWithOriginalAuthor(Long initiativeId) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, INITIATIVE_MUNICIPALITY)
                .innerJoin(municipalityInitiative.initiativeAuthorFk, QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, AUTHOR_MUNICIPALITY)
                .where(municipalityInitiative.id.eq(initiativeId));

        Initiative initiative = query.uniqueResult(initiativeInfoMapping);
        if (initiative == null) {
            throw new NotFoundException("initiative", initiativeId);
        }
        return initiative;
    }

    @Override
    public Initiative getById(Long initiativeId, String authorsManagementHash) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, INITIATIVE_MUNICIPALITY)
                .innerJoin(municipalityInitiative._participantMunicipalityInitiativeIdFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, AUTHOR_MUNICIPALITY)
                .innerJoin(QParticipant.participant._authorParticipantFk, QAuthor.author)
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(QAuthor.author.managementHash.eq(authorsManagementHash));

        Initiative initiative = query.uniqueResult(initiativeInfoMapping);
        if (initiative == null) {
            throw new NotFoundException("initiative", "Invalid managementhash or initiative id");
        }
        return initiative;
    }

    @Override
    @Transactional(readOnly = false)
    public void assignAuthor(Long initiativeId, Long participantId, String managementHash) {

        Long newAuthorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.managementHash, managementHash)
                .set(QAuthor.author.participantId, participantId)
                .executeWithKey(QAuthor.author.id);

        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, newAuthorId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(municipalityInitiative.authorId.eq(PREPARATION_ID))
                .execute());
    }

    @Override
    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        Expression<String> caseBuilder = new CaseBuilder()
                .when(municipalityInitiative.sent.isNull())
                .then(new ConstantImpl<String>(InitiativeSearch.Show.collecting.name()))
                .otherwise(new ConstantImpl<String>(InitiativeSearch.Show.sent.name()));

        SimpleExpression<String> simpleExpression = Expressions.as(caseBuilder, "showCategory");

        PostgresQuery from = queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.state.eq(InitiativeState.PUBLISHED));

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
    @Transactional(readOnly = false)
    public Long prepareInitiative(Long municipalityId) {
        return queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.municipalityId, municipalityId)
                .set(municipalityInitiative.authorId, PREPARATION_ID)
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
                .set(municipalityInitiative.comment, editDto.getExtraInfo())
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());

        Long authorId = queryFactory
                .from(municipalityInitiative)
                .where(municipalityInitiative.id.eq(initiativeId))
                .uniqueResult(municipalityInitiative.authorId);

        assertSingleAffection(queryFactory.update(QAuthor.author)
                .set(QAuthor.author.name, editDto.getContactInfo().getName())
                .set(QAuthor.author.address, editDto.getContactInfo().getAddress())
                .set(QAuthor.author.phone, editDto.getContactInfo().getPhone())
                .where(QAuthor.author.id.eq(authorId))
                .execute());

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .set(QParticipant.participant.showName, editDto.getShowName())
                .set(QParticipant.participant.name, editDto.getContactInfo().getName())
                .set(QParticipant.participant.email, editDto.getContactInfo().getEmail())
                .where(QParticipant.participant.municipalityInitiativeId.eq(initiativeId))
                .execute());

    }

    @Override
    public Author getAuthorInformation(Long initiativeId, String managementHash) {
        return queryFactory.from(municipalityInitiative)
                .innerJoin(municipalityInitiative._participantMunicipalityInitiativeIdFk, QParticipant.participant)
                .innerJoin(QParticipant.participant._authorParticipantFk, QAuthor.author)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(QAuthor.author.managementHash.eq(managementHash))
                .uniqueResult(authorMapping);
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
    public void updateAcceptedInitiative(Long initiativeId, String managementHash, InitiativeUIUpdateDto updateDto) {

        Long participantId = queryFactory.from(QParticipant.participant)
                .where(QParticipant.participant.municipalityInitiativeId.eq(initiativeId))
                .innerJoin(QParticipant.participant._authorParticipantFk, QAuthor.author)
                .where(QAuthor.author.managementHash.eq(managementHash))
                .singleResult(QParticipant.participant.id);

        assertSingleAffection(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.comment, updateDto.getExtraInfo())
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute());

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .set(QParticipant.participant.showName, Boolean.TRUE.equals(updateDto.getShowName()))
                .set(QParticipant.participant.name, updateDto.getContactInfo().getName())
                .set(QParticipant.participant.email, updateDto.getContactInfo().getEmail())
                .where(QParticipant.participant.id.eq(participantId))
                .execute());

        assertSingleAffection(queryFactory.update(QAuthor.author)
                .set(QAuthor.author.address, updateDto.getContactInfo().getAddress())
                .set(QAuthor.author.name, updateDto.getContactInfo().getName())
                .set(QAuthor.author.phone, updateDto.getContactInfo().getPhone())
                .where(QAuthor.author.participantId.eq(participantId))
                .execute());
    }

    public static void assertSingleAffection(long affectedRows) {
        Assert.isTrue(affectedRows == 1, "Should have affected only one row. Affected: " + affectedRows);
    }

    // Mappings:

    private Expression<Author> authorMapping =
            new MappingProjection<Author>(Author.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected Author map(Tuple row) {

                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(QAuthor.author.address));
                    contactInfo.setPhone(row.get(QAuthor.author.phone));
                    contactInfo.setEmail(row.get(QParticipant.participant.email));
                    contactInfo.setName(row.get(QParticipant.participant.name));

                    Author author = new Author();
                    author.setId(row.get(QAuthor.author.id));
                    author.setContactInfo(contactInfo);
                    author.setMunicipality(parseMunicipality(row));

                    return author;


                }
            };


    private static Municipality parseMunicipality(Tuple row) {
        return new Municipality(
                row.get(QMunicipality.municipality.id),
                row.get(QMunicipality.municipality.name),
                row.get(QMunicipality.municipality.nameSv));
    }

    private static Municipality parseMunicipality(Tuple row, QMunicipality municipality) {
        return new Municipality(
                row.get(municipality.id),
                row.get(municipality.name),
                row.get(municipality.nameSv));
    }

    Expression<Initiative> initiativeInfoMapping =
            new MappingProjection<Initiative>(Initiative.class,
                    municipalityInitiative.all(),
                    AUTHOR_MUNICIPALITY.all(),
                    INITIATIVE_MUNICIPALITY.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected Initiative map(Tuple row) {
                    Initiative info = new Initiative();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row, INITIATIVE_MUNICIPALITY)
                    );
                    info.setType(row.get(municipalityInitiative.type));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setAuthorName(row.get(QParticipant.participant.name));
                    info.setShowName(row.get(QParticipant.participant.showName));
                    info.setManagementHash(Maybe.of(row.get(QAuthor.author.managementHash)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setState(row.get(municipalityInitiative.state));
                    info.setStateTime(row.get(municipalityInitiative.stateTimestamp).toLocalDate());
                    info.setComment(row.get(municipalityInitiative.comment));
                    info.setModeratorComment(Strings.nullToEmpty(row.get(municipalityInitiative.moderatorComment)));

                    Author author = new Author();
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(QAuthor.author.address));
                    contactInfo.setPhone(row.get(QAuthor.author.phone));
                    contactInfo.setName(row.get(QAuthor.author.name));
                    contactInfo.setEmail(row.get(QParticipant.participant.email));
                    author.setId(row.get(QAuthor.author.id));
                    author.setContactInfo(contactInfo);
                    author.setMunicipality(parseMunicipality(row, AUTHOR_MUNICIPALITY));

                    info.setAuthor(author);

                    return info;
                }
            };

    private static Maybe<LocalDate> maybeLocalDate(DateTime sentTime) {
        if (sentTime != null) {
            return Maybe.of(sentTime.toLocalDate());
        }
        return Maybe.absent();
    }

    Expression<InitiativeListInfo> initiativeListInfoMapping =
            new MappingProjection<InitiativeListInfo>(InitiativeListInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row));
                    info.setCollectable(InitiativeType.isCollectable(row.get(municipalityInitiative.type)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    info.setType(row.get(municipalityInitiative.type));
                    return info;
                }
            };
}
