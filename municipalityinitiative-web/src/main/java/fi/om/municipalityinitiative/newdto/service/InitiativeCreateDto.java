package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.util.Maybe;

public class InitiativeCreateDto {

    public String name;
    public String proposal;

    public String contactName;
    public String contactEmail;
    public String contactPhone;
    public String contactAddress;
    public Long municipalityId;
    public Maybe<String> managementHash;
}
