package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.EmailAttachmentType;

import java.util.List;
import java.util.Optional;

public interface EmailDao {

    Long addEmail(Long initiativeId,
                  String subject,
                  List<String> recipients,
                  String bodyHtml,
                  String bodyText,
                  String sender,
                  String replyTo,
                  EmailAttachmentType attachmentType);

    Optional<EmailDto> popUntriedEmailForUpdate();

    List<EmailDto> findUntriedEmails();

    EmailDto get(Long emailId);

    void succeed(Long emailId);

    void failed(Long sendableEmail);

    List<EmailDto> findFailedEmails();

    List<EmailDto> findSucceeded(long offset);

    List<EmailDto> findNotSucceeded(long offset);

    long retryFailedEmails();

    List<EmailDto> findTriedNotSucceeded();
}
