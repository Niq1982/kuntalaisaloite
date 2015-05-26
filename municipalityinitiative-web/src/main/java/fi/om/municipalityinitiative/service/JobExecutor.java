package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.service.email.EmailReportService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class JobExecutor {

    private static final String EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * *";

    @Resource
    private EmailReportService emailReportService;

    @Resource
    private SupportCountService supportCountService;


    @PostConstruct
    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    public void sendReportEmailsForInitiativesAcceptedButNotPublished() {
        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
    }

    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    @PostConstruct
    public void sendQuarterReportsForInitiatives() {
        emailReportService.sendQuarterReports();
    }

    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    @PostConstruct
    public void updateDenormalizedSupportCountForInitiatives() {
        supportCountService.updateDenormalizedSupportCountForInitiatives();

    }
}
