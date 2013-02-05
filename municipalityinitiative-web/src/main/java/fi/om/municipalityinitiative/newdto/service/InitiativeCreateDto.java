package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
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

    public static InitiativeCreateDto parse(InitiativeUICreateDto source, Maybe<String> managementHash) {

        InitiativeCreateDto initiativeCreateDto = new InitiativeCreateDto();
        initiativeCreateDto.name = source.getName();
        initiativeCreateDto.proposal = source.getProposal();
        initiativeCreateDto.municipalityId = source.getMunicipality();
        initiativeCreateDto.contactName = source.getContactInfo().getName();
        initiativeCreateDto.contactPhone= source.getContactInfo().getPhone();
        initiativeCreateDto.contactAddress = source.getContactInfo().getAddress();
        initiativeCreateDto.contactEmail = source.getContactInfo().getEmail();
        initiativeCreateDto.managementHash = managementHash;

        return initiativeCreateDto;
    }
}
