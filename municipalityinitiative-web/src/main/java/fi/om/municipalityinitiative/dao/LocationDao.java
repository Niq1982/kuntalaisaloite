package fi.om.municipalityinitiative.dao;


import fi.om.municipalityinitiative.dto.service.Location;

import java.util.List;

public interface LocationDao {

    public void setLocations(Long initiativeId, List<Location> locations);

    public List<Location> getLocations(Long initiativeId);

    public void removeLocations(Long initiativeId);
}
