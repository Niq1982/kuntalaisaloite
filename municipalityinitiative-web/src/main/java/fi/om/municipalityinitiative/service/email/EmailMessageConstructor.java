package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import fi.om.municipalityinitiative.conf.EnvironmentSettings;
import fi.om.municipalityinitiative.dao.EmailDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Maybe;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class EmailMessageConstructor {

    @Resource
    private EnvironmentSettings environmentSettings;

    @Resource
    private FreeMarkerConfigurer freemarkerConfig;

    @Resource
    private EmailDao emailDao;

    private static final Logger log = LoggerFactory.getLogger(EmailMessageConstructor.class);

    private String solveEmailFrom() {
        return "Kuntalaisaloitepalvelu" + (environmentSettings.hasAnyTestOptionsEnabled() ? " TEST" : "");
    }

    private String processTemplate(String templateName, Map<String, Object> dataMap) {
        final Configuration cfg = freemarkerConfig.getConfiguration();

        try {
            Template template;
            template = cfg.getTemplate("emails/" + templateName + ".ftl");
            Writer out = new StringWriter();
            template.process(dataMap, out);
            out.flush();
            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public EmailMessageDraft fromTemplate(Long initiativeId, String templateName) {
        return new EmailMessageDraft(initiativeId, templateName);
    }

    public class EmailMessageDraft {
        private final Long initiativeId;
        private final String templateName;
        private List<String> recipients = Lists.newArrayList();
        private String subject;
        private Map<String, Object> dataMap;

        private Maybe<Initiative> attachmentInitiative = Maybe.absent();
        private Maybe<? extends List<? extends Participant>> attachmentParticipants = Maybe.absent();

        public EmailMessageDraft(Long initiativeId, String templateName) {
            this.initiativeId = initiativeId;
            this.templateName = templateName;
        }

        public EmailMessageDraft withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailMessageDraft addRecipients(List<String> givenRecipients) {
            this.recipients.addAll(givenRecipients);
            return this;
        }
        public EmailMessageDraft addRecipient(String sendTo) {
            this.recipients.add(sendTo);
            return this;
        }

        public EmailMessageDraft withDataMap(Map<String, Object> dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        public EmailMessageDraft withAttachment(Initiative initiative, List<? extends Participant> participants) {
            this.attachmentInitiative = Maybe.of(initiative);
            this.attachmentParticipants = Maybe.of(participants);
            return this;
        }

        public void send() {

            Assert.isTrue(recipients.size() > 0, "recipients");
            Assert.notNull(subject, "subject");
            Assert.notNull(templateName, "templateName");
            Assert.notNull(dataMap, "dataMap");

            log.info("Persisting email to " + recipients + ": " + subject);
            emailDao.addEmail(initiativeId, subject, recipients,
                    processTemplate(templateName + "-html", dataMap),
                    processTemplate(templateName + "-text", dataMap),
                    solveEmailFrom(),
                    environmentSettings.getDefaultReplyTo(),
                    attachmentInitiative.isPresent() ? EmailAttachmentType.PARTICIPANTS : EmailAttachmentType.NONE
                    );

            // Uncomment this if you want to print all email-html files for preview etc and run EmailTests.
//            try {
//                String content = processTemplate(templateName + "-html", dataMap);
//                new FileOutputStream(subject.replace("/", "")+".html").write(content.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }
}
