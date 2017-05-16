package fi.om.municipalityinitiative.web;


import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public class CreateVideoWebTest extends WebTestBase {

    private Long normalInitiativeId;
    private Long verifiedInitiativeId;

    public static final String VERIFIED_USER_AUTHOR_SSN = "010190-0001";

    public static final String VIDEO_NAME = "videoName";
    public static final String VIDEO_URL = "videoUrl";
    public static final String VIDEO_ATTACH = "video.attach";
    public static final String VALID_VIDEO_URL = "https://www.youtube.com/watch?v=PPmcYewZJuU";
    public static final String VALID_VIDEO_NAME = "Solita wants you";
    public static final String SUCCESS_VIDEO_ADDED = "success.video-added";

    private static final String MSG_ADD_VIDEO = "video.add.btn";

    @Override
    protected void childSetup() {
        normalInitiativeId = testHelper.createWithAuthor(HELSINKI_ID, InitiativeState.DRAFT, InitiativeType.COLLABORATIVE);
        verifiedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(HELSINKI_ID)
                .withState(InitiativeState.DRAFT)
                .applyAuthor(VERIFIED_USER_AUTHOR_SSN)
                .toInitiativeDraft());
    }

    @Test
    public void add_video_to_initiative(){
        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.management(normalInitiativeId));

        clickLink(getMessage(MSG_ADD_VIDEO));

        inputText(VIDEO_URL, VALID_VIDEO_URL);
        inputText(VIDEO_NAME, VALID_VIDEO_NAME);

        getElemContaining(getMessage(VIDEO_ATTACH), "button").click();

        assertSuccessMessage(getMessage(SUCCESS_VIDEO_ADDED));


    }

    @Test
    public void remove_video_from_initiative() {

        loginAsAuthorForLastTestHelperCreatedNormalInitiative();

        open(urls.management(normalInitiativeId));

        clickLink(getMessage(MSG_ADD_VIDEO));

        inputText(VIDEO_URL, VALID_VIDEO_URL);
        inputText(VIDEO_NAME, VALID_VIDEO_NAME);

        getElemContaining(getMessage(VIDEO_ATTACH), "button").click();

        assertSuccessMessage(getMessage(SUCCESS_VIDEO_ADDED));

        clickElementByCSS(".js-delete-video");
        getElemContaining(getMessage("deleteVideo.btn"), "button").click();
        assertSuccessMessage("Video poistettu");

    }

}
