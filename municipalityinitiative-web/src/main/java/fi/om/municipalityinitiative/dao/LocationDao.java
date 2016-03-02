package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.Location;

import java.util.List;

public interface LocationDao {

    void setLocations(Long initiativeId, List<Location> locations);

    List<Location> getLocations(Long initiativeId);

    void removeLocations(Long initiativeId);
}
