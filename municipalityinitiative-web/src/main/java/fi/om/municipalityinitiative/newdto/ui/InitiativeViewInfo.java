package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

public class InitiativeViewInfo {

    private final Initiative initiative;

    private InitiativeViewInfo(Initiative initiative) {
        this.initiative = initiative;
    }

    public static InitiativeViewInfo parse(Initiative initiative) {
        return new InitiativeViewInfo(initiative);
    }

    public String getName() {
        return initiative.getName();
    }

    public String getProposal() {
        return initiative.getProposal();
    }

    public String getAuthorName() {
        return initiative.getAuthorName();
    }

    public boolean isShowName() {
        return initiative.getAuthor().getContactInfo().isShowName();
    }

    public LocalDate getCreateTime() {
        return initiative.getCreateTime();
    }

    public Long getId() {
        return initiative.getId();
    }

    public Maybe<String> getManagementHash() {
        return initiative.getManagementHash();
    }

    public boolean isCollectable() {
        return initiative.isCollectable();
    }

    public boolean isSent() {
        return initiative.isSent();
    }

    public Maybe<LocalDate> getSentTime() {
        return initiative.getSentTime();
    }

    public InitiativeState getState() {
        return initiative.getState();
    }

    public Municipality getMunicipality() {
        return initiative.getMunicipality();
    }

    public LocalDate getStateTime() {
        return initiative.getStateTime();
    }

    public String getExtraInfo() {
        return initiative.getExtraInfo();
    }

}
