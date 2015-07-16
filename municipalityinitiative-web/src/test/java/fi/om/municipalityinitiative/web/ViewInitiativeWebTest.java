package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ViewInitiativeWebTest extends WebTestBase {

    /**
     * Localization keys as constants.
     */
    private static final String MANAGEMENT_WARNING_TITLE = "management.title";
    private static final String INITIATIVE_VIEW_HEADER = "initiative.proposal.title";
    private Long draftInitiativeId;

    @Override
    public void childSetup() {

        draftInitiativeId = testHelper.createDraft(HELSINKI_ID);
    }

    @Test
    public void management_view_opens_if_logged_in_with_correct_management_hash() {
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(draftInitiativeId));
        assertThat(getElement(By.tagName("h2")).getText(), is(getMessage(MANAGEMENT_WARNING_TITLE)));
    }

    @Test
    public void management_view_shows_404_if_not_logged_in() {
        open(urls.getManagement(draftInitiativeId));
        assert404();
    }

    @Test
    public void management_view_shows_404_if_logged_in_with_wrong_management_hash() {

        Long otherInitiative = testHelper.createDraft(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.getManagement(draftInitiativeId));
        assert404();
    }

    @Test
    public void accepted_but_not_published_initiative_cannot_be_viewed_if_not_logged_in() {
        open(urls.view(testHelper.createCollaborativeAccepted(HELSINKI_ID)));
        assert404();
    }

    @Test
    public void not_published_initiative_cannot_be_viewed_if_wrong_author() {
        Long otherInitiative = testHelper.createDraft(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.view(draftInitiativeId));
        assert404();
    }

    @Test
    public void initiative_with_fixState_other_than_ok_can_not_be_viewed_if_not_logged_in_as_author() {
        Long initiative = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.REVIEW));

        open(urls.view(initiative));
        assert404();
    }

    @Test
    public void not_published_initiative_can_be_viewed_if_logged_in_as_author() {
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.view(draftInitiativeId));
        assertThat(getElement(By.tagName("h2")).getText(), is(getMessage(INITIATIVE_VIEW_HEADER)));
    }

    @Test
    public void even_drafts_may_be_viewed_if_logged_in_as_om_user() {
        loginAsOmUser();
        open(urls.view(draftInitiativeId));
        assertThat(getElement(By.tagName("h2")).getText(), is(getMessage(INITIATIVE_VIEW_HEADER)));
    }

    @Test
    public void management_view_redirects_to_create_page_if_initiative_name_is_empty() {
        Long emptyDraftId = testHelper.createEmptyDraft(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        open(urls.management(emptyDraftId));

        assertThat(driver.getCurrentUrl(), is(urls.edit(emptyDraftId)));
    }

    @Test
    public void view_participants_page_shows_public_accepted_participants() {
        Long defaultInitiative = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        String publicName = "Public Name";
        String privateName = "Private Name";
        String notAcceptedName = "NotAcceptedName";

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(defaultInitiative, HELSINKI_ID)
                .withParticipantName(publicName)
                .withPublicName(true));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(defaultInitiative, HELSINKI_ID)
                .withParticipantName(privateName)
                .withPublicName(false));

        testHelper.createUnconfirmedParticipant(new TestHelper.AuthorDraft(defaultInitiative, HELSINKI_ID)
                .withPublicName(true)
                .withParticipantName(notAcceptedName), "someConfirmationCode");

        open(urls.participantList(defaultInitiative));

        assertTextContainedByClass("name-container", publicName);

        assertTextNotContainedByClass("name-container", privateName);
        assertTextNotContainedByClass("name-container", notAcceptedName);

    }

    @Test
    public void iframe_page_accepts_municipality_parameter_and_shows_initiative_oldest_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframeWithOldestApiMunicipality(HELSINKI_ID));
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }
    @Test
    public void iframe_page_with_no_parameter_oldest_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframeWithOldestApiMunicipality());
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_accepts_municipality_parameter_and_only_shows_one_initiative_oldest_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        String titleHyvinkaa = "Hyvinkää";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HYVINKAA_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(titleHyvinkaa));

        open(urls.iframeWithOldestApiMunicipality(HELSINKI_ID));
        assertThat(getElement(By.tagName("ul")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("ul")).getText(), containsString(title));

        assertThat(getElement(By.tagName("ul")).getText(), not(containsString(titleHyvinkaa)));
    }

    @Test
    public void iframe_page_accepts_limit_parameter_oldest_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        String title2 = "Newst initiative";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title2));

        open(urls.iframeWithOldestApiMunicipality(HELSINKI_ID) + "&limit=1");
        assertThat(getElement(By.tagName("ul")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("ul")).getText(), containsString(title2));

        assertThat(getElement(By.tagName("ul")).getText(), not(containsString(title)));
    }

    @Test
    public void iframe_page_shows_empty_list_if_no_initiatives_oldest_municipality_paramater_iframe() {
        open(urls.iframeWithOldestApiMunicipality(-1L));
        assertThat(getElement(By.className("search-results")).getText(), is("Ei vielä yhtään aloitetta"));
    }

    @Test
    public void iframe_page_accepts_municipality_parameter_and_shows_initiative_old_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframeWithOldApiMunicipality(HELSINKI_ID));
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_shows_empty_list_if_no_initiatives_old_municipality_paramater_iframe() {
        open(urls.iframeWithOldApiMunicipality(-1L));
        assertThat(getElement(By.className("search-results")).getText(), is("Ei vielä yhtään aloitetta"));
    }


    @Test
    public void iframe_page_accepts_limit_parameter_old_municipality_paramater_iframe() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        String title2 = "Newst initiative";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title2));

        open(urls.iframeWithOldApiMunicipality(HELSINKI_ID) + "&limit=1");
        assertThat(getElement(By.tagName("ul")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("ul")).getText(), containsString(title2));

        assertThat(getElement(By.tagName("ul")).getText(), not(containsString(title)));
    }

    @Test
    public void iframe_shows_initiative() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframe());

        assertThat(driver.getTitle(), is("Sisältöä näyttävä widget eli leijuke - Kuntalaisaloitepalvelu"));
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_accepts_municipality_parameter_and_shows_initiative() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframe(HELSINKI_ID));
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }
    @Test
    public void iframe_page_accepts_several_municipality_parameters_and_shows_initiative() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.iframe(HELSINKI_ID, HYVINKAA_ID));
        assertThat(getElement(By.tagName("li")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_accepts_limit_parameter() {

        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        String title2 = "Newst initiative";
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title2));

        open(urls.iframe(HELSINKI_ID) + "&limit=1");
        assertThat(getElement(By.tagName("ul")).getText(), containsString(stateTime.toString("d.M.yyyy")));
        assertThat(getElement(By.tagName("ul")).getText(), containsString(title2));

        assertThat(getElement(By.tagName("ul")).getText(), not(containsString(title)));
    }

    @Test
    public void iframe_page_shows_empty_list_if_no_initiatives() {
        open(urls.iframe(-1L));
        assertThat(getElement(By.className("search-results")).getText(), is("Ei vielä yhtään aloitetta"));
    }
    @Test
    public void iframe_page_shows_empty_list_if_no_initiatives_with_several_municipalities() {
        open(urls.iframe(-1L, 2L));
        assertThat(getElement(By.className("search-results")).getText(), is("Ei vielä yhtään aloitetta"));
    }

    @Test
    public void show_graph_iframe_for_initiative() {
        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        String publicName = "Public Name";
        String privateName = "Private Name";


        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, HELSINKI_ID)
                .withParticipantName(publicName)
                .withPublicName(true));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, HELSINKI_ID)
                .withParticipantName(privateName)
                .withPublicName(false));

        open(urls.graphIFrame(initiativeId));

        assertThat(getElement(By.className("public-names")).getText(), containsString("1 nimi julkaistu palvelussa"));

        assertThat(getElement(By.className("private-names")).getText(), containsString("1 nimi ei julkaistu palvelussa"));
    }
    @Test
    public void show_graph_iframe_for_initiative_with_title_and_date() {
        DateTime stateTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.PUBLISHED)
                .withStateTime(stateTime)
                .withName(title));

        open(urls.graphIFrame(initiativeId) + "?showTitle=true");
        assertThat(getElement(By.tagName("p")).getText(), containsString(title));
        assertThat(getElement(By.className("extra-info")).getText(), containsString(stateTime.toString("d.M.yyyy")));

    }




    // TODO: Redirect-tests if initiative at REVIEW, ACCEPTED, sent etc and trying to open edit/management-page


}
