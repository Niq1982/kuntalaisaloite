package fi.om.municipalityinitiative.newdto.ui;

import com.google.common.base.Optional;
import org.joda.time.DateTime;

public class InitiativeViewInfo {
    private Long id;
    private String name;
    private String proposal;
    private String municipalityName;
    private Long municipalityId;

    private String authorName;
    private boolean showName;
    private DateTime createTime;
    
    private Optional<String> maybeManagementHash = Optional.absent();
    private Optional<DateTime> sentTime = Optional.absent();

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

    public void setMaybeManagementHash(Optional<String> maybeManagementHash) {
        this.maybeManagementHash = maybeManagementHash;
    }

    public String getManagementHash() {
        return maybeManagementHash.orNull();
    }

    public boolean isCollectable() {
        return maybeManagementHash.isPresent();
    }

    public Optional<DateTime> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Optional<DateTime> sentTime) {
        this.sentTime = sentTime;
    }
}
