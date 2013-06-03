package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.Participant;
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
        emailService.sendPrepareCreatedEmail(createDefaultInitiative(), AUTHOR_ID, MANAGEMENT_HASH, CONTACT_EMAIL, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Olet saanut linkin kuntalaisaloitteen tekemiseen Kuntalaisaloite.fi-palvelussa"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(MANAGEMENT_HASH)));
    }

    @Test
    public void sending_new_management_hash_contains_all_information() throws Exception {

        emailService.sendManagementHashRenewed(createDefaultInitiative(), MANAGEMENT_HASH, CONTACT_EMAIL);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Sinulle on luotu uusi aloitteen hallintalinkki Kuntalaisaloite.fi-palvelussa"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(MANAGEMENT_HASH)));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
    }
    
    @Test
    public void review_notification_to_moderator_contains_all_information() throws Exception {
        emailService.sendNotificationToModerator(createDefaultInitiative(),defaultAuthors(), "TEMP_EMAIL@example.com");
          assertThat(javaMailSenderFake.getSingleRecipient(), is("TEMP_EMAIL@example.com"));
//        assertThat(getSingleRecipient(), is(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM)); // XXX: Restore this when we want to send emails to om
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite tarkastettavaksi"));
        
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_PHONE));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.moderation(INITIATIVE_ID)));
        
    }

    @Test
    public void participation_confirmation_email_contains_all_information() throws Exception {
        String confirmationCode = "confirmationCode";
        long participantId = 1L;
        String participantEmail = "participant@example.com";

        emailService.sendParticipationConfirmation(createDefaultInitiative(), participantEmail, participantId, confirmationCode, Locales.LOCALE_FI);
        assertThat(javaMailSenderFake.getSingleRecipient(), is(participantEmail));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Aloitteeseen osallistumisen vahvistaminen"));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.confirmParticipant(participantId, confirmationCode)));
    }

    @Test
    public void single_to_municipality_contains_all_information() throws Exception {

        emailService.sendSingleToMunicipality(createDefaultInitiative(), defaultAuthors(), MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(MUNICIPALITY_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_PHONE));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.view(INITIATIVE_ID)));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(SENT_COMMENT));
    }

    @Test
    public void author_has_been_deleted_email_to_everyone_contains_all_information() throws Exception {
        emailService.sendAuthorDeletedEmailToOtherAuthors(createDefaultInitiative(), AUTHOR_EMAILS, contactInfo());

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Vastuuhenkilö on poistettu aloitteestasi"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(CONTACT_PHONE));
    }

    @Test
    public void author_has_been_deleted_email_to_author_contains_all_information() throws Exception {
        emailService.sendAuthorDeletedEmailToDeletedAuthor(createDefaultInitiative(), CONTACT_EMAIL);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Sinut on poistettu aloitteen vastuuhenkilöistä"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(CONTACT_EMAIL));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString("Et ole enää aloitteen vastuuhenkilö"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
    }

    @Test
    public void author_invitation_contains_all_information() throws Exception {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setEmail("email@example.com");
        authorInvitation.setConfirmationCode("rockrock");
        emailService.sendAuthorInvitation(createDefaultInitiative(), authorInvitation);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), containsString("Sinut on kutsuttu vastuuhenkilöksi kuntalaisaloitteeseen"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(authorInvitation.getEmail()));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.invitation(INITIATIVE_ID, authorInvitation.getConfirmationCode())));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.alt().invitation(INITIATIVE_ID, authorInvitation.getConfirmationCode())));

    }

    @Test
    public void collaborative_to_municipality_contains_all_information() throws Exception {
        emailService.sendCollaborativeToMunicipality(createDefaultInitiative(), defaultAuthors(), Lists.<Participant>newArrayList(), MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(MUNICIPALITY_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(SENT_COMMENT));
    }

    @Test
    public void send_invitation_acceptance() throws Exception {

        emailService.sendAuthorConfirmedInvitation(createDefaultInitiative(), CONTACT_EMAIL, "hash", Locales.LOCALE_FI);
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(Urls.get(Locales.LOCALE_FI).loginAuthor("hash")));

    }

    @Test
    public void send_author_message_confirmation_contains_all_information() throws Exception {
        String confirmationCode = "conf-code";
        emailService.sendAuthorMessageConfirmationEmail(createDefaultInitiative(), CONTACT_EMAIL, confirmationCode, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Olet lähettämässä viestiä aloitteen vastuuhenkilöille"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(Urls.get(Locales.LOCALE_FI).confirmAuthorMessage(confirmationCode)));

    }
}
