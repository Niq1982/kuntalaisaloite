package fi.om.municipalityinitiative.conf;

public final class PropertyNames {

    private PropertyNames() {}

    public static final String omImageDirection = "om.image.directory";

    public static final String omUserSalt = "om.user.salt";

    public static final String baseURL = "app.baseURL"; 
    
    public static final String registeredUserSecret = "security.registeredUserSecret";
    
    
    public static final String jdbcDriver = "jdbc.driver";
    
    public static final String jdbcURL = "jdbc.url";
    
    public static final String jdbcUser = "jdbc.user";
    
    public static final String jdbcPassword = "jdbc.password";
    
   
    public static final String vetumaURL = "vetuma.url";
    
    public static final String vetumaSharedSecret = "vetuma.sharedSecret";
    
    public static final String vetumaRCVID = "vetuma.rcvid";

    public static final String vetumaSO = "vetuma.so";

    public static final String vetumaSOLIST = "vetuma.solist";

    public static final String vetumaAP = "vetuma.ap";

    public static final String vetumaAPPNAME = "vetuma.appname";

    public static final String vetumaAPPID = "vetuma.appid";
    
    
    public static final String emailSmtpServer = "email.smtp.server";

    public static final String emailSmtpServerPort = "email.smtp.server.port";
    
    public static final String emailDefaultReplyTo = "email.default.reply-to";
    
    public static final String emailSendToOM = "email.send-to.om";
    
    public static final String errorFeedbackEmail = "error.feedbackEmail";

    
    public static final String testEmailSendTo = "test.email.send-to";
    
    public static final String testEmailConsoleOutput = "test.email.consoleOutput";

    public static final String testEmailMunicipalityEmailsToAuthor = "test.email.municipality.emails.to.author";

    public static final String testMessageSourceCacheSeconds = "test.messageSourceCacheSeconds";

    public static final String testFreemarkerShowErrorsOnPage = "test.freemarker.showErrorsOnPage";

    public static final String optimizeResources = "app.optimizeResources";

    public static final String resourcesVersion = "app.resourcesVersion";

    public static final String omPiwicId = "om.piwic.id";

    public static final String appVersion = "appVersion";
}
