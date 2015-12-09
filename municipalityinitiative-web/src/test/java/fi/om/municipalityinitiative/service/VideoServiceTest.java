package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.ui.VideoCreateDto;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VideoServiceTest extends ServiceIntegrationTestBase{

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";

    public static final String VIDEO_ID = "lq-54gox6-k";
    public static final String VALID_EMBED_VIDEO_URL = "https://www.youtube.com/embed/lq-54gox6-k";
    public static final String INVALID_YOUTUBE_URL = "https://www.youtube.com/aBQzKKM6480?t=97";
    public static final String UNALLOWED_QUERY_PARAMETERS = "https://youtu.be/aBQzKKM6480?t=97";
    public static final String VIDEONAME = "randomname";

    public static final String VALID_VIMEO_URL = "https://vimeo.com/93350761";
    public static final String EMBEDDED_VIMEO_URL ="https://player.vimeo.com/video/93350761";

    public static final String VALID_VIDEO_URL = "https://www.youtube.com/watch?v=lq-54gox6-k";
    public static final String VALID_YOUTUBE_SHORT_URL = "https://youtu.be/lq-54gox6-k";

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
        assertThat(VALID_VIDEO_URL.matches(videoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void validate_embedded_youtube_video_url_success() {
        assertThat(VALID_EMBED_VIDEO_URL.matches(videoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void validate_vimeo_url_success(){
        assertThat(VALID_VIMEO_URL.matches(videoService.VIDEO_REGEX), is(true));
    }

    @Test
    public void get_parameters_invalid(){
        assertThat(INVALID_YOUTUBE_URL.matches(videoService.VIDEO_REGEX), is(false));
    }

    @Test
    public void wrong_host_invalid() {
        assertThat(UNALLOWED_QUERY_PARAMETERS.matches(videoService.VIDEO_REGEX), is(false));
    }

    @Test
    @Transactional
    public void save_public_youtube_video_url(){
        Long initiativeId = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(testMunicipalityId)
                        .withState(InitiativeState.PUBLISHED)
                        .applyAuthor()
                        .toInitiativeDraft());
        try {
            videoService.addVideoUrl(new VideoCreateDto(Maybe.of(VALID_VIDEO_URL), Maybe.of(VIDEONAME)), initiativeId);
        } catch (InvalidVideoUrlException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            Initiative initiative = initiativeDao.get(initiativeId);


            assertThat(initiative.getVideoUrl().isPresent(), is(true));
            assertThat(initiative.getVideoUrlName().isPresent(), is(true));

            VideoCreateDto videoCreateDto = new VideoCreateDto(initiative.getVideoUrl(), initiative.getVideoUrlName());
            assertThat(videoCreateDto.getVideoUrl(), is(VALID_EMBED_VIDEO_URL));
            assertThat(videoCreateDto.getVideoName(), is(VIDEONAME));
        }
    }

    @Test
    @Transactional
    public void save_short_youtube_video_url(){
        Long initiativeId = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(testMunicipalityId)
                        .withState(InitiativeState.PUBLISHED)
                        .applyAuthor()
                        .toInitiativeDraft());
        try {
            videoService.addVideoUrl(new VideoCreateDto(Maybe.of(VALID_YOUTUBE_SHORT_URL), Maybe.of(VIDEONAME)), initiativeId);
        } catch (InvalidVideoUrlException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            Initiative initiative = initiativeDao.get(initiativeId);

            assertThat(initiative.getVideoUrl().isPresent(), is(true));
            assertThat(initiative.getVideoUrlName().isPresent(), is(true));

            VideoCreateDto videoCreateDto = new VideoCreateDto(initiative.getVideoUrl(), initiative.getVideoUrlName());
            assertThat(videoCreateDto.getVideoUrl(), is(VALID_EMBED_VIDEO_URL));
            assertThat(videoCreateDto.getVideoName(), is(VIDEONAME));
        }
    }
    @Test
    @Transactional
    public void save_embed_youtube_video_url(){
        Long initiativeId = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(testMunicipalityId)
                        .withState(InitiativeState.PUBLISHED)
                        .applyAuthor()
                        .toInitiativeDraft());
        try {
            videoService.addVideoUrl(new VideoCreateDto(Maybe.of(VALID_EMBED_VIDEO_URL), Maybe.of(VIDEONAME)), initiativeId);
        } catch (InvalidVideoUrlException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            Initiative initiative = initiativeDao.get(initiativeId);

            assertThat(initiative.getVideoUrl().isPresent(), is(true));
            assertThat(initiative.getVideoUrlName().isPresent(), is(true));

            VideoCreateDto videoCreateDto = new VideoCreateDto(initiative.getVideoUrl(), initiative.getVideoUrlName());
            assertThat(videoCreateDto.getVideoUrl(), is(VALID_EMBED_VIDEO_URL));
            assertThat(videoCreateDto.getVideoName(), is(VIDEONAME));
        }
    }


    @Test
    @Transactional
    public void save_valid_vimeo_video_url(){
        Long initiativeId = testHelper.createDefaultInitiative(
                new TestHelper.InitiativeDraft(testMunicipalityId)
                        .withState(InitiativeState.PUBLISHED)
                        .applyAuthor()
                        .toInitiativeDraft());
        try {
            videoService.addVideoUrl(new VideoCreateDto(Maybe.of(VALID_VIMEO_URL), Maybe.of(VIDEONAME)), initiativeId);
        } catch (InvalidVideoUrlException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            Initiative initiative = initiativeDao.get(initiativeId);

            assertThat(initiative.getVideoUrl().isPresent(), is(true));
            assertThat(initiative.getVideoUrlName().isPresent(), is(true));

            VideoCreateDto videoCreateDto = new VideoCreateDto(initiative.getVideoUrl(), initiative.getVideoUrlName());
            assertThat(videoCreateDto.getVideoUrl(), is(EMBEDDED_VIMEO_URL));
            assertThat(videoCreateDto.getVideoName(), is(VIDEONAME));
        }
    }
}
