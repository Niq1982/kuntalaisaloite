package fi.om.municipalityinitiative.service;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.SupportCountDao;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SupportCountService {

    private static final Logger log = LoggerFactory.getLogger(SupportCountService.class);

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    SupportCountDao supportCountDao;


    @Transactional(readOnly = true)
    public String getSupportVotesPerDateJson(Long initiativeId) {
        if (initiativeId == null) {
            return "[]";
        }
        String supportCountDataJson = supportCountDao.getDenormalizedSupportCountDataJson(initiativeId);
        if (Strings.isNullOrEmpty(supportCountDataJson)) {
            return "[]";
        }
        return supportCountDataJson;
    }
    @Transactional
    public void updateDenormalizedSupportCountForInitiatives() {
        // Support counts are denormalized in one-day-delay (today we will denormalize history until yesterday).
        // Therefore the last time we'll denormalize supports for initiative is the day after it's ended.
        log.info("Denormalizing support data");

        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Long> initiativeIdsForRunningInitiatives = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(yesterday);

        for (Long initiativeForUpdating : initiativeIdsForRunningInitiatives) {

            log.info("Denormalizing support data for init with id " + initiativeForUpdating);

            Map<LocalDate, Long> supportVoteCountByDateUntil = initiativeDao.getSupportVoteCountByDateUntil(initiativeForUpdating, yesterday );

            supportCountDao.saveDenormalizedSupportCountDataJson(initiativeForUpdating, toJson(supportVoteCountByDateUntil));

            System.out.println(toJson(supportVoteCountByDateUntil));

            supportCountDao.saveDenormalizedSupportCountData(initiativeForUpdating, supportVoteCountByDateUntil);

        }

    }

    private String toJson(Map<LocalDate, Long> supportVoteCountByDateUntil) {

        TreeMap<LocalDate, Long> orderedMap = new TreeMap<>(supportVoteCountByDateUntil);

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Map.Entry<LocalDate, Long> localDateEntry : orderedMap.entrySet()) {
            if (builder.length() > 1) {
                builder.append(",");
            }
            builder.append(String.format("{\"d\":\"%s\",\"n\":%d}", localDateEntry.getKey(), localDateEntry.getValue()));
        }

        builder.append("]");
        return builder.toString();
    }
}
