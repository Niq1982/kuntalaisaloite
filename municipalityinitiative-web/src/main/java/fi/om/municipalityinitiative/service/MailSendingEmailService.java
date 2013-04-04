package fi.om.municipalityinitiative.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Assert;
import fi.om.municipalityinitiative.conf.EmailSettings;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.pdf.ParticipantToPdfExporter;
import fi.om.municipalityinitiative.util.Task;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Task
public class MailSendingEmailService implements EmailService {

    private static final String NOT_COLLECTABLE_TEMPLATE = "municipality-not-collectable";
    private static final String COLLECTABLE_TEMPLATE = "municipality-collectable";
    private static final String FILE_NAME = "Kuntalaisaloite_{0}_{1}_osallistujat.pdf";

    @Resource
    FreeMarkerConfigurer freemarkerConfig;

    @Resource
    MessageSource messageSource;

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MunicipalityDao municipalityDao;

    private static final Logger log = LoggerFactory.getLogger(MailSendingEmailService.class);

    private EmailSettings emailSettings;

    public MailSendingEmailService(EmailSettings emailSettings) {
        this.emailSettings = emailSettings;
    }

    @Override
    public void sendNotCollectableToMunicipality(InitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {
        MimeMessage message = javaMailSender.createMimeMessage();
        parseBasicEmailData(message,
                municipalityEmail,
                messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale),
                NOT_COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));
        send(message);
    }

    @Override
    public void sendNotCollectableToAuthor(InitiativeEmailInfo emailInfo, Locale locale) {
        MimeMessage message = javaMailSender.createMimeMessage();
        parseBasicEmailData(message,
                emailInfo.getContactInfo().getEmail(),
                messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale),
                NOT_COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));
        send(message);
    }

    @Override
    public void sendCollectableToMunicipality(CollectableInitiativeEmailInfo emailInfo, String municipalityEmail, Locale locale) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = parseBasicEmailData(message, emailInfo.getContactInfo().getEmail(), // XXX: Temporarily is set to same as authors email.
                messageSource.getMessage("email.not.collectable.municipality.subject", new String[]{emailInfo.getName()}, locale),
                COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));

        addAttachment(mimeMessageHelper, emailInfo);

        send(message);
    }

    @Override
    public void sendCollectableToAuthor(CollectableInitiativeEmailInfo emailInfo, Locale locale) {
        MimeMessage message = javaMailSender.createMimeMessage();

        parseBasicEmailData(message,
                emailInfo.getContactInfo().getEmail(),
                messageSource.getMessage("email.not.collectable.author.subject", new String[]{emailInfo.getName()}, locale),
                COLLECTABLE_TEMPLATE,
                setDataMap(emailInfo, locale));
        send(message);
    }

    private static void addAttachment(MimeMessageHelper multipart, CollectableInitiativeEmailInfo emailInfo) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ParticipantToPdfExporter.createPdf(emailInfo, outputStream);

            byte[] bytes = outputStream.toByteArray();
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

            String fileName = MessageFormat.format(FILE_NAME, new LocalDate().toString("yyyy-MM-dd"), emailInfo.getId());

            log.info("Attaching "+fileName +" to email.");
            multipart.addAttachment(fileName, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, Object> setDataMap(InitiativeEmailInfo emailInfo, Locale locale) {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("emailInfo", emailInfo);
        dataMap.put("localizations", new EmailLocalizationProvider(messageSource, locale));
        return dataMap;
    }

    private MimeMessageHelper parseBasicEmailData(MimeMessage mimeMessage, String sendTo, String subject, String templateName, Map<String, Object> dataMap) {

        String text = processTemplate(templateName + "-text", dataMap);
        String html = processTemplate(templateName + "-html", dataMap);

        text = stripTextRows(text, 2);

        if (emailSettings.getTestSendTo().isPresent()) {
            text = "TEST OPTION REPLACED THE EMAIL ADDRESS!\nThe original address was: " + sendTo + "\n\n\n-------------\n" + text;
            html = "TEST OPTION REPLACED THE EMAIL ADDRESS!\nThe original address was: " + sendTo + "<hr>" + html;
            sendTo = emailSettings.getTestSendTo().get();
        }

        Assert.notNull(sendTo, "sendTo"); // TODO: Move to the beginning of the function?

        if (emailSettings.isTestConsoleOutput()) {
            System.out.println("----------------------------------------------------------");
            System.out.println("To: " + sendTo);
            System.out.println("Reply-to: " + emailSettings.getDefaultReplyTo());
            System.out.println("Subject: " + subject);
            System.out.println("---");
            System.out.println(text);
            System.out.println("----------------------------------------------------------");
            return null; //FIXME: May cause nullpointer exception
        }

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(sendTo);
            helper.setFrom(emailSettings.getDefaultReplyTo());
            helper.setReplyTo(emailSettings.getDefaultReplyTo());
            helper.setSubject(subject);
            helper.setText(text, html);
            log.info("About to send email to " + sendTo + ": " + subject);
            return helper;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private void send(MimeMessage message) {
        javaMailSender.send(message);
        log.info("Email sent.");
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
    private static String stripTextRows(String text, int maxEmptyRows) {
        List<String> rows = Lists.newArrayList(Splitter.on('\n').trimResults().split(text));
       
        int emptyRows = maxEmptyRows;
        for (int i = rows.size()-1; i >= 0; i--) {
            if (Strings.isNullOrEmpty(rows.get(i))) {
                emptyRows++;
            } else {
                emptyRows = 0;
            }
            if (emptyRows > maxEmptyRows) {
                rows.remove(i);
            }
        }
        
        //remove remaining empty rows from the beginning
        while (rows.size() > 0 && Strings.isNullOrEmpty(rows.get(0))) {
            rows.remove(0);
        }

        return Joiner.on("\r\n").join(rows);
    }

    public static class EmailLocalizationProvider {
        private final Locale locale;
        private final MessageSource messageSource;

        public EmailLocalizationProvider(MessageSource messageSource, Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        public String getMessage(String key, Object ... args) {
            return messageSource.getMessage(key, args, locale);
        }
    }
}
