package fi.om.municipalityinitiative.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LanguageCode {
    FI,
    SV;
    
    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
    
}
