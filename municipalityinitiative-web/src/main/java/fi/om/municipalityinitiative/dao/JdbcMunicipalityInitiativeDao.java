package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeCreateDto;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;

import javax.annotation.Resource;

@SQLExceptionTranslated
public class JdbcMunicipalityInitiativeDao implements  MunicipalityInitiativeDao{

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public QMunicipalityInitiative find() {
        return null;
    }

    @Override
    public QMunicipalityInitiative create(MunicipalityInitiativeCreateDto dto) {
        return null;
    }
}
