package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.MunicipalityLoginDetails;
import org.joda.time.DateTime;

public interface MunicipalityUserDao {

    Long assignMunicipalityUser(Long initiativeId, String managementHash);

    void assignMunicipalityUserLoginHash(Long initiativeId,
                                         String managementHash,
                                         String managementLoginHash,
                                         DateTime now);

    Long getInitiativeId(String managementHash);

    Long getInitiativeId(String managementHash, String managementLoginHash);

    MunicipalityLoginDetails getMunicipalityUserHashAttachedToInitiative(Long initiativeId);
}
