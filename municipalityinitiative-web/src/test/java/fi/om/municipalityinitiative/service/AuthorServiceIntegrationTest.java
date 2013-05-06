package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.newweb.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    AuthorService authorService;

    @Resource
    AuthorDao authorDao;

    @Resource
    TestHelper testHelper;

    @Resource
    InitiativeDao initiativeDao;

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
    }

    @Test(expected = AccessDeniedException.class)
    public void create_invitation_checks_management_rights_for_initiative() {
        authorService.createAuthorInvitation(null, unknownLoginUserHolder, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void create_invitation_not_allowed_if_initiative_already_sent() {

        Long initiativeId = testHelper.createSingleSent(testHelper.createTestMunicipality("name"));

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, null);
    }

    @Test
    public void create_invitation_sets_required_information() {
        Long initiativeId = testHelper.createCollectableReview(testHelper.createTestMunicipality("name"));

        AuthorInvitationUICreateDto authorInvitationUICreateDto = authorInvitation();

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitationUICreateDto);

        AuthorInvitation createdInvitation = authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious());
        assertThat(createdInvitation.getConfirmationCode(), is(RandomHashGenerator.getPrevious()));
        assertThat(createdInvitation.getName(), is("name"));
        assertThat(createdInvitation.getInvitationTime(), is(notNullValue()));
        assertThat(createdInvitation.getEmail(), is("email"));
    }

    @Test
    public void reject_author_invitation() {
        Long initiativeId = testHelper.createCollectableReview(testHelper.createTestMunicipality("name"));

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitation());
        assertThat(authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious()).isRejected(), is(false));

        authorDao.rejectAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious());

        assertThat(authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious()).isRejected(), is(true));

    }

    @Test
    public void accept_author_invitation_adds_new_author_with_given_information() {

        Long municipalityId = testHelper.createTestMunicipality("name");
        Long initiativeId = testHelper.createCollectableReview(municipalityId);
        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitation());

        AuthorInvitationUIConfirmDto createDto = new AuthorInvitationUIConfirmDto();
        createDto.setInitiativeMunicipality(municipalityId);
        createDto.setName("name");
        createDto.setAddress("address");
        createDto.setParticipantEmail("email");
        createDto.setPhone("phone");
        createDto.setShowName(true);
        createDto.setConfirmCode(RandomHashGenerator.getPrevious());
        createDto.setHomeMunicipality(municipalityId);


        precondition(authorService.findAuthors(initiativeId, authorLoginUserHolder), hasSize(1));
        precondition(participantCountOfInitiative(initiativeId), is(1));

        authorService.confirmAuthorInvitation(initiativeId, createDto);

        List<Author> authors = authorService.findAuthors(initiativeId, authorLoginUserHolder);

        // Author count is increased
        assertThat(authors, hasSize(2));

        // Check new author information
        Author createdAuthor = authors.get(0);
        assertThat(createdAuthor.getContactInfo().getName(), is(createDto.getName()));
        assertThat(createdAuthor.getContactInfo().getEmail(), is(createDto.getParticipantEmail()));
//        assertThat(createdAuthor.getContactInfo().getAddress(), is(createDto.getAddress()));
//        assertThat(createdAuthor.getContactInfo().getPhone(), is(createDto.getPhone()));
        assertThat(createdAuthor.getMunicipality().getId(), is(municipalityId));
        assertThat(createdAuthor.getContactInfo().isShowName(), is(true));

        // TODO: Check that managementHash is created and ok

        assertThat(participantCountOfInitiative(initiativeId), is(2));
    }

    private int participantCountOfInitiative(Long initiativeId) {
        return initiativeDao.getByIdWithOriginalAuthor(initiativeId).getParticipantCount();
    }

    // TODO: Not allowed
    // TODO: Expired
    // TODO: Rejected
    // TODO: Invalid confirmationCode
    // TODO: Invitation is removed after acceptance

    private static AuthorInvitationUICreateDto authorInvitation() {
        AuthorInvitationUICreateDto authorInvitationUICreateDto = new AuthorInvitationUICreateDto();
        authorInvitationUICreateDto.setAuthorName("name");
        authorInvitationUICreateDto.setAuthorEmail("email");
        return authorInvitationUICreateDto;
    }

}
