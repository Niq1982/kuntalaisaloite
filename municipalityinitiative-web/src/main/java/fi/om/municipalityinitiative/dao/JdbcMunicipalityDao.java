package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.MunicipalityInfo;
import fi.om.municipalityinitiative.dto.MunicipalityInitiativeInfo;
import fi.om.municipalityinitiative.sql.QMunicipality;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

public class JdbcMunicipalityDao implements MunicipalityDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
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
                    dto.id = row.get(QMunicipality.municipality.id);
                    dto.name = row.get(QMunicipality.municipality.name);
                    return dto;
                }
            };
}
