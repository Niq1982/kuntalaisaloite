package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

public class InitiativeViewInfo {

    @JsonId(path= Urls.INITIATIVE)
    private Long id;
    private String name;
    private String proposal;
    private String municipalityName;
    private Long municipalityId;

    private String authorName;
    private boolean showName;
    private LocalDate createTime;
    
    private Maybe<String> managementHash = Maybe.absent();
    private Maybe<LocalDate> sentTime = Maybe.absent();

    public String getName() {
        return name;
    }

    public String getProposal() {
        return proposal;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }
    
    public Long getMunicipalityId() {
        return municipalityId;
    }

    public String getAuthorName() {
        return authorName;
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

    public void setMunicipalityId(Long municipalityId) {
        this.municipalityId = municipalityId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setManagementHash(Maybe<String> managementHash) {
        this.managementHash = managementHash;
    }

    public Maybe<String> getManagementHash() {
        return managementHash;
    }

    public boolean isCollectable() {
        return managementHash.isPresent();
    }

    public boolean isSent() {
        return sentTime.isPresent();
    }

    public Maybe<LocalDate> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Maybe<LocalDate> sentTime) {
        this.sentTime = sentTime;
    }
}
