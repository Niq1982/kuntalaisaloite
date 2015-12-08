package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.util.Maybe;

public class EnvironmentSettings {

    private final Maybe<String> testSendTo;
    private final String defaultReplyTo;
    private final boolean testConsoleOutput;
    private final boolean testSendMunicipalityEmailsToAuthor;
    private final String moderatorEmail;
    private final boolean testSendModeratorEmailsToAuthor;
    private final boolean enableVerifiedInitiatives;
    private final boolean isTestEmailSender;
    private final String googleMapsApiKey;
    private final boolean googleMapsEnabled;
    private final boolean superSearchEnabled;
    private String appEnvironment;


    public EnvironmentSettings(String defaultReplyTo,
                               Maybe<String> testSendTo,
                               boolean testConsoleOutput,
                               String moderatorEmail,
                               boolean testSendMunicipalityEmailsToAuthor,
                               boolean testSendModeratorEmailsToAuthor,
                               boolean enableVerifiedInitiatives,
                               boolean isTestEmailSender,
                               String googleMapsApiKey,
                               boolean googleMapsEnabled,
                               boolean superSearchEnabled,
                               String appEnvironment) {
        this.defaultReplyTo = defaultReplyTo;
        this.testSendTo = testSendTo;
        this.testConsoleOutput = testConsoleOutput;
        this.moderatorEmail = moderatorEmail;
        this.testSendMunicipalityEmailsToAuthor = testSendMunicipalityEmailsToAuthor;
        this.testSendModeratorEmailsToAuthor = testSendModeratorEmailsToAuthor;
        this.enableVerifiedInitiatives = enableVerifiedInitiatives;
        this.isTestEmailSender = isTestEmailSender;
        this.googleMapsApiKey = googleMapsApiKey;
        this.googleMapsEnabled = googleMapsEnabled;
        this.superSearchEnabled = superSearchEnabled;
        this.appEnvironment = appEnvironment;
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

    public boolean isTestSendMunicipalityEmailsToAuthor() {
        return testSendMunicipalityEmailsToAuthor;
    }

    public boolean isTestSendModeratorEmailsToAuthor() {
        return testSendModeratorEmailsToAuthor;
    }

    public boolean isEnableVerifiedInitiatives() {
        return enableVerifiedInitiatives;
    }

    public boolean isTestEmailSender() {
        return isTestEmailSender;
    }

    public boolean hasAnyTestOptionsEnabled() {
        return testConsoleOutput
                || testSendTo.isPresent()
                || testSendModeratorEmailsToAuthor
                || testSendMunicipalityEmailsToAuthor
                || isTestEmailSender;


    }

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }

    public boolean isGoogleMapsEnabled() {
        return googleMapsEnabled;
    }

    public boolean isSuperSearchEnabled() {
        return superSearchEnabled;
    }

    public String getAppEnvironment() {
        return appEnvironment;
    }
}
