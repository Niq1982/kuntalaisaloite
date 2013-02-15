package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;

public class InitiativeListInfo {
    private Long id;
    private String name;
    private String municipalityName;
    private DateTime createTime;
    private boolean collectable;
    private Maybe<DateTime> sentTime;
    private long participantCount;

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

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public Maybe<DateTime> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Maybe<DateTime> sentTime) {
        this.sentTime = sentTime;
    }

    public void setParticipantCount(long participantCount) {
        this.participantCount = participantCount;
    }

    public long getParticipantCount() {
        return participantCount;
    }
}
