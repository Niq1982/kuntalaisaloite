package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import org.joda.time.LocalDate;

import javax.annotation.Resource;
import java.util.Map;

import static fi.om.municipalityinitiative.sql.QInitiativeSupportVoteDay.initiativeSupportVoteDay;
import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

/**
 * Created by liisasa on 20.5.2015.
 */
public class SupportCountDao {

    @Resource
    PostgresQueryFactory queryFactory;

    public String getDenormalizedSupportCountDataJson(Long initiativeId) {
        return queryFactory.from(municipalityInitiative)
                .where(municipalityInitiative.id.eq(initiativeId))
                .singleResult(municipalityInitiative.supportCountData);
    }

    public void saveDenormalizedSupportCountData(Long initiativeId, Map<LocalDate, Long> denormalizedData) {
        if (!denormalizedData.isEmpty()) {
            queryFactory.delete(initiativeSupportVoteDay).where(initiativeSupportVoteDay.initiativeId.eq(initiativeId)).execute();

            SQLInsertClause insert = queryFactory.insert(initiativeSupportVoteDay);
            for (Map.Entry<LocalDate, Long> localDateLongEntry : denormalizedData.entrySet()) {
                insert.set(initiativeSupportVoteDay.initiativeId, initiativeId)
                        .set(initiativeSupportVoteDay.supportCount, localDateLongEntry.getValue().intValue())
                        .set(initiativeSupportVoteDay.supportDate, localDateLongEntry.getKey())
                        .addBatch();
            }

            insert.execute();
        }
    }

    public void saveDenormalizedSupportCountDataJson(Long initiativeForUpdating, String s) {
        queryFactory.update(municipalityInitiative).set(municipalityInitiative.supportCountData, s)
                .where(municipalityInitiative.id.eq(initiativeForUpdating))
                .execute();
    }

    public Map<LocalDate, Integer> getDenormalizedSupportCountData(Long initiativeId) {
        return queryFactory.from(initiativeSupportVoteDay)
                .where(initiativeSupportVoteDay.initiativeId.eq(initiativeId))
                    .map(initiativeSupportVoteDay.supportDate, initiativeSupportVoteDay.supportCount);
    }
}
