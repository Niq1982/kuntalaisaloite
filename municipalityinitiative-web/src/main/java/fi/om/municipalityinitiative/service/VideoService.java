package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.ui.VideoCreateDto;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

public class VideoService {

    @Resource
    InitiativeDao initiativeDao;

    private static final String videoBaseUrl = "//www.youtube.com/embed/";

    @Transactional
    public void addVideoUrl(VideoCreateDto video, Long initiativeId) throws InvalidVideoUrlException {

        String videoId = getYouTubeVideoId(video.getVideoUrl());

        video.setVideoUrl(videoBaseUrl + htmlEscape(videoId));
        initiativeDao.addVideoUrl(video, initiativeId);
    }

    @Transactional
    public void removeVideoUrl(Long initiativeId) {
        initiativeDao.removeVideoUrl(initiativeId);
    }

    public String getYouTubeVideoId(String videoUrl) throws InvalidVideoUrlException {
        if (videoUrl.contains("embed/")) {
            return videoUrl.substring(videoUrl.indexOf("embed/") + "embed/".length());
        } else if (videoUrl.contains("watch?v=")) {
            return videoUrl.substring(videoUrl.indexOf("watch?v=") + "watch?v=".length());
        }
        throw new InvalidVideoUrlException();
    }

}
