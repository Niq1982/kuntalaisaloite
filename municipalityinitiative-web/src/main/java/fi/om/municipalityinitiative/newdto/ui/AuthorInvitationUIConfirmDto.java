package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;

@ValidMunicipalMembership
public class AuthorInvitationUIConfirmDto extends ParticipantUICreateBase {

    private String confirmCode;

    private ContactInfo contactInfo;

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    private Long initiativeMunicipality;

    @Override
    public Long getMunicipality() {
        return initiativeMunicipality;
    }

    public void setInitiativeMunicipality(Long initiativeMunicipality) {
        this.initiativeMunicipality = initiativeMunicipality;
    }

}
