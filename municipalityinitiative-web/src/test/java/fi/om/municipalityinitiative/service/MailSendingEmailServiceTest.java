package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class MailSendingEmailServiceTest extends MailSendingEmailServiceTestBase {

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
    public void review_notification_to_moderator_contains_all_information() throws Exception {
        emailService.sendNotificationToModerator(createDefaultInitiative(), Locales.LOCALE_FI, "TEMP_EMAIL@example.com");
          assertThat(getSingleRecipient(), is("TEMP_EMAIL@example.com"));
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

    @Test
    public void participation_confirmation_email_contains_all_information() throws Exception {
        String confirmationCode = "confirmationCode";
        long participantId = 1L;
        String participantEmail = "participant@example.com";

        emailService.sendParticipationConfirmation(createDefaultInitiative(), participantEmail, participantId, confirmationCode, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(participantEmail));
        assertThat(getSingleSentMessage().getSubject(), is("Aloitteeseen osallistumisen vahvistaminen"));

        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getMessageContent().html, containsString(urls.confirmParticipant(participantId, confirmationCode)));
    }

    @Test
    public void single_to_municipality_contains_all_information() throws Exception {

        emailService.sendSingleToMunicipality(createDefaultInitiative(), MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL)); // XXX: MUNICIPALITY_EMAIL
        assertThat(getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(getMessageContent().html, containsString(CONTACT_ADDRESS));
        assertThat(getMessageContent().html, containsString(CONTACT_EMAIL));
        assertThat(getMessageContent().html, containsString(CONTACT_NAME));
        assertThat(getMessageContent().html, containsString(CONTACT_PHONE));
        assertThat(getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
        assertThat(getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(getMessageContent().html, containsString(SENT_COMMENT));
    }

    @Test
    public void author_invitation_contains_all_information() throws Exception {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setEmail("email@example.com");
        authorInvitation.setConfirmationCode("rockrock");
        emailService.sendAuthorInvitation(createDefaultInitiative(), authorInvitation);

        assertThat(getSingleSentMessage().getSubject(), containsString("Sinut on kutsuttu vastuuhenkilöksi kuntalaisaloitteesen"));
        assertThat(getSingleRecipient(), is(authorInvitation.getEmail()));

        assertThat(getMessageContent().html, containsString(urls.invitation(INITIATIVE_ID, authorInvitation.getConfirmationCode())));
        assertThat(getMessageContent().html, containsString(urls.alt().invitation(INITIATIVE_ID, authorInvitation.getConfirmationCode())));

    }

    @Test
    public void collaborative_to_municipality_contains_all_information() throws Exception {
        emailService.sendCollaborativeToMunicipality(createDefaultInitiative(), Lists.<Participant>newArrayList(), MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(getSingleRecipient(), is(MUNICIPALITY_EMAIL));
        assertThat(getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(getMessageContent().html, containsString(SENT_COMMENT));
    }

    @Test
    public void send_invitation_acceptanve() throws Exception {

        emailService.sendAuthorConfirmedInvitation(createDefaultInitiative(), CONTACT_EMAIL, "hash", Locales.LOCALE_FI);
        assertThat(getMessageContent().html, containsString(Urls.get(Locales.LOCALE_FI).loginAuthor(INITIATIVE_ID, "hash")));

    }
}
