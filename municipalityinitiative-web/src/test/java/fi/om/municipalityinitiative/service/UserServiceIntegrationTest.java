package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.conf.PropertyNames;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.InvalidLoginException;
import fi.om.municipalityinitiative.util.FakeSession;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class UserServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Inject
    private Environment environment;

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
        testHelper.createTestAdminUser("admin", "password", "Some Admin Name", getCurrentSalt());

        userService.adminLogin("admin", "password", requestMock);

        assertThat(userService.getRequiredOmLoginUserHolder(requestMock).getUser().isOmUser(), is(true));
    }

    @Test(expected = InvalidLoginException.class)
    public void throws_exception_if_user_not_found() {
        userService.adminLogin("admin", "password", requestMock);
    }

    private String getCurrentSalt() {
        return environment.getProperty(PropertyNames.omUserSalt);
    }

}
