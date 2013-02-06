package fi.om.municipalityinitiative.newdto.email;

import org.joda.time.DateTime;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Maybe;

public class InitiativeEmailInfo {

    private String name;

    private String proposal;
    
    private DateTime createTime;
    
    private Maybe<DateTime> sentTime;

    private String municipalityName;

    private String url;

    private ContactInfo contactInfo;

    public static InitiativeEmailInfo parse(ContactInfo contactInfo, InitiativeViewInfo initiative, String url) {

        InitiativeEmailInfo initiativeEmailInfo = new InitiativeEmailInfo();

        initiativeEmailInfo.setName(initiative.getName());
        initiativeEmailInfo.setProposal(initiative.getProposal());
        initiativeEmailInfo.setCreateTime(initiative.getCreateTime());
        initiativeEmailInfo.setSentTime(initiative.getSentTime());
        initiativeEmailInfo.setMunicipalityName(initiative.getMunicipalityName());
        initiativeEmailInfo.setUrl(url);
        initiativeEmailInfo.setContactInfo(contactInfo);

        return initiativeEmailInfo;
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
    
    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public Maybe<DateTime> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Maybe<DateTime> sentTime) {
        this.sentTime = sentTime;
    }
    
    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
