package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.ui.VideoCreateDto;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class VideoService {

    @Resource
    InitiativeDao initiativeDao;

    @Transactional
    public void addVideoUrl(VideoCreateDto video, Long initiativeId) throws InvalidVideoUrlException {
        if(!validateVideoUrl(video.getVideoUrl())) {
            throw new InvalidVideoUrlException();
        }
        initiativeDao.addVideoUrl(video, initiativeId);
    }

    @Transactional
    public void removeVideoUrl(Long initiativeId) {
        initiativeDao.removeVideoUrl(initiativeId);
    }
    public boolean validateVideoUrl(String videoUrl) {
        return true;
    }
}
