package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.sql.QMunicipality;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.dao.JdbcInitiativeDao.assertSingleAffection;

@SQLExceptionTranslated
public class JdbcMunicipalityDao implements MunicipalityDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public List<Municipality> findMunicipalities(boolean orderByFinnishNames) {
        PostgresQuery query = queryFactory.from(QMunicipality.municipality)
                .orderBy(orderByFinnishNames
                        ? QMunicipality.municipality.name.asc()
                        : QMunicipality.municipality.nameSv.asc());

        return query.list(municipalityWrapper);
    }

    @Override
    public String getMunicipalityEmail(Long municipalityId) {
        return queryFactory.from(QMunicipality.municipality)
                .where(QMunicipality.municipality.id.eq(municipalityId))
                .singleResult(QMunicipality.municipality.email);
    }

    @Override
    public Municipality getMunicipality(Long id) {
        return queryFactory.from(QMunicipality.municipality)
                .where(QMunicipality.municipality.id.eq(id))
                .singleResult(municipalityWrapper);
    }

    @Override
    public void updateMunicipality(Long municipalityId, String email, boolean active) {
        assertSingleAffection(queryFactory.update(QMunicipality.municipality)
                .set(QMunicipality.municipality.email, email)
                .set(QMunicipality.municipality.active, active)
                .where(QMunicipality.municipality.id.eq(municipalityId))
                .execute());

    }

    @Override
    public List<MunicipalityEditDto> findMunicipalitiesForEdit() {
        return queryFactory.from(QMunicipality.municipality)
                .orderBy(QMunicipality.municipality.name.asc())
                .list(municipalityEditWrapper);

    }

    private static Expression<Municipality> municipalityWrapper =
            new MappingProjection<Municipality>(Municipality.class, QMunicipality.municipality.all()) {

                @Override
                protected Municipality map(Tuple row) {
                    Municipality dto = new Municipality(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv),
                            row.get(QMunicipality.municipality.active)
                    );
                    return dto;
                }
            };

    private static Expression<MunicipalityEditDto> municipalityEditWrapper =
            new MappingProjection<MunicipalityEditDto>(MunicipalityEditDto.class, QMunicipality.municipality.all()) {

                @Override
                protected MunicipalityEditDto map(Tuple row) {
                    MunicipalityEditDto dto = new MunicipalityEditDto(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv),
                            row.get(QMunicipality.municipality.active),
                            row.get(QMunicipality.municipality.email)
                    );
                    return dto;
                }
            };
}
