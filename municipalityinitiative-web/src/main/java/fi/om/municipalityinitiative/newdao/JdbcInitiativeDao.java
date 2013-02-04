package fi.om.municipalityinitiative.newdao;

import com.mysema.commons.lang.Assert;
import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.path.StringPath;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
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
    public List<InitiativeListInfo> findNewestFirst(InitiativeSearch search) {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .leftJoin(municipalityInitiative.municipalityInitiativeAuthorFk, QParticipant.participant)
                .orderBy(municipalityInitiative.id.desc());

        searchParameters(query, search);

        return query.list(initiativeListInfoMapping);

    }

    private void searchParameters(PostgresQuery query, InitiativeSearch search) {
        if (search.getMunicipality() != null) {
            query.where(municipalityInitiative.municipalityId.eq(search.getMunicipality()));
        }
        if (search.getSearch() != null) {
            query.where(toLikePredicate(municipalityInitiative.name, search.getSearch()));
        }
    }

    private Predicate toLikePredicate(StringPath name, String search) {
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

        return insert.executeWithKey(municipalityInitiative.id);

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
    public InitiativeViewInfo getById(Long id) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeAuthorFk, QParticipant.participant)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(municipalityInitiative.id.eq(id));

        return query.uniqueResult(initiativeInfoMapping);
    }

    @Override
    @Transactional(readOnly = false)
    public void markAsSended(Long initiativeId) {

        long affectedRows = queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.sent, new DateTime())
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

    // Mappings:

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

    Expression<InitiativeViewInfo> initiativeInfoMapping =
            new MappingProjection<InitiativeViewInfo>(InitiativeViewInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all()) {
                @Override
                protected InitiativeViewInfo map(Tuple row) {
                    InitiativeViewInfo info = new InitiativeViewInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified));
                    info.setMunicipalityName(row.get(QMunicipality.municipality.name));
                    info.setMunicipalityId(row.get(QMunicipality.municipality.id));
                    info.setName(row.get(municipalityInitiative.name));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setAuthorName(row.get(QParticipant.participant.name));
                    info.setShowName(row.get(QParticipant.participant.showName));
                    info.setMaybeManagementHash(Maybe.fromNullable(row.get(municipalityInitiative.managementHash)));
                    info.setSentTime(Maybe.fromNullable(row.get(municipalityInitiative.sent)));
                    return info;
                }
            };

    Expression<InitiativeListInfo> initiativeListInfoMapping =
            new MappingProjection<InitiativeListInfo>(InitiativeListInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified));
                    info.setMunicipalityName(row.get(QMunicipality.municipality.name));
                    info.setName(row.get(municipalityInitiative.name));
                    return info;
                }
            };
}
