package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
import org.springframework.transaction.annotation.Transactional;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

import javax.annotation.Resource;

import java.util.List;

@SQLExceptionTranslated
public class JdbcMunicipalityInitiativeDao implements MunicipalityInitiativeDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<MunicipalityInitiativeInfo> findNewestFirst() {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .orderBy(municipalityInitiative.id.desc());

        return query.list(initiativeInfoMapping);

    }

    @Override
    @Transactional(readOnly = false)
    public Long create(MunicipalityInitiativeCreateDto dto) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        setInitiativeBasicInfo(dto, insert);
        setContactInfo(dto, insert);

        Long createId = insert.executeWithKey(municipalityInitiative.id);

        return createId;

    }

    private void setContactInfo(MunicipalityInitiativeCreateDto dto, SQLInsertClause insert) {
        insert.set(municipalityInitiative.contactAddress, dto.contactAddress);
        insert.set(municipalityInitiative.contactEmail, dto.contactEmail);
        insert.set(municipalityInitiative.contactPhone, dto.contactPhone);
        insert.set(municipalityInitiative.contactName, dto.contactName);
    }

    private void setInitiativeBasicInfo(MunicipalityInitiativeCreateDto dto, SQLInsertClause insert) {
        insert.set(municipalityInitiative.name, dto.name);
        insert.set(municipalityInitiative.proposal, dto.proposal);
        insert.set(municipalityInitiative.municipalityId, dto.municipalityId);
    }

    @Override
    @Transactional(readOnly = false)
    public MunicipalityInitiativeInfo getById(Long createId) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .where(municipalityInitiative.id.eq(createId));

        return query.uniqueResult(initiativeInfoMapping);

    }


    // Mappings:

    Expression<MunicipalityInitiativeInfo> initiativeInfoMapping =
            new MappingProjection<MunicipalityInitiativeInfo>(MunicipalityInitiativeInfo.class, municipalityInitiative.all()) {
                @Override
                protected MunicipalityInitiativeInfo map(Tuple row) {
                    MunicipalityInitiativeInfo info = new MunicipalityInitiativeInfo();
                    info.name = row.get(municipalityInitiative.name);
                    info.proposal = row.get(municipalityInitiative.proposal);
                    info.contactAddress = row.get(municipalityInitiative.contactAddress);
                    info.contactEmail = row.get(municipalityInitiative.contactEmail);
                    info.contactName = row.get(municipalityInitiative.contactName);
                    info.contactPhone = row.get(municipalityInitiative.contactPhone);
                    return info;
                }
            };
}
