package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.conf.PropertyNames;
import fi.om.municipalityinitiative.dao.TestHelper;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class UserServiceIntegrationTest extends ServiceIntegrationTestBase{

    @Inject
    private Environment environment;

    @Resource
    private TestHelper testHelper;

    @Resource
    private UserService userService;

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void admin_user_login_is_ok() {
        testHelper.createTestAdminUser("admin", "password", "Some Admin Name", getCurrentSalt());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = new FakeSession();
        stub(request.getSession()).toReturn(session);

        userService.adminLogin("admin", "password", request);

        assertThat(userService.getRequiredOmLoginUserHolder(request).getUser().isOmUser(), is(true));
    }

    private String getCurrentSalt() {
        return environment.getProperty(PropertyNames.omUserSalt);
    }

}
