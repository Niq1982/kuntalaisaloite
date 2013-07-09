package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.springframework.util.Assert;

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
        Assert.notNull(initiativeMunicipality);
        return initiativeMunicipality;
    }

    public void assignInitiativeMunicipality(Long initiativeMunicipality) {
        this.initiativeMunicipality = initiativeMunicipality;
    }

}
