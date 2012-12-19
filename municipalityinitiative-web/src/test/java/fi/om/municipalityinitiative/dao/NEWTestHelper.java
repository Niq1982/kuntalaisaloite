package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QSupportVote;
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
    }
}
