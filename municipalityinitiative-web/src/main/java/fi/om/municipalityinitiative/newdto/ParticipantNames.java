package fi.om.municipalityinitiative.newdto;

import com.google.common.collect.Lists;

import java.util.List;

public class ParticipantNames {

    private List<String> franchise = Lists.newArrayList();
    private List<String> noFranchise = Lists.newArrayList();

    public List<String> getFranchise() {
        return franchise;
    }

    public List<String> getNoFranchise() {
        return noFranchise;
    }
}
