package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReviewHistoryType;
import org.joda.time.DateTime;

public class ReviewHistoryRow {

    private ReviewHistoryType type;
    private DateTime created;
    private Maybe<String> message;
    private Maybe<String> snapshot;

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

    public Maybe<String> getMessage() {
        return message;
    }

    public void setMessage(Maybe<String> message) {
        this.message = message;
    }

    public Maybe<String> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Maybe<String> snapshot) {
        this.snapshot = snapshot;
    }
}
