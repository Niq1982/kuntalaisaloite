package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Optional;

public class Initiative {

    private Long id;
    private String name;
    private String proposal;
    private String extraInfo;
    private int externalParticipantCount;

    private Municipality municipality;

    private LocalDate createTime;

    private Optional<LocalDate> sentTime = Optional.empty();
    private InitiativeType type;
    private InitiativeState state;
    private LocalDate stateTime;
    private String moderatorComment;
    private int participantCount;
    private int participantCountPublic;
    private int participantCountCitizen;
    private String sentComment;
    private FixState fixState;
    private DateTime lastEmailReportTime;
    private EmailReportType lastEmailReportType;
    private Optional<Long> youthInitiativeId = Optional.empty();
    private Optional<String> decision = Optional.empty();
    private Optional<DateTime> decisionDate = Optional.empty();
    private Optional<DateTime> decisionModifiedDate = Optional.empty();
    private Optional<String> videoUrl = Optional.empty();


    public Optional<Long> getYouthInitiativeId() {
        return youthInitiativeId;
    }

    public void setYouthInitiativeId(Long youthInitiativeId) {
        this.youthInitiativeId = Optional.of(youthInitiativeId);
    }

    public String getName() {
        return name;
    }

    public String getProposal() {
        return proposal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
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

    public boolean isCollaborative() {
        return InitiativeType.isCollaborative(type);
    }

    public boolean isSent() {
        return sentTime.isPresent();
    }

    public Optional<LocalDate> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Optional<LocalDate> sentTime) {
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public LocalDate getStateTime() {
        return stateTime;
    }

    public void setStateTime(LocalDate stateTime) {
        this.stateTime = stateTime;
    }

    public String getModeratorComment() {
        return moderatorComment;
    }

    public void setModeratorComment(String moderatorComment) {
        this.moderatorComment = moderatorComment;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public void setParticipantCountCitizen(int participantCountCitizen) {
        this.participantCountCitizen = participantCountCitizen;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public String getSentComment() {
        return sentComment;
    }

    public void setSentComment(String sentComment) {
        this.sentComment = sentComment;
    }

    public FixState getFixState() {
        return fixState;
    }

    public void setFixState(FixState fixState) {
        this.fixState = fixState;
    }

    public int getExternalParticipantCount() {
        return externalParticipantCount;
    }

    public void setExternalParticipantCount(int externalParticipantCount) {
        this.externalParticipantCount = externalParticipantCount;
    }

    public int getParticipantCountPublic() {
        return participantCountPublic;
    }

    public int getParticipantCountCitizen() {
        return participantCountCitizen;
    }

    public void setParticipantCountPublic(int participantCountPublic) {
        this.participantCountPublic = participantCountPublic;
    }

    public boolean isPublic() {
        return state == InitiativeState.PUBLISHED && fixState == FixState.OK;
    }

    public void setLastEmailReportTime(DateTime lastEmailReportTime) {
        this.lastEmailReportTime = lastEmailReportTime;
    }

    public DateTime getLastEmailReportTime() {
        return lastEmailReportTime;
    }

    public void setLastEmailReportType(EmailReportType lastEmailReportType) {
        this.lastEmailReportType = lastEmailReportType;
    }

    public EmailReportType getLastEmailReportType() {
        return lastEmailReportType;
    }


    public void setDecision(String decision){
        this.decision = Optional.of(decision);
    }

    public Optional<String> getDecision() {
        return decision;
    }

    public void setDecisionDate(DateTime decisionDate) {this.decisionDate = Optional.of(decisionDate);}

    public Optional<DateTime> getDecisionDate(){return decisionDate;}

    public Optional<DateTime> getDecisionModifiedDate() {
        return decisionModifiedDate;
    }
    public void setDecisionModifiedDate(DateTime date) {
        this.decisionModifiedDate = Optional.of(date);
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = Optional.of(videoUrl);
    }

    public Optional<String> getVideoUrl() {
        return videoUrl;
    }

}
