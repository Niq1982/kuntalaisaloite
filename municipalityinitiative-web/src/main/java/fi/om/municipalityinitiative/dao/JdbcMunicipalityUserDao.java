package fi.om.municipalityinitiative.dao;


import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.Visitor;
import fi.om.municipalityinitiative.dto.service.MunicipalityLoginDetails;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityUser;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;

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
    public void assignMunicipalityUserLoginHash(Long initiativeId,
                                                String managementHash,
                                                String managementLoginHash,
                                                DateTime now) {

        assertSingleAffection(
                queryFactory.update(QMunicipalityUser.municipalityUser)
                        .set(QMunicipalityUser.municipalityUser.loginHash, managementLoginHash)
                        .set(QMunicipalityUser.municipalityUser.loginHashCreateTime, now)
                        .where(QMunicipalityUser.municipalityUser.initiativeId.eq(initiativeId))
                        .where(QMunicipalityUser.municipalityUser.managementHash.eq(managementHash))
                        .execute()
        );

    }

    @Override
    public Long getInitiativeId(String managementHash) {
        return queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.managementHash.eq(managementHash))
                .uniqueResult(QMunicipalityUser.municipalityUser.initiativeId);
    }

    @Override
    public Long getInitiativeId(String managementHash, String managementLoginHash) {
        return queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.managementHash.eq(managementHash))
                .where(QMunicipalityUser.municipalityUser.loginHash.eq(managementLoginHash))
                .where(QMunicipalityUser.municipalityUser.loginHashCreateTime.after(DateTime.now().minusHours(1)))
                .uniqueResult(QMunicipalityUser.municipalityUser.initiativeId);
    }


    @Override
    public MunicipalityLoginDetails getMunicipalityUserHashAttachedToInitiative(Long initiativeId) {

        return queryFactory.from(QMunicipalityUser.municipalityUser)
                .where(QMunicipalityUser.municipalityUser.initiativeId.eq(initiativeId))
                .uniqueResult(new MappingProjection<MunicipalityLoginDetails>(MunicipalityLoginDetails.class, QMunicipalityUser.municipalityUser.all()) {
                    @Override
                    protected MunicipalityLoginDetails map(Tuple row) {
                        return new MunicipalityLoginDetails(
                                row.get(QMunicipalityUser.municipalityUser.managementHash),
                                row.get(QMunicipalityUser.municipalityUser.loginHash)
                        );
                    }
                });
    }
}
