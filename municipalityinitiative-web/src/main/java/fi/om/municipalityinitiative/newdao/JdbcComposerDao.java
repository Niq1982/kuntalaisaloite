package fi.om.municipalityinitiative.newdao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.expr.Wildcard;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Map;

import static fi.om.municipalityinitiative.sql.QComposer.composer;

@Transactional(readOnly = true)
@SQLExceptionTranslated
public class JdbcComposerDao implements ComposerDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = false)
    public Long add(ComposerCreateDto createDto) {
        return queryFactory.insert(composer)
                .set(composer.isMunicipalityCitizen, createDto.right_of_voting)
                .set(composer.municipalityId, createDto.municipalityId)
                .set(composer.municipalityInitiativeId, createDto.municipalityInitiativeId)
                .set(composer.name, createDto.name)
                .set(composer.showName, createDto.showName)
                .executeWithKey(composer.id);
    }

    @Override
    public SupportCount countSupports(Long municipalityId) {
        Map<Boolean, Long> map = queryFactory.from(composer)
                .where(composer.municipalityInitiativeId.eq(municipalityId))
                .groupBy(composer.isMunicipalityCitizen)
                .map(composer.isMunicipalityCitizen, Wildcard.count);

        SupportCount supportCount = new SupportCount();
        supportCount.no_right_of_voting = map.get(false);
        supportCount.right_of_voting = map.get(true);
        return supportCount;
    }
}
