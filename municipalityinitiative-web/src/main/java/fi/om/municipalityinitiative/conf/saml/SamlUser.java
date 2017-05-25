package fi.om.municipalityinitiative.conf.saml;

import fi.om.municipalityinitiative.dto.service.Municipality;

import java.util.Optional;

public class SamlUser {

    private final String fullName;
    private final String ssn;
    private final String address;
    private final Optional<Municipality> municipality;

    public SamlUser(String fullName, String ssn, String address, Optional<Municipality> municipality) {
        this.fullName = fullName;
        this.ssn = ssn;
        this.address = address;
        this.municipality = municipality;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSsn() {
        return ssn;
    }

    public String getAddress() {
        return address;
    }

    public Optional<Municipality> getMunicipality() {
        return municipality;
    }
}
