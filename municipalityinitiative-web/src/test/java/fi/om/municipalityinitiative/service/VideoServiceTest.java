package fi.om.municipalityinitiative.service;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VideoServiceTest extends ServiceIntegrationTestBase{

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";
    public static final String VALID_VIDEO_URL = "www.youtube.com/watch?v=lq-54gox6-k";
    public static final String WRONG_HOST = "www.novideoservice.com";
    private VideoService videoService;


    @Override
    protected void childSetup() {
        videoService =  new VideoService();
    }

    @Test
    public void valid_url() {
        assertThat(videoService.validateVideoUrl(HTTPS_PROTOCOL+VALID_VIDEO_URL), is(true));
    }

    @Test
    public void valid_protocol(){
   //     assertThat(videoService.validateVideoUrl(HTTP_PROTOCOL), is(false));
    }

    @Test
    public void correct_host() {
       // assertThat(videoService.validateVideoUrl(WRONG_HOST), is(false));
    }


}
