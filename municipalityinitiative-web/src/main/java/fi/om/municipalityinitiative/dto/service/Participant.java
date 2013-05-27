package fi.om.municipalityinitiative.dto.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.util.Membership;
import org.joda.time.LocalDate;

public class Participant {
    private String name;
    private LocalDate participateDate;
    private Municipality homeMunicipality;
    private String email;
    private Membership membership;
    private Long id;

    public Participant() {

    }

    public String getName() {
        return name;
    }

    public LocalDate getParticipateDate() {
        return participateDate;
    }

    public Municipality getHomeMunicipality() {
        return homeMunicipality;
    }

    public String getEmail() {
        return email;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipateDate(LocalDate participateDate) {
        this.participateDate = participateDate;
    }

    public void setHomeMunicipality(Municipality homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
