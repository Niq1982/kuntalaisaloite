package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;

import javax.validation.constraints.Size;

public class CommentUIDto {

    @Size(max = InitiativeConstants.INITIATIVE_COMMENT_MAX)
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
