package fi.om.municipalityinitiative.newdto.service;

import org.joda.time.LocalDate;

public class PublicParticipant {
    private String name;
    private boolean franchise;
    private LocalDate participateDate;

    public PublicParticipant(LocalDate participateDate, String name, boolean franchise) {
        this.name = name;
        this.franchise = franchise;
        this.participateDate = participateDate;
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
}
