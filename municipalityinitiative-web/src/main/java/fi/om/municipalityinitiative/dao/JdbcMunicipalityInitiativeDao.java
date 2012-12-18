package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.springframework.transaction.annotation.Transactional;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

import javax.annotation.Resource;

@SQLExceptionTranslated
public class JdbcMunicipalityInitiativeDao implements MunicipalityInitiativeDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public QMunicipalityInitiative find() {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public QMunicipalityInitiative create(MunicipalityInitiativeCreateDto dto) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        setInitiativeBasicInfo(dto, insert);
        setContactInfo(dto, insert);

        Long createId = insert.executeWithKey(municipalityInitiative.id);

        return null;

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
        insert.set(municipalityInitiative.municipalityId, -5L);
    }
}
