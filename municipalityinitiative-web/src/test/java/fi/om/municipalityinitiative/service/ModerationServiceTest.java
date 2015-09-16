package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.user.MunicipalityUserHolder;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.ui.ModerationService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ModerationServiceTest extends ServiceIntegrationTestBase {

    @Resource
    private ModerationService moderationService;
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
        MunicipalityUserHolder municipalityUserHolder = new MunicipalityUserHolder(User.municipalityLoginUser("municipality user"));
        assertThat(moderationService.findAuthors(municipalityUserHolder, initiativeId), hasSize(1));
    }

    @Test
    public void get_authors_for_initiative_as_normal_user(){
        Long initiativeId = createVerifiedInitiativeWithAuthor();
        MunicipalityUserHolder municipalityUserHolder = new MunicipalityUserHolder(User.municipalityLoginUser("municipality user"));
        assertThat(moderationService.findAuthors(municipalityUserHolder, initiativeId), hasSize(1));
    }

    private Long createVerifiedInitiativeWithAuthor() {
        return testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().toInitiativeDraft());
    }

}
