package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.service.email.EmailReportService;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public class JobExecutor {

    private static final String EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * *";

    @Resource
    private EmailReportService emailReportService;

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
}
