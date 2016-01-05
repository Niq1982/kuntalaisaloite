package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

public class VideoService {

    public static final String EMBED = "embed/";
    public static final String WATCH_V = "watch?v=";

    public static final String YOUTUBE = "www.youtube.com";
    public static final String YOUTUBE_SHORT ="youtu.be";
    public static final String VIMEO = "vimeo.com";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/embed/";
    private static final String VIMEO_BASE_URL = "https://player.vimeo.com/video/";


    public static final String VIDEO_REGEX = "^https://((www.youtube.com/(watch\\?v=|embed/)([^#&?]*))|(youtu.be/([^#&?]*))|(vimeo.com/([^#&?]*)))";

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ValidationService validationService;


    public String convertVideoUrl(String rawurl) throws MalformedURLException, InvalidVideoUrlException {
        if (rawurl == null || rawurl.equals("")) {
            return "";
        }
        URL url = new URL(rawurl);

        switch (url.getHost()) {

            case YOUTUBE:
                return YOUTUBE_BASE_URL + htmlEscape(getYouTubeVideoId(url.toString()));

            case YOUTUBE_SHORT:
                return VIMEO_BASE_URL + htmlEscape(getYouTubeVideoIdFromShort(url.getPath()));

            case VIMEO:
                return VIMEO_BASE_URL + htmlEscape(getVidemoId(url.getPath()));

            default:
                throw new InvalidVideoUrlException();

        }
    }



    private String getVidemoId(String path) throws InvalidVideoUrlException {
        if (!path.contains("/") || path.length() < 2){
            throw new InvalidVideoUrlException();
        }
        return path.substring(path.indexOf("/") + 1);
    }

    private String getYouTubeVideoIdFromShort(String path) throws InvalidVideoUrlException {
        if (!path.contains("/") || path.length() < 2){
            throw new InvalidVideoUrlException();
        }
        return path.substring(path.indexOf("/") + 1);
    }


    public String getYouTubeVideoId(String videoUrl) throws InvalidVideoUrlException {
        if (videoUrl.contains(EMBED)) {
            String id =  videoUrl.substring(videoUrl.indexOf(EMBED) + EMBED.length());
            if (id.length() < 1){
                throw new InvalidVideoUrlException();
            }
            return id;
        } else if (videoUrl.contains(WATCH_V)) {
            return videoUrl.substring(videoUrl.indexOf(WATCH_V) + WATCH_V.length());
        }
        throw new InvalidVideoUrlException();
    }


}
