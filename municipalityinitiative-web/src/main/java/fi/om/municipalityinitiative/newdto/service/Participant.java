package fi.om.municipalityinitiative.newdto.service;

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
    private boolean isAuthor;

    public Participant() {

    }

    public String getName() {
        return name;
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getParticipateDate() {
        return participateDate;
    }

    public Municipality getHomeMunicipality() {
        return homeMunicipality;
    }

    @JsonIgnore
    public String getEmail() {
        return email;
    }

    @JsonIgnore
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

    @JsonIgnore
    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }
}
