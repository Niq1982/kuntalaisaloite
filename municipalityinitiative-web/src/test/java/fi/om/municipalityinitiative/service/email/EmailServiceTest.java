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
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@Transactional
public class EmailServiceTest extends MailSendingEmailServiceTestBase {

    private static final String MANAGEMENT_HASH = "managementHash";
    public static final String FOLLOWEREMAIL = "test@test.fi";
    public static final String MUNICIPALITY_MANAGEMENT_HASH = "hashahs";

    private Urls urls;

    @Before
    public void setup() {
        super.setup();
        urls = Urls.get(Locales.LOCALE_FI);
    }

    private static final String EMAIL_TEMP_DIR = "target/test-emails/";

    @BeforeClass
    public static void lol() throws IOException {
        FileUtils.forceMkdir(new File(EMAIL_TEMP_DIR));
        FileUtils.cleanDirectory(new File(EMAIL_TEMP_DIR));
    }

    @After
    public void printEmail() {
        EmailDto singleQueuedEmail = testHelper.getSingleQueuedEmail();

        File file = new File(EMAIL_TEMP_DIR
                + singleQueuedEmail.getEmailId()
                + "_"
                + singleQueuedEmail.getSubject().replace("/", " - ")
                + ".html");
        FileUtil.writeAsString(file, singleQueuedEmail.getBodyHtml());
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


        municipalityUserDao.assignMunicipalityUser(initiativeId(), MUNICIPALITY_MANAGEMENT_HASH);

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

        assertThat(email.getBodyHtml(), containsString(urls.loginMunicipality(MUNICIPALITY_MANAGEMENT_HASH)));

        assertThat(email.getBodyHtml(), not(containsString("video")));

    }
    @Test
    public void single_to_municipality_contains_all_information_video() throws Exception {

        testHelper.addVideo(initiativeId(), "randomString", "randomString");

        municipalityUserDao.assignMunicipalityUser(initiativeId(), MUNICIPALITY_MANAGEMENT_HASH);

        emailService.sendSingleToMunicipality(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getBodyHtml(), containsString("video"));

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
    public void municipality_answers_contains_all_information() {
        emailService.sendMunicipalityDecisionToAuthors(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), containsString("Kunta on vastannut tekemääsi kuntalaisaloitteeseen"));
        assertThat(email.getSubject(), containsString("Kommunen har svarat på ditt invånarinitiativ"));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getRecipientsAsString(), is(AUTHOR_EMAIL));

    }

    @Test
    public void municipality_answers_to_followers_contains_all_information() {

        String removeHash = testHelper.addFollower(initiativeId(), FOLLOWEREMAIL);

        emailService.sendMunicipalityDecisionToFollowers(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), containsString("Kunta on vastannut tekemääsi kuntalaisaloitteeseen"));
        assertThat(email.getSubject(), containsString("Kommunen har svarat på ditt invånarinitiativ"));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString(urls.view(initiativeId())));
        assertThat(email.getRecipientsAsString(), is(FOLLOWEREMAIL));
        assertThat(email.getBodyHtml(), containsString(removeHash));

    }


    @Test
    public void collaborative_to_municipality_contains_all_information() throws Exception {

        testHelper.addAttachment(initiativeId(), "accepted", true, "jpg");
        testHelper.addAttachment(initiativeId(), "not accepted", false, "jpg");

        municipalityUserDao.assignMunicipalityUser(initiativeId(), MUNICIPALITY_MANAGEMENT_HASH);

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

        assertThat(email.getBodyHtml(), containsString(urls.loginMunicipality(MUNICIPALITY_MANAGEMENT_HASH)));

        assertThat(email.getBodyHtml(), not(containsString("video")));
    }

    @Test
    public void collaborative_to_municipality_contains_all_information_video() throws Exception {

        testHelper.addVideo(initiativeId(), "randomString", "randomString");

        municipalityUserDao.assignMunicipalityUser(initiativeId(), MUNICIPALITY_MANAGEMENT_HASH);

        emailService.sendCollaborativeToMunicipality(initiativeId(), Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getBodyHtml(), containsString("video"));
    }


    @Test
    public void collaborative_to_authors_contains_all_information() throws Exception {

        testHelper.addAttachment(initiativeId(), "accepted", true, "jpg");
        testHelper.addAttachment(initiativeId(), "not accepted", false, "jpg");

        municipalityUserDao.assignMunicipalityUser(initiativeId(), MUNICIPALITY_MANAGEMENT_HASH);

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


        assertThat(email.getBodyHtml(), not(containsString(urls.loginMunicipality(MUNICIPALITY_MANAGEMENT_HASH))));
    }

    @Test
    public void send_confirm_to_followers_contains_all_information() {
        String removeHash = testHelper.addFollower(initiativeId(), FOLLOWEREMAIL);

        emailService.sendConfirmToFollower(initiativeId(), FOLLOWEREMAIL, removeHash, Locales.LOCALE_FI);

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getSubject(), is("Olet tilannut aloitteen sähköpostitiedotteet"));

        assertThat(email.getRecipientsAsString(), containsString(FOLLOWEREMAIL));
        assertThat(email.getBodyHtml(), containsString(removeHash));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_NAME));
        assertThat(email.getBodyHtml(), containsString(INITIATIVE_MUNICIPALITY));
        assertThat(email.getBodyHtml(), containsString("Olet tilannut aloitteen sähköpostitiedotteet"));
        assertThat(email.getBodyHtml(), containsString("Saat viestin sähköpostiisi kun aloite lähetetään kuntaan käsitelväksi ja jos kunta vastaa aloitteeseen palvelussa"));

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
        assertThat(email.getSubject(), is("Aloite toimitettu kuntaan / Initiativet har skickats till kommunen"));

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

    @Test
    public void sends_municipality_login_email() {


        String managementHash = randomAlphabetic(40);
        String loginHash = randomAlphabetic(40);

        municipalityUserDao.assignMunicipalityUser(initiativeId(), managementHash);
        municipalityUserDao.assignMunicipalityUserLoginHash(
                initiativeId(),
                managementHash,
                loginHash,
                DateTime.now()
        );

        emailService.sendLoginLinkToMunicipality(initiativeId());

        EmailDto email = testHelper.getSingleQueuedEmail();

        assertThat(email.getRecipientsAsString(), is(MUNICIPALITY_EMAIL));
        assertThat(email.getSubject(), is("Kuntalaisaloitteeseen vastaaminen / Att svara på invånarinitiativet"));
        assertThat(email.getBodyHtml(), containsString(
                Urls.get(Locales.LOCALE_FI).municipalityLogin(managementHash, loginHash)
                .replace("&", "&amp;")
        ));

        assertThat(email.getBodyHtml(), containsString(
                Urls.get(Locales.LOCALE_SV).municipalityLogin(managementHash, loginHash)
                        .replace("&", "&amp;")
        ));



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
