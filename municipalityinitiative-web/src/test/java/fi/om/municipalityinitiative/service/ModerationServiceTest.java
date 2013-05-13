package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.LoginUser;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
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

        loginUserHolder = new LoginUserHolder(LoginUser.normalUser(null, Collections.<Long>emptySet()), Maybe.<Initiative>absent());
    }

    @Test
    public void all_functions_require_om_rights() throws InvocationTargetException, IllegalAccessException {

        for (Method method : ModerationService.class.getDeclaredMethods()) {
            Object[] parameters = new Object[method.getParameterTypes().length];
            parameters[0] = loginUserHolder;
            try {
                method.invoke(moderationService, parameters);
                fail("Should have checked om-rights for user: " + method.getName());
            } catch (InvocationTargetException e) {
                assertThat(e.getCause(), instanceOf(AccessDeniedException.class));
            }
        }
    }

    private void setOmUser() {
        loginUserHolder = new LoginUserHolder(LoginUser.omUser(), Maybe.<Initiative>absent());
    }

    @Test
    public void accepting_initiative_sets_state_as_accepted_if_type_is_undefined() {

        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.ACCEPTED);
    }

    @Test
    public void accepting_initiative_sends_correct_state_email_if_type_is_undefined() {

        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyList(), anyString(), eq(EmailMessageType.ACCEPTED_BY_OM));
    }

    @Test
    public void accepting_initiative_saves_comment_if_type_is_undefined() {
        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        String comment = "this is om-comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, comment, Locales.LOCALE_FI);

        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);
    }

    @Test
    public void accepting_initiative_sets_state_as_published_if_type_single() {

        setOmUser();
        Initiative initiative = initiativeWithAuthorEmailTypeUndefined();
        initiative.setType(InitiativeType.SINGLE);
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiative);

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.PUBLISHED);

    }

    @Test
    public void accepting_initiative_sends_correct_state_email_if_type_single() {

        setOmUser();
        Initiative initiative = initiativeWithAuthorEmailTypeUndefined();
        initiative.setType(InitiativeType.SINGLE);
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiative);

        moderationService.accept(loginUserHolder, INITIATIVE_ID, null, Locales.LOCALE_FI);

        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.ACCEPTED_BY_OM_AND_SENT));
        verify(moderationService.emailService).sendSingleToMunicipality(any(Initiative.class), anyListOf(Author.class), anyString(), eq(Locales.LOCALE_FI));

    }


    @Test
    public void accepting_initiative_saves_comment_if_type_is_single() {
        setOmUser();
        Initiative initiative = initiativeWithAuthorEmailTypeUndefined();
        initiative.setType(InitiativeType.SINGLE);
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiative);

        String comment = "this is om-comment";
        moderationService.accept(loginUserHolder, INITIATIVE_ID, comment, Locales.LOCALE_FI);

        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);
    }

    @Test
    public void rejecting_initiative_sets_state_as_draft() {

        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        moderationService.reject(loginUserHolder, INITIATIVE_ID, null);
        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.DRAFT);
    }

    @Test
    public void rejecting_initiative_sends_email() {

        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        moderationService.reject(loginUserHolder, INITIATIVE_ID, null);
        verify(moderationService.emailService).sendStatusEmail(any(Initiative.class), anyListOf(String.class), anyString(), eq(EmailMessageType.REJECTED_BY_OM));
    }

    @Test
    public void rejecting_initiative_saves_comment() {

        setOmUser();
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmailTypeUndefined());

        String comment = "this is om-comment";
        moderationService.reject(loginUserHolder, INITIATIVE_ID, comment);

        verify(initiativeDaoMock).updateModeratorComment(INITIATIVE_ID, comment);

    }

    private static Initiative initiativeWithAuthorEmailTypeUndefined() {
        Initiative initiative = new Initiative();

        initiative.setState(InitiativeState.REVIEW);
        initiative.setType(InitiativeType.UNDEFINED);

        initiative.setMunicipality(new Municipality(0, "", "", false));

        return initiative;
    }

}
