package fi.om.municipalityinitiative.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.*;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class InitiativeModerationWebTest extends WebTestBase {

    /**
     * Form values as constants.
     */
    private static final String COMMENT = "Moderoijan saate";
    
    private Long testMunicipality1Id;

    @Before
    public void setup() {
        testMunicipality1Id = testHelper.createTestMunicipality("Tuusula");
    }

    @Test
    public void moderation_redirects_to_loginpage_if_not_logged_in() {
        open(urls.moderation(testHelper.createCollectableAccepted(testMunicipality1Id)));
        assertThat(driver.getCurrentUrl(), startsWith(urls.login()));
    }

    @Test
    public void moderation_page_fails_if_logged_in_as_author() {
        Long initiativeId = testHelper.createCollectableAccepted(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedInitiative();

        open(urls.moderation(initiativeId));
        assert404();
    }

    @Test
    public void accept_initiative(){
        overrideDriverToFirefox(true);
        
        Long initiativeId = testHelper.createCollectableReview(testMunicipality1Id);

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        getElemContaining(getMessage(MSG_BTN_ACCEPT_INITIATIVE), "a").click();

        inputTextByCSS("#commentAccept",COMMENT);

        clickByName(Urls.ACTION_ACCEPT_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_ACCEPT_INITIATIVE);

        assertTextContainedByClass("extra-info", "Aloite on hyväksytty");

    }

    @Test
    public void reject_initiative(){
        overrideDriverToFirefox(true);
        
        Long initiativeId = testHelper.createCollectableReview(testMunicipality1Id);

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        getElemContaining(getMessage(MSG_BTN_REJECT_INITIATIVE), "a").click();

        inputTextByCSS("#commentReject",COMMENT);

        clickByName(Urls.ACTION_REJECT_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_REJECT_INITIATIVE);
        assertTextContainedByClass("extra-info", "Aloite odottaa julkaisuun lähetystä");
    }
}
