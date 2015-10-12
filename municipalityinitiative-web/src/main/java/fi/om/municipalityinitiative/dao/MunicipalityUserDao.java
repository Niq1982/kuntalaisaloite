package fi.om.municipalityinitiative.dao;


public interface MunicipalityUserDao {

    Long createMunicipalityUser(Long initiativeId, String managementHash);

    Long getInitiativeId(String managementHash);

    String getMunicipalityUserHashAttachedToInitiative(Long initiativeId);
}
