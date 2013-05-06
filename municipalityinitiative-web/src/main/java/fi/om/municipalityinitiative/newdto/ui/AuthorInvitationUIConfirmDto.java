package fi.om.municipalityinitiative.newdto.ui;

public class AuthorInvitationUIConfirmDto extends ParticipantUICreateBase {

    String name;
    String phone;
    String address;
    String confirmCode;
    private boolean showName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }
}
