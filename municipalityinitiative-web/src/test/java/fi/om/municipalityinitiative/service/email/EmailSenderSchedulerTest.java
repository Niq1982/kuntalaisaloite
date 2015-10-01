package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.EmailDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Maybe;
import org.junit.After;
import org.junit.Test;
import org.springframework.mail.MailException;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.*;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class EmailSenderSchedulerTest extends ServiceIntegrationTestBase {

    @Resource
    protected EmailSender emailSender;

    @Resource
    private EmailSenderScheduler emailSenderScheduler;

    @Resource
    private EmailDao emailDao;

    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    private Long initiativeId;

    @Override
    protected void childSetup() {
        javaMailSenderFake.clearSentMessages();
        emailSender.setJavaMailSender(javaMailSenderFake);
        initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testHelper.createTestMunicipality("municipalityName")));
    }

    @After
    public void resetEmailDao() {
        emailSender.setEmailDao(emailDao); // Reset original emailDao in case it's overriden by previous test
    }

    @Test
    public void concurrent_sending_tries_will_not_re_send_any_emails() throws InterruptedException {

        createRandomEmails(5);
        multipleConcurrentSendExecutions();

        assertThat(javaMailSenderFake.getSentMessages(), is(5));
        assertThat(testHelper.findQueuedEmails(), hasSize(0));
        assertThat(testHelper.findTriedEmails(), hasSize(5));
    }

    @Test
    public void failing_to_send_email_marks_it_as_failed() throws InterruptedException {
        emailSender.setJavaMailSender(failingJavaMailSenderFake());

        Long emailId = testHelper.createRandomEmail(initiativeId, EmailAttachmentType.NONE);

        precondition(testHelper.getEmail(emailId).getLastFailed(), isNotPresent());
        emailSenderScheduler.sendEmails();
        assertThat(testHelper.getEmail(emailId).getLastFailed(), isPresent());

        assertThat(javaMailSenderFake.getSentMessages(), is(0));
        assertThat(testHelper.findQueuedEmails(), hasSize(0));
        assertThat(testHelper.findTriedEmails(), hasSize(1));
    }

    @Test
    public void concurrent_sending_tries_will_not_resend_any_emails_if_getting_email_fails() throws InterruptedException {
        emailSender.setEmailDao(popNextEmailFailingEmailDao());

        createRandomEmails(5);
        multipleConcurrentSendExecutions();

        assertThat(javaMailSenderFake.getSentMessages(), is(0));
        assertThat(testHelper.findQueuedEmails(), hasSize(5));
        assertThat(testHelper.findTriedEmails(), hasSize(0));
    }

    @Test
    public void concurrent_sending_tries_will_not_re_send_any_emails_if_sending_is_ok_but_marking_as_succeeded_fails() throws InterruptedException {
        emailSender.setEmailDao(successFailingEmailDao());

        createRandomEmails(5);
        multipleConcurrentSendExecutions();

        assertThat(javaMailSenderFake.getSentMessages(), is(5)); // Emails are sent, this simulates jdbc-error after sending.
        assertThat(testHelper.findQueuedEmails(), hasSize(0));
        assertThat(testHelper.findTriedEmails(), hasSize(5));
    }

    @Test
    public void concurrent_sending_with_failing_email_send_and_failure_when_marking_email_as_sent() throws InterruptedException {
        emailSender.setJavaMailSender(failingJavaMailSenderFake());
        emailSender.setEmailDao(failureFailingEmailDao());

        createRandomEmails(5);
        multipleConcurrentSendExecutions();

        assertThat(javaMailSenderFake.getSentMessages(), is(0));
        assertThat(testHelper.findQueuedEmails(), hasSize(0));
        assertThat(testHelper.findTriedEmails(), hasSize(5));
    }

    @Test
    public void sends_emails_with_attachments() {
        testHelper.createRandomEmail(initiativeId, EmailAttachmentType.PARTICIPANTS);
        emailSenderScheduler.sendEmails();
        assertThat(javaMailSenderFake.getSentMessages(), is(1));
    }

    private void multipleConcurrentSendExecutions() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        List<Callable<Boolean>> executions = Lists.newArrayList();

        for (int i = 0; i < 12; ++i) {
            executions.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    emailSenderScheduler.sendEmails();
                    return true;
                }
            });
        }

        List<Future<Boolean>> futures = executor.invokeAll(executions);

        for (Future future : futures) {
            try {
                future.get(1, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

    }

    private void createRandomEmails(int count) {
        for (int i = 0; i < count; ++i) {
            testHelper.createRandomEmail(initiativeId, EmailAttachmentType.NONE);
        }
    }

    private static JavaMailSenderFake failingJavaMailSenderFake() {
        return new JavaMailSenderFake() {
            @Override
            public void send(MimeMessage mimeMessage) throws MailException {
                throw new RuntimeException("Dummy exception on send()");
            }
        };
    }

    private EmailDao successFailingEmailDao() {
        return new ReplicatedEmailDao() {
            @Override
            public void succeed(Long emailId) {
                throw new RuntimeException("Dummy exception on succeed()");
            }
        };
    }

    private EmailDao failureFailingEmailDao() {
        return new ReplicatedEmailDao() {
            @Override
            public void failed(Long sendableEmail) {
                throw new RuntimeException("Dummy exception on popUntriedEmailForUpdate failed()");
            }
        };
    }

    private EmailDao popNextEmailFailingEmailDao() {
        return new ReplicatedEmailDao() {
            @Override
            public Maybe<EmailDto> popUntriedEmailForUpdate() {
                throw new RuntimeException("Dummy exception on popUntriedEmailForUpdate()");
            }
        };
    }

    private class ReplicatedEmailDao implements EmailDao {
        @Override
        public Long addEmail(Long initiativeId, String subject, List<String> recipients, String bodyHtml, String bodyText, String sender, String replyTo, EmailAttachmentType attachmentType) {
            return emailDao.addEmail(initiativeId, subject, recipients, bodyHtml, bodyText, sender, replyTo, attachmentType);
        }

        @Override
        public Maybe<EmailDto> popUntriedEmailForUpdate() {
            return emailDao.popUntriedEmailForUpdate();
        }

        @Override
        public List<EmailDto> findUntriedEmails() {
            return emailDao.findUntriedEmails();
        }

        @Override
        public EmailDto get(Long emailId) {
            return emailDao.get(emailId);
        }

        @Override
        public void succeed(Long emailId) {
            emailDao.succeed(emailId);
        }

        @Override
        public void failed(Long sendableEmail) {
            emailDao.failed(sendableEmail);
        }

        @Override
        public List<EmailDto> findFailedEmails() {
            return emailDao.findFailedEmails();
        }

        @Override
        public List<EmailDto> findSucceeded(long offset) {
            return emailDao.findSucceeded(offset);
        }

        @Override
        public List<EmailDto> findNotSucceeded(long offset) {
            return emailDao.findNotSucceeded(offset);
        }

        @Override
        public long retryFailedEmails() {
            return emailDao.retryFailedEmails();
        }

        @Override
        public List<EmailDto> findTriedNotSucceeded() {
            return emailDao.findTriedNotSucceeded();
        }
    }

}
