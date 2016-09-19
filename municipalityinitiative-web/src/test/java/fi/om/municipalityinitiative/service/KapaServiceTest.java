package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

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
    public void returns_authors_initiatives_by_ssn() {

        String firstSsn = "010101-0000";
        String firstName = "Some name";
        String secondSsn = "020202-0000";
        String secondName = "Another name";

        testHelper.createVerifiedInitiative(
                new TestHelper.InitiativeDraft(testMunicipality)
                        .withState(InitiativeState.DRAFT)
                        .withName(firstName)
                        .applyAuthor(firstSsn).toInitiativeDraft());

        testHelper.createVerifiedInitiative(
                new TestHelper.InitiativeDraft(testMunicipality)
                        .withState(InitiativeState.DRAFT)
                        .withName(secondName)
                        .applyAuthor(secondSsn).toInitiativeDraft());

        List<InitiativeListInfo> firstUser = kapaService.findInitiativesForUser(firstSsn);

        assertThat(firstUser, hasSize(1));
        assertThat(firstUser.get(0).getMunicipality().getId(), is(testMunicipality));
        assertThat(firstUser.get(0).getName(), is(firstName));


        List<InitiativeListInfo> secondUser = kapaService.findInitiativesForUser(secondSsn);
        assertThat(secondUser, hasSize(1));
        assertThat(secondUser.get(0).getMunicipality().getId(), is(testMunicipality));
        assertThat(secondUser.get(0).getName(), is(secondName));

    }

    @Test
    public void returns_empty_list_if_no_initiatives_for_user() {

        assertThat(kapaService.findInitiativesForUser("999999-999"), hasSize(0));
    }
}