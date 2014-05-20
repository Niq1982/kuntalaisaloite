package fi.om.municipalityinitiative.web;

import org.junit.Test;
import org.openqa.selenium.By;

import static fi.om.municipalityinitiative.web.MessageSourceKeys.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

public class InitiativeModerationWebTest extends WebTestBase {

    /**
     * Form values as constants.
     */
    private static final String COMMENT = "Moderoijan saate";

    @Override
    public void childSetup() {
    }

    @Test
    public void moderationpage_shows_404_if_not_logged_in() {
        open(urls.moderation(testHelper.createCollaborativeAccepted(HELSINKI_ID)));
//        assertThat(driver.getCurrentUrl(), startsWith(urls.vetumaLogin()));
        assert404();
    }

    @Test
    public void moderation_page_fails_if_logged_in_as_author() {
        Long initiativeId = testHelper.createCollaborativeAccepted(HELSINKI_ID);
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.moderation(initiativeId));
        assert404();
    }

    @Test
    public void accept_initiative(){
        Long initiativeId = testHelper.createCollaborativeReview(HELSINKI_ID);

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        getElemContaining(getMessage(MSG_BTN_ACCEPT_INITIATIVE), "a").click();

        inputTextByCSS("#commentAccept",COMMENT);

        clickByName(Urls.ACTION_ACCEPT_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_ACCEPT_INITIATIVE);

        assertTextContainedByClass("extra-info", "Aloite on hyväksytty");

        assertReviewHistoryElement("Hyväksytty julkaistavaksi", COMMENT);
        assertTotalEmailsInQueue(1);

    }

    @Test
    public void add_moderator_comment() {

        Long initiativeId = testHelper.createCollaborativeReview(HELSINKI_ID);

        loginAsOmUser();
        open(urls.moderation(initiativeId));
        getElement(By.name(Urls.ACTION_MODERATOR_ADD_COMMENT)).sendKeys(COMMENT);
        getElemContaining("Lisää merkintä", "button").click();

        assertReviewHistoryElement("Oikeusministeriön merkintä", COMMENT);
    }

    private void assertReviewHistoryElement(String historyItemHeader, String historyItemMessage) {
        assertTextContainedByClass("review-history-description", historyItemHeader);
        assertTextContainedByClass("review-history-message", historyItemMessage);
    }

    @Test
    public void reject_initiative(){
        Long initiativeId = testHelper.createCollaborativeReview(HELSINKI_ID);

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        getElemContaining(getMessage(MSG_BTN_REJECT_INITIATIVE), "a").click();

        inputTextByCSS("#commentReject",COMMENT);

        clickByName(Urls.ACTION_REJECT_INITIATIVE);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_REJECT_INITIATIVE);
        assertTextContainedByClass("extra-info", "Aloite odottaa julkaisuun lähetystä");

        assertReviewHistoryElement("Palautettu korjattavaksi", COMMENT);

        assertTotalEmailsInQueue(1);

    }

    @Test
    public void return_published_initiative_to_fix_and_send_it_to_review_and_accept_it(){
        Long initiativeId = testHelper.createCollaborativeAccepted(HELSINKI_ID);

        loginAsOmUser();
        open(urls.moderation(initiativeId));

        clickLinkContaining("Palauta aloite");
        inputTextByCSS("#commentReject",COMMENT);
        getElemContaining("Palauta aloite", "button").click();

        assertTextContainedByClass("msg-success","Aloite palautettu korjattavaksi");
        assertTotalEmailsInQueue(1);

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        clickLinkContaining("Lähetä aloite tarkastettavaksi");
        getElemContaining("Lähetä aloite tarkastettavaksi", "button").click();

        assertTextContainedByClass("msg-success","Aloite lähetetty tarkastettavaksi");
        assertTotalEmailsInQueue(3);

        logout();
        loginAsOmUser();
        open(urls.moderation(initiativeId));

        clickLinkContaining("Hyväksy aloite");
        inputTextByCSS("#commentAccept",COMMENT);
        getElemContaining("Hyväksy aloite", "button").click();

        assertTextContainedByClass("msg-success","Aloite on hyväksytty");
        assertTotalEmailsInQueue(4);
    }


    @Test
    public void resend_management_hash(){
        Long initiativeId = testHelper.createCollaborativeReview(HELSINKI_ID);

        loginAsOmUser();

        open(urls.moderation(initiativeId));
        findElementWhenClickable(By.className("resend")).click();

        getElemContaining("Luo ja lähetä uusi ylläpitolinkki", "button").click();

        assertTextContainedByClass("msg-success", "Uusi ylläpitolinkki lähetetty");
        assertTotalEmailsInQueue(1);

    }

    @Test
    public void moderadion_page_lists_attachments() {
        String imageDescription = "jpg-attachment name";
        String imageFileName = "jpg_attachment_name"; // Parsed from imageDescription
        long imageAttachmentId = -1L;

        String pdfDescription = "pdf-attachment name";
        String pdfFileName = "pdf_attachment_name";
        long pdfAttachmentId = -2L;

        Long initiativeId = testHelper.createCollaborativeReview(HELSINKI_ID);
        testHelper.addAttachment(initiativeId, imageDescription, false, "jpg", imageAttachmentId); // src/test/resources/-1.jpg
        testHelper.addAttachment(initiativeId, pdfDescription, false, "pdf", pdfAttachmentId); // does not matter because will not be loaded, it's just a link resource.

        loginAsOmUser();

        open(urls.moderation(initiativeId));

        // Assert image url and thumbnail
        assertThat(driver.getPageSource(), containsString(urls.getAttachmentThumbnail(imageAttachmentId)));
        assertThat(driver.getPageSource(), containsString(urls.attachment(imageAttachmentId, imageFileName)));


        // Assert no thumbnail for pdf, but instead pdf icon and link to pdf.
        assertThat(driver.getPageSource(), not(containsString(urls.getAttachmentThumbnail(pdfAttachmentId))));
        assertThat(driver.getPageSource(), containsString("/img/pdficon_large.png"));
        assertThat(driver.getPageSource(), containsString(urls.attachment(pdfAttachmentId, pdfFileName)));
    }



}
