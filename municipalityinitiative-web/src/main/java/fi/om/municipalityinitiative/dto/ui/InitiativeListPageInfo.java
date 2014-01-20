package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.dto.service.Municipality;

import java.util.List;

public class InitiativeListPageInfo {
    public final InitiativeListWithCount initiatives;
    public final InitiativeCounts initiativeCounts;
    public final List<Municipality> municipalities;

    public InitiativeListPageInfo(InitiativeListWithCount initiatives, InitiativeCounts initiativeCounts, List<Municipality> municipalities) {

        this.initiatives = initiatives;
        this.initiativeCounts = initiativeCounts;
        this.municipalities = municipalities;
    }
}
