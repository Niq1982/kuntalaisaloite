package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.InvitationNotValidException;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcAuthorDaoTest {

    @Resource
    AuthorDao authorDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    TestHelper testHelper;
    private Long testMunicipality;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
        testMunicipality = testHelper.createTestMunicipality("what");
    }

    @Test
    public void create_and_get_and_delete() {
        AuthorInvitation original = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        original.setInitiativeId(testHelper.createCollaborativeAccepted(testMunicipality));

        // Create
        authorDao.addAuthorInvitation(original);

        // Get
        AuthorInvitation gotFromDao = authorDao.getAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());
        ReflectionTestUtils.assertReflectionEquals(original, gotFromDao);

        // Delete
        authorDao.deleteAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());

        try {
            authorDao.getAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());
            fail("Should have failed due not found");
        } catch (InvitationNotValidException e) { }
    }

    @Test
    public void find_authors_returns_at_least_one() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality);
        assertThat(authorDao.findAuthors(initiativeId), hasSize(1));
    }

    @Test
    public void login_as_author_returns_authors_initiative() {
        Long collectableAccepted = testHelper.createCollaborativeAccepted(testMunicipality);

        Set<Long> ids = authorDao.loginAndGetAuthorsInitiatives(TestHelper.PREVIOUS_TEST_MANAGEMENT_HASH);

        assertThat(ids, hasSize(1));
        assertThat(ids, contains(collectableAccepted));
    }

    @Test
    public void get_author_by_id_returns_all_information() {
        testHelper.createSingleSent(testMunicipality);

        Author author = authorDao.getAuthor(testHelper.getLastAuthorId());
        assertThat(author.getContactInfo().getAddress(), is(TestHelper.DEFAULT_AUTHOR_ADDRESS));
        assertThat(author.getContactInfo().getName(), is(TestHelper.DEFAULT_PARTICIPANT_NAME));
        assertThat(author.getContactInfo().getEmail(), is(TestHelper.DEFAULT_PARTICIPANT_EMAIL));
        assertThat(author.getContactInfo().getPhone(), is(TestHelper.DEFAULT_AUTHOR_PHONE));
        assertThat(author.getMunicipality().getId(), is(testMunicipality));
        assertThat(author.getCreateTime(), is(new LocalDate()));

        ReflectionTestUtils.assertNoNullFields(author);
    }

    @Test
    public void delete_author_removes_author_and_its_participant() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality);
        Long authorId = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality));

        int originalAuthorCount = authorDao.findAuthors(initiativeId).size();
        int originalParticipantCount = participantDao.findAllParticipants(initiativeId).size();

        authorDao.deleteAuthor(authorId);

        assertThat(authorDao.findAuthors(initiativeId), hasSize(originalAuthorCount - 1));
        assertThat(participantDao.findAllParticipants(initiativeId), hasSize(originalParticipantCount - 1));
    }

    @Test
    public void deleting_final_author_is_not_allowed() {
        testHelper.createCollaborativeAccepted(testMunicipality);

        thrown.expect(OperationNotAllowedException.class);
        thrown.expectMessage(containsString("Deleting last author is forbidden"));
        authorDao.deleteAuthor(testHelper.getLastAuthorId());
    }

}
