package fi.om.municipalityinitiative.newdao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;

import javax.annotation.Resource;

public class JdbcComposerDao implements ComposerDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long add(ComposerCreateDto createDto) {
        return null;
    }

    @Override
    public SupportCount countSupports(Long municipalityId) {
        return null;
    }
}
