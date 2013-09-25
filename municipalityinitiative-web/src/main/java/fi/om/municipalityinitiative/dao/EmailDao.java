package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.List;

public interface EmailDao {

    Long addEmail(Long initiativeId,
                  String subject,
                  List<String> recipients,
                  String bodyHtml,
                  String bodyText,
                  String sender,
                  String replyTo,
                  EmailAttachmentType attachmentType);

    Maybe<EmailDto> popUntriedEmailForUpdate();

    List<EmailDto> findUntriedEmails();

    EmailDto get(Long emailId);

    void succeed(Long emailId);

    void failed(Long sendableEmail);

    List<EmailDto> findFailedEmails();

    List<EmailDto> findSucceeded(long offset);

    List<EmailDto> findNotSucceeded(long offset);

    void retryFailedEmails();
}
