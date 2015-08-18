package fi.om.municipalityinitiative.validation;

import fi.om.municipalityinitiative.dto.service.Location;

import java.util.List;

public interface InitiativeWithLocationInformation {

    List<Location> getLocations();

}
