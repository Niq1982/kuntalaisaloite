package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.service.AttachmentUtil;
import org.joda.time.DateTime;

public class MunicipalityDecisionInfo {

    private String decisionText;

    private AttachmentUtil.Attachments attachments;

    private DateTime date;

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getDate() {
        return date;
    }

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

    public static MunicipalityDecisionInfo build(String decisionText, DateTime date, AttachmentUtil.Attachments attachments) {
        MunicipalityDecisionInfo municipalityDecisionInfo =  new MunicipalityDecisionInfo();
        municipalityDecisionInfo.setDecisionText(decisionText);
        municipalityDecisionInfo.setDate(date);
        municipalityDecisionInfo.setAttachments(attachments);
        return municipalityDecisionInfo;
    }



}
