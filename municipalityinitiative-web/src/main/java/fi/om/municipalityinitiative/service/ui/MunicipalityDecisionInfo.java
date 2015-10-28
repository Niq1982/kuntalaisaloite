package fi.om.municipalityinitiative.service.ui;


import fi.om.municipalityinitiative.service.AttachmentUtil;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;

public class MunicipalityDecisionInfo {

    private Maybe<String> decisionText = Maybe.absent();

    private AttachmentUtil.Attachments attachments;

    private DateTime date;

    private Maybe<DateTime> modifiedDate = Maybe.absent();

    public void setDate(DateTime date) {
        this.date = date;
    }
    public DateTime getDate() {
        return date;
    }

    public Maybe<String> getDecisionText() {
        return decisionText;
    }
    public void setDecisionText(Maybe<String> decisionText) {
        this.decisionText = decisionText;
    }

    public void setModifiedDate(Maybe<DateTime> modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public Maybe<DateTime> getModifiedDate() {
        return modifiedDate;
    }

    public AttachmentUtil.Attachments getAttachments() {
        return attachments;
    }
    public void setAttachments(AttachmentUtil.Attachments attachments) {
        this.attachments = attachments;
    }

    public static MunicipalityDecisionInfo build(Maybe<String> decisionText, DateTime date, Maybe<DateTime> modifiedDate, AttachmentUtil.Attachments attachments) {
        MunicipalityDecisionInfo municipalityDecisionInfo = new MunicipalityDecisionInfo();

        municipalityDecisionInfo.setDate(date);
        municipalityDecisionInfo.setDecisionText(decisionText);
        municipalityDecisionInfo.setModifiedDate(modifiedDate);
        municipalityDecisionInfo.setAttachments(attachments);
        return municipalityDecisionInfo;
    }

}
