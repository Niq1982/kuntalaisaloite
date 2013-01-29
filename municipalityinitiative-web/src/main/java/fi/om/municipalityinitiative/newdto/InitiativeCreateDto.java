package fi.om.municipalityinitiative.newdto;

import com.google.common.base.Optional;

public class InitiativeCreateDto {

    public String name;
    public String proposal;

    public String contactName;
    public String contactEmail;
    public String contactPhone;
    public String contactAddress;
    public Long municipalityId;
    public Optional<String> managementHash;
}
