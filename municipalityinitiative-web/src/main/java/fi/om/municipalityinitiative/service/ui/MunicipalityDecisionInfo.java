package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.service.AttachmentUtil;
import org.joda.time.DateTime;

import java.util.Optional;

public class MunicipalityDecisionInfo {

    private Optional<String> decisionText = Optional.empty();

    private AttachmentUtil.Attachments attachments;

    private DateTime date;

    private Optional<DateTime> modifiedDate = Optional.empty();

    public void setDate(DateTime date) {
        this.date = date;
    }
    public DateTime getDate() {
        return date;
    }

    public Optional<String> getDecisionText() {
        return decisionText;
    }
    public void setDecisionText(Optional<String> decisionText) {
        this.decisionText = decisionText;
    }

    public void setModifiedDate(Optional<DateTime> modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public Optional<DateTime> getModifiedDate() {
        return modifiedDate;
    }

    public AttachmentUtil.Attachments getAttachments() {
        return attachments;
    }
    public void setAttachments(AttachmentUtil.Attachments attachments) {
        this.attachments = attachments;
    }

    public static MunicipalityDecisionInfo build(Optional<String> decisionText, DateTime date, Optional<DateTime> modifiedDate, AttachmentUtil.Attachments attachments) {
        MunicipalityDecisionInfo municipalityDecisionInfo = new MunicipalityDecisionInfo();

        municipalityDecisionInfo.setDate(date);
        municipalityDecisionInfo.setDecisionText(decisionText);
        municipalityDecisionInfo.setModifiedDate(modifiedDate);
        municipalityDecisionInfo.setAttachments(attachments);
        return municipalityDecisionInfo;
    }

}
