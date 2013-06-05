package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Locales;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class ServiceIntegrationTestBase {

    @Resource
    protected JavaMailSenderFake javaMailSenderFake;

    @Resource
    protected TestHelper testHelper;

    @Resource
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
//        Thread.sleep(100); // Hope this is enough to verify that emails about to be sent are sent
        javaMailSenderFake.clearSentMessages();
        testHelper.dbCleanup();
        childSetup();
    }

    protected abstract void childSetup();

    protected void assertUniqueSentEmail(String expectedRecipient, String expectedSubjectPropertyKey, String... argsInSubject) throws InterruptedException, MessagingException {
        MimeMessage singleSentMessage = javaMailSenderFake.getSingleSentMessage();

        assertMessage(expectedRecipient, expectedSubjectPropertyKey, singleSentMessage, argsInSubject);
    }

    protected void assertFirstSentEmail(String expectedRecipient, String expectedSubjectPropertyKey) throws MessagingException {

        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages(2);
        MimeMessage firstSentMessage = sentMessages.get(0);

        assertMessage(expectedRecipient, expectedSubjectPropertyKey, firstSentMessage, null);

    }
    protected void assertSecondSentEmail(String expectedRecipient, String expectedSubjectPropertyKey) throws MessagingException {

        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages(2);
        MimeMessage secondSendMessage = sentMessages.get(1);

        assertMessage(expectedRecipient, expectedSubjectPropertyKey, secondSendMessage, null);
    }

    private void assertMessage(String expectedRecipient, String expectedSubjectPropertyKey, MimeMessage secondSendMessage, String[] argsInSubject) throws MessagingException {
        assertThat(JavaMailSenderFake.getSingleRecipient(secondSendMessage), is(expectedRecipient));
        String expectedSubject = messageSource.getMessage(expectedSubjectPropertyKey, argsInSubject, Locales.LOCALE_FI);
        assertThat(secondSendMessage.getSubject(), is(expectedSubject));
    }

}
