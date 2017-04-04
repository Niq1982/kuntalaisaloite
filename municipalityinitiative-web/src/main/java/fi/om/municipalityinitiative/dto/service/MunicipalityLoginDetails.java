package fi.om.municipalityinitiative.dto.service;

public class MunicipalityLoginDetails {
    public final String managementHash;
    public final String loginHash;

    public MunicipalityLoginDetails(String managementHash, String loginHash) {
        this.managementHash = managementHash;
        this.loginHash = loginHash;
    }
}
