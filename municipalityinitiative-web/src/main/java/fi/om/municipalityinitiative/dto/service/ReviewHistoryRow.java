package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.util.ReviewHistoryType;
import org.joda.time.DateTime;

import java.util.Optional;

public class ReviewHistoryRow {

    private Long id;
    private ReviewHistoryType type;
    private DateTime created;
    private Optional<String> message;
    private Optional<String> snapshot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReviewHistoryType getType() {
        return type;
    }

    public void setType(ReviewHistoryType type) {
        this.type = type;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public void setMessage(Optional<String> message) {
        this.message = message;
    }

    public Optional<String> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Optional<String> snapshot) {
        this.snapshot = snapshot;
    }
}
