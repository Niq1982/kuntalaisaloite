package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class MailSenderEmailServiceTest {

    @Resource
    private EmailService emailService;

    @Resource
    private JavaMailSenderFake javaMailSenderFake;

    private final Long MUNICIPALITY_ID = -5L;
    private final String MUNICIPALITY_EMAIL = "some@example.com";
    private static final String REPLY_TO = "reply_to@example.com";
    private static final String TEST_SEND_TO = "test_send_to@example.com";

    @Before
    public void setup() {
    }

    @Test
    public void assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeViewInfo initiative = new InitiativeViewInfo();
        initiative.setMunicipalityId(MUNICIPALITY_ID);

        emailService.sendToMunicipality(initiative, "some_test_address@example.com");

        assertThat(getSentMessage().getAllRecipients().length, is(1));
        assertThat(getSentMessage().getAllRecipients()[0].toString(), is("some_test_address@example.com"));
    }

    private MimeMessage getSentMessage() throws InterruptedException {
        waitUntilEmailSent();
        assertThat(javaMailSenderFake.getSentMessages(), hasSize(1));
        return javaMailSenderFake.getSentMessages().get(0);
    }

    private void waitUntilEmailSent() throws InterruptedException {
        for (int i = 0; i < 50; ++i) {
            if (javaMailSenderFake.getSentMessages().size() == 0)
                Thread.sleep(100);
            else
                return;
        }
        throw new RuntimeException("Email was not sent in time");
    }

}
