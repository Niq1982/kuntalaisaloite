package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class InitiativeDraftUIEditDto {

    // Not editable after set
    private Municipality municipality;

    // Editable by author via ui

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_NAME_MAX)
    private String name;

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String proposal;

    @Size(max = InitiativeConstants.INITIATIVE_EXTRA_INFO_MAX)
    private String extraInfo;

    @Valid
    private ContactInfo contactInfo;
    private Integer externalParticipantCount;

    public InitiativeDraftUIEditDto() {
        // For freemarker
    }

    public static InitiativeDraftUIEditDto parse(Initiative initiative, ContactInfo contactInfo) {
        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto();
        editDto.setExtraInfo(initiative.getExtraInfo());
        editDto.setName(initiative.getName());
        editDto.setProposal(initiative.getProposal());
        editDto.municipality = initiative.getMunicipality();
        editDto.setContactInfo(new ContactInfo(contactInfo));
        editDto.setExternalParticipantCount(initiative.getExternalParticipantCount());
        return editDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setExternalParticipantCount(Integer externalParticipantCount) {
        this.externalParticipantCount = externalParticipantCount;
    }

    public Integer getExternalParticipantCount() {
        return externalParticipantCount;
    }
}
