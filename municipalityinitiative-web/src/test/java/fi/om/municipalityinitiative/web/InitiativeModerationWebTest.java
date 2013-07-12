package fi.om.municipalityinitiative.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.*;

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
    public void moderationpage_shows_404_if_not_logged_in() {
        open(urls.moderation(testHelper.createCollaborativeAccepted(testMunicipality1Id)));
//        assertThat(driver.getCurrentUrl(), startsWith(urls.login()));
        assert404();
    }

    @Test
    public void moderation_page_fails_if_logged_in_as_author() {
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality1Id);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.moderation(initiativeId));
        assert404();
    }

    @Test
    public void accept_initiative(){
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality1Id);

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
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality1Id);

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        getElemContaining(getMessage(MSG_BTN_REJECT_INITIATIVE), "a").click();

        inputTextByCSS("#commentReject",COMMENT);

        clickByName(Urls.ACTION_REJECT_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_REJECT_INITIATIVE);
        assertTextContainedByClass("extra-info", "Aloite odottaa julkaisuun lähetystä");
    }
    
    @Test
    public void return_published_initiative_to_fix_and_send_it_to_review_and_accept_it(){
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality1Id);

        loginAsOmUser();
        open(urls.moderation(initiativeId));
        
        clickLinkContaining("Palauta aloite");
        inputTextByCSS("#commentReject",COMMENT);
        getElemContaining("Palauta aloite", "button").click();
        
        assertTextContainedByClass("msg-success","Aloite palautettu korjattavaksi");
        
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();
        
        clickLinkContaining("Lähetä aloite tarkastettavaksi");
        getElemContaining("Lähetä aloite tarkastettavaksi", "button").click();
        
        assertTextContainedByClass("msg-success","Aloite lähetetty tarkastettavaksi");
        
        logout();
        loginAsOmUser();
        open(urls.moderation(initiativeId));
        
        clickLinkContaining("Hyväksy aloite");
        inputTextByCSS("#commentAccept",COMMENT);
        getElemContaining("Hyväksy aloite", "button").click();
        
        assertTextContainedByClass("msg-success","Aloite on hyväksytty");
    }
    
    
    @Test
    public void resend_management_hash(){
        Long initiativeId = testHelper.createCollaborativeReview(testMunicipality1Id);

        loginAsOmUser();

        open(urls.moderation(initiativeId));
        findElementWhenClickable(By.className("resend")).click();

        getElemContaining("Luo ja lähetä uusi ylläpitolinkki", "button").click();

        assertTextContainedByClass("msg-success", "Uusi ylläpitolinkki lähetetty");

    }
}
