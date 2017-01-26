package fi.om.municipalityinitiative.service.ui;

import com.google.common.base.Strings;

public class Notification {

    private String text;
    private String link;
    private String linkText;
    private Long createTime;

    public Notification() {}

    public Notification(String text, String link, String linkText, Long createTime) {
        this.text = text;
        this.link = link;
        this.linkText = linkText;
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {

        if (!Strings.isNullOrEmpty(link) && !link.matches("^\\w+?://.*")) {
            return "http://" + link;
        }
        else {
            return link;
        }

    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public Long getCreateTime() {
        return createTime;
    }
}
