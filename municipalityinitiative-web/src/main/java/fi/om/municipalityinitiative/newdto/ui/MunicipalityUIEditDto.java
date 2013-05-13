package fi.om.municipalityinitiative.newdto.ui;

public class MunicipalityUIEditDto {

    private Long id;
    private String municipalityEmail;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMunicipalityEmail() {
        return municipalityEmail;
    }

    public void setMunicipalityEmail(String email) {
        this.municipalityEmail = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
