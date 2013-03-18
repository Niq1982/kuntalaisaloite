package fi.om.municipalityinitiative.newdao;

import com.mysema.commons.lang.Assert;
import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLInsertClause;
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
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.InitiativeEditDto;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QParticipant;
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

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<InitiativeListInfo> find(InitiativeSearch search) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .leftJoin(municipalityInitiative.municipalityInitiativeAuthorFk, QParticipant.participant);

        filterByTitle(query, search.getSearch());
        filterByMunicipality(query, search.getMunicipality());
        filterByState(query, search);
        restrictResults(query, search);
        orderBy(query, search.getOrderBy());

        List<InitiativeListInfo> list = query.list(initiativeListInfoMapping);

        return list;

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
                query.where(municipalityInitiative.sent.isNull())
                     .where(municipalityInitiative.managementHash.isNotNull());
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
    @Transactional(readOnly = false)
    public Long create(InitiativeCreateDto dto) {

        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        setInitiativeBasicInfo(dto, insert);
        setContactInfo(dto, insert);

        insert.set(municipalityInitiative.newAuthorId, PREPARATION_ID);

        Long initiativeId = insert.executeWithKey(municipalityInitiative.id);

        Long authorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.initiativeId, initiativeId)
                .executeWithKey(QAuthor.author.id);

        queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.newAuthorId, authorId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute();

        return initiativeId;

    }

    private void setContactInfo(InitiativeCreateDto dto, SQLInsertClause insert) {
        insert.set(municipalityInitiative.contactAddress, dto.contactAddress);
        insert.set(municipalityInitiative.contactEmail, dto.contactEmail);
        insert.set(municipalityInitiative.contactPhone, dto.contactPhone);
        insert.set(municipalityInitiative.contactName, dto.contactName);
    }

    private void setInitiativeBasicInfo(InitiativeCreateDto dto, SQLInsertClause insert) {
        insert.set(municipalityInitiative.authorId, PREPARATION_ID);
        insert.set(municipalityInitiative.name, dto.name);
        insert.set(municipalityInitiative.proposal, dto.proposal);
        insert.set(municipalityInitiative.municipalityId, dto.municipalityId);
        if (dto.managementHash.isPresent()) {
            insert.set(municipalityInitiative.managementHash, dto.managementHash.get());
        }
        else {
            insert.set(municipalityInitiative.sent, CURRENT_TIME);
        }
    }

    @Override
    public Initiative getById(Long id) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeAuthorFk, QParticipant.participant)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(municipalityInitiative.id.eq(id));

        Initiative initiative = query.uniqueResult(initiativeInfoMapping);
        if (initiative == null) {
            throw new NotFoundException("initiative", id);
        }
        return initiative;
    }

    @Override
    @Transactional(readOnly = false)
    public void markAsSendedAndUpdateContactInfo(Long initiativeId, ContactInfo contactInfo) {

        long affectedRows = queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sent, new DateTime())
                .set(municipalityInitiative.contactPhone, contactInfo.getPhone())
                .set(municipalityInitiative.contactEmail, contactInfo.getEmail())
                .set(municipalityInitiative.contactAddress, contactInfo.getAddress())
                .set(municipalityInitiative.contactName, contactInfo.getName())
                .where(municipalityInitiative.id.eq(initiativeId))
                .where(municipalityInitiative.managementHash.isNotNull())
                .where(municipalityInitiative.sent.isNull())
                .execute();

        if (affectedRows != 1) {
            throw new NotCollectableException("Initiative already sent or not collectable");
        }

    }

    @Override
    public void assignAuthor(Long municipalityInitiativeId, Long participantId) {
        long affectedRows = queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, participantId)
                .where(municipalityInitiative.authorId.eq(PREPARATION_ID))
                .where(municipalityInitiative.id.eq(municipalityInitiativeId))
                .execute();
        Assert.isTrue(affectedRows == 1, "About to update " + affectedRows + " instead of 1.");
    }

    @Override
    public ContactInfo getContactInfo(Long initiativeId) {
        return queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.id.eq(initiativeId))
                .uniqueResult(contactInfoMapping);
    }

    @Override
    public InitiativeCounts getInitiativeCounts(Maybe<Long> municipality) {
        Expression<String> caseBuilder = new CaseBuilder()
                .when(municipalityInitiative.sent.isNull().and(municipalityInitiative.managementHash.isNotNull()))
                    .then(new ConstantImpl<String>(InitiativeSearch.Show.collecting.name()))
                .otherwise(new ConstantImpl<String>(InitiativeSearch.Show.sent.name()));

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
        return counts;
    }

    @Override
    @Transactional(readOnly = false)
    public Long prepareInitiative(Long municipalityId, String managementHash) {
        Long initiativeId = queryFactory.insert(municipalityInitiative)
                .set(municipalityInitiative.managementHash, managementHash) // XXX: Remove when not needed
                .set(municipalityInitiative.municipalityId, municipalityId)
                .set(municipalityInitiative.authorId, PREPARATION_ID)
                .set(municipalityInitiative.newAuthorId, PREPARATION_ID)
                .executeWithKey(municipalityInitiative.id);

        Long newAuthorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.initiativeId, initiativeId)
                .set(QAuthor.author.managementHash, managementHash)
                .executeWithKey(QAuthor.author.id);

        Assert.isTrue(queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.newAuthorId, newAuthorId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute() == 1, "Should have saved author to initiative");

        return initiativeId;
    }

    @Override
    @Transactional(readOnly = false)
    public InitiativeEditDto getInitiativeForEdit(Long initiativeId) {
        return queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeAuthorFk, QParticipant.participant)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .leftJoin(municipalityInitiative.initiativeAuthorFk, QAuthor.author)
                .where(municipalityInitiative.id.eq(initiativeId))

                .uniqueResult(initiativeEditMapping);
    }

    // Mappings:

    Expression<InitiativeEditDto> initiativeEditMapping =
            new MappingProjection<InitiativeEditDto>(InitiativeEditDto.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected InitiativeEditDto map(Tuple row) {
                    InitiativeEditDto info = new InitiativeEditDto(
                            row.get(municipalityInitiative.id),
                            new Municipality(
                                    row.get(QMunicipality.municipality.id),
                                    row.get(QMunicipality.municipality.name),
                                    row.get(QMunicipality.municipality.nameSv))
                    );
                    info.setManagementHash(row.get(QAuthor.author.managementHash));
                    info.setName(row.get(municipalityInitiative.name));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setShowName(row.get(QParticipant.participant.showName));
                    info.setContactInfo(new ContactInfo());
                    return info;
                }
            };

    private Expression<ContactInfo> contactInfoMapping =
            new MappingProjection<ContactInfo>(ContactInfo.class,
                    municipalityInitiative.all()) {

                @Override
                protected ContactInfo map(Tuple row) {
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(municipalityInitiative.contactAddress));
                    contactInfo.setEmail(row.get(municipalityInitiative.contactEmail));
                    contactInfo.setName(row.get(municipalityInitiative.contactName));
                    contactInfo.setPhone(row.get(municipalityInitiative.contactPhone));
                    return contactInfo;
                }
            };

    Expression<Initiative> initiativeInfoMapping =
            new MappingProjection<Initiative>(Initiative.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all()) {
                @Override
                protected Initiative map(Tuple row) {
                    Initiative info = new Initiative();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(new Municipality(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv))
                    );
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setAuthorName(row.get(QParticipant.participant.name));
                    info.setShowName(row.get(QParticipant.participant.showName));
                    info.setManagementHash(Maybe.fromNullable(row.get(municipalityInitiative.managementHash)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
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
                    QParticipant.participant.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(new Municipality(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv))
                    );
                    info.setCollectable(row.get(municipalityInitiative.managementHash) != null);
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    return info;
                }
            };
}
