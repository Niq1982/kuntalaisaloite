package fi.om.municipalityinitiative.dao;


import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QMunicipalityUser;

import javax.annotation.Resource;

public class JdbcMunicipalityUserDao implements MunicipalityUserDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long assignMunicipalityUser(Long initiativeId, String managementHash) {
        queryFactory.delete(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.initiativeId.eq(initiativeId))
                .execute();

        return queryFactory.insert(QMunicipalityUser.municipalityUser)
                .set(QMunicipalityUser.municipalityUser.initiativeId, initiativeId)
                .set(QMunicipalityUser.municipalityUser.managementHash, managementHash)
                .executeWithKey(QMunicipalityUser.municipalityUser.id);
    }

    @Override
    public Long getInitiativeId(String managementHash) {
        return queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.managementHash.eq(managementHash))
                .uniqueResult(QMunicipalityUser.municipalityUser.initiativeId);
    }

    @Override
    public String getMunicipalityUserHashAttachedToInitiative(Long initiativeId) {

        return queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.initiativeId.eq(initiativeId))
                .uniqueResult(QMunicipalityUser.municipalityUser.managementHash);
    }
}
