package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InvitationNotValidException;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newweb.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthorServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    AuthorService authorService;

    @Resource
    AuthorDao authorDao;

    @Resource
    TestHelper testHelper;

    @Resource
    InitiativeDao initiativeDao;
    private Long testMunicipality;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
        testMunicipality = testHelper.createTestMunicipality("municipality");
    }

    @Test(expected = AccessDeniedException.class)
    public void create_invitation_checks_management_rights_for_initiative() {
        authorService.createAuthorInvitation(null, unknownLoginUserHolder, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void create_invitation_not_allowed_if_initiative_already_sent() {

        Long initiativeId = testHelper.createSingleSent(testMunicipality);

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, null);
    }

    @Test
    public void create_invitation_sets_required_information() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);

        AuthorInvitationUICreateDto authorInvitationUICreateDto = authorInvitation();

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitationUICreateDto);

        AuthorInvitation createdInvitation = authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious());
        assertThat(createdInvitation.getConfirmationCode(), is(RandomHashGenerator.getPrevious()));
        assertThat(createdInvitation.getName(), is("name"));
        assertThat(createdInvitation.getInvitationTime(), is(notNullValue()));
        assertThat(createdInvitation.getEmail(), is("email"));
    }

    @Test
    public void reject_author_invitation() { // TODO: Implement service method, this uses dao layer.
        Long initiativeId = testHelper.createCollectableReview(testHelper.createTestMunicipality("name"));

        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitation());
        assertThat(authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious()).isRejected(), is(false));

        authorDao.rejectAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious());

        assertThat(authorDao.getAuthorInvitation(initiativeId, RandomHashGenerator.getPrevious()).isRejected(), is(true));

    }

    @Test
    public void confirm_author_invitation_adds_new_author_with_given_information() {

        Long authorsMunicipality = testHelper.createTestMunicipality("name");
        Long initiativeId = testHelper.createCollectableReview(authorsMunicipality);
        AuthorInvitation invitation = createInvitation(initiativeId);

        AuthorInvitationUIConfirmDto createDto = new AuthorInvitationUIConfirmDto();
        createDto.setContactInfo(new ContactInfo());
        createDto.setInitiativeMunicipality(testMunicipality);
        createDto.getContactInfo().setName("name");
        createDto.getContactInfo().setAddress("address");
        createDto.getContactInfo().setEmail("email");
        createDto.getContactInfo().setPhone("phone");
        createDto.getContactInfo().setShowName(true);
        createDto.setConfirmCode(invitation.getConfirmationCode());
        createDto.setMunicipalMembership(Membership.community); //XXX: Not tested
        createDto.setHomeMunicipality(authorsMunicipality);

        precondition(currentAuthors(initiativeId), hasSize(1));
        precondition(participantCountOfInitiative(initiativeId), is(1));

        authorService.confirmAuthorInvitation(initiativeId, createDto);

        // Author count is increased
        assertThat(currentAuthors(initiativeId), hasSize(2));

        // Check new author information
        Author createdAuthor = currentAuthors(initiativeId).get(0);
        assertThat(createdAuthor.getContactInfo().getName(), is(createDto.getContactInfo().getName()));
        assertThat(createdAuthor.getContactInfo().getEmail(), is(createDto.getContactInfo().getEmail()));
        assertThat(createdAuthor.getContactInfo().getAddress(), is(createDto.getContactInfo().getAddress()));
        assertThat(createdAuthor.getContactInfo().getPhone(), is(createDto.getContactInfo().getPhone()));
        assertThat(createdAuthor.getContactInfo().isShowName(), is(createDto.getContactInfo().isShowName()));
        assertThat(createdAuthor.getMunicipality().getId(), is(authorsMunicipality));

        // TODO: Check that managementHash is created and ok

        assertThat(participantCountOfInitiative(initiativeId), is(2));
    }

    private List<Author> currentAuthors(Long initiativeId) {
        return authorDao.findAuthors(initiativeId);
    }

    private int participantCountOfInitiative(Long initiativeId) {
        return initiativeDao.getByIdWithOriginalAuthor(initiativeId).getParticipantCount();
    }

    @Test(expected = OperationNotAllowedException.class)
    public void confirm_author_invitation_not_allowed() {
        Long initiativeId = testHelper.createSingleSent(testMunicipality);

        authorService.confirmAuthorInvitation(initiativeId, new AuthorInvitationUIConfirmDto());
    }

    @Test
    public void confirm_author_with_expired_invitation_throws_exception() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation expiredInvitation = createExpiredInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.setConfirmCode(expiredInvitation.getConfirmationCode());

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is expired");
        authorService.confirmAuthorInvitation(initiativeId, confirmDto);
    }

    @Test
    public void confirm_author_with_rejected_invitation_throws_exception() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation rejectedInvitation = createRejectedInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.setConfirmCode(rejectedInvitation.getConfirmationCode());

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is rejected");
        authorService.confirmAuthorInvitation(initiativeId, confirmDto);
    }

    @Test
    public void confirm_author_with_invalid_confirmCode_throws_exception() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        createInvitation(initiativeId);

        AuthorInvitationUIConfirmDto invitationUIConfirmDto = new AuthorInvitationUIConfirmDto();
        invitationUIConfirmDto.setConfirmCode("bätmään!");

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(containsString("bätmään"));
        authorService.confirmAuthorInvitation(initiativeId, invitationUIConfirmDto);
    }

    @Test
    public void invitation_is_removed_after_confirmation() {

        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation authorInvitation = createInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = ReflectionTestUtils.modifyAllFields(new AuthorInvitationUIConfirmDto());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());
        confirmDto.setInitiativeMunicipality(testMunicipality);
        confirmDto.setHomeMunicipality(testMunicipality);

        precondition(authorDao.findInvitations(initiativeId), hasSize(1));
        authorService.confirmAuthorInvitation(initiativeId, confirmDto);
        assertThat(authorDao.findInvitations(initiativeId), hasSize(0));


    }

    private static DateTime expiredInvitationTime() {
        return DateTime.now().minusMonths(1);
    }

    @Test
    public void prefilled_author_confirmation_contains_all_information() {
        Long municipalityId = testHelper.createTestMunicipality("name");
        Long initiativeId = testHelper.createCollectableReview(municipalityId);
        authorService.createAuthorInvitation(initiativeId, authorLoginUserHolder, authorInvitation());

        AuthorInvitationUIConfirmDto confirmDto = authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, RandomHashGenerator.getPrevious());
        assertThat(confirmDto.getMunicipality(), is(municipalityId));
        assertThat(confirmDto.getContactInfo().getName(), is(authorInvitation().getAuthorName()));
        assertThat(confirmDto.getContactInfo().getEmail(), is(authorInvitation().getAuthorEmail()));
        assertThat(confirmDto.getConfirmCode(), is(RandomHashGenerator.getPrevious()));
    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_expired() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation expiredInvitation = createExpiredInvitation(initiativeId);

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is expired");
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, expiredInvitation.getConfirmationCode());

    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_rejected() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation rejectedInvitation = createRejectedInvitation(initiativeId);

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is rejected");
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, rejectedInvitation.getConfirmationCode());

    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_not_found() {
        Long initiativeId = testHelper.createCollectableReview(testMunicipality);
        AuthorInvitation invitation = createInvitation(initiativeId);

        thrown.expect(NotFoundException.class);
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, "töttöröö");
    }

    private AuthorInvitation createExpiredInvitation(Long initiativeId) {
        AuthorInvitation authorInvitation = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setInvitationTime(expiredInvitationTime());
        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }

    private AuthorInvitation createInvitation(Long initiativeId) {
        AuthorInvitation authorInvitation = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setInvitationTime(DateTime.now());
        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }

    private AuthorInvitation createRejectedInvitation(Long initiativeId) {
        AuthorInvitation authorInvitation = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setInvitationTime(DateTime.now());
        authorDao.addAuthorInvitation(authorInvitation);
        authorDao.rejectAuthorInvitation(initiativeId, authorInvitation.getConfirmationCode());
        return authorInvitation;
    }

    private static AuthorInvitationUICreateDto authorInvitation() {
        AuthorInvitationUICreateDto authorInvitationUICreateDto = new AuthorInvitationUICreateDto();
        authorInvitationUICreateDto.setAuthorName("name");
        authorInvitationUICreateDto.setAuthorEmail("email");
        return authorInvitationUICreateDto;
    }

}
