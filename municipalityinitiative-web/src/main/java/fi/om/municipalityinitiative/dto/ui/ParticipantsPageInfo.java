package fi.om.municipalityinitiative.dto.ui;

import java.util.List;

public class ParticipantsPageInfo {

    public final InitiativeViewInfo initiative;
    public final List<ParticipantListInfo> participants;

    public ParticipantsPageInfo(InitiativeViewInfo initiative, List<ParticipantListInfo> participants) {
        this.initiative = initiative;
        this.participants = participants;
    }
}
