package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.util.Maybe;

public class EmailSettings {

    private final Maybe<String> testSendTo;
    private final String defaultReplyTo;
    private final boolean testConsoleOutput;
    private String moderatorEmail;

    public EmailSettings(String defaultReplyTo, Maybe<String> testSendTo, boolean testConsoleOutput, String moderatorEmail) {
        this.defaultReplyTo = defaultReplyTo;
        this.testSendTo = testSendTo;
        this.testConsoleOutput = testConsoleOutput;
        this.moderatorEmail = moderatorEmail;
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

    public String getModeratorEmail() {
        return moderatorEmail;
    }
}
