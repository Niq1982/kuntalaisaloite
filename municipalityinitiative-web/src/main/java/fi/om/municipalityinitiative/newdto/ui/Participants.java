package fi.om.municipalityinitiative.newdto.ui;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.service.Participant;

import java.util.List;

public class Participants {

    private List<Participant> franchise = Lists.newArrayList();
    private List<Participant> noFranchise = Lists.newArrayList();

    public List<Participant> getFranchise() {
        return franchise;
    }

    public List<Participant> getNoFranchise() {
        return noFranchise;
    }
}
