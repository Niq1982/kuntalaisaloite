package fi.om.municipalityinitiative.newdto.service;

import org.joda.time.LocalDate;

public class PublicParticipant {
    private String name;
    private boolean franchise;
    private LocalDate participateDate;
    private final String homeMunicipality;

    public PublicParticipant(LocalDate participateDate, String name, boolean franchise, String homeMunicipality) {
        this.name = name;
        this.franchise = franchise;
        this.participateDate = participateDate;
        this.homeMunicipality = homeMunicipality;
    }

    public String getName() {
        return name;
    }

    public boolean isFranchise() {
        return franchise;
    }

    public LocalDate getParticipateDate() {
        return participateDate;
    }

    public String getHomeMunicipality() {
        return homeMunicipality;
    }
}
