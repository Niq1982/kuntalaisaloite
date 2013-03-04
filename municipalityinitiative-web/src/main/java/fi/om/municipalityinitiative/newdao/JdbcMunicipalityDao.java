package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.sql.QMunicipality;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

@SQLExceptionTranslated
@Transactional(readOnly = true)
public class JdbcMunicipalityDao implements MunicipalityDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<Municipality> findMunicipalities(boolean orderByFinnishNames) {
        PostgresQuery query = queryFactory.from(QMunicipality.municipality)
                .orderBy(orderByFinnishNames
                        ? QMunicipality.municipality.name.asc()
                        : QMunicipality.municipality.nameSv.asc());

        return query.list(municipalityInfoMapper);
    }

    @Override
    public String getMunicipalityEmail(Long municipalityId) {
        return queryFactory.from(QMunicipality.municipality)
                .where(QMunicipality.municipality.id.eq(municipalityId))
                .singleResult(QMunicipality.municipality.email);
    }

    private static Expression<Municipality> municipalityInfoMapper =
            new MappingProjection<Municipality>(Municipality.class, QMunicipality.municipality.all()) {

                @Override
                protected Municipality map(Tuple row) {
                    Municipality dto = new Municipality(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv)
                    );
                    return dto;
                }
            };
}
