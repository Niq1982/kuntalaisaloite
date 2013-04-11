package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InitiativeUIUpdateDto {

    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String extraInfo;

    @Valid
    private ContactInfo contactInfo;

    @NotNull
    private Boolean showName;

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }


}
