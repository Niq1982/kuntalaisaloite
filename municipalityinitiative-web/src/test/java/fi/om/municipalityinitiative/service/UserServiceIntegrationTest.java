package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.dto.LoginUserHolder;
import fi.om.municipalityinitiative.util.FakeSession;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class UserServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Resource
    private TestHelper testHelper;

    @Resource
    private UserService userService;

    private HttpServletRequest requestMock;

    @Before
    public void setup() {
        testHelper.dbCleanup();

        requestMock = mock(HttpServletRequest.class);
        HttpSession session = new FakeSession();
        stub(requestMock.getSession()).toReturn(session);
    }

    @Test
    public void admin_user_login_is_ok() {
        testHelper.createTestAdminUser("admin", "password", "Some Admin Name");

        userService.adminLogin("admin", "password", requestMock);

        assertThat(userService.getRequiredOmLoginUserHolder(requestMock).getUser().isOmUser(), is(true));
    }

    @Test(expected = InvalidLoginException.class)
    public void throws_exception_if_user_not_found() {
        userService.adminLogin("admin", "password", requestMock);
    }

    @Test(expected = InvalidLoginException.class)
    public void author_login_with_invalid_hash_throws_exception() {
        userService.authorLogin("unknown hash", requestMock);
    }

    @Test
    public void author_login_with_management_hash_logs_user_in() {
        Long initiative = testHelper.createCollaborativeAccepted(testHelper.createTestMunicipality("moi"));

        userService.authorLogin(TestHelper.PREVIOUS_TEST_MANAGEMENT_HASH, requestMock);
        LoginUserHolder loginUserHolder = userService.getRequiredLoginUserHolder(requestMock);

        assertThat(loginUserHolder.getAuthorId(), is(testHelper.getLastAuthorId()));
        assertThat(loginUserHolder.getUser().hasRightToInitiative(initiative), is(true));
        assertThat(loginUserHolder.getUser().hasRightToInitiative(-1L), is(false));
    }


}
