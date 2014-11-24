package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;

public class YouthInitiativeCreateDto {
    private Long municipality;
    private ContactInfo contactInfo;
    private String name;
    private String proposal;
    private String extraInfo;
    private long youthInitiativeId;

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public Long getMunicipality() {
        return municipality;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getProposal() {
        return proposal;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setYouthInitiativeId(long youthInitiativeId) {
        this.youthInitiativeId = youthInitiativeId;
    }

    public long getYouthInitiativeId() {
        return youthInitiativeId;
    }
}
