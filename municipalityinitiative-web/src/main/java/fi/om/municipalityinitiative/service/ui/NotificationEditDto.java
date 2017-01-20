package fi.om.municipalityinitiative.service.ui;

public class NotificationEditDto {
    private String fi;
    private String urlFi;
    private String urlFiText;

    private String sv;
    private String urlSv;
    private String urlSvText;

    private boolean enabled;

    public String getFi() {
        return fi;
    }

    public void setFi(String fi) {
        this.fi = fi;
    }

    public String getSv() {
        return sv;
    }

    public void setSv(String sv) {
        this.sv = sv;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrlFi() {
        return urlFi;
    }

    public void setUrlFi(String urlFi) {
        this.urlFi = urlFi;
    }

    public String getUrlFiText() {
        return urlFiText;
    }

    public void setUrlFiText(String urlFiText) {
        this.urlFiText = urlFiText;
    }

    public String getUrlSv() {
        return urlSv;
    }

    public void setUrlSv(String urlSv) {
        this.urlSv = urlSv;
    }

    public String getUrlSvText() {
        return urlSvText;
    }

    public void setUrlSvText(String urlSvText) {
        this.urlSvText = urlSvText;
    }
}
