package fi.om.municipalityinitiative.dto;

public class InitiativeCounts {
    public long collecting = 0;
    public long sent = 0;
    public long draft = 0;
    public long accepted = 0;
    public long review = 0;

    public long getCollecting() {
        return collecting;
    }

    public long getSent() {
        return sent;
    }

    public long getAll() {
        return collecting+sent;
    }

    public long getDraft() {
        return draft;
    }

    public long getAccepted() {
        return accepted;
    }

    public long getReview() {
        return review;
    }
}
