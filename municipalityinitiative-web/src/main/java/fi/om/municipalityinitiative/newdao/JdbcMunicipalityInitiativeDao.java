package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipality;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcMunicipalityInitiativeDao implements MunicipalityInitiativeDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<MunicipalityInitiativeInfo> findAllNewestFirst() {
        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .orderBy(municipalityInitiative.id.desc());

        return query.list(initiativeInfoMapping);

    }

    @Override
    @Transactional(readOnly = false)
    public Long create(MunicipalityInitiativeCreateDto dto) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        setInitiativeBasicInfo(dto, insert);
        setContactInfo(dto, insert);

        return insert.executeWithKey(municipalityInitiative.id);

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
    public MunicipalityInitiativeInfo getById(Long createId) {

        PostgresQuery query = queryFactory
                .from(municipalityInitiative)
                .leftJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .where(municipalityInitiative.id.eq(createId));

        return query.uniqueResult(initiativeInfoMapping);

    }


    // Mappings:

    Expression<MunicipalityInitiativeInfo> initiativeInfoMapping =
            new MappingProjection<MunicipalityInitiativeInfo>(MunicipalityInitiativeInfo.class, municipalityInitiative.all(), QMunicipality.municipality.all()) {
                @Override
                protected MunicipalityInitiativeInfo map(Tuple row) {
                    MunicipalityInitiativeInfo info = new MunicipalityInitiativeInfo();
                    info.setName(row.get(municipalityInitiative.name));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setContactAddress(row.get(municipalityInitiative.contactAddress));
                    info.setContactEmail(row.get(municipalityInitiative.contactEmail));
                    info.setContactName(row.get(municipalityInitiative.contactName));
                    info.setContactPhone(row.get(municipalityInitiative.contactPhone));
                    info.setMunicipalityName(row.get(QMunicipality.municipality.name));
                    info.setCreateTime(row.get(municipalityInitiative.modified));
                    return info;
                }
            };
}
