package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;

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

    @Before
    public void setUp() throws Exception {
        javaMailSenderFake.clearSentMessages();
        testHelper.dbCleanup();
        childSetup();
    }

    protected abstract void childSetup();

    protected void assertOneEmailSent() {
        try {
            javaMailSenderFake.getSingleSentMessage().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error while checking sent email", e);
        }

    }
}
