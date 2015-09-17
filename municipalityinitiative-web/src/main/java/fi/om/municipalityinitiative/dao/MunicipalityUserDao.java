package fi.om.municipalityinitiative.dao;


public interface MunicipalityUserDao {

    public Long createMunicipalityUser(Long initiativeId, String managementHash);

    public Long getInitiativeId(String managementHash);
}
