package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.Id;
import org.joda.time.LocalDate;

public abstract class Participant<E extends Id> {
    private String name;
    private LocalDate participateDate;
    private String email;
    private E id;

    public Participant() {

    }

    public String getName() {
        return name;
    }

    public LocalDate getParticipateDate() {
        return participateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipateDate(LocalDate participateDate) {
        this.participateDate = participateDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }
}
