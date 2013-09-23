package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class ServiceIntegrationTestBase {

    @Resource
    protected TestHelper testHelper;

    @Resource
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
        childSetup();
    }

    protected abstract void childSetup();

    protected void assertUniqueSentEmail(String expectedRecipient, String expectedSubjectPropertyKey, String... argsInSubject) throws InterruptedException, MessagingException {
//        MimeMessage singleSentMessage = javaMailSenderFake.getSingleSentMessage();
//
//        assertMessage(expectedRecipient, expectedSubjectPropertyKey, singleSentMessage, argsInSubject);

        EmailDto email = testHelper.getSingleQueuedEmail();
        assertMessage(expectedRecipient, expectedSubjectPropertyKey, email, argsInSubject);
    }

    protected void assertFirstSentEmail(String expectedRecipient, String expectedSubjectPropertyKey, String... argsInSubject) throws MessagingException {

        assertThat(testHelper.getQueuedEmails(), hasSize(2));

        EmailDto email = testHelper.getQueuedEmails().get(0);
        assertMessage(expectedRecipient, expectedSubjectPropertyKey, email, argsInSubject);

    }
    protected void assertSecondSentEmail(String expectedRecipient, String expectedSubjectPropertyKey, String... argsInSubject) throws MessagingException {

        assertThat(testHelper.getQueuedEmails(), hasSize(2));

        EmailDto email = testHelper.getQueuedEmails().get(1);
        assertMessage(expectedRecipient, expectedSubjectPropertyKey, email, argsInSubject);
    }

    private void assertMessage(String expectedRecipient, String expectedSubjectPropertyKey, EmailDto emailDto, String[] argsInSubject) throws MessagingException {
        assertThat(emailDto.getRecipientsAsString(), is(expectedRecipient));
        String expectedSubject = messageSource.getMessage(expectedSubjectPropertyKey, argsInSubject, Locales.LOCALE_FI);
        assertThat(emailDto.getSubject(), is(expectedSubject));
    }

}
