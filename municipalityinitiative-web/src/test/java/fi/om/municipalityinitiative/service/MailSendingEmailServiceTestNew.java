package fi.om.municipalityinitiative.service;

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
}
