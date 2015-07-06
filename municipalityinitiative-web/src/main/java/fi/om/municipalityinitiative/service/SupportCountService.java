package fi.om.municipalityinitiative.service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.SupportCountDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

            Initiative initiative = initiativeDao.get(initiativeForUpdating);

            log.info("Denormalizing support data for init with id " + initiativeForUpdating);

            Map<LocalDate, Long> realSupportVotes = Maps.newHashMap();

            for (Map.Entry<LocalDate, Long> localDateLongEntry : initiativeDao.getSupportVoteCountByDateUntil(initiativeForUpdating, yesterday ).entrySet()) {

                LocalDate supportDate = localDateLongEntry.getKey();
                Long supportCount = localDateLongEntry.getValue();

                addSupportVoteDay(realSupportVotes,
                        supportDate.isBefore(initiative.getStateTime()) ? initiative.getStateTime() : supportDate,
                        supportCount);
            }


            supportCountDao.saveDenormalizedSupportCountDataJson(initiativeForUpdating, toJson(realSupportVotes));

            log.info(toJson(realSupportVotes));

            supportCountDao.saveDenormalizedSupportCountData(initiativeForUpdating, realSupportVotes);

        }

    }

    private void addSupportVoteDay(Map<LocalDate, Long> realSupportVotes, LocalDate localDate, Long supportCount) {

        System.out.println(localDate);
        if (!realSupportVotes.containsKey(localDate)) {
            realSupportVotes.put(localDate, 0l);
        }

        realSupportVotes.put(localDate, realSupportVotes.get(localDate) + supportCount);
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
