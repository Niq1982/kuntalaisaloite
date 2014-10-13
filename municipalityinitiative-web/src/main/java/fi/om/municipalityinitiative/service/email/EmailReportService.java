package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class EmailReportService {


    private static final ReadablePeriod QUARTER_REPORT_INTERVAL = Months.THREE;
    public static final Days ACCEPTED_BUT_NOT_PUBLISHED_EMAIL_DELAY = Days.days(14);
    @Resource
    private EmailService emailService;

    @Resource
    private InitiativeDao initiativeDao;

    @Transactional(readOnly = false)
    public void sendReportEmailsForInitiativesAcceptedButNotPublished() {
        for (Initiative initiative : getInitiativesAcceptedButNotPublishedIn(ACCEPTED_BUT_NOT_PUBLISHED_EMAIL_DELAY)) {
            emailService.sendInitiativeAcceptedButNotPublishedEmail(initiative);
            initiativeDao.markInitiativeReportSent(initiative.getId(), EmailReportType.IN_ACCEPTED, new DateTime());
        }
    }

    private List<Initiative> getInitiativesAcceptedButNotPublishedIn(ReadablePeriod days) {
        List<Initiative> result = Lists.newArrayList();

        for (Initiative initiative : initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, new LocalDate().minus(days))) {
            if (initiative.getLastEmailReportType() == null) {
                result.add(initiative);
            }
        }
        return result;

    }

    @Transactional(readOnly = false)
    public void sendQuarterReports() {
        for (Initiative initiative : findInitiativesForQuarterReport()) {
            emailService.sendQuarterReport(initiative);
            initiativeDao.markInitiativeReportSent(initiative.getId(), EmailReportType.QUARTER_REPORT, new DateTime());
        }
    }

    private List<Initiative> findInitiativesForQuarterReport() {
        List<Initiative> result = Lists.newArrayList();

        for (Initiative initiative : initiativeDao.findAllPublishedNotSent()) {
            if (quarterReporPreviouslytSentThreeMonthsAgo(initiative)
                    || quarterReportNeverSentAndTimeToSend(initiative)) {
                result.add(initiative);
            }
        }
        return result;
    }

    private boolean quarterReportNeverSentAndTimeToSend(Initiative initiative) {
        return (initiative.getLastEmailReportType() == null || initiative.getLastEmailReportType() == EmailReportType.IN_ACCEPTED)
                && initiative.getStateTime().isBefore(new LocalDate().minus(QUARTER_REPORT_INTERVAL));
    }

    private boolean quarterReporPreviouslytSentThreeMonthsAgo(Initiative initiative) {
        return initiative.getLastEmailReportType() == EmailReportType.QUARTER_REPORT
                && initiative.getLastEmailReportTime().toLocalDate().isBefore(new LocalDate().minus(QUARTER_REPORT_INTERVAL));
    }

}
