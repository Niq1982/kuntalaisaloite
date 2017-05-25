package fi.om.municipalityinitiative.dto.ui;


import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Optional;

public class VideoCreateDto {

    public VideoCreateDto() {

    }

    public VideoCreateDto(Optional<String> videoUrl, Optional<String> name) {
        if (videoUrl.isPresent()) {
            this.videoUrl = videoUrl.get();
        }
        if (name.isPresent()) {
            this.videoName = name.get();
        }
    }

    @NotEmpty
    @Size(max = InitiativeConstants.VIDEO_MAX_LENGTH)
    private String videoUrl;

    @NotEmpty
    private String videoName;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
