package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InvitationNotValidException;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcAuthorDaoTest {

    @Resource
    AuthorDao authorDao;

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
        Long collaborativeAccepted = testHelper.createCollaborativeAccepted(testMunicipality);

        Set<Long> ids = authorDao.getAuthorsInitiatives(testHelper.getPreviousTestManagementHash());

        assertThat(ids, hasSize(1));
        assertThat(ids, contains(collaborativeAccepted));
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
        long originalParticipantCount = participantCountOfInitiative(initiativeId);

        authorDao.deleteAuthor(authorId);

        assertThat(authorDao.findAuthors(initiativeId), hasSize(originalAuthorCount - 1));
        assertThat(participantCountOfInitiative(initiativeId), is(originalParticipantCount - 1));
    }

    @Test
    public void delete_author_decreases_denormalized_participant_count() {

        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality);
        Long authorId = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality));

        int originalAuthorCount = authorDao.findAuthors(initiativeId).size();
        int originalParticipantCount = testHelper.getInitiative(initiativeId).getParticipantCount();

        authorDao.deleteAuthor(authorId);

        assertThat(authorDao.findAuthors(initiativeId), hasSize(originalAuthorCount - 1));
        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(originalParticipantCount - 1));

    }

    private Long participantCountOfInitiative(Long initiativeId) {
        return testHelper.countAll(QParticipant.participant, QParticipant.participant.municipalityInitiativeId.eq(initiativeId));
    }

    @Test
    public void deleting_final_author_is_not_allowed() {
        testHelper.createCollaborativeAccepted(testMunicipality);

        thrown.expect(OperationNotAllowedException.class);
        thrown.expectMessage(containsString("Deleting last author is forbidden"));
        authorDao.deleteAuthor(testHelper.getLastAuthorId());
    }

    @Test
    public void updating_management_hash() {

        testHelper.createCollaborativeReview(testMunicipality);

        assertThat(authorDao.getAuthorsInitiatives(testHelper.getPreviousTestManagementHash()), hasSize(1));
        authorDao.updateManagementHash(testHelper.getLastAuthorId(), "some other");
        assertThat(authorDao.getAuthorsInitiatives(testHelper.getPreviousTestManagementHash()), hasSize(0));

    }

}
