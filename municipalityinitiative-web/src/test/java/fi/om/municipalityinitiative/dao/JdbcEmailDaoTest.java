package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
    public void find_sendable_emails() {
        createSendableEmail();
        List<EmailDto> sendableEmails = emailDao.findSendableEmails();
        assertThat(sendableEmails, hasSize(1));

        ReflectionTestUtils.assertNoNullFields(sendableEmails.get(0));

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
