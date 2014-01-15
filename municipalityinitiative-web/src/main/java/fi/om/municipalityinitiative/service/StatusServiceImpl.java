package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.EmailDao;
import fi.om.municipalityinitiative.dao.JdbcSchemaVersionDao;
import fi.om.municipalityinitiative.dto.SchemaVersion;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailSenderScheduler;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.TaskExecutorAspect;
import fi.om.municipalityinitiative.web.HelpPage;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Locale;

public class StatusServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final DateTime appStartTime = DateTime.now();

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATETIME_FORMAT_SHORT = "yyyy-MM-dd HH:mm:ss";

    private final String testEmailSendTo;
    private final boolean testEmailConsoleOutput;
    private final int  messageSourceCacheSeconds;
    private final boolean testFreemarkerShowErrorsOnPage;
    private final Boolean optimizeResources;
    private final String resourcesVersion;
    private final String appVersion;

    @Resource
    private JdbcSchemaVersionDao schemaVersionDao;

    @Resource
    private TaskExecutorAspect taskExecutorAspect;


    @Resource
    private EmailSenderScheduler emailSenderScheduler;

    @Resource
    private EmailDao emailDao;

    @Resource
    private InfoTextService infoTextService;

    public static class KeyValueInfo {
        private String key;
        private Object value;

        public KeyValueInfo(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
    }

    public StatusServiceImpl(String testEmailSendTo, boolean testEmailConsoleOutput,
                             int messageSourceCacheSeconds, boolean testFreemarkerShowErrorsOnPage,
                             Boolean optimizeResources, String resourcesVersion, String appVersion) {
        this.testEmailSendTo = testEmailSendTo;
        this.testEmailConsoleOutput = testEmailConsoleOutput;
        this.messageSourceCacheSeconds = messageSourceCacheSeconds;
        this.testFreemarkerShowErrorsOnPage = testFreemarkerShowErrorsOnPage;
        this.optimizeResources = optimizeResources;
        this.resourcesVersion = resourcesVersion;
        // this.omPiwicId = omPiwicId;
        this.appVersion = appVersion;
    }

    @Override
    @Transactional(readOnly=true)
    public List<KeyValueInfo> getApplicationInfo() {
        List<KeyValueInfo> list = Lists.newArrayList();

        list.add(new KeyValueInfo("appStartTime", appStartTime.toString(DATETIME_FORMAT)));
        list.add(new KeyValueInfo("appVersion", appVersion));
        list.add(new KeyValueInfo("appBuildTimeStamp", getFormattedBuildTimeStamp(resourcesVersion)));
//        list.add(new KeyValueInfo("initiativeCount", initiativeDao.getInitiativeCount()));
        list.add(new KeyValueInfo("taskQueueLength", taskExecutorAspect.getQueueLength()));
        list.add(new KeyValueInfo("unsucceededEmails", emailDao.findTriedNotSucceeded().size()));

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailDto> findUntriedEmails() {
        return emailDao.findUntriedEmails();
    }

    @Override
    @Transactional(readOnly=true)
    public List<KeyValueInfo> getSchemaVersionInfo() {
        List<SchemaVersion> schemaVersions = schemaVersionDao.findExecutedScripts();
        List<KeyValueInfo> list = Lists.newArrayList();

        for (SchemaVersion schemaVersion : schemaVersions) {
            list.add(new KeyValueInfo(schemaVersion.getScript(), schemaVersion.getExecuted().toString(DATETIME_FORMAT)));
        }

        return list;
    }

    @Override
    @Transactional(readOnly=true)
    public List<KeyValueInfo> getConfigurationInfo() {
        List<KeyValueInfo> list = Lists.newArrayList();

        list.add(new KeyValueInfo("baseUrl", Urls.FI.getBaseUrl()));
        list.add(new KeyValueInfo("resourcesVersion", resourcesVersion));
        list.add(new KeyValueInfo("optimizeResources", ""+optimizeResources));

        return list;
    }

    @Override
    @Transactional(readOnly=true)
    public List<KeyValueInfo> getConfigurationTestInfo() {
        List<KeyValueInfo> list = Lists.newArrayList();

        list.add(new KeyValueInfo("testEmailSendTo", testEmailSendTo));
        list.add(new KeyValueInfo("testEmailConsoleOutput", ""+testEmailConsoleOutput));
        list.add(new KeyValueInfo("testMessageSourceCacheSeconds", messageSourceCacheSeconds));
        list.add(new KeyValueInfo("testFreemarkerShowErrorsOnPage", ""+testFreemarkerShowErrorsOnPage));

        return list;
    }

    @Override
    public List<KeyValueInfo> getSystemInfo() {
        List<KeyValueInfo> list = Lists.newArrayList();

        list.add(new KeyValueInfo("system time", DateTime.now().toString(DATETIME_FORMAT)));
        list.add(new KeyValueInfo("active threads", Thread.activeCount()));
        list.add(new KeyValueInfo("available processors", Runtime.getRuntime().availableProcessors()));
        list.add(new KeyValueInfo("total memory, Kb", Runtime.getRuntime().totalMemory() / 1024));
        list.add(new KeyValueInfo("max memory, Kb", Runtime.getRuntime().maxMemory() / 1024));
        list.add(new KeyValueInfo("free memory, Kb", Runtime.getRuntime().freeMemory() / 1024));

        list.add(new KeyValueInfo("loaded class count", ManagementFactory.getClassLoadingMXBean().getLoadedClassCount()));
        list.add(new KeyValueInfo("nonHeapMemUsage, Kb", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / 1024));
        list.add(new KeyValueInfo("heapMemUsage, Kb", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024));
        list.add(new KeyValueInfo("OS load avg", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()));
        list.add(new KeyValueInfo("JVM uptime, minutes", ManagementFactory.getRuntimeMXBean().getUptime() / (60 * 1000)));
        list.add(new KeyValueInfo("start time JVM", "" + new LocalDateTime(ManagementFactory.getRuntimeMXBean().getStartTime()).toString(DATETIME_FORMAT)));

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailDto> findTriedNotSucceededEmails() {
        return emailDao.findTriedNotSucceeded();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailDto> findSucceededEmails(Long offset) {
        return emailDao.findSucceeded(offset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailDto> findNotSucceededEmails() {
        return emailDao.findNotSucceeded(0);
    }

    @Override
    public String getAppVersion() {
        return appVersion;
    }

    protected static String getFormattedBuildTimeStamp(String resourcesVersion) {
        DateTime buildTimeStamp = getBuildTimeStamp(resourcesVersion);
        if (buildTimeStamp != null) {
            return buildTimeStamp.toString(DATETIME_FORMAT_SHORT);
        }
        else {
            return "-";
        }
    }

    protected static DateTime getBuildTimeStamp(String resourcesVersion) {
        DateTime buildTimeStamp;
        try {
            buildTimeStamp = DateTime.parse(resourcesVersion, DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        } catch (Exception ex) {
            buildTimeStamp = null;
        }
        return buildTimeStamp;
    }

    @Override
    @Transactional(readOnly = false)
    public void resendFailedEmailsAndContinueScheduledMailSender() {
        long resentEmails = emailDao.retryFailedEmails();
        log.info("Moderator marked " + resentEmails + " emails for resending.");
    }

    @Override
    public List<KeyValueInfo> getInvalidHelpUris() {


        List<KeyValueInfo> list = Lists.newArrayList();
        for (HelpPage helpPage : HelpPage.values()) {
            addHelpUriStatus(list, helpPage, Locales.LOCALE_FI);
            addHelpUriStatus(list, helpPage, Locales.LOCALE_SV);
        }
        return list;
    }

    private void addHelpUriStatus(List<KeyValueInfo> list, HelpPage helpPage, Locale locale) {
        try {
            infoTextService.getPublished(helpPage.getUri(locale.toLanguageTag()));
            list.add(new KeyValueInfo("OK", Urls.get(locale).help(helpPage.getUri(locale.toLanguageTag()))));
        } catch (NotFoundException e) {
            list.add(new KeyValueInfo("FAILURE", Urls.get(locale).help(helpPage.getUri(locale.toLanguageTag()))));
        }
    }

}

