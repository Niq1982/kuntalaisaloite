package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ViewInitiativeWebTest extends WebTestBase {

    /**
     * Localization keys as constants.
     */
    private static final String MANAGEMENT_WARNING_TITLE = "management.title";
    private static final String INITIATIVE_VIEW_HEADER = "initiative.proposal.title";
    public static final String MUNICIPALITY_NAME = "Tuusula";
    private Long municipalityId;
    private Long draftInitiativeId;

    @Before
    public void setup() {
        municipalityId = testHelper.createTestMunicipality(MUNICIPALITY_NAME);

        draftInitiativeId = testHelper.createDraft(municipalityId);

        draftInitiativeId = testHelper.createDraft(municipalityId);
    }

    @Test
    public void management_view_opens_if_logged_in_with_correct_management_hash() {
        loginAsAuthorForLastTestHelperCreatedInitiative();
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

        Long otherInitiative = testHelper.createDraft(municipalityId);
        loginAsAuthorForLastTestHelperCreatedInitiative();

        open(urls.getManagement(draftInitiativeId));
        assert404();
    }

    @Test
    public void accepted_but_not_published_initiative_cannot_be_viewed_if_not_logged_in() {
        open(urls.view(testHelper.createCollaborativeAccepted(municipalityId)));
        assert404();
    }

    @Test
    public void not_published_initiative_cannot_be_viewed_if_wrong_author() {
        Long otherInitiative = testHelper.createDraft(municipalityId);
        loginAsAuthorForLastTestHelperCreatedInitiative();

        open(urls.view(draftInitiativeId));
        assert404();
    }

    @Test
    public void initiative_with_fixState_other_than_ok_can_not_be_viewed_if_not_logged_in_as_author() {
        Long initiative = testHelper.createInitiative(new TestHelper.InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.REVIEW));

        open(urls.view(initiative));
        assert404();
    }

    @Test
    public void not_published_initiative_can_be_viewed_if_logged_in_as_author() {
        loginAsAuthorForLastTestHelperCreatedInitiative();
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
        Long emptyDraftId = testHelper.createEmptyDraft(municipalityId);
        loginAsAuthorForLastTestHelperCreatedInitiative();
        open(urls.management(emptyDraftId));

        assertThat(driver.getCurrentUrl(), is(urls.edit(emptyDraftId)));
    }

    @Test
    public void iframe_shows_initiative() {

        DateTime modifyTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createInitiative(new TestHelper.InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withModified(modifyTime)
                .withName(title));

        open(urls.iframe());

        assertThat(driver.getTitle(), is("Leijuke - Kuntalaisaloitepalvelu"));
        assertThat(getElement(By.tagName("li")).getText(), containsString(modifyTime.toString("dd.MM.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_accepts_municipality_parameter_and_shows_initiative() {

        DateTime modifyTime = new DateTime(2011, 1, 1, 0, 0);
        String title = "Yeah rock rock";
        testHelper.createInitiative(new TestHelper.InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withModified(modifyTime)
                .withName(title));

        open(urls.iframe(municipalityId));
        assertThat(getElement(By.tagName("li")).getText(), containsString(modifyTime.toString("dd.MM.yyyy")));
        assertThat(getElement(By.tagName("li")).getText(), containsString(title));
    }

    @Test
    public void iframe_page_shows_empty_list_if_no_initiatives() {
        open(urls.iframe(-1L));
        assertThat(getElement(By.className("search-results")).getText(), is("Ei vielä yhtään aloitetta"));
    }



    // TODO: Redirect-tests if initiative at REVIEW, ACCEPTED, sent etc and trying to open edit/management-page


}
