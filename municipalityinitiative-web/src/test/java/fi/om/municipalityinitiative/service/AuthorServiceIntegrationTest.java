package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newweb.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    AuthorService authorService;

    @Resource
    AuthorDao authorDao;

    @Resource
    TestHelper testHelper;

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

    private static AuthorInvitationUICreateDto authorInvitation() {
        AuthorInvitationUICreateDto authorInvitationUICreateDto = new AuthorInvitationUICreateDto();
        authorInvitationUICreateDto.setAuthorName("name");
        authorInvitationUICreateDto.setAuthorEmail("email");
        return authorInvitationUICreateDto;
    }

}
