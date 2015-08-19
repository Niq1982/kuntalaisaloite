package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.LocationDao;
import fi.om.municipalityinitiative.dto.service.Location;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class LocationService {

    @Resource
    private LocationDao locationDao;

    @Transactional(readOnly = true)
    public List<Location> getLocations(Long initiativeId) {
        return locationDao.getLocations(initiativeId);
    }

}
