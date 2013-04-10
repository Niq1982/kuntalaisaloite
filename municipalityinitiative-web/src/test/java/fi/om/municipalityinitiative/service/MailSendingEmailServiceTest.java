package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;

import javax.mail.MessagingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Ignore ("Until InitiativeEmailInfo is removed")
public class MailSendingEmailServiceTest extends MailSendingEmailServiceTestBase{

    // Not Collectable

    @Test
    public void notCollectable_to_municipality_assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(MUNICIPALITY_EMAIL));
    }

    @Test
    public void notCollectable_to_municipality_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
    }

    @Test
    public void notCollectable_to_municipality_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void notCollectable_to_municipality_uses_localizations_at_content() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiativeEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    @Ignore("Un-comment implementation")
    public void notCollectable_to_municipality_assigns_senders_email_to_repllyTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getReplyTo()[0].toString(), is(CONTACT_EMAIL));

    }

    @Test
    public void notCollectable_to_author_assign_author_email_to_sendTo_field() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
    }

    @Test
    public void notCollectable_to_author_reads_subject_to_email() throws InterruptedException, MessagingException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }

    @Test
    public void notCollectable_to_author_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiative, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void notCollectable_to_author_uses_finnish_localization() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
    }

    @Test
    public void notCollectable_to_author_uses_sv_localization_localization() throws Exception {
        InitiativeEmailInfo initiativeEmailInfo = createEmailInfo();
        emailService.sendNotCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_SV);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Kontaktuppgifter"));
    }
    
    // Collectable

    @Test
    public void collectable_to_municipality_assigns_municipality_email_to_sendTo_field() throws MessagingException, InterruptedException {
        InitiativeEmailInfo initiative = createEmailInfo();
        emailService.sendNotCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(MUNICIPALITY_EMAIL));
    }

    @Test
    public void collectable_to_municipality_reads_subject_to_email() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Kuntalaisaloite: " + INITIATIVE_NAME));
    }

    @Test
    public void collectable_to_municipality_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void collectable_to_municipality_uses_localizations_at_content() throws Exception {
        CollectableInitiativeEmailInfo initiativeEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiativeEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }

    @Test
    @Ignore("Un-comment implementation")
    public void collectable_to_municipality_assigns_senders_email_to_repllyTo_field() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(initiative, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getReplyTo()[0].toString(), is(CONTACT_EMAIL));

    }

    @Test
    public void collectable_to_author_assign_author_email_to_sendTo_field() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleRecipient(), is(CONTACT_EMAIL));
    }

    @Test
    public void collectable_to_author_reads_subject_to_email() throws InterruptedException, MessagingException {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);
        assertThat(getSingleSentMessage().getSubject(), is("Aloite on lähetetty kuntaan"));
    }

    @Test
    public void collectable_to_author_adds_initiativeInfo_and_contactInfo_to_email_message() throws Exception {
        CollectableInitiativeEmailInfo initiative = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiative, Locales.LOCALE_FI);

        assertEmailHasInitiativeDetailsAndContactInfo();
    }

    @Test
    public void collectable_to_author_uses_localizations_at_content() throws Exception {
        CollectableInitiativeEmailInfo initiativeEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToAuthor(initiativeEmailInfo, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString("Yhteystiedot"));
        assertThat(messageContent.text, containsString("Yhteystiedot"));
    }
    
    @Test
    public void collectable_to_municipality_adds_comment_to_email() throws Exception {
        CollectableInitiativeEmailInfo collectableEmailInfo = createCollectableEmailInfo();
        emailService.sendCollectableToMunicipality(collectableEmailInfo, MUNICIPALITY_EMAIL, Locales.LOCALE_FI);

        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString(COMMENT));
        assertThat(messageContent.text, containsString(COMMENT));
    }

    private void assertEmailHasInitiativeDetailsAndContactInfo() throws Exception {
        MessageContent messageContent = getMessageContent();
        assertThat(messageContent.html, containsString(INITIATIVE_NAME));
        assertThat(messageContent.html, containsString(INITIATIVE_NAME));
        assertThat(messageContent.text, containsString(INITIATIVE_PROPOSAL));
        assertThat(messageContent.text, containsString(INITIATIVE_PROPOSAL));
        assertThat(messageContent.html, containsString(INITIATIVE_MUNICIPALITY));
        assertThat(messageContent.text, containsString(INITIATIVE_MUNICIPALITY));

        assertThat(messageContent.html, containsString(CONTACT_PHONE));
        assertThat(messageContent.text, containsString(CONTACT_PHONE));
        assertThat(messageContent.html, containsString(CONTACT_EMAIL));
        assertThat(messageContent.text, containsString(CONTACT_EMAIL));
        assertThat(messageContent.html, containsString(CONTACT_ADDRESS));
        assertThat(messageContent.text, containsString(CONTACT_ADDRESS));
        assertThat(messageContent.html, containsString(CONTACT_NAME));
        assertThat(messageContent.text, containsString(CONTACT_NAME));
    }

    private static InitiativeEmailInfo createEmailInfo() {
        Initiative initiative = new Initiative();
        initiative.setName(INITIATIVE_NAME);
        initiative.setProposal(INITIATIVE_PROPOSAL);
        initiative.setMunicipality(new Municipality(INITIATIVE_MUNICIPALITY_ID,INITIATIVE_MUNICIPALITY, INITIATIVE_MUNICIPALITY));
        initiative.setCreateTime(new LocalDate(2013, 1, 1));
        initiative.setSentTime(Maybe.of(new LocalDate(2013, 1, 1)));

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setPhone(CONTACT_PHONE);
        contactInfo.setEmail(CONTACT_EMAIL);
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setAddress(CONTACT_ADDRESS);

        return InitiativeEmailInfo.parse(contactInfo, InitiativeViewInfo.parse(initiative), INITIATIVE_URL);
    }

    private static CollectableInitiativeEmailInfo createCollectableEmailInfo() {
        InitiativeEmailInfo emailInfo = createEmailInfo();
        return CollectableInitiativeEmailInfo.parse(emailInfo, COMMENT, Lists.<Participant>newArrayList());
    }


}
