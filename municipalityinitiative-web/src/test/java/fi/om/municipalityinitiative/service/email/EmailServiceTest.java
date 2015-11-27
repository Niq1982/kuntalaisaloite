package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.conf.IntegrationTestFakeEmailConfiguration;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.EmailDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.util.EmailAttachmentType;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@Transactional
public class EmailServiceTest extends MailSendingEmailServiceTestBase {

    private static final String MANAGEMENT_HASH = "managementHash";
    public static final String FOLLOWEREMAIL = "test@test.fi";

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    @Test
    public void prepare_initiative_sets_subject_and_login_url() throws Exception {
        emailService.sendPrepareCreatedEmail(initiativeId(), new NormalAuthorId(authorId()), MANAGEMENT_HASH, Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Olet saanut linkin kuntalaisaloitteen tekemiseen Kuntalaisaloite.fi-palvelussa"));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(MANAGEMENT_HASH)));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void sending_new_management_hash_contains_all_information() throws Exception {

        emailService.sendManagementHashRenewed(initiativeId(), MANAGEMENT_HASH, authorId());

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Sinulle on luotu uusi aloitteen ylläpitolinkki Kuntalaisaloite.fi-palvelussa / En ny hanteringslänk har skapats för dig i webbtjänsten Invånarinitiativ.fi"));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(MANAGEMENT_HASH)));
        assertThat(email.getBodyHtml(), containsString(urls.alt().loginAuthor(MANAGEMENT_HASH)));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }
    
    @Test
    public void review_notification_to_moderator_contains_all_information() throws Exception {

        emailService.sendNotificationToModerator(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(IntegrationTestFakeEmailConfiguration.EMAIL_DEFAULT_OM));
        assertThat(email.getSubject(), is("Kuntalaisaloite tarkastettavaksi"));
        
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_PROPOSAL));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_ADDRESS));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_NAME));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_PHONE));
        assertThat(email.getBodyHtml(), containsString(urls.moderatorLogin(initiativeId())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
        
    }

    @Test
    public void participation_confirmation_email_contains_all_information() throws Exception {
        String confirmationCode = "confirmationCode";
        long participantId = 1L;
        String participantEmail = "participant@example.com";

        emailService.sendParticipationConfirmation(initiativeId(), participantEmail, participantId, confirmationCode, Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();
        
        assertThat(email.getRecipientsAsString(), is(participantEmail));
        assertThat(email.getSubject(), is("Aloitteeseen osallistumisen vahvistaminen"));

        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(urls.confirmParticipant(participantId, confirmationCode)));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void single_to_municipality_contains_all_information() throws Exception {

        testHelper.addAttachment(initiativeId(), "accepted", true, "jpg");
        testHelper.addAttachment(initiativeId(), "not accepted", false, "jpg");

        emailService.sendSingleToMunicipality(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
        assertThat(email.getRecipientsAsString(), is(MUNICIPALITY_EMAIL));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_PROPOSAL));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_ADDRESS));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_NAME));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_PHONE));
        assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getBodyHtml(), containsString(EXTRA_INFO));
        assertThat(email.getBodyHtml(), containsString(SENT_COMMENT));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
        assertThat(email.getBodyHtml(), containsString("1 liite"));

    }

    @Test
    public void author_has_been_deleted_email_to_everyone_contains_all_information() throws Exception {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress("Vastuuhenkilön Osoite");
        contactInfo.setEmail("sposti@example.com");
        contactInfo.setName("Nimmii");
        contactInfo.setPhone("Puhnummi");
        emailService.sendAuthorDeletedEmailToOtherAuthors(initiativeId(), contactInfo);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Vastuuhenkilö on poistettu aloitteestasi / Ansvarspersonen har tagits bort från ditt initiativ"));
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(contactInfo.getAddress()));
        assertThat(email.getBodyHtml(), containsString(contactInfo.getEmail()));
        assertThat(email.getBodyHtml(), containsString(contactInfo.getName()));
        assertThat(email.getBodyHtml(), containsString(contactInfo.getPhone()));
        assertThat(email.getBodyHtml(), containsString(urls.loginAuthor(managementHash())));
        assertThat(email.getBodyHtml(), containsString(urls.alt().loginAuthor(managementHash())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void author_has_been_deleted_email_to_author_contains_all_information() throws Exception {
        emailService.sendAuthorDeletedEmailToDeletedAuthor(initiativeId(), AUTHOR_EMAIL);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Sinut on poistettu aloitteen vastuuhenkilöistä / Du har tagits bort som ansvarsperson för initiativet"));
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));

        assertThat(email.getBodyHtml(), containsString("Et ole enää aloitteen vastuuhenkilö"));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
    }

    @Test
    public void author_invitation_contains_all_information() throws Exception {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setEmail("email@example.com");
        authorInvitation.setConfirmationCode("rockrock");
        emailService.sendAuthorInvitation(initiativeId(), authorInvitation);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), containsString("Sinut on kutsuttu vastuuhenkilöksi kuntalaisaloitteeseen"));
        assertThat(email.getRecipientsAsString(), is(authorInvitation.getEmail()));

        assertThat(email.getBodyHtml(), containsString(urls.invitation(initiativeId(), authorInvitation.getConfirmationCode())));
        assertThat(email.getBodyHtml(), containsString(urls.alt().invitation(initiativeId(), authorInvitation.getConfirmationCode())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));

    }

    @Test
    public void collaborative_to_municipality_contains_all_information() throws Exception {

        testHelper.addAttachment(initiativeId(), "accepted", true, "jpg");
        testHelper.addAttachment(initiativeId(), "not accepted", false, "jpg");

        emailService.sendCollaborativeToMunicipality(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Kuntalaisaloite: "+ INITIATIVE_NAME));
        assertThat(email.getRecipientsAsString(), is(MUNICIPALITY_EMAIL));
        assertThat(email.getBodyHtml(), containsString(EXTRA_INFO));
        assertThat(email.getBodyHtml(), containsString(SENT_COMMENT));

        assertThat(email.getBodyHtml(), containsString(AUTHOR_NAME));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_ADDRESS));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_PHONE));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.PARTICIPANTS));
        assertThat(email.getBodyHtml(), containsString("1 liite"));
    }

    @Test
    public void collaborative_to_authors_contains_all_information() throws Exception {

        testHelper.addAttachment(initiativeId(), "accepted", true, "jpg");
        testHelper.addAttachment(initiativeId(), "not accepted", false, "jpg");

        emailService.sendCollaborativeToAuthors(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Aloite on lähetetty kuntaan"));
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(EXTRA_INFO));
        assertThat(email.getBodyHtml(), containsString(SENT_COMMENT));

        assertThat(email.getBodyHtml(), containsString(AUTHOR_NAME));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_EMAIL));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_ADDRESS));
        assertThat(email.getBodyHtml(), containsString(AUTHOR_PHONE));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.PARTICIPANTS));
        assertThat(email.getBodyHtml(), containsString("1 liite"));
    }

    @Test
    public void collaborative_to_municipality_to_followers() throws Exception{

        String removeHash = testHelper.addFollower(initiativeId(), FOLLOWEREMAIL);

        emailService.sendCollaborativeToMunicipalityToFollowers(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), containsString(FOLLOWEREMAIL));
        assertThat(email.getBodyHtml(), containsString(removeHash));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));
        assertThat(email.getBodyHtml(), not(containsString(SENT_COMMENT)));
        assertThat(email.getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));

    }



    @Test
    public void collaborative_to_authors_has_no_information_of_attachments_if_has_not_any() {
        emailService.sendCollaborativeToAuthors(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();
        assertThat(email.getBodyHtml(), not(containsString("liitetiedosto")));

    }

    @Test
    public void send_invitation_acceptance() throws Exception {

        emailService.sendAuthorConfirmedInvitation(initiativeId(), AUTHOR_EMAIL, "hash", Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();
        assertThat(email.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).loginAuthor("hash")));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));

    }

    @Test
    public void send_author_message_confirmation_contains_all_information() throws Exception {
        AuthorMessage authorMessage = authorMessage();
        String confirmationCode = "conf-code";
        authorMessage.setConfirmationCode(confirmationCode);
        emailService.sendAuthorMessageConfirmationEmail(initiativeId(), authorMessage, Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(authorMessage.getContactEmail()));
        assertThat(email.getSubject(), is("Olet lähettämässä viestiä aloitteen vastuuhenkilöille"));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getMessage()));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getContactEmail()));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getContactName()));
        assertThat(email.getBodyHtml(), containsString(urls.confirmAuthorMessage(confirmationCode)));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));

    }

    @Test
    public void send_message_to_authors_contains_all_information() throws Exception {

        AuthorMessage authorMessage = authorMessage();

        emailService.sendAuthorMessages(initiativeId(), authorMessage);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Olet saanut yhteydenoton aloitteeseesi liittyen / Du har kontaktats gällande ditt initiativ"));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getContactEmail()));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getContactName()));
        assertThat(email.getBodyHtml(), containsString(authorMessage.getMessage()));
        assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getBodyHtml(), containsString(urls.alt().view(initiativeId())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));

    }

    @Test
    public void verified_initiative_created_contains_all_information() throws Exception {
        emailService.sendVeritiedInitiativeManagementLink(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));
        assertThat(email.getSubject(), is("Aloiteluonnoksesi on tallennettu Kuntalaisaloite.fi-palveluun"));
        assertThat(email.getBodyHtml(), containsString(Urls.get(Locales.LOCALE_FI).loginToManagement(initiativeId())));
        assertThat(email.getAttachmentType(), is(EmailAttachmentType.NONE));

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
