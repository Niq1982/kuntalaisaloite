package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.ui.VideoCreateDto;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

public class VideoService {

    public static final String EMBED = "embed/";
    public static final String WATCH_V = "watch?v=";

    public static final String YOUTUBE = "www.youtube.com";
    public static final String VIMEO = "vimeo.com";

    private static final String YOUTUBE_BASE_URL = "//www.youtube.com/embed/";
    private static final String VIMEO_BASE_URL = "//player.vimeo.com/video/";


    public static final String VIDEO_REGEX = "^https://((www.youtube.com/(watch\\?v=|embed/)([^#&?]*))|(vimeo.com/([^#&?]*)))";

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ValidationService validationService;




    @Transactional
    public void addVideoUrl(VideoCreateDto video, Long initiativeId) throws InvalidVideoUrlException, MalformedURLException {

        URL url = new URL(video.getVideoUrl());

        switch (url.getHost()) {

            case YOUTUBE:
                initiativeDao.addVideoUrl(YOUTUBE_BASE_URL + htmlEscape(getYouTubeVideoId(url.toString())), video.getVideoName(), initiativeId);
                break;

            case VIMEO:
                initiativeDao.addVideoUrl(VIMEO_BASE_URL + htmlEscape(getVidemoId(url.getPath())), video.getVideoName(), initiativeId);
                break;

            default:
                throw new InvalidVideoUrlException();
        }
    }

    private String getVidemoId(String path) {
        return path.substring(path.indexOf("/") + 1);
    }

    @Transactional
    public void removeVideoUrl(Long initiativeId) {
        initiativeDao.removeVideoUrl(initiativeId);
    }

    public String getYouTubeVideoId(String videoUrl) throws InvalidVideoUrlException {
        if (videoUrl.contains(EMBED)) {
            return videoUrl.substring(videoUrl.indexOf(EMBED) + EMBED.length());
        } else if (videoUrl.contains(WATCH_V)) {
            return videoUrl.substring(videoUrl.indexOf(WATCH_V) + WATCH_V.length());
        }
        throw new InvalidVideoUrlException();
    }

    public boolean validationSuccessful(VideoCreateDto video, BindingResult bindingResult, Model model) {
        if (!video.getVideoUrl().matches(VIDEO_REGEX)) {
            bindingResult.addError(new FieldError("video", "videoUrl", video.getVideoUrl(), false, new String[]{"invalidUrl"}, new String[]{"invalidUrl"}, ""));
        }
        return validationService.validationSuccessful(video, bindingResult, model);

    }
}
