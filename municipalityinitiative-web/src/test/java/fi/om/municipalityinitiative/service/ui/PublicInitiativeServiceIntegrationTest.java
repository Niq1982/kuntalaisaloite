package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.AuthorMessage;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.ServiceIntegrationTestBase;
import fi.om.municipalityinitiative.service.email.EmailSubjectPropertyKeys;
import fi.om.municipalityinitiative.sql.QAuthorMessage;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


public class PublicInitiativeServiceIntegrationTest extends ServiceIntegrationTestBase {

    @Resource
    private PublicInitiativeService service;

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
        Long initiativeId = testHelper.createCollaborativeAccepted(testMunicipality.getId());
        InitiativeViewInfo initiative = service.getMunicipalityInitiative(initiativeId);
        assertThat(initiative.getState(), is(InitiativeState.ACCEPTED));
        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getId(), is(initiativeId));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.isCollaborative(), is(true));
        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void participating_allowance_is_checked() {
        Long initiative = testHelper.createCollaborativeReview(testMunicipality.getId());

        ParticipantUICreateDto participant = participantUICreateDto();
        service.createParticipant(participant, initiative, null);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void accepting_participation_allowance_is_checked() {
        Long initiative = testHelper.createSingleSent(testMunicipality.getId());

        service.confirmParticipation(testHelper.getLastParticipantId(), null);
    }

    @Test
    public void adding_participant_does_not_increase_denormalized_participantCount_but_accepting_does() throws MessagingException, InterruptedException {
        Long initiativeId = testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        long originalParticipantCount = getSingleInitiativeInfo().getParticipantCount();

        Long participantId = service.createParticipant(participantUICreateDto(), initiativeId, null);
        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(originalParticipantCount));

        service.confirmParticipation(participantId, RandomHashGenerator.getPrevious());
        assertThat(getSingleInitiativeInfo().getParticipantCount(), is(originalParticipantCount +1));

        assertUniqueSentEmail(participantUICreateDto().getParticipantEmail(), EmailSubjectPropertyKeys.EMAIL_PARTICIPATION_CONFIRMATION_SUBJECT);
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

    private InitiativeListInfo getSingleInitiativeInfo() {
        List<InitiativeListInfo> initiatives = service.findMunicipalityInitiatives(new InitiativeSearch().setShow(InitiativeSearch.Show.all), TestHelper.authorLoginUserHolder);
        precondition(initiatives, hasSize(1));
        return initiatives.get(0);
    }

    @Test
    public void preparing_initiative_sets_municipality() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);
        InitiativeViewInfo municipalityInitiative = service.getMunicipalityInitiative(initiativeId);

        assertThat(municipalityInitiative.getMunicipality().getId(), is(testMunicipality.getId()));
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

        Participant createdParticipant = testHelper.getUniqueParticipant(initiativeId);
        assertThat(createdParticipant.getHomeMunicipality().getId(), is(participantMunicipality.getId()));
        assertThat(createdParticipant.getParticipateDate(), is(LocalDate.now()));
    }

    @Test
    public void preparing_initiative_saved_email_and_municipality_and_membership() {
        Long initiativeId = service.prepareInitiative(prepareDto(), Locales.LOCALE_FI);

        Participant createdParticipant = testHelper.getUniqueParticipant(initiativeId);

        assertThat(createdParticipant.getHomeMunicipality().getId(), is(prepareDto().getHomeMunicipality()));
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

        service.confirmAndSendAuthorMessage(RandomHashGenerator.getPrevious());
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

    private AuthorUIMessage authorUIMessage() {
        AuthorUIMessage authorUIMessage = new AuthorUIMessage();
        authorUIMessage.setInitiativeId(testHelper.createSingleSent(testMunicipality.getId()));
        authorUIMessage.setContactEmail("contact@example.com");
        authorUIMessage.setMessage("Message");
        authorUIMessage.setContactName("Contact Name");
        return authorUIMessage;
    }

    private static ParticipantUICreateDto participantUICreateDto() {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();
        participant.setParticipantName("Some Name");
        participant.setParticipantEmail("participant@example.com");
        participant.setShowName(true);
        participant.setHomeMunicipality(testMunicipality.getId());
        participant.setMunicipality(testMunicipality.getId());
        return participant;
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

