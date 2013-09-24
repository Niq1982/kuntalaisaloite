package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcEmailDaoTest {

    public static final String SUBJECT = "subject";
    public static final String RECIPIENT = "recipient";
    public static final String BODYHTML = "bodyhtml";
    public static final String BODYTEXT = "bodytext";
    public static final String SENDER = "sender";
    public static final String REPLYTO = "replyto";

    @Resource
    TestHelper testHelper;

    @Resource
    private EmailDao emailDao;

    private Long testInitiative;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testInitiative = testHelper.create(testHelper.createTestMunicipality("asd"), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
    }

    @Test
    public void adds_email_with_no_status_information() {
        Long emailId = createSendableEmail();
        assertThat(emailId, is(notNullValue()));
    }

    @Test
    public void find_untried_emails() {
        createSendableEmail();
        List<EmailDto> untriedEmails = emailDao.findUntriedEmails();
        assertThat(untriedEmails, hasSize(1));

        ReflectionTestUtils.assertNoNullFields(untriedEmails.get(0));
    }

    @Test
    public void get_untried_email_for_update() {
        Long emailId = createSendableEmail();
        Maybe<EmailDto> untriedEmailForUpdate = emailDao.getUntriedEmailForUpdate();
        assertThat(untriedEmailForUpdate.isPresent(), is(true));
        assertThat(untriedEmailForUpdate.get().getEmailId(), is(emailId));
    }

    @Test
    public void mark_email_as_sent_sets_success_time() {
        Long sendableEmail = createSendableEmail();
        EmailDto unsent = emailDao.get(sendableEmail);
        precondition(unsent.getSucceeded().isPresent(), is(false));
        precondition(unsent.getLastFailed().isPresent(), is(false));
        precondition(unsent.isTried(), is(false));

        emailDao.succeed(sendableEmail);

        EmailDto sent = emailDao.get(sendableEmail);

        assertThat(sent.isTried(), is(true));
        assertThat(sent.getSucceeded().isPresent(), is(true));
        assertThat(sent.getLastFailed().isPresent(), is(false));
    }

    @Test
    public void mark_email_as_failed_sets_failure_time() {
        Long sendableEmail = createSendableEmail();
        EmailDto unsent = emailDao.get(sendableEmail);
        precondition(unsent.getSucceeded().isPresent(), is(false));
        precondition(unsent.getLastFailed().isPresent(), is(false));
        precondition(unsent.isTried(), is(false));

        emailDao.failed(sendableEmail);

        EmailDto sent = emailDao.get(sendableEmail);

        assertThat(sent.isTried(), is(true));
        assertThat(sent.getLastFailed().isPresent(), is(true));
        assertThat(sent.getSucceeded().isPresent(), is(false));
    }

    @Test
    public void find_untried_emails_returns_only_untried_emails() {
        Long untried = createSendableEmail();

        Long failed = createSendableEmail();
        emailDao.failed(failed);

        Long succeeded = createSendableEmail();
        emailDao.succeed(succeeded);

        List<EmailDto> untriedEmails = emailDao.findUntriedEmails();
        assertThat(untriedEmails, hasSize(1));
        assertThat(untriedEmails.get(0).getEmailId(), is(untried));

    }

    @Test
    public void get_untried_email_for_update_returns_only_untried_emails() {
        Long untried = createSendableEmail();

        Long failed = createSendableEmail();
        emailDao.failed(failed);

        Long succeeded = createSendableEmail();
        emailDao.succeed(succeeded);

        Maybe<EmailDto> untriedEmail = emailDao.getUntriedEmailForUpdate();
        assertThat(untriedEmail.isPresent(), is(true));
        assertThat(untriedEmail.get().getEmailId(), is(untried));

        emailDao.succeed(untriedEmail.get().getEmailId());
        assertThat(emailDao.getUntriedEmailForUpdate().isPresent(), is(false));

    }

    private Long createSendableEmail() {
        return emailDao.addEmail(testInitiative,
                SUBJECT,
                Collections.singletonList(RECIPIENT),
                BODYHTML,
                BODYTEXT,
                SENDER,
                REPLYTO, EmailAttachmentType.NONE
        );
    }

}
