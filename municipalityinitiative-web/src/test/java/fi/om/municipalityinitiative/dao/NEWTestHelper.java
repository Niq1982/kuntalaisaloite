package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QComposer;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class NEWTestHelper {

    @Resource
    PostgresQueryFactory queryFactory;

    public NEWTestHelper() {
    }

    public NEWTestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QComposer.composer).execute();
        queryFactory.delete(QMunicipality.municipality).execute();
    }

    @Transactional
    public Long createTestMunicipality(String name) {
        return queryFactory.insert(QMunicipality.municipality)
                    .set(QMunicipality.municipality.name, name)
                .executeWithKey(QMunicipality.municipality.id);
    }
}
