package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InvitationNotValidException;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUICreateDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QAuthorInvitation;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthorServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    AuthorService authorService;

    @Resource
    AuthorDao authorDao; // FIXME: Do not depend on this

    private Long testMunicipality;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    public void childSetup() {
        testMunicipality = testHelper.createTestMunicipality("municipality");
    }

    @Test(expected = AccessDeniedException.class)
    public void create_invitation_checks_management_rights_for_initiative() {
        authorService.createAuthorInvitation(null, TestHelper.unknownLoginUserHolder, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void create_invitation_not_allowed_if_initiative_already_sent() {

        Long initiativeId = testHelper.createSingleSent(testMunicipality);

        authorService.createAuthorInvitation(initiativeId, TestHelper.authorLoginUserHolder, null);
    }

    @Test
    public void create_invitation_sets_required_information() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);

        AuthorInvitationUICreateDto authorInvitationUICreateDto = authorInvitation();

        authorService.createAuthorInvitation(initiativeId, TestHelper.authorLoginUserHolder, authorInvitationUICreateDto);

        AuthorInvitation createdInvitation = testHelper.getAuthorInvitation(RandomHashGenerator.getPrevious());
        assertThat(createdInvitation.getConfirmationCode(), is(RandomHashGenerator.getPrevious()));
        assertThat(createdInvitation.getName(), is("name"));
        assertThat(createdInvitation.getInvitationTime(), is(notNullValue()));
        assertThat(createdInvitation.getEmail(), is("email"));
    }

    @Test
    public void reject_author_invitation() {
        Long initiativeId = testHelper.createCollaborativeReview(testHelper.createTestMunicipality("name"));

        authorService.createAuthorInvitation(initiativeId, TestHelper.authorLoginUserHolder, authorInvitation());
        precondition(testHelper.getAuthorInvitation(RandomHashGenerator.getPrevious()).isRejected(), is(false));

        authorService.rejectInvitation(initiativeId, RandomHashGenerator.getPrevious());

        assertThat(testHelper.getAuthorInvitation(RandomHashGenerator.getPrevious()).isRejected(), is(true));

    }

    @Test
    public void confirm_author_invitation_adds_new_author_with_given_information() {

        Long authorsMunicipality = testHelper.createTestMunicipality("name");
        Long initiativeId = testHelper.createCollaborativeReview(authorsMunicipality);
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

        precondition(countAllAuthors(), is(1L)); // XXX: This does not care if the authors does not belong to this initiative
        precondition(participantCountOfInitiative(initiativeId), is(1));

        authorService.confirmAuthorInvitation(initiativeId, createDto, null);

        // Author count is increased
        precondition(countAllAuthors(), is(2L));

        // Check new author information
        List<Author> currentAuthors = currentAuthors(initiativeId);
        Author createdAuthor = currentAuthors.get(currentAuthors.size() -1);
        assertThat(createdAuthor.getContactInfo().getName(), is(createDto.getContactInfo().getName()));
        assertThat(createdAuthor.getContactInfo().getEmail(), is(createDto.getContactInfo().getEmail()));
        assertThat(createdAuthor.getContactInfo().getAddress(), is(createDto.getContactInfo().getAddress()));
        assertThat(createdAuthor.getContactInfo().getPhone(), is(createDto.getContactInfo().getPhone()));
        assertThat(createdAuthor.getContactInfo().isShowName(), is(createDto.getContactInfo().isShowName()));
        assertThat(createdAuthor.getMunicipality().getId(), is(authorsMunicipality));

        assertThat(participantCountOfInitiative(initiativeId), is(2));
    }

    private List<Author> currentAuthors(Long initiativeId) {
        return authorDao.findAuthors(initiativeId);
    }

    private int participantCountOfInitiative(Long initiativeId) {
        return testHelper.getInitiative(initiativeId).getParticipantCount();
    }

    @Test(expected = OperationNotAllowedException.class)
    public void confirm_author_invitation_not_allowed() {
        Long initiativeId = testHelper.createSingleSent(testMunicipality);

        authorService.confirmAuthorInvitation(initiativeId, new AuthorInvitationUIConfirmDto(), null);
    }

    @Test
    public void confirm_author_with_expired_invitation_throws_exception() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        AuthorInvitation expiredInvitation = createExpiredInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.setConfirmCode(expiredInvitation.getConfirmationCode());

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is expired");
        authorService.confirmAuthorInvitation(initiativeId, confirmDto, null);
    }

    @Test
    public void confirm_author_with_rejected_invitation_throws_exception() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        AuthorInvitation rejectedInvitation = createRejectedInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = new AuthorInvitationUIConfirmDto();
        confirmDto.setConfirmCode(rejectedInvitation.getConfirmationCode());

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is rejected");
        authorService.confirmAuthorInvitation(initiativeId, confirmDto, null);
    }

    @Test
    public void confirm_author_with_invalid_confirmCode_throws_exception() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        createInvitation(initiativeId);

        AuthorInvitationUIConfirmDto invitationUIConfirmDto = new AuthorInvitationUIConfirmDto();
        invitationUIConfirmDto.setConfirmCode("bätmään!");

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(containsString("bätmään"));
        authorService.confirmAuthorInvitation(initiativeId, invitationUIConfirmDto, null);
    }

    @Test
    public void invitation_is_removed_after_confirmation() {

        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        AuthorInvitation authorInvitation = createInvitation(initiativeId);

        AuthorInvitationUIConfirmDto confirmDto = ReflectionTestUtils.modifyAllFields(new AuthorInvitationUIConfirmDto());
        confirmDto.setConfirmCode(authorInvitation.getConfirmationCode());
        confirmDto.setInitiativeMunicipality(testMunicipality);
        confirmDto.setHomeMunicipality(testMunicipality);

        precondition(allCurrentInvitations(), is(1L));
        authorService.confirmAuthorInvitation(initiativeId, confirmDto, null);
        assertThat(allCurrentInvitations(), is(0L));

    }

    private static DateTime expiredInvitationTime() {
        return DateTime.now().minusMonths(1);
    }

    @Test
    public void prefilled_author_confirmation_contains_all_information() {
        Long municipalityId = testHelper.createTestMunicipality("name");
        Long initiativeId = testHelper.createCollaborativeReview(municipalityId);
        authorService.createAuthorInvitation(initiativeId, TestHelper.authorLoginUserHolder, authorInvitation());

        AuthorInvitationUIConfirmDto confirmDto = authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, RandomHashGenerator.getPrevious());
        assertThat(confirmDto.getMunicipality(), is(municipalityId));
        assertThat(confirmDto.getContactInfo().getName(), is(authorInvitation().getAuthorName()));
        assertThat(confirmDto.getContactInfo().getEmail(), is(authorInvitation().getAuthorEmail()));
        assertThat(confirmDto.getConfirmCode(), is(RandomHashGenerator.getPrevious()));
    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_expired() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        AuthorInvitation expiredInvitation = createExpiredInvitation(initiativeId);

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is expired");
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, expiredInvitation.getConfirmationCode());

    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_rejected() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        AuthorInvitation rejectedInvitation = createRejectedInvitation(initiativeId);

        thrown.expect(InvitationNotValidException.class);
        thrown.expectMessage("Invitation is rejected");
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, rejectedInvitation.getConfirmationCode());
    }

    @Test
    public void prefilled_author_confirmation_throws_exception_if_invitation_not_found() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);

        thrown.expect(InvitationNotValidException.class);
        authorService.getPrefilledAuthorInvitationConfirmDto(initiativeId, "töttöröö");
    }

    @Test
    public void resend_invitation_throws_exception_if_no_management_rights() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        thrown.expect(AccessDeniedException.class);
        authorService.resendInvitation(initiativeId, TestHelper.unknownLoginUserHolder, null);
    }

    @Test
    public void resend_invitation_updates_invitation_time_to_current_time() {
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality);
        DateTime invitationTime = new DateTime(2010, 1, 1, 0, 0);
        AuthorInvitation invitation = createInvitation(initiativeId, invitationTime);

        precondition(testHelper.getAuthorInvitation(invitation.getConfirmationCode()).getInvitationTime(), is(invitationTime));
        precondition(allCurrentInvitations(), is(1L));

        authorService.resendInvitation(initiativeId, TestHelper.authorLoginUserHolder, invitation.getConfirmationCode());

        assertThat(testHelper.getAuthorInvitation(invitation.getConfirmationCode()).getInvitationTime().toLocalDate(), is(new LocalDate()));
        assertThat(allCurrentInvitations(), is(1L));

    }

    @Test
    public void deleting_author_throws_exception_if_not_management_rights() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality);

        thrown.expect(AccessDeniedException.class);
        authorService.deleteAuthor(initiativeId, TestHelper.unknownLoginUserHolder, testHelper.getLastAuthorId());

    }

    @Test
    public void deleting_final_author_is_not_allowed() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality);
        LoginUserHolder fakeLoginUserHolderWithManagementRights = new LoginUserHolder(User.normalUser(-5L, Collections.singleton(initiativeId)));

        thrown.expect(OperationNotAllowedException.class);
        thrown.expectMessage(containsString("Unable to delete author"));
        authorService.deleteAuthor(initiativeId, fakeLoginUserHolderWithManagementRights, testHelper.getLastAuthorId());
    }

    @Test
    public void deleting_author_fails_if_initiativeId_and_authorId_mismatch() {
        Long initiative1 = testHelper.createCollaborativeAccepted(testMunicipality);
        Long author1 = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiative1, testMunicipality));

        Long initiative2 = testHelper.createCollaborativeAccepted(testMunicipality);
        Long author2 = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiative2, testMunicipality));

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(containsString("initiative"));
        thrown.expectMessage(containsString("author"));
        authorService.deleteAuthor(initiative2, TestHelper.authorLoginUserHolder, author1);

    }

    @Test
    public void deleting_author_fails_if_trying_to_delete_myself() {
        Long initiative = testHelper.createCollaborativeAccepted(testMunicipality);
        Long anotherAuthor = testHelper.getLastAuthorId();
        Long currentAuthor = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiative, testMunicipality));

        thrown.expect(OperationNotAllowedException.class);
        thrown.expectMessage(containsString("Removing yourself from authors is not allowed"));
        authorService.deleteAuthor(initiative, TestHelper.authorLoginUserHolder, currentAuthor);

    }

    @Test
    public void deleting_author_succeeds_and_sends_emails() throws Exception {

        Long initiative = testHelper.createCollaborativeAccepted(testMunicipality);
        Long anotherAuthor = testHelper.getLastAuthorId();
        Long currentAuthor = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiative, testMunicipality).withParticipantEmail("author_left@example.com"));

        precondition(countAllAuthors(), is(2L));

        authorService.deleteAuthor(initiative, TestHelper.authorLoginUserHolder, anotherAuthor);

        assertThat(countAllAuthors(), is(1L));

        List<MimeMessage> sentMessages = javaMailSenderFake.getSentMessages(2);

        MimeMessage messageToOtherAuthors = sentMessages.get(0);
        assertThat(messageToOtherAuthors.getAllRecipients()[0].toString(), is("author_left@example.com"));
        assertThat(messageToOtherAuthors.getSubject(), containsString("Vastuuhenkilö on poistettu aloitteestasi"));
        assertThat(JavaMailSenderFake.getMessageContent(messageToOtherAuthors).html, containsString(TestHelper.DEFAULT_PARTICIPANT_EMAIL));

        MimeMessage messageToDeletedAuthor = sentMessages.get(1);
        assertThat(JavaMailSenderFake.getSingleRecipient(messageToDeletedAuthor), is(TestHelper.DEFAULT_PARTICIPANT_EMAIL));
        assertThat(messageToDeletedAuthor.getSubject(), containsString("Sinut on poistettu aloitteen vastuuhenkilöistä"));

    }

    @Test
    public void two_concurrent_tries_to_remove_two_last_authors_will_fail() {

        final Long initiative = testHelper.createCollaborativeAccepted(testMunicipality);

        final Long author1 = testHelper.getLastAuthorId();
        final Long author2 = testHelper.createAuthorAndParticipant(new TestHelper.AuthorDraft(initiative, testMunicipality));

        final LoginUserHolder loginUserHolder =  new LoginUserHolder(User.normalUser(-1L, Collections.singleton(initiative)));

        List<Callable<Boolean>> callables = Lists.newArrayList();
        callables.add(authorDeletorCallable(initiative, loginUserHolder, author1));
        callables.add(authorDeletorCallable(initiative, loginUserHolder, author2));

        executeCallablesSilently(callables);

        assertThat(countAllAuthors(), is(1L));
    }

    private Long countAllAuthors() {
        return testHelper.countAll(QAuthor.author);
    }

    private static void executeCallablesSilently(List<Callable<Boolean>> threads) {
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            for (Future<Boolean> future : executor.invokeAll(threads)) {
                future.get();
            }
        } catch (Exception e) {
            //e.printStackTrace();

        } finally {
            executor.shutdownNow();
        }
    }

    private Callable<Boolean> authorDeletorCallable(final Long initiative, final LoginUserHolder loginUserHolder, final Long givenAuthor) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                deleteAuthorAndSleepForSecond(initiative, loginUserHolder, givenAuthor);
                return true;
            }
        };
    }

    private void deleteAuthorAndSleepForSecond(Long initiativeId, LoginUserHolder loginUserHolder, Long authorId) throws InterruptedException {
        authorService.deleteAuthor(initiativeId, loginUserHolder, authorId);
    }

    private Long allCurrentInvitations() {
        return testHelper.countAll(QAuthorInvitation.authorInvitation);
    }

    private AuthorInvitation createExpiredInvitation(Long initiativeId) {
        AuthorInvitation authorInvitation = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setInvitationTime(expiredInvitationTime());
        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }

    private AuthorInvitation createInvitation(Long initiativeId, DateTime invitationTime) {
        AuthorInvitation authorInvitation = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setInvitationTime(invitationTime);
        authorDao.addAuthorInvitation(authorInvitation);
        return authorInvitation;
    }
    private AuthorInvitation createInvitation(Long initiativeId) {
        return createInvitation(initiativeId, DateTime.now());
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
