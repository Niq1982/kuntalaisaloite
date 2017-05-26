package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateBase;
import fi.om.municipalityinitiative.dto.ui.PrepareVerifiedInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.util.Membership;

import java.util.Optional;

public class MunicipalMembershipSolver {

    private Long initiativeMunicipality;
    private Optional<Long> userGivenHomeMunicipality;
    private Optional<Long> verifiedMunicipality;
    private Optional<Membership> userGivenMembership;


    public MunicipalMembershipSolver(User user, Long initiativeMunicipality,
                                     ParticipantUICreateBase createBase) {
        this(user, initiativeMunicipality, createBase.getHomeMunicipality(), createBase.getMunicipalMembership());
    }

    public MunicipalMembershipSolver(VerifiedUser verifiedUser, Long municipality, PrepareVerifiedInitiativeUICreateDto uiCreateDto) {
        this(verifiedUser, municipality, uiCreateDto.getUserGivenHomeMunicipality(), null);
    }

    private MunicipalMembershipSolver(User user, Long initiativeMunicipality, Long userGivenHomeMunicipality, Membership userGivenMunicipalMembership) {
        this.initiativeMunicipality = initiativeMunicipality;
        this.userGivenHomeMunicipality = Optional.ofNullable(userGivenHomeMunicipality);
        this.userGivenMembership = Optional.ofNullable(userGivenMunicipalMembership);
        this.verifiedMunicipality = user.isVerifiedUser()
                ? ((VerifiedUser) user).getHomeMunicipality().map(Municipality::getId)
                : Optional.empty();
    }

    public void assertMunicipalityForVerifiedInitiative() {
        if (!homeMunicipalityMatches()) {
            throw new InvalidHomeMunicipalityException("Unable to create/participate initiative for municipality " + initiativeMunicipality + ", homeMunicipality: " + getHomeMunicipality());
        }
    }

    public boolean homeMunicipalityMatches() {
        return initiativeMunicipality.equals(
                verifiedMunicipality.orElse(userGivenHomeMunicipality.orElse(null)));
    }

    public boolean isHomeMunicipalityVerified() {
        return verifiedMunicipality.isPresent();
    }

    public void assertMunicipalityOrMembershipForNormalInitiative() {
        if (!homeMunicipalityMatches() && userGivenMembership.orElse(Membership.none).equals(Membership.none)) {
            throw new InvalidHomeMunicipalityException("Unable to create/participate initiative for another municipality without membership.");
        }
    }

    public Long getHomeMunicipality() {
        if (verifiedMunicipality.isPresent()) {
            return verifiedMunicipality.get();
        }
        else {
            return userGivenHomeMunicipality.orElseThrow(() -> new InvalidHomeMunicipalityException("No homemunicipality given"));
        }
    }

    public Membership getMunicipalMembership() {
        return homeMunicipalityMatches() ? Membership.none : userGivenMembership.orElseThrow(() -> new InvalidHomeMunicipalityException("No membership given and municipalities mismatch"));
    }
}
