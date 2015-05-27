package fi.om.municipalityinitiative.service;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.SupportCountDao;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liisasa on 20.5.2015.
 */
public class SupportCountService {


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
        System.out.println ("Denormalizing support data");

        DateTime yesterday = DateTime.now().minusDays(1);

        LocalDate yesterdayLocalTime = LocalDate.now().minusDays(1);

        List<Long> initiativeIdsForRunningInitiatives = initiativeDao.getRunningInitiativesWithSupport(yesterday);

        for (Long initiativeForUpdating : initiativeIdsForRunningInitiatives) {

            System.out.println("Denormalizing support data for init with id " + initiativeForUpdating);

            Map<LocalDate, Long> supportVoteCountByDateUntil = initiativeDao.getSupportVoteCountByDateUntil(initiativeForUpdating, yesterdayLocalTime );

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
