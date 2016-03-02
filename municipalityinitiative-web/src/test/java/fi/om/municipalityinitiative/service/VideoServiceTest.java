package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore
public class VideoServiceTest extends ServiceIntegrationTestBase{

    public static final String VIDEONAME = "randomname";

    public static final String VIDEO_ID = "lq-54gox6-k";
    public static final String EMBEDDED_YOUTUBE_VIDEO_URL_BASE = "https://www.youtube.com/embed/";
    public static final String SHORT_YOUTUBE_VIDEO_URL_BASE = "https://youtu.be/";
    public static final String NORMAL_YOUTUBE_VIDEO_URL_BASE = "https://www.youtube.com/watch?v=";

    public static final String VALID_EMBED_VIDEO_URL = EMBEDDED_YOUTUBE_VIDEO_URL_BASE + "lq-54gox6-k";
    public static final String INVALID_YOUTUBE_URL = "https://www.youtube.com/aBQzKKM6480?t=97";
    public static final String UNALLOWED_QUERY_PARAMETERS = "https://youtu.be/aBQzKKM6480?t=97";
    public static final String VALID_VIDEO_URL = "https://www.youtube.com/watch?v=lq-54gox6-k";
    public static final String VALID_YOUTUBE_SHORT_URL = "https://youtu.be/lq-54gox6-k";


    public static final String P_TAG = "<p></p>";
    public static final String P_ESCAPED = "&lt;p&gt;&lt;/p&gt;";

    public static final String VIMEO_URL_BASE = "https://vimeo.com/";
    public static final String VALID_VIMEO_URL = VIMEO_URL_BASE + "93350761";
    public static final String VIMEO_EMBEDDED_BASE = "https://player.vimeo.com/video/";
    public static final String EMBEDDED_VIMEO_URL = VIMEO_EMBEDDED_BASE + "93350761";

    @Resource
    private VideoService videoService;

    @Resource
    private InitiativeDao initiativeDao;

    private Long testMunicipalityId;

    @Override
    protected void childSetup() {
        testMunicipalityId = testHelper.createTestMunicipality("Some municipality");
    }

    @Test
    public void get_youtubevideoid_from_watch_url() throws InvalidVideoUrlException {
        assertThat(videoService.getYouTubeVideoId(VALID_VIDEO_URL), is(VIDEO_ID));
    }

    @Test
    public void get_youtubevideoid_from_embed_url() throws InvalidVideoUrlException {
        assertThat(videoService.getYouTubeVideoId(VALID_EMBED_VIDEO_URL), is(VIDEO_ID));
    }

    @Test
    public void validate_youtube_video_url_success() {
        assertThat(VALID_VIDEO_URL.matches(VideoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void validate_embedded_youtube_video_url_success() {
        assertThat(VALID_EMBED_VIDEO_URL.matches(VideoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void validate_vimeo_url_success(){
        assertThat(VALID_VIMEO_URL.matches(VideoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void get_parameters_invalid(){
        assertThat(INVALID_YOUTUBE_URL.matches(VideoService.VIDEO_REGEX), is(false));
    }

    @Test
    public void wrong_host_invalid() {
        assertThat(UNALLOWED_QUERY_PARAMETERS.matches(VideoService.VIDEO_REGEX), is(false));
    }


}
