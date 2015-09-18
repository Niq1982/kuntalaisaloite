package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.dto.service.AttachmentFileInfo;

import java.util.List;

public class MunicipalityDecisionInfo {

    private String decisionText;

    private List<AttachmentFileInfo> attachments;

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public List<AttachmentFileInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentFileInfo> attachments) {
        this.attachments = attachments;
    }

}
