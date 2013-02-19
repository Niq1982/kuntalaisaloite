package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import org.joda.time.DateTime;

public class InitiativeEmailInfo {

    private Long id;
    
    private String name;

    private String proposal;
    
    private DateTime createTime;
    
    private DateTime sentTime;

    private String municipalityName;

    private String url;

    private ContactInfo contactInfo;

    InitiativeEmailInfo() { }

    public static InitiativeEmailInfo parse(ContactInfo contactInfo, InitiativeViewInfo initiative, String url) {

        InitiativeEmailInfo initiativeEmailInfo = new InitiativeEmailInfo();

        initiativeEmailInfo.setId(initiative.getId());
        initiativeEmailInfo.setName(initiative.getName());
        initiativeEmailInfo.setProposal(initiative.getProposal());
        initiativeEmailInfo.setCreateTime(initiative.getCreateTime());
        initiativeEmailInfo.setSentTime(initiative.getSentTime().get());
        initiativeEmailInfo.setMunicipalityName(initiative.getMunicipalityName());
        initiativeEmailInfo.setUrl(url);
        initiativeEmailInfo.setContactInfo(contactInfo);

        return initiativeEmailInfo;
    }
    InitiativeEmailInfo(InitiativeEmailInfo original) {
        setId(original.getId());
        setName(original.getName());
        setProposal(original.getProposal());
        setCreateTime(original.getCreateTime());
        setSentTime(original.getSentTime());
        setMunicipalityName(original.getMunicipalityName());
        setUrl(original.getUrl());
        setContactInfo(original.getContactInfo());
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(DateTime sentTime) {
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
