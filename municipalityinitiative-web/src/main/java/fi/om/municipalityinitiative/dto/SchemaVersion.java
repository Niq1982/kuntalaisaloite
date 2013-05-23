package fi.om.municipalityinitiative.dto;

import org.joda.time.DateTime;

public class SchemaVersion {

    private String script;
    private DateTime executed;

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setExecuted(DateTime executed) {
        this.executed = executed;
    }

    public DateTime getExecuted() {
        return executed;
    }
}
