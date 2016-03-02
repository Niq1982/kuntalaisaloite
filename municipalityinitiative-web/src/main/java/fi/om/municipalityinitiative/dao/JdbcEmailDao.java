package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.expr.DateTimeExpression;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.sql.QEmail;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;

public class JdbcEmailDao implements EmailDao {

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long addEmail(Long initiativeId,
                         String subject,
                         List<String> recipients,
                         String bodyHtml,
                         String bodyText,
                         String sender,
                         String replyTo,
                         EmailAttachmentType attachmentType) {
        return queryFactory.insert(QEmail.email)
                .set(QEmail.email.attachment, attachmentType)
                .set(QEmail.email.subject, subject)
                .set(QEmail.email.bodyHtml, bodyHtml)
                .set(QEmail.email.bodyText, bodyText)
                .set(QEmail.email.initiativeId, initiativeId)
                .set(QEmail.email.recipients, EmailDto.emailsToString(recipients))
                .set(QEmail.email.sender, sender)
                .set(QEmail.email.replyTo, replyTo)
                .executeWithKey(QEmail.email.id);
    }

    @Override
    public List<EmailDto> findUntriedEmails() {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.tried.eq(false))
                .list(emailMapping);
    }

    @Override
    public Maybe<EmailDto> popUntriedEmailForUpdate() {
        Maybe<EmailDto> emailDtoMaybe = Maybe.fromNullable(queryFactory.from(QEmail.email)
                .where(QEmail.email.tried.eq(false))
                .forUpdate()
                .singleResult(emailMapping));

        if (emailDtoMaybe.isPresent()) {
            assertSingleAffection(queryFactory.update(QEmail.email)
                    .where(QEmail.email.id.eq(emailDtoMaybe.get().getEmailId()))
                    .set(QEmail.email.tried, true)
                    .execute());
        }
        return emailDtoMaybe;
    }

    @Override
    public List<EmailDto> findFailedEmails() {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.lastFailed.isNotNull())
                .where(QEmail.email.succeeded.isNull())
                .list(emailMapping);
    }

    @Override
    public List<EmailDto> findTriedNotSucceeded() {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.tried.eq(true))
                .where(QEmail.email.succeeded.isNull())
                .list(emailMapping);

    }

    @Override
    public EmailDto get(Long emailId) {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.id.eq(emailId))
                .uniqueResult(emailMapping);
    }

    @Override
    public void succeed(Long emailId) {
        assertSingleAffection(queryFactory.update(QEmail.email)
                .set(QEmail.email.succeeded, CURRENT_TIME)
                .set(QEmail.email.tried, true)
                .where(QEmail.email.id.eq(emailId))
                .execute());
    }

    @Override
    public void failed(Long emailId) {
        assertSingleAffection(queryFactory.update(QEmail.email)
                .set(QEmail.email.lastFailed, CURRENT_TIME)
                .set(QEmail.email.tried, true)
                .where(QEmail.email.id.eq(emailId))
                .execute());
    }

    @Override
    public List<EmailDto> findNotSucceeded(long offset) {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.succeeded.isNull())
                .orderBy(QEmail.email.id.desc())
                .list(emailMapping);
    }

    @Override
    public long retryFailedEmails() {
        long failedEmails = queryFactory.update(QEmail.email)
                .set(QEmail.email.tried, false)
                .where(QEmail.email.tried.eq(true))
                .where(QEmail.email.lastFailed.isNotNull())
                .where(QEmail.email.succeeded.isNull())
                .execute();

        long unknownStatus = queryFactory.update(QEmail.email)
                .set(QEmail.email.tried, false)
                .where(QEmail.email.tried.eq(true))
                .where(QEmail.email.lastFailed.isNull())
                .where(QEmail.email.succeeded.isNull())
                .execute();

        return failedEmails+unknownStatus;
    }

    @Override
    public List<EmailDto> findSucceeded(long offset) {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.succeeded.isNotNull())
                .orderBy(QEmail.email.succeeded.desc())
                .offset(offset)
                .limit(50)
                .list(emailMapping);
    }

    static final MappingProjection<EmailDto> emailMapping
            = new MappingProjection<EmailDto>(EmailDto.class, QEmail.email.all()) {
        @Override
        protected EmailDto map(Tuple row) {
            EmailDto emailDto = new EmailDto(row.get(QEmail.email.id), row.get(QEmail.email.initiativeId));
            emailDto.setAttachmentType(row.get(QEmail.email.attachment));
            emailDto.setBodyHtml(row.get(QEmail.email.bodyHtml));
            emailDto.setBodyText(row.get(QEmail.email.bodyText));
            emailDto.setLastFailed(row.get(QEmail.email.lastFailed));
            emailDto.setSucceeded(row.get(QEmail.email.succeeded));
            emailDto.setTried(row.get(QEmail.email.tried));
            emailDto.setReplyTo(row.get(QEmail.email.replyTo));
            emailDto.setSubject(row.get(QEmail.email.subject));
            emailDto.setRecipients(EmailDto.parseEmails(row.get(QEmail.email.recipients)));
            emailDto.setSender(row.get(QEmail.email.sender));
            return emailDto;
        }
    };
}
