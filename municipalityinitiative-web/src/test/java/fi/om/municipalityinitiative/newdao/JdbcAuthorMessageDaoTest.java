package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcAuthorMessageDaoTest {

    private static final String CONFIRMATION_CODE = "confirmation code";
    private static final String CONTACT_EMAIL = "contact@example.com";
    private static final String CONTACT_NAME = "Contact Name";
    private static final String MESSAGE = "Some message for all the authors";
    @Resource
    private TestHelper testHelper;

    @Resource
    private AuthorMessageDao authorMessageDao;

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
    }

    @Test
    public void add_and_get() {

        Long initiative = testHelper.createSingleSent(testHelper.createTestMunicipality("Municipality"));
        AuthorMessage original = authorMessage(initiative);

        authorMessageDao.addAuthorMessage(original);

        AuthorMessage result = authorMessageDao.getAuthorMessage(CONFIRMATION_CODE);
        ReflectionTestUtils.assertReflectionEquals(original, result);

    }

    @Test
    public void add_and_delete() {
        Long initiative = testHelper.createSingleSent(testHelper.createTestMunicipality("Municipality"));
        AuthorMessage authorMessage = authorMessage(initiative);

        authorMessageDao.addAuthorMessage(authorMessage);

        precondition(authorMessageDao.getAuthorMessage(CONFIRMATION_CODE), is(notNullValue()));
        authorMessageDao.deleteAuthorMessage(CONFIRMATION_CODE);
        assertThat(authorMessageDao.getAuthorMessage(CONFIRMATION_CODE), is(nullValue()));

    }

    private static AuthorMessage authorMessage(Long initiative) {
        AuthorMessage original = new AuthorMessage();
        original.setConfirmationCode(CONFIRMATION_CODE);
        original.setContactEmail(CONTACT_EMAIL);
        original.setContactName(CONTACT_NAME);
        original.setMessage(MESSAGE);
        original.setInitiativeId(initiative);
        return original;
    }
}
