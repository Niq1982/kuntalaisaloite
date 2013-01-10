package fi.om.municipalityinitiative.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.commons.lang.Assert;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Task;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

@Task
public class EmailServiceImpl implements EmailService {

    @Resource FreeMarkerConfigurer freemarkerConfig;
    @Resource MessageSource messageSource;
    @Resource JavaMailSender javaMailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class); 
    
    private final String baseURL;
    private final String defaultReplyTo;
    private final String testSendTo;
    private final boolean testConsoleOutput;
    private final int invitationExpirationDays;
    private String sendToOM;
    private String sendToVRK;

    private enum NotificationKey {
        OM,
        VRK;
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public EmailServiceImpl(FreeMarkerConfigurer freemarkerConfig, MessageSource messageSource, JavaMailSender javaMailSender, 
                        String baseURL, String defaultReplyTo, String sendToOM, String sendToVRK, 
                        int invitationExpirationDays, String testSendTo, boolean testConsoleOutput) {
        this.freemarkerConfig = freemarkerConfig;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
        this.baseURL = baseURL;
        this.defaultReplyTo = defaultReplyTo;
        this.invitationExpirationDays = invitationExpirationDays;
        this.sendToOM = sendToOM;
        this.sendToVRK = sendToVRK;
        
        if (Strings.isNullOrEmpty(testSendTo)) {
            this.testSendTo = null;
        } else {
            this.testSendTo = testSendTo;
        }
        this.testConsoleOutput = testConsoleOutput;
    }




    private <T extends Enum<?>> void addEnum(Class<T> enumType, Map<String, Object> dataMap) {
        Map<String, T> values = Maps.newHashMap();
        for (T value : enumType.getEnumConstants()) {
            values.put(value.name(), value);
        }
        dataMap.put(enumType.getSimpleName(), values);
    }

    private String getEmailSubject(String code, String param) {
        code = "email.subject." + code;
        if (param == null) {
            return messageSource.getMessage(code, null, Locales.LOCALE_FI);
        } else {
            Object[] args = {param};
            return messageSource.getMessage(code, args, Locales.LOCALE_FI);
        }
    }
    
    private String getEmailSubject(String code) {
        return getEmailSubject(code, null);
    }

    private void sendEmails(List<String> sendTos, String replyTo, String subject, String templateName,  Map<String, Object> dataMap) {
        for (String sendTo : sendTos) {
            sendEmail(sendTo, replyTo, subject, templateName,  dataMap);
        }
    }

    private void sendEmail(String sendTo, String replyTo, String subject, String templateName,  Map<String, Object> dataMap) {
        Assert.notNull(sendTo, "sendTo");

        String text = processTemplate(templateName + "-text", dataMap); 
        String html = processTemplate(templateName + "-html", dataMap); 
        
        text = stripTextRows(text, 2);
        
        if (testSendTo != null) {
            text = "TEST OPTION REPLACED THE EMAIL ADDRESS!\nThe original address was: " + sendTo + "\n\n\n-------------\n" + text;
            html = "TEST OPTION REPLACED THE EMAIL ADDRESS!\nThe original address was: " + sendTo + "<hr>" + html;
            sendTo = testSendTo;
        }
        if (replyTo == null) {
            replyTo = defaultReplyTo;
        }
        
        if (testConsoleOutput) {
            System.out.println("----------------------------------------------------------");
            System.out.println("To: " + sendTo);
            System.out.println("Reply-to: " + replyTo);
            System.out.println("Subject: " + subject);
            System.out.println("---");
            System.out.println(text);
            System.out.println("----------------------------------------------------------");
            return;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(sendTo);
            helper.setFrom(defaultReplyTo); //to avoid spam filters
            helper.setReplyTo(replyTo);
            helper.setSubject(subject);
            helper.setText(text, html);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
        log.info("Email message sent to " + sendTo + ": " + subject);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
    private String stripTextRows(String text, int maxEmptyRows) {
//        Iterable<String> textRows = Splitter.on('\n').trimResults().split(text);
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
        
        String result = Joiner.on("\r\n").join(rows); // Splitter removes '\r' characters
        return result;
    }
}
