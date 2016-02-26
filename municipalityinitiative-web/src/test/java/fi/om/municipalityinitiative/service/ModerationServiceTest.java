package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.MunicipalityUserDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.ui.ModerationService;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ModerationServiceTest extends ServiceIntegrationTestBase {

    @Resource
    private ModerationService moderationService;

    // XXX: Should not depend of this but fine for now.
    @Resource
    private MunicipalityUserDao municipalityUserDao;


    private Long testMunicipalityId;

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void get_authors_for_initiative_as_om_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));
        assertThat(moderationService.findAuthors(omLoginUserHolder, initiativeId), hasSize(1));
    }

    @Test
    public void get_authors_for_initiative_as_municipality_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        MunicipalityUserHolder municipalityUserHolder = new MunicipalityUserHolder(User.municipalityLoginUser( initiativeId));
        List<? extends Author> authors = new ArrayList<>();
        try {
            authors = moderationService.findAuthors(municipalityUserHolder, initiativeId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertThat(authors, hasSize(0));
        }
    }

    @Test
    public void get_authors_for_initiative_as_anomous_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        LoginUserHolder anomUser = new LoginUserHolder(User.anonym());
        List<? extends Author> authors = new ArrayList<>();
        try {
            authors = moderationService.findAuthors(anomUser, initiativeId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assertThat(authors, hasSize(0));
        }
    }

    @Test
    @Transactional
    public void renew_municipality_management_hash() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId));

        String oldHash = RandomHashGenerator.longHash();

        municipalityUserDao.assignMunicipalityUser(initiativeId, oldHash);

        OmLoginUserHolder omLoginUserHolder = new OmLoginUserHolder(User.omUser("om user"));

        moderationService.renewMunicipalityManagementHash(omLoginUserHolder, initiativeId, new Locale("fi"));

        String newHash = municipalityUserDao.getMunicipalityUserHashAttachedToInitiative(initiativeId);

        assertThat(oldHash, not(newHash));
    }

    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }




}
