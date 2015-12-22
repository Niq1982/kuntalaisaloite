package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.service.email.EmailReportService;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public class JobExecutor {

    private static final String EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * *";

    @Resource
    private EmailReportService emailReportService;

    @Resource
    private SupportCountService supportCountService;

    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    public void sendReportEmailsForInitiativesAcceptedButNotPublished() {
        emailReportService.sendReportEmailsForInitiativesAcceptedButNotPublished();
    }

    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    public void sendQuarterReportsForInitiatives() {
        emailReportService.sendQuarterReports();
    }

    @Scheduled(cron = EVERY_DAY_AT_MIDNIGHT)
    public void updateDenormalizedSupportCountForInitiatives() {
        supportCountService.updateDenormalizedSupportCountForInitiatives();
    }

    @PostConstruct
    public void executeAllJobs() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDenormalizedSupportCountForInitiatives();
                sendQuarterReportsForInitiatives();
                sendReportEmailsForInitiativesAcceptedButNotPublished();
            }
        }).start();

    }
}
