package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ViewInitiativeWebTest extends WebTestBase {

    /**
     * Localization keys as constants.
     */
    private static final String MANAGEMENT_WARNING_TITLE = "management.warning.title";
    private static final String INITIATIVE_VIEW_HEADER = "initiative.proposal.title";
    private Long municipalityId;
    private Long draftInitiativeId;

    @Before
    public void setup() {
        municipalityId = testHelper.createTestMunicipality("Tuusula");

        draftInitiativeId = testHelper.createSingleDraft(municipalityId);

        draftInitiativeId = testHelper.createSingleDraft(municipalityId);
    }

    @Override
    protected boolean overrideDriverToHtmlUnit() {
        return true;
    }

    @Test
    public void management_view_opens_if_logged_in_with_correct_management_hash() {
        loginAsAuthor(draftInitiativeId);
        open(urls.management(draftInitiativeId));
        assertThat(driver.findElement(By.tagName("h2")).getText(), is(getMessage(MANAGEMENT_WARNING_TITLE)));
    }

    @Test
    public void management_view_shows_404_if_not_logged_in() {
        open(urls.getManagement(draftInitiativeId));
        assert404();
    }

    @Test
    public void management_view_shows_404_if_logged_in_with_wrong_management_hash() {

        Long otherInitiative = testHelper.createSingleDraft(municipalityId);
        loginAsAuthor(otherInitiative);

        open(urls.getManagement(draftInitiativeId));
        assert404();
    }

    @Test
    public void accepted_but_not_published_initiative_cannot_be_viewed_if_not_logged_in() {
        open(urls.view(testHelper.createCollectableAccepted(municipalityId)));
        assert404();
    }

    @Test
    public void not_published_initiative_cannot_be_viewed_if_wrong_author() {
        Long otherInitiative = testHelper.createSingleDraft(municipalityId);
        loginAsAuthor(otherInitiative);

        open(urls.view(draftInitiativeId));
        assert404();
    }

    @Test
    public void not_published_initiative_can_be_viewed_if_logged_in_as_author() {
        loginAsAuthor(draftInitiativeId);
        open(urls.view(draftInitiativeId));
        assertThat(driver.findElement(By.tagName("h2")).getText(), is(getMessage(INITIATIVE_VIEW_HEADER)));
    }

    @Test
    public void even_drafts_may_be_viewed_if_logged_in_as_om_user() {
        loginAsOmUser();
        open(urls.view(draftInitiativeId));
        assertThat(driver.findElement(By.tagName("h2")).getText(), is(getMessage(INITIATIVE_VIEW_HEADER)));
    }

    @Test
    public void management_view_redirects_to_create_page_if_initiative_name_is_empty() {
        Long emptyDraftId = testHelper.createEmptyDraft(municipalityId);
        loginAsAuthor(emptyDraftId);
        open(urls.management(emptyDraftId));

        assertThat(driver.getCurrentUrl(), is(urls.edit(emptyDraftId)));
    }

    // TODO: Redirect-tests if initiative at REVIEW, ACCEPTED, sent etc and trying to open edit/management-page


}
