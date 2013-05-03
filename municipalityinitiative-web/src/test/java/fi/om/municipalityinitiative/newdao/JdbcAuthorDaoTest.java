package fi.om.municipalityinitiative.newdao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcAuthorDaoTest {

    @Resource
    AuthorDao authorDao;

    @Resource
    TestHelper testHelper;

    @Before
    public void setUp() throws Exception {
        testHelper.dbCleanup();
    }

    @Test
    public void create_and_get_and_delete() {
        AuthorInvitation original = ReflectionTestUtils.modifyAllFields(new AuthorInvitation());
        original.setInitiativeId(testHelper.createCollectableAccepted(testHelper.createTestMunicipality("what")));

        // Create
        authorDao.addAuthorInvitation(original);

        // Get
        AuthorInvitation gotFromDao = authorDao.getAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());
        ReflectionTestUtils.assertReflectionEquals(original, gotFromDao);

        // Delete
        authorDao.deleteAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());

        try {
            authorDao.getAuthorInvitation(original.getInitiativeId(), original.getConfirmationCode());
            fail("Should have failed due not found");
        } catch (NotFoundException e) { }
    }
}
