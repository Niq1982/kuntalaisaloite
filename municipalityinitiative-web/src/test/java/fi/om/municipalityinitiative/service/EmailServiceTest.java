package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class EmailServiceTest extends MailSendingEmailServiceTestBase {

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    @Test
    public void prepare_initiative_sets_subject_and_login_url() throws Exception {
        emailService.sendPrepareCreatedEmail(initiativeId(), authorId(), MANAGEMENT_HASH, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Olet saanut linkin kuntalaisaloitteen tekemiseen Kuntalaisaloite.fi-palvelussa"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(MANAGEMENT_HASH)));
    }

    @Test
    public void sending_new_management_hash_contains_all_information() throws Exception {

        emailService.sendManagementHashRenewed(initiativeId(), MANAGEMENT_HASH, authorId());

        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Sinulle on luotu uusi aloitteen ylläpitolinkki Kuntalaisaloite.fi-palvelussa"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.loginAuthor(MANAGEMENT_HASH)));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
    }
    
    @Test
    public void review_notification_to_moderator_contains_all_information() throws Exception {

        emailService.sendNotificationToModerator(initiativeId());
        assertThat(javaMailSenderFake.getSingleRecipient(), is(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite tarkastettavaksi"));
        
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_PHONE));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.moderation(initiativeId())));
        
    }

    @Test
    public void participation_confirmation_email_contains_all_information() throws Exception {
        String confirmationCode = "confirmationCode";
        long participantId = 1L;
        String participantEmail = "participant@example.com";

        emailService.sendParticipationConfirmation(initiativeId(), participantEmail, participantId, confirmationCode, Locales.LOCALE_FI);
        assertThat(javaMailSenderFake.getSingleRecipient(), is(participantEmail));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Aloitteeseen osallistumisen vahvistaminen"));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.confirmParticipant(participantId, confirmationCode)));
    }

    @Test
    public void single_to_municipality_contains_all_information() throws Exception {

        emailService.sendSingleToMunicipality(initiativeId(), Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(MUNICIPALITY_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_PROPOSAL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_PHONE));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.view(initiativeId())));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(SENT_COMMENT));
    }

    @Test
    public void author_has_been_deleted_email_to_everyone_contains_all_information() throws Exception {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress("Vastuuhenkilön Osoite");
        contactInfo.setEmail("sposti@example.com");
        contactInfo.setName("Nimmii");
        contactInfo.setPhone("Puhnummi");
        emailService.sendAuthorDeletedEmailToOtherAuthors(initiativeId(), contactInfo);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Vastuuhenkilö on poistettu aloitteestasi"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(contactInfo.getAddress()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(contactInfo.getEmail()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(contactInfo.getName()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(contactInfo.getPhone()));
    }

    @Test
    public void author_has_been_deleted_email_to_author_contains_all_information() throws Exception {
        emailService.sendAuthorDeletedEmailToDeletedAuthor(initiativeId(), AUTHOR_EMAIL);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Sinut on poistettu aloitteen vastuuhenkilöistä"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString("Et ole enää aloitteen vastuuhenkilö"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(INITIATIVE_MUNICIPALITY));
    }

    @Test
    public void author_invitation_contains_all_information() throws Exception {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setEmail("email@example.com");
        authorInvitation.setConfirmationCode("rockrock");
        emailService.sendAuthorInvitation(initiativeId(), authorInvitation);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), containsString("Sinut on kutsuttu vastuuhenkilöksi kuntalaisaloitteeseen"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(authorInvitation.getEmail()));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.invitation(initiativeId(), authorInvitation.getConfirmationCode())));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(urls.alt().invitation(initiativeId(), authorInvitation.getConfirmationCode())));

    }

    @Test
    public void collaborative_to_municipality_contains_all_information() throws Exception {
        emailService.sendCollaborativeToMunicipality(initiativeId(), Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(MUNICIPALITY_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(SENT_COMMENT));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_PHONE));
    }

    @Test
    public void collaborative_to_authors_contains_all_information() throws Exception {
        emailService.sendCollaborativeToAuthors(initiativeId());

        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(EXTRA_INFO));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(SENT_COMMENT));

        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_NAME));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_ADDRESS));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(AUTHOR_PHONE));
    }

    @Test
    public void send_invitation_acceptance() throws Exception {

        emailService.sendAuthorConfirmedInvitation(initiativeId(), AUTHOR_EMAIL, "hash", Locales.LOCALE_FI);
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(Urls.get(Locales.LOCALE_FI).loginAuthor("hash")));

    }

    @Test
    public void send_author_message_confirmation_contains_all_information() throws Exception {
        AuthorMessage authorMessage = authorMessage();
        authorMessage.setConfirmationCode("conf-code");
        emailService.sendAuthorMessageConfirmationEmail(initiativeId(), authorMessage, Locales.LOCALE_FI);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(authorMessage.getContactEmail()));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Olet lähettämässä viestiä aloitteen vastuuhenkilöille"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getMessage()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getContactEmail()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getContactName()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(Urls.get(Locales.LOCALE_FI).confirmAuthorMessage("conf-code")));

    }

    @Test
    public void send_message_to_authors_contains_all_information() throws Exception {

        AuthorMessage authorMessage = authorMessage();

        emailService.sendAuthorMessages(initiativeId(), authorMessage);

        assertThat(javaMailSenderFake.getSingleRecipient(), is(AUTHOR_EMAIL));
        assertThat(javaMailSenderFake.getSingleSentMessage().getSubject(), is("Olet saanut yhteydenoton aloitteeseesi liittyen / Samma på svenska"));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getContactEmail()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getContactName()));
        assertThat(javaMailSenderFake.getMessageContent().html, containsString(authorMessage.getMessage()));

    }

    private static AuthorMessage authorMessage() {
        String contactorEmail = "someContactor@example.com";
        String contactName = "Contact Name";
        String message = "This is the message";

        AuthorMessage authorMessage = new AuthorMessage();
        authorMessage.setContactEmail(contactorEmail);
        authorMessage.setContactName(contactName);
        authorMessage.setMessage(message);
        return authorMessage;
    }
}
