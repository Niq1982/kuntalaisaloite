package fi.om.municipalityinitiative.dto.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mysema.query.types.query.ListSubQuery;
import com.sun.deploy.util.StringUtils;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import org.joda.time.DateTime;
import org.mockito.internal.util.collections.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class EmailDto {

    public static final String EMAIL_SEPARATOR = ";";

    private Long initiativeId;

    private List<String> recipients;

    private String subject;

    private String bodyHtml;

    private String bodyText;

    private String replyTo;

    private String sender;

    private DateTime sent;

    private DateTime failed;

    private boolean status;

    private EmailAttachmentType attachmentType = EmailAttachmentType.NONE;

    public EmailDto(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    public EmailDto withRecipients(List<String> recipients) {
        this.recipients = recipients;
        return this;
    }

    public EmailDto withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailDto withContent(String bodyHtml, String bodyText) {
        this.bodyHtml = bodyHtml;
        this.bodyText = bodyText;
        return this;
    }

    public EmailDto withSender(String senderName, String replyTo) {
        this.sender = senderName;
        this.replyTo = replyTo;
        return this;
    }

    public EmailDto withSent(DateTime sent, boolean status) {
        if (sent == null && status) {
            throw new IllegalStateException("Email had no sent-time but status was sent!");
        }
        if (sent != null && !status) {
            throw new IllegalStateException("Email had sent-time but status was not sent!");
        }

        this.sent = sent;
        this.status = status;
        return this;
    }

    public EmailDto withFailed(DateTime failed) {
        this.failed = failed;
        return this;
    }

    public EmailDto withAttachment(EmailAttachmentType attachmentType) {
        this.attachmentType = attachmentType;
        return this;
    }

    public static List<String> parseEmails(String source) {
        return Lists.newArrayList(Splitter.on(EMAIL_SEPARATOR)
                .trimResults()
                .omitEmptyStrings()
                .split(source));
    }

    /**
     * Recursive splitting with EMAIL_SEPARATOR, even list object may have multiple emails separated with EMAIL_SEPARATOR
     * @param emails
     * @return
     */
    public static String emailsToString(List<String> emails) {
        return joinEmails(parseEmails(joinEmails(emails)));
    }

    private static String joinEmails(List<String> emails) {
        return Joiner.on(EMAIL_SEPARATOR)
                .skipNulls()
                .join(emails);


    }

    public Long getInitiativeId() {
        return initiativeId;
    }

    public List<String> getRecipientsList() {
        return recipients;
    }

    public String getRecipientsAsString() {
        return emailsToString(recipients);
    }

    public String getSubject() {
        return subject;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getSender() {
        return sender;
    }

    public DateTime getSent() {
        return sent;
    }

    public DateTime getFailed() {
        return failed;
    }

    public boolean isStatus() {
        return status;
    }

    public EmailAttachmentType getAttachmentType() {
        return attachmentType;
    }
}
