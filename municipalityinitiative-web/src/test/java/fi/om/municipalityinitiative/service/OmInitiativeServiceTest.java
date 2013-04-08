package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Locales;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class OmInitiativeServiceTest {

    private static final String AUTHOR_EMAIL = "some@example.com";
    public static final long INITIATIVE_ID = 3L;

    private OmInitiativeService omInitiativeService;

    private InitiativeDao initiativeDaoMock;

    private IntegrationTestConfiguration.FakeUserService fakeUserService;

    @Before
    public void setup() throws Exception {
        initiativeDaoMock = mock(InitiativeDao.class);
        fakeUserService = new IntegrationTestConfiguration.FakeUserService();
        omInitiativeService = new OmInitiativeService();

        omInitiativeService.initiativeDao = initiativeDaoMock;
        omInitiativeService.userService = fakeUserService;
        omInitiativeService.emailService = mock(EmailService.class);
    }

    @Test
    public void all_functions_require_om_rights() throws InvocationTargetException, IllegalAccessException {
        fakeUserService.setOmUser(false);

        for (Method method : OmInitiativeService.class.getDeclaredMethods()) {
            Object[] parameters = new Object[method.getParameterTypes().length];
            try {
                method.invoke(omInitiativeService, parameters);
                fail("Should have checked om-rights for user");
            } catch (InvocationTargetException e) {
                assertThat(e.getCause(), instanceOf(AccessDeniedException.class));
            }
        }
    }

    @Test
    public void accepting_initiative_sets_state_as_accepted() {

        fakeUserService.setOmUser(true);
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmail());

        omInitiativeService.accept(INITIATIVE_ID, Locales.LOCALE_FI);

        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.ACCEPTED);
    }

    private static Initiative initiativeWithAuthorEmail() {
        Initiative initiative = new Initiative();
        ContactInfo contactInfo = new ContactInfo();
        Author author = new Author();

        author.setContactInfo(contactInfo);
        initiative.setAuthor(author);

        contactInfo.setEmail(AUTHOR_EMAIL);
        return initiative;
    }

    @Test
    public void rejecting_initiative_sets_state_as_draft() {

        fakeUserService.setOmUser(true);
        stub(initiativeDaoMock.getByIdWithOriginalAuthor(INITIATIVE_ID)).toReturn(initiativeWithAuthorEmail());

        omInitiativeService.reject(INITIATIVE_ID, Locales.LOCALE_FI);
        verify(initiativeDaoMock).updateInitiativeState(INITIATIVE_ID, InitiativeState.DRAFT);

    }

}
