package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.service.StatusServiceImpl.KeyValueInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StatusService {

    List<KeyValueInfo> getSystemInfo();

    List<KeyValueInfo> getSchemaVersionInfo();

    List<KeyValueInfo> getApplicationInfo();

    List<KeyValueInfo> getConfigurationInfo();

    List<KeyValueInfo> getConfigurationTestInfo();

    String getAppVersion();

    List<EmailDto> findTriedNotSucceededEmails();

    List<EmailDto> findSucceededEmails(Long offset);

    List<EmailDto> findNotSucceededEmails();

    void resendFailedEmailsAndContinueScheduledMailSender();

    @Transactional(readOnly = true)
    List<EmailDto> findUntriedEmails();
}