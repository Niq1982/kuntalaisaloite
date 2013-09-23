package fi.om.municipalityinitiative.dto.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.EmailDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.junit.Test;
import org.springframework.mail.MailException;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class EmailSenderSchedulerTest extends ServiceIntegrationTestBase{

    @Resource
    private EmailSender emailSender;

    @Resource
    private EmailDao emailDao;

    @Resource
    private JavaMailSenderFake javaMailSenderFake;
    private Long initiativeId;

    @Override
    protected void childSetup() {
        emailSender.setJavaMailSender(javaMailSenderFake);
        javaMailSenderFake.clearSentMessages();

        initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testHelper.createTestMunicipality("municipalityName")));

    }

    @Test
    public void concurrent_sending_tries_will_not_re_send_any_emails() throws InterruptedException {

        createRandomEmail(initiativeId);
        createRandomEmail(initiativeId);
        createRandomEmail(initiativeId);

        ExecutorService executor = Executors.newCachedThreadPool();

        List<Callable<Boolean>> executions = Lists.newArrayList();

        for (int i = 0; i < 12; ++i) {
            executions.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return emailSender.sendNextEmail();
                }
            });
        }

        executor.invokeAll(executions);

        assertThat(javaMailSenderFake.getSentMessages(), is(3));
        assertThat(testHelper.getQueuedEmails(), hasSize(0));
    }

    @Test
    public void failing_to_send_email_marks_it_as_failed() throws InterruptedException {
        emailSender.setJavaMailSender(failingJavaMailSenderFake());

        Long emailId = testHelper.createRandomEmail(initiativeId);

        precondition(testHelper.getEmail(emailId).getLastFailed().isPresent(), is(false));
        emailSender.sendNextEmail();
        assertThat(testHelper.getEmail(emailId).getLastFailed().isPresent(), is(true));
    }

    private static JavaMailSenderFake failingJavaMailSenderFake() {
        return new JavaMailSenderFake() {
            @Override
            public void send(MimeMessage mimeMessage) throws MailException {
                throw new RuntimeException("Some exception while sending email");
            }
        };
    }

    private Long createRandomEmail(Long initiativeId) {
        return testHelper.createRandomEmail(initiativeId);
    }

}
