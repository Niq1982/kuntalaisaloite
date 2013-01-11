package fi.om.municipalityinitiative.newdao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

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
                .set(composer.franchise, createDto.right_of_voting)
                .set(composer.municipalityId, createDto.municipalityId)
                .set(composer.municipalityInitiativeId, createDto.municipalityInitiativeId)
                .set(composer.name, createDto.name)
                .set(composer.showName, createDto.showName)
                .executeWithKey(composer.id);
    }

    @Override
    public SupportCount countSupports(Long municipalityId) {
        List<Object[]> resultRowArray = queryFactory.query()
                .from(composer)
                .where(composer.municipalityInitiativeId.eq(municipalityId))
                .groupBy(composer.franchise)
                .groupBy(composer.showName)
                .list(composer.id.count(), composer.franchise, composer.showName);

        return parseToSupportCount(resultRowArray);

    }

    // XXX: This looks pretty messed up. Does querydsl support some other way of doing this?
    // Row headers are:
    // count(id) | hasRightOfVoting | showName
    private static SupportCount parseToSupportCount(List<Object[]> resultRowList) {
        SupportCount supportCount = new SupportCount();
        for (Object[] row : resultRowList) {
            if ((Boolean)row[1] == true) {
                if ((Boolean)row[2] == true) {
                    supportCount.getRightOfVoting().setPublicNames((Long) row[0]);
                }
                else {
                    supportCount.getRightOfVoting().setPrivateNames((Long) row[0]);
                }
            }
            else {
                if ((Boolean)row[2] == true) {
                    supportCount.getNoRightOfVoting().setPublicNames((Long) row[0]);
                }
                else {
                    supportCount.getNoRightOfVoting().setPrivateNames((Long) row[0]);
                }
            }
        }
        return supportCount;
    }
}
