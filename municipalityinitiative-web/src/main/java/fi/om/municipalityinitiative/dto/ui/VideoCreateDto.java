package fi.om.municipalityinitiative.dto.ui;


import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.util.Maybe;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class VideoCreateDto {

    public VideoCreateDto() {

    }

    public VideoCreateDto(Maybe<String> videoUrl, Maybe<String> name) {
        if (videoUrl.isPresent()) {
            this.videoUrl = videoUrl.getValue();
        }
        if (name.isPresent()) {
            this.videoName = name.getValue();
        }
    }

    @NotEmpty
    @Size(max = InitiativeConstants.VIDEO_MAX_LENGTH)
    @Pattern(regexp = "^https://((www.youtube.com/(watch\\?v=|embed/)([^#&?]*))|(vimeo.com/([^#&?]*)))")
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
