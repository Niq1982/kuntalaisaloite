package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Municipality;

public class MunicipalityEditDto extends Municipality {

    private String email;

    public MunicipalityEditDto(long id, String finnishName, String swedishName, Boolean active, String email) {
        super(id, finnishName, swedishName, active);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
