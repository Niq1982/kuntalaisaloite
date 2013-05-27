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
        emailService.sendStatusEmail(createDefaultInitiative(), AUTHOR_EMAILS, MUNICIPALITY_EMAIL, EmailMessageType.ACCEPTED_BY_OM);

        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on hyväksytty"));
        assertThat(getMessageContent().html, containsString(MODERATOR_COMMENT));
    }

    @Test
    public void om_accept_initiative_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), AUTHOR_EMAILS, MUNICIPALITY_EMAIL, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on hyväksytty ja lähetetty kuntaan"));
        assertThat(getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
        assertThat(getMessageContent().html, containsString("Kuntalaisaloitteesi on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan"));
        // TODO: assertThat(getMessageContent().html, containsString("SV Kuntalaisaloitteesi on julkaistu Kuntalaisaloite.fi-palvelussa ja lähetetty kuntaan"));
    }

    @Test
    public void om_reject_initiative_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), AUTHOR_EMAILS, MUNICIPALITY_EMAIL, EmailMessageType.REJECTED_BY_OM);

        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloitteesi on palautettu korjattavaksi"));
        assertThat(getMessageContent().html, containsString(MODERATOR_COMMENT));
    }

    @Test
    public void author_publish_and_start_collecting_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), AUTHOR_EMAILS, MUNICIPALITY_EMAIL, EmailMessageType.PUBLISHED_COLLECTING);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getSingleSentMessage().getSubject(), is("Aloitteesi on julkaistu ja siihen kerätään osallistujia Kuntalaisaloite.fi-palvelussa"));
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
    }

    @Test
    public void author_publish_and_send_to_municipality_sets_subject_and_contains_all_information() throws Exception {
        emailService.sendStatusEmail(createDefaultInitiative(), AUTHOR_EMAILS, MUNICIPALITY_EMAIL, EmailMessageType.SENT_TO_MUNICIPALITY);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getSingleSentMessage().getSubject(), is("Aloitteesi on lähetetty kuntaan"));
    }


}
