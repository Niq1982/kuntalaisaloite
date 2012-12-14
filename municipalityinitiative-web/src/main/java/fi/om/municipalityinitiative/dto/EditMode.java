package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.validation.group.Basic;
import fi.om.municipalityinitiative.validation.group.CurrentAuthor;
import fi.om.municipalityinitiative.validation.group.Extra;
import fi.om.municipalityinitiative.validation.group.Organizers;

public enum EditMode {
    FULL(Basic.class, Extra.class, Organizers.class, CurrentAuthor.class),
    BASIC(Basic.class),
    EXTRA(Extra.class),
    ORGANIZERS(Organizers.class),
    CURRENT_AUTHOR(CurrentAuthor.class),
    NONE();
    
    private final Class<?>[] validationGroups;
    
    private EditMode(Class<?>... validationGroups) {
        this.validationGroups = validationGroups;
    }
    
    public Class<?>[] getValidationGroups() {
        return validationGroups;
    }

}
