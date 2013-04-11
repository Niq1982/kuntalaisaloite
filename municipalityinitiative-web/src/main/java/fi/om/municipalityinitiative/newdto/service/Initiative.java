package fi.om.municipalityinitiative.newdto.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

public class Initiative {

    @JsonId(path= Urls.INITIATIVE)
    private Long id;
    private String name;
    private String proposal;
    private Municipality municipality;

    private String authorName;
    private Boolean showName;
    @JsonSerialize(using=LocalDateJsonSerializer.class)
    private LocalDate createTime;

    private Maybe<String> managementHash = Maybe.absent();

    private Maybe<LocalDate> sentTime = Maybe.absent();
    private InitiativeType type;
    private InitiativeState state;
    private String comment;
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getProposal() {
        return proposal;
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

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
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
        return InitiativeType.isCollectable(type);
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

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public InitiativeType getType() {
        return type;
    }

    public void setType(InitiativeType type) {
        this.type = type;
    }

    public InitiativeState getState() {
        return state;
    }

    public void setState(InitiativeState state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
