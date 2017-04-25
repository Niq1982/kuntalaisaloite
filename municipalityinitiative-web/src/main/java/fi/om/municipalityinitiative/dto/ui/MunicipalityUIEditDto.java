package fi.om.municipalityinitiative.dto.ui;

import com.google.common.base.Strings;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

public class MunicipalityUIEditDto {

    private Long id;

    @Pattern(regexp = fi.om.municipalityinitiative.dto.ui.ContactInfo.EMAIL_PATTERN)
    private String municipalityEmail;

    private Boolean active;

    private String municipalityDescriptionFi;

    private String municipalityDescriptionSv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMunicipalityEmail() {
        return municipalityEmail;
    }

    public String getMunicipalityDescriptionFi() {
        return municipalityDescriptionFi;
    }

    public String getMunicipalityDescriptionSv() { return municipalityDescriptionSv; }

    public void setMunicipalityEmail(String email) {
        this.municipalityEmail = email;
    }

    public void setMunicipalityDescriptionFi(String municipalityDescriptionFi) { this.municipalityDescriptionFi = municipalityDescriptionFi; }

    public void setMunicipalityDescriptionSv(String municipalityDescriptionSv) { this.municipalityDescriptionSv = municipalityDescriptionSv; }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @AssertTrue(message = "Sähköpostiosoite on pakollinen jos kunta käyttää palvelua") // XXX: Why cannot this be localized?
    public boolean isEmailNotNullIfActive() {
        boolean isActive = Boolean.TRUE.equals(this.active);
        return !isActive || !Strings.isNullOrEmpty(municipalityEmail);
    }
}
