package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class KapaServiceTest extends ServiceIntegrationTestBase{

    private Long testMunicipality;

    @Resource
    private KapaService kapaService;

    @Override
    protected void childSetup() {
        testMunicipality = testHelper.createTestMunicipality("Some municipality");
    }


    @Test
    public void findInitiativesForUser_returns_authors_initiatives_by_ssn() {

        // Create 1 initiative, no supports

        String authorSSN = "020202-0000";
        String initiativeName = "Name";

        testHelper.createVerifiedInitiative(
                new TestHelper.InitiativeDraft(testMunicipality)
                        .withState(InitiativeState.DRAFT)
                        .withName(initiativeName)
                        .applyAuthor(authorSSN).toInitiativeDraft());

        KapaService.KapaInitiativeResult secondUser = kapaService.findInitiativesForUser(authorSSN);
        assertThat(secondUser.initiatives, hasSize(1));
        assertThat(secondUser.initiatives.get(0).getMunicipality().getId(), is(testMunicipality));
        assertThat(secondUser.initiatives.get(0).getName(), is(initiativeName));
        assertThat(secondUser.supports, hasSize(0));

    }

    @Test
    public void findInitiativesForUser_returns_authors_initiatives_and_participations() {

        // Create 1 initiative and 1 support for another initiative

        String authorSSN = "010101-0000";
        String authorInitiativeName = "Some name";

        testHelper.createVerifiedInitiative(
                new TestHelper.InitiativeDraft(testMunicipality)
                        .withState(InitiativeState.DRAFT)
                        .withName(authorInitiativeName)
                        .applyAuthor(authorSSN).toInitiativeDraft());
        Long firstUserId = testHelper.getLastVerifiedUserId();

        String participationInitiativeName = "support initiative name";
        Long participatInitiative = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality)
                .withName(participationInitiativeName));

        testHelper.createVerifiedParticipantWithVerifiedUserId(new TestHelper.AuthorDraft(participatInitiative, testMunicipality)
                .withVerifiedUserId(firstUserId));

        KapaService.KapaInitiativeResult firstUser = kapaService.findInitiativesForUser(authorSSN);

        assertThat(firstUser.initiatives, hasSize(1));
        assertThat(firstUser.initiatives.get(0).getMunicipality().getId(), is(testMunicipality));
        assertThat(firstUser.initiatives.get(0).getName(), is(authorInitiativeName));

        assertThat(firstUser.supports, hasSize(1));
        assertThat(firstUser.supports.get(0).getMunicipality().getId(), is(testMunicipality));
        assertThat(firstUser.supports.get(0).getName(), is(participationInitiativeName));

    }

    @Test
    public void findInitiativesForUser_returns_empty_list_if_no_initiatives_for_user() {

        KapaService.KapaInitiativeResult initiativesForUser = kapaService.findInitiativesForUser("999999-999");
        assertThat(initiativesForUser.supports, hasSize(0));
        assertThat(initiativesForUser.initiatives, hasSize(0));
    }
}