package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;

import java.util.List;

public class MunicipalityDecisionInfo {

    private String decisionText;

    private List<DecisionAttachmentFile> attachments;

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public List<DecisionAttachmentFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<DecisionAttachmentFile> attachments) {
        this.attachments = attachments;
    }

    public static MunicipalityDecisionInfo build(String decisionText, List<DecisionAttachmentFile> attachments) {
        MunicipalityDecisionInfo municipalityDecisionInfo =  new MunicipalityDecisionInfo();
        municipalityDecisionInfo.setDecisionText(decisionText);
        municipalityDecisionInfo.setAttachments(attachments);
        return municipalityDecisionInfo;
    }
}
