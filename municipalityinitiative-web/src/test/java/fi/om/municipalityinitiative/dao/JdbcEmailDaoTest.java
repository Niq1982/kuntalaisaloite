package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcEmailDaoTest {

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


        assertThat(emailDao.addEmail(new EmailDto(testInitiative)
                .withSubject("otsikko")
                .withContent("bodyhtml", "bodytext")
                .withRecipients(Collections.singletonList("recipient"))
                .withSender("sender", "replyto")
        ), is(notNullValue()));

    }


}
