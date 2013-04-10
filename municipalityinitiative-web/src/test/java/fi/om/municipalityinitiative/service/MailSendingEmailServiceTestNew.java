package fi.om.municipalityinitiative.service;

import javax.mail.MessagingException;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class MailSendingEmailServiceTestNew extends MailSendingEmailServiceTestBase {

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    @Test
    public void prepare_initiative_sets_subject_and_login_url() throws Exception {
        emailService.sendPrepareCreatedEmail(createDefaultInitiative(), CONTACT_EMAIL, Locales.LOCALE_FI);

        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Olet saanut linkin kuntalaisaloitteen tekemiseen Kuntalaisaloite.fi-palvelussa"));
        assertThat(getMessageContent().html, containsString(urls.loginAuthor(INITIATIVE_ID, MANAGEMENT_HASH)));
    }
    
    @Test
    public void collectable_notification_to_moderator() throws Exception {
        emailService.sendNotificationToModerator(createDefaultInitiative(), Locales.LOCALE_FI);
          assertThat(getSingleRecipient(), is(CONTACT_EMAIL)); 
//        assertThat(getSingleRecipient(), is(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM)); // XXX: Restore this when we want to send emails to om
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite tarkastettavaksi"));
        
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(getMessageContent().html, containsString(CONTACT_ADDRESS));
        assertThat(getMessageContent().html, containsString(CONTACT_EMAIL));
        assertThat(getMessageContent().html, containsString(CONTACT_NAME));
        assertThat(getMessageContent().html, containsString(CONTACT_PHONE));
        assertThat(getMessageContent().html, containsString(urls.moderation(INITIATIVE_ID)));
        
    }
}
