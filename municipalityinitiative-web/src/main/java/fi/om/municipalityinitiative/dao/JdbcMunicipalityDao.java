package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityInfoDto;
import fi.om.municipalityinitiative.sql.QMunicipality;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;

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
    public Municipality getMunicipality(Long id) {
        return queryFactory.from(QMunicipality.municipality)
                .where(QMunicipality.municipality.id.eq(id))
                .uniqueResult(municipalityWrapper);
    }

    @Override
    public void updateMunicipality(Long municipalityId, String email, boolean active, String descriptionFi, String descriptionSv) {
        assertSingleAffection(queryFactory.update(QMunicipality.municipality)
                .set(QMunicipality.municipality.email, email)
                .set(QMunicipality.municipality.active, active)
                .set(QMunicipality.municipality.description, descriptionFi)
                .set(QMunicipality.municipality.descriptionSv, descriptionSv)
                .where(QMunicipality.municipality.id.eq(municipalityId))
                .execute());

    }


    @Override
    public MunicipalityInfoDto getMunicipalityInfo(Long municipality) {
        return queryFactory.from(QMunicipality.municipality)
                .where(QMunicipality.municipality.id.eq(municipality))
                .uniqueResult(municipalityInfoWrapper);
    }

    @Override
    public List<MunicipalityInfoDto> findMunicipalitiesForEdit() {
        return queryFactory.from(QMunicipality.municipality)
                .orderBy(QMunicipality.municipality.name.asc())
                .list(municipalityInfoWrapper);

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

    private static Expression<MunicipalityInfoDto> municipalityInfoWrapper =
            new MappingProjection<MunicipalityInfoDto>(MunicipalityInfoDto.class, QMunicipality.municipality.all()) {

                @Override
                protected MunicipalityInfoDto map(Tuple row) {
                    MunicipalityInfoDto dto = new MunicipalityInfoDto(
                            row.get(QMunicipality.municipality.id),
                            row.get(QMunicipality.municipality.name),
                            row.get(QMunicipality.municipality.nameSv),
                            row.get(QMunicipality.municipality.active),
                            row.get(QMunicipality.municipality.email),
                            row.get(QMunicipality.municipality.description),
                            row.get(QMunicipality.municipality.descriptionSv)
                    );
                    return dto;
                }
            };
}
