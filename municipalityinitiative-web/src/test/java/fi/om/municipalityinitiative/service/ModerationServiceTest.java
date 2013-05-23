package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.util.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ModerationServiceTest {

    public static final long INITIATIVE_ID = 3L;

    private ModerationService moderationService;

    private InitiativeDao initiativeDaoMock;

    private LoginUserHolder loginUserHolder;

    @Before
    public void setup() throws Exception {
        initiativeDaoMock = mock(InitiativeDao.class);
        moderationService = new ModerationService();

        moderationService.initiativeDao = initiativeDaoMock;
        moderationService.emailService = mock(EmailService.class);
        moderationService.municipalityDao = mock(MunicipalityDao.class);
        moderationService.authorDao = mock(AuthorDao.class);
        stub(moderationService.authorDao.getAuthorEmails(anyLong())).toReturn(Collections.singletonList("")); // Avoid nullpointer temporarily

        loginUserHolder = new LoginUserHolder(User.normalUser(null, Collections.<Long>emptySet()));
    }

    @Test
    public void all_functions_require_om_rights() throws InvocationTargetException, IllegalAccessException {

        for (Method method : ModerationService.class.getDeclaredMethods()) {
            if (method.getModifiers() != 1) {
                continue;
            }
            Object[] parameters = new Object[method.getParameterTypes().length];
            parameters[0] = loginUserHolder;
            try {
                System.out.println("Checking that method requires om rights: " + method.getName());
                method.invoke(moderationService, parameters);
                fail("Should have checked om-rights for user: " + method.getName());
            } catch (InvocationTargetException e) {
                assertThat(e.getCause(), instanceOf(AccessDeniedException.class));
            }
        }
    }

    private void setOmUser() {
        loginUserHolder = new LoginUserHolder(User.omUser(""));
    }

    @Test(expected = OperationNotAllowedException.class)
    public void accepting_initiative_checks_that_it_can_be_accepted() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.ACCEPTED, InitiativeType.UNDEFINED));
        moderationService.accept(loginUserHolder, INITIATIVE_ID, "", null);
    }

    @Test
    public void accepting_initiative_sets_state_as_accepted_and_saves_comment_if_type_is_undefined() {

        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.REVIEW, InitiativeType.UNDEFINED));

        String comment = "this is om-comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, comment, Locales.LOCALE_FI);

        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);
        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.ACCEPTED);
        verifyNoMoreInteractions(initiativeDaoMock);

    }

    @Test
    public void accepting_initiative_sends_correct_state_email_if_type_is_undefined() {

        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.REVIEW, InitiativeType.UNDEFINED));

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyList(), anyString(), eq(EmailMessageType.ACCEPTED_BY_OM));
    }

    @Test
    public void accepting_initiative_sets_state_as_published_and_saves_comment_and_sets_sent_time_if_type_single() {

        setOmUser();
        Initiative initiative = initiative(InitiativeState.REVIEW, InitiativeType.SINGLE);
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative);

        String comment = "this is om-comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, comment, Locales.LOCALE_FI);

        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);
        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.PUBLISHED);
        verify(initiativeDaoMock).markInitiativeAsSent(INITIATIVE_ID);
        verifyNoMoreInteractions(initiativeDaoMock);

    }


    @Test
    public void accepting_initiative_sends_correct_state_email_if_type_single() {

        setOmUser();
        Initiative initiative = initiative(InitiativeState.REVIEW, InitiativeType.SINGLE);
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative);

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.ACCEPTED_BY_OM_AND_SENT));
        verify(moderationService.emailService).sendSingleToMunicipality(any(Initiative.class), anyListOf(Author.class), anyString(), eq(Locales.LOCALE_FI));

    }

    @Test(expected = OperationNotAllowedException.class)
    public void rejecting_initiative_checks_that_it_can_be_accepted() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.ACCEPTED, InitiativeType.UNDEFINED));
        moderationService.reject(loginUserHolder, INITIATIVE_ID, "");
    }

    @Test
    public void rejecting_initiative_sets_state_as_draft_and_saves_comment() {

        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.REVIEW, InitiativeType.UNDEFINED));

        String comment = "this is om-comment";
        moderationService.reject(loginUserHolder, INITIATIVE_ID, comment);
        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.DRAFT);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);
        verifyNoMoreInteractions(initiativeDaoMock);
    }

    @Test
    public void rejecting_initiative_sends_email() {

        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.REVIEW, InitiativeType.UNDEFINED));

        moderationService.reject(loginUserHolder, INITIATIVE_ID, null);
        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.REJECTED_BY_OM));
    }

    @Test
    public void accepting_fixState_review_initiative_sets_moderator_comment_and_fixState() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(publishedCollaborative(FixState.REVIEW));

        String moderatorComment = "moderator comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, moderatorComment, null);
        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, moderatorComment);
        verify(initiativeDaoMock).updateInitiativeFixState(INITIATIVE_ID, FixState.OK);
        verifyNoMoreInteractions(initiativeDaoMock);
    }

    @Test
    public void accepting_fixState_review_initiative_sends_status_email() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(publishedCollaborative(FixState.REVIEW));

        String moderatorComment = "moderator comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, moderatorComment, null);
        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.ACCEPTED_BY_OM));
        verifyNoMoreInteractions(moderationService.emailService);
    }

    @Test
    public void rejecting_fixState_review_initiative_sets_moderator_comment_and_fixState() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(publishedCollaborative(FixState.REVIEW));

        String moderatorComment = "moderator comment";
        moderationService.reject(loginUserHolder, INITIATIVE_ID, moderatorComment);
        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, moderatorComment);
        verify(initiativeDaoMock).updateInitiativeFixState(INITIATIVE_ID, FixState.FIX);
        verifyNoMoreInteractions(initiativeDaoMock);
    }

    @Test
    public void rejecting_fixState_review_initiative_sends_status_email() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(publishedCollaborative(FixState.REVIEW));

        String moderatorComment = "moderator comment";
        moderationService.reject(loginUserHolder, INITIATIVE_ID, moderatorComment);
        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.REJECTED_BY_OM));
        verifyNoMoreInteractions(moderationService.emailService);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void sendInitiativeBackForFixing_checks_that_initiative_may_be_sent_back() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.DRAFT, InitiativeType.UNDEFINED));
        moderationService.sendInitiativeBackForFixing(loginUserHolder, INITIATIVE_ID, "");
    }

    @Test
    public void sendInitiativeBackForFixing_sets_initiative_fixState_and_adds_moderator_comment() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE));

        String moderatorComment = "some mod comment";
        moderationService.sendInitiativeBackForFixing(loginUserHolder, INITIATIVE_ID, moderatorComment);

        verify(initiativeDaoMock, times(2)).get(INITIATIVE_ID);
        verify(initiativeDaoMock).updateInitiativeFixState(INITIATIVE_ID, FixState.FIX);
        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, moderatorComment);

    }

    @Test
    public void sendInitiativeBackForFixing_sends_status_email() {
        setOmUser();
        stub(initiativeDaoMock.get(INITIATIVE_ID)).toReturn(initiative(InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE));

        moderationService.sendInitiativeBackForFixing(loginUserHolder, INITIATIVE_ID, "");
        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.REJECTED_BY_OM));
    }

    private static Initiative publishedCollaborative(FixState fixState) {
        Initiative initiative = initiative(InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        initiative.setFixState(fixState);
        return initiative;
    }

    private static Initiative initiative(InitiativeState state, InitiativeType type) {
        Initiative initiative = new Initiative();
        initiative.setId(INITIATIVE_ID);
        initiative.setState(state);
        initiative.setType(type);
        initiative.setFixState(FixState.OK);
        initiative.setMunicipality(new Municipality(0, "", "", false));

        return initiative;

    }

}
