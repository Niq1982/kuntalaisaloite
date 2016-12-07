package fi.om.municipalityinitiative.dto.json;

import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.web.Urls;

public class ViewUrlJson {

    private Long initiativeId;

    public ViewUrlJson(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    @JsonId(path =  Urls.VIEW_FI, useApiUrl = false)
    public Long getFi() {
        return initiativeId;
    }

    @JsonId(path =  Urls.VIEW_SV, useApiUrl = false)
    public Long getSv() {
        return initiativeId;
    }
}
