package fi.om.municipalityinitiative.dao;


import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.sql.QLocation;

import javax.annotation.Resource;
import java.util.List;

import static fi.om.municipalityinitiative.sql.QLocation.location;

public class JdbcLocationDao implements LocationDao{

    @Resource
    PostgresQueryFactory queryFactory;

    static final Expression<Location> locationMapping = new MappingProjection<Location>(Location.class, QLocation.location.all()) {
        @Override
        protected Location map(Tuple tuple) {
            Location l = new Location();
            l.setLat(tuple.get(location.locationLat));
            l.setLng(tuple.get(location.locationLng));
            l.setLocationDescription(tuple.get(location.locationDescription));
            return l;
        }
    };

    @Override
    public void setLocations(Long initiativeId, List<Location> locations) {
        for (Location location: locations) {
            addLocation(initiativeId, location);
        }
    }

    private void addLocation(Long initiativeId, Location l){
        queryFactory.insert(location).set(location.locationLat, l.getLat())
                .set(location.locationLng, l.getLng())
                .set(location.locationDescription, l.getLocationDescription())
                .set(location.initiativeId, initiativeId).execute();
    }

    @Override
    public List<Location> getLocations(Long initiativeId) {
        return queryFactory.from(location).where(location.initiativeId.eq(initiativeId)).list(locationMapping);
    }

    @Override
    public void removeLocations(Long initiativeId) {
        queryFactory.delete(location).where(location.initiativeId.eq(initiativeId)).execute();
    }
}
