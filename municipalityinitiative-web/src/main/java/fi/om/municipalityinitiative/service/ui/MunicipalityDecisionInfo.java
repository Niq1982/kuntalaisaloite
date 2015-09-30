package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.service.AttachmentUtil;

public class MunicipalityDecisionInfo {

    private String decisionText;

    private AttachmentUtil.Attachments attachments;

    public String getDecisionText() {
        return decisionText;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public AttachmentUtil.Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentUtil.Attachments attachments) {
        this.attachments = attachments;
    }

    public static MunicipalityDecisionInfo build(String decisionText, AttachmentUtil.Attachments attachments) {
        MunicipalityDecisionInfo municipalityDecisionInfo =  new MunicipalityDecisionInfo();
        municipalityDecisionInfo.setDecisionText(decisionText);
        municipalityDecisionInfo.setAttachments(attachments);
        return municipalityDecisionInfo;
    }
}
