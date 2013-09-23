package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.sql.QEmail;

import javax.annotation.Resource;

public class JdbcEmailDao implements EmailDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public Long addEmail(EmailDto emailDto) {
        return queryFactory.insert(QEmail.email)
                .set(QEmail.email.attachment, emailDto.getAttachmentType())
                .set(QEmail.email.subject, emailDto.getSubject())
                .set(QEmail.email.bodyHtml, emailDto.getBodyHtml())
                .set(QEmail.email.bodyText, emailDto.getBodyText())
                .set(QEmail.email.initiativeId, emailDto.getInitiativeId())
                .set(QEmail.email.recipients, emailDto.getRecipientsAsString())
                .set(QEmail.email.sender, emailDto.getSender())
                .set(QEmail.email.replyTo, emailDto.getReplyTo())
                .executeWithKey(QEmail.email.id);
    }

}
