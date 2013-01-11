package fi.om.municipalityinitiative.newdto;

import org.joda.time.DateTime;

public class MunicipalityInitiativeInfo {
    private Long id;
    private String name;
    private String proposal;
    private String municipalityName;

    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;
    private boolean showName;
    private DateTime createTime;

    public String getName() {
        return name;
    }

    public String getProposal() {
        return proposal;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
    
    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
