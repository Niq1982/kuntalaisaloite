package fi.om.municipalityinitiative.dto;

public class InitiativeCounts {
    public int collecting = 0;
    public int sent = 0;

    public int getCollecting() {
        return collecting;
    }

    public int getSent() {
        return sent;
    }

    public int getAll() {
        return collecting+sent;
    }
}
