package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.Maybe;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestFakeEmailConfiguration.class})
public abstract class ServiceIntegrationTestBase {

    protected final static LoginUserHolder authorLoginUserHolder = mock(LoginUserHolder.class);

    protected final static LoginUserHolder unknownLoginUserHolder = mock(LoginUserHolder.class);

    static {
        doThrow(new AccessDeniedException("Access denied")).when(unknownLoginUserHolder).assertManagementRightsForInitiative(anyLong());
        Initiative initiative = new Initiative();
        initiative.setAuthor(new Author());
        initiative.setManagementHash(Maybe.of(TestHelper.TEST_MANAGEMENT_HASH));
        stub(authorLoginUserHolder.getInitiative()).toReturn(Maybe.of(initiative));
    }
}
