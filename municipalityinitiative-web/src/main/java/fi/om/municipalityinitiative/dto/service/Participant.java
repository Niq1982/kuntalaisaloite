package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.Id;
import fi.om.municipalityinitiative.util.Membership;
import org.joda.time.LocalDate;

import java.util.Optional;

public abstract class Participant<E extends Id> {
    private String name;
    private LocalDate participateDate;
    private String email;
    private E id;
    private boolean isMunicipalityVerified;
    private Membership membership;
    private boolean showName;
    private Optional<Municipality> homeMunicipality;

    public abstract boolean isVerified();

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public final Optional<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }

    public void setHomeMunicipality(Optional<Municipality> homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public boolean isMunicipalityVerified() {
        return isMunicipalityVerified;
    }

    public void setMunicipalityVerified(boolean municipalityVerified) {
        this.isMunicipalityVerified = municipalityVerified;
    }

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

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }
}
