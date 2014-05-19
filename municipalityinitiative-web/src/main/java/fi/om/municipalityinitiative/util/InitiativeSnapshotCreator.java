package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.dto.service.Initiative;

public class InitiativeSnapshotCreator {

    public static String create(Initiative initiative) {
        return initiative.getName()
                + "\n\n"
                + initiative.getProposal()
                + "\n\n"
                + initiative.getExtraInfo();
    }
}
