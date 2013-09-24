package fi.om.municipalityinitiative.dto.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;

import java.util.List;

public class EmailDto {

    public static final String EMAIL_SEPARATOR = ";";

    private final Long emailId;

    private final Long initiativeId;

    private List<String> recipients;

    private String subject;

    private String bodyHtml;

    private String bodyText;

    private String replyTo;

    private String sender;

    private Maybe<DateTime> succeeded;

    private Maybe<DateTime> lastFailed;

    private boolean tried;

    private EmailAttachmentType attachmentType;

    public EmailDto(Long emailId, Long initiativeId) {
        this.emailId = emailId;
        this.initiativeId = initiativeId;
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

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSucceeded(DateTime succeeded) {
        this.succeeded = Maybe.fromNullable(succeeded);
    }

    public void setLastFailed(DateTime lastFailed) {
        this.lastFailed = Maybe.fromNullable(lastFailed);
    }

    public void setTried(boolean tried) {
        this.tried = tried;
    }

    public void setAttachmentType(EmailAttachmentType attachmentType) {
        this.attachmentType = attachmentType;
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

    public Maybe<DateTime> getSucceeded() {
        return succeeded;
    }

    public Maybe<DateTime> getLastFailed() {
        return lastFailed;
    }

    public boolean isTried() {
        return tried;
    }

    public EmailAttachmentType getAttachmentType() {
        return attachmentType;
    }

    public Long getEmailId() {
        return emailId;
    }
}
