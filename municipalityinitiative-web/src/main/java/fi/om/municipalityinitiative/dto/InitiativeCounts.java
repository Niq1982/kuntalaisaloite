package fi.om.municipalityinitiative.dto;

public class InitiativeCounts {
    public long collecting = 0;
    public long sent = 0;

    public long getCollecting() {
        return collecting;
    }

    public long getSent() {
        return sent;
    }

    public long getAll() {
        return collecting+sent;
    }
}
