package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.ui.AuthorUIMessage;
import fi.om.municipalityinitiative.dto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareInitiativeUICreateDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.service.email.EmailSubjectPropertyKeys;
import fi.om.municipalityinitiative.sql.QAuthorMessage;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.util.hash.PreviousHashGetter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;


public class NormalInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase {

    @Resource
    private NormalInitiativeService service;

    private static Municipality testMunicipality;

    private static Municipality participantMunicipality;

    @Override
    public void childSetup() {

        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

        municipalityName = "Participant municipality";
        participantMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

    }

    @Test
    public void all_fields_are_set_when_getting_municipalityInitiativeInfo() throws Exception {

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withSent(new DateTime())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .witEmailReportSent(EmailReportType.IN_ACCEPTED, new DateTime())
                .applyAuthor()
                .toInitiativeDraft());
        InitiativeViewInfo initiative = service.getPublicInitiative(initiativeId);
        assertThat(initiative.getState(), is(InitiativeState.PUBLISHED));
        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.isCollaborative(), is(true));
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void sets_participant_count_to_one_when_adding_new_collaborative_initiative() {
        PrepareInitiativeUICreateDto prepareInitiativeUICreateDto = new PrepareInitiativeUICreateDto();
        prepareInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeUICreateDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeUICreateDto.setParticipantEmail("authorEmail@example.com");
        Long initiativeId = service.prepareInitiative(prepareInitiativeUICreateDto, Locales.LOCALE_FI);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));
    }



    @Test(expected = AccessDeniedException.class)
    public void trying_to_prepare_initiative_to_non_active_municipality_is_forbidden() {
        Long unactiveMunicipality = testHelper.createTestMunicipality("Some Unactive Municipality", false);

        PrepareInitiativeUICreateDto createDto = new PrepareInitiativeUICreateDto();
        createDto.setMunicipality(unactiveMunicipality);

        service.prepareInitiative(createDto, null);
    }

    @Test
    public void preparing_initiative_sets_municipality() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        Initiative initiative = testHelper.getInitiative(initiativeId);

        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
    }

    @Test
    public void preparing_initiative_sends_email() throws MessagingException, InterruptedException {
        service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        assertUniqueSentEmail(prepareDto().getParticipantEmail(), EmailSubjectPropertyKeys.EMAIL_PREPARE_CREATE_SUBJECT);
    }

    @Test
    public void preparing_initiative_sets_participant_information() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        assertThat(testHelper.getInitiative(initiativeId).getParticipantCount(), is(1));

        NormalParticipant createdParticipant = testHelper.getUniqueNormalParticipant(initiativeId);
        assertThat(createdParticipant.getHomeMunicipality().get().getId(), is(participantMunicipality.getId()));
        assertThat(createdParticipant.getParticipateDate(), is(LocalDate.now()));
    }

    @Test
    public void preparing_initiative_saved_email_and_municipality_and_membership() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        NormalParticipant createdParticipant = testHelper.getUniqueNormalParticipant(initiativeId);

        assertThat(createdParticipant.getHomeMunicipality().get().getId(), is(prepareDto().getHomeMunicipality()));
        assertThat(createdParticipant.getEmail(), is(prepareDto().getParticipantEmail()));
        assertThat(createdParticipant.getMembership(), is(prepareDto().getMunicipalMembership()));

        // Note that all fields are not set when preparing
    }

    @Test
    public void addAuthorMessage_increases_amount_of_author_messages_and_confirming_deletes() {
        AuthorUIMessage authorUIMessage = authorUIMessage();

        precondition(testHelper.countAll(QAuthorMessage.authorMessage), is(0L));

        service.addAuthorMessage(authorUIMessage, Locales.LOCALE_FI);
        assertThat(testHelper.countAll(QAuthorMessage.authorMessage), is(1L));

        service.confirmAndSendAuthorMessage(PreviousHashGetter.get());
        assertThat(testHelper.countAll(QAuthorMessage.authorMessage), is(0L));
    }

    @Test
    public void confirmAuthorMessage_sends_email_to_authors() throws MessagingException, InterruptedException {
        String confirmationCode = "conf-code";
        testHelper.createAuthorMessage(new AuthorMessage(authorUIMessage(), confirmationCode));

        service.confirmAndSendAuthorMessage(confirmationCode);
        assertUniqueSentEmail(TestHelper.DEFAULT_PARTICIPANT_EMAIL, EmailSubjectPropertyKeys.EMAIL_AUTHOR_MESSAGE_TO_AUTHORS_SUBJECT);
    }

    @Test
    public void addAuthorMessage_sends_verification_email() throws MessagingException, InterruptedException {
        service.addAuthorMessage(authorUIMessage(), Locales.LOCALE_FI);
        assertUniqueSentEmail(authorUIMessage().getContactEmail(), EmailSubjectPropertyKeys.EMAIL_AUTHOR_MESSAGE_CONFIRMATION_SUBJECT);
    }

    // This test might not be valid until end of times, it was just used to verify
    // that transaction is not committed if exception is thrown.
    @Test
    public void transaction_is_rollbacked_if_exception_is_thrown() {

        Long initiativesAtFirst = testHelper.countAll(QMunicipalityInitiative.municipalityInitiative);
        PrepareInitiativeUICreateDto createDto = new PrepareInitiativeUICreateDto();
        createDto.setMunicipality(testMunicipality.getId());
        try {
            service.prepareInitiative(createDto, Locales.LOCALE_FI);
            fail("Should have thrown exception");
        } catch (Throwable t) {
            precondition(t, instanceOf(DataIntegrityViolationException.class)); // Creating participant should throw this after the initiative has been created
            assertThat(initiativesAtFirst, is(testHelper.countAll(QMunicipalityInitiative.municipalityInitiative)));
        }

    }

    private AuthorUIMessage authorUIMessage() {
        AuthorUIMessage authorUIMessage = new AuthorUIMessage();
        authorUIMessage.setInitiativeId(testHelper.createSingleSent(testMunicipality.getId()));
        authorUIMessage.setContactEmail("contact@example.com");
        authorUIMessage.setMessage("Message");
        authorUIMessage.setContactName("Contact Name");
        return authorUIMessage;
    }

    private static PrepareInitiativeUICreateDto prepareDto() {
        PrepareInitiativeUICreateDto prepareInitiativeUICreateDto = new PrepareInitiativeUICreateDto();
        prepareInitiativeUICreateDto.setMunicipality(testMunicipality.getId());
        prepareInitiativeUICreateDto.setHomeMunicipality(participantMunicipality.getId());
        prepareInitiativeUICreateDto.setParticipantEmail("authorEmail@example.com");
        prepareInitiativeUICreateDto.setMunicipalMembership(Membership.property);
        return prepareInitiativeUICreateDto;
    }




}

