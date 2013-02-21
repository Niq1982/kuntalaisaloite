package fi.om.municipalityinitiative.newdto.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.json.LocalDateJsonSerializer;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;

public class InitiativeJson {

    private InitiativeViewInfo initiativeViewInfo;

    private InitiativeJson(InitiativeViewInfo initiativeViewInfo) {
        this.initiativeViewInfo = initiativeViewInfo;
    }

    @JsonId(path= Urls.INITIATIVE)
    public Long getId() {
        return initiativeViewInfo.getId();
    }

    public String getName() {
        return initiativeViewInfo.getName();
    }

    public String getProposal() {
        return initiativeViewInfo.getProposal();
    }

    public String getMunicipalityName() {
        return initiativeViewInfo.getMunicipalityName();
    }

    public String getAuthorName() {
        return initiativeViewInfo.isShowName() ? initiativeViewInfo.getAuthorName() : null;
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getCreateTime() {
        return initiativeViewInfo.getCreateTime();
    }

    public boolean isCollectable() {
        return initiativeViewInfo.isCollectable();
    }

    @JsonSerialize(using=LocalDateJsonSerializer.class)
    public LocalDate getSentTime() {
        return initiativeViewInfo.isSent() ? initiativeViewInfo.getSentTime().get() : null;
    }

    public static InitiativeJson from(InitiativeViewInfo initiativeInfo) {
        return new InitiativeJson(initiativeInfo);
    }
}
