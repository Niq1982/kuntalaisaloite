package fi.om.municipalityinitiative.dto.ui;

import java.util.List;

public class InitiativeListWithCount {

    public final List<InitiativeListInfo> list;
    public final long count;

    public InitiativeListWithCount(List<InitiativeListInfo> list, long count) {
        this.list = list;
        this.count = count;
    }
}
