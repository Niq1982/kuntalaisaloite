package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;

public class MunicipalityEditDto extends Municipality {

    private String email;
    private String description;
    private String descriptionSv;

    public MunicipalityEditDto(long id, String finnishName, String swedishName, Boolean active, String email, String description, String descriptionSv) {
        super(id, finnishName, swedishName, active);
        this.email = email;
        this.description = description;
        this.descriptionSv = descriptionSv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDescriptionSv() { return descriptionSv; }

    public void setDescriptionSv(String descriptionSv) { this.descriptionSv = descriptionSv; }
}
