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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

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
        javaMailSenderFake.clearSentMessages();
        testHelper.dbCleanup();
        childSetup();
    }

    protected abstract void childSetup();

    protected void assertOneEmailSent(String expectedRecipient, String expectedSubjectPropertyKey, String ... argsInSubject) {
        try {

            assertThat(javaMailSenderFake.getSingleRecipient(), is(expectedRecipient));
            String expectedSubject = messageSource.getMessage(expectedSubjectPropertyKey, argsInSubject, Locales.LOCALE_FI);
            assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is(expectedSubject));
        } catch (Exception e) {
            throw new RuntimeException("Error while checking sent email", e);
        }

    }
}
