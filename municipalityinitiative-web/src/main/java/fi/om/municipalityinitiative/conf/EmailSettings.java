package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.util.Maybe;

public class EmailSettings {

    private final Maybe<String> testSendTo;
    private final String defaultReplyTo;
    private final boolean testConsoleOutput;

    public EmailSettings(String defaultReplyTo, Maybe<String> testSendTo, boolean testConsoleOutput) {
        this.defaultReplyTo = defaultReplyTo;
        this.testSendTo = testSendTo;
        this.testConsoleOutput = testConsoleOutput;
    }

    public String getDefaultReplyTo() {
        return defaultReplyTo;
    }

    public Maybe<String> getTestSendTo() {
        return testSendTo;
    }

    public boolean isTestConsoleOutput() {
        return testConsoleOutput;
    }
}
