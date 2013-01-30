package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
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
    public List<MunicipalityInfo> findMunicipalities() {
        PostgresQuery query = queryFactory.from(QMunicipality.municipality)
                .orderBy(QMunicipality.municipality.name.asc());

        return query.list(municipalityInfoMapper);
    }

    private static Expression<MunicipalityInfo> municipalityInfoMapper =
            new MappingProjection<MunicipalityInfo>(MunicipalityInfo.class, QMunicipality.municipality.all()) {

                @Override
                protected MunicipalityInfo map(Tuple row) {
                    MunicipalityInfo dto = new MunicipalityInfo();
                    dto.setId(row.get(QMunicipality.municipality.id));
                    dto.setName(row.get(QMunicipality.municipality.name));
                    return dto;
                }
            };
}
