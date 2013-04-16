package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class MailSendingEmailServiceStatusTest extends MailSendingEmailServiceTestBase {

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    @Test
    public void om_accept_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), CONTACT_EMAIL, EmailMessageType.ACCEPTED_BY_OM, Locales.LOCALE_FI);

        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite on hyväksytty"));
        assertThat(getMessageContent().html, containsString(urls.loginAuthor(INITIATIVE_ID, MANAGEMENT_HASH)));
        assertThat(getMessageContent().html, containsString(MODERATOR_COMMENT));
    }

    @Test
    public void om_accept_initiative_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), CONTACT_EMAIL, EmailMessageType.ACCEPTED_BY_OM_AND_SENT, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite on hyväksytty ja lähetetty kuntaan"));
        assertThat(getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
    }

    @Test
    public void om_reject_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), CONTACT_EMAIL, EmailMessageType.REJECTED_BY_OM, Locales.LOCALE_FI);

        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite on palautettu korjattavaksi"));
        assertThat(getMessageContent().html, containsString(urls.loginAuthor(INITIATIVE_ID, MANAGEMENT_HASH)));
        assertThat(getMessageContent().html, containsString(MODERATOR_COMMENT));
    }

    @Test
    public void author_publish_and_start_collecting_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), CONTACT_EMAIL, EmailMessageType.PUBLISHED_COLLECTING, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on julkaistu Kuntalaisaloite.fi-palvelussa ja siihen voi osallistua"));
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
    }

    @Test
    public void author_publish_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), CONTACT_EMAIL, EmailMessageType.SENT_TO_MUNICIPALITY, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }


}
