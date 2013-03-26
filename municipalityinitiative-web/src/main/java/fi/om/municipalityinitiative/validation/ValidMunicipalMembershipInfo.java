package fi.om.municipalityinitiative.validation;

public interface ValidMunicipalMembershipInfo {

    Long getHomeMunicipality();
    Long getMunicipality();
    boolean hasMunicipalMembership();
}
