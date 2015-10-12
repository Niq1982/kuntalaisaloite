package fi.om.municipalityinitiative.dao;


import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QMunicipalityUser;

import javax.annotation.Resource;

public class JdbcMunicipalityUserDao implements MunicipalityUserDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long createMunicipalityUser(Long initiativeId, String managementHash) {
        return queryFactory.insert(QMunicipalityUser.municipalityUser)
                .set(QMunicipalityUser.municipalityUser.initiativeId, initiativeId)
                .set(QMunicipalityUser.municipalityUser.managementHash, managementHash)
                .executeWithKey(QMunicipalityUser.municipalityUser.id);
    }

    @Override
    public Long getInitiativeId(String managementHash) {
        Long initiativeId = queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.managementHash.eq(managementHash))
                .uniqueResult(QMunicipalityUser.municipalityUser.initiativeId);
        return initiativeId;
    }

    @Override
    public String getMunicipalityUserHashAttachedToInitiative(Long initiativeId) {
        String hash =  queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.initiativeId.eq(initiativeId))
                .uniqueResult(QMunicipalityUser.municipalityUser.managementHash);

        return hash;
    }
}
