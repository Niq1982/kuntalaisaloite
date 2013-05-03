package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public class ServiceIntegrationTestBase {

    protected final static LoginUserHolder authorLoginUserHolder = mock(LoginUserHolder.class);

    protected final static LoginUserHolder unknownLoginUserHolder = mock(LoginUserHolder.class);

    static {
        doThrow(new AccessDeniedException("Access denied")).when(unknownLoginUserHolder).assertManagementRightsForInitiative(anyLong());
    }
}
