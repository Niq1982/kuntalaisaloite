package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional(readOnly = false)
public class JdbcParticipantDaoTest {

    public static final String PARTICIPANTS_NAME = "Participants Name";
    public static final String PARTICIPANT_EMAIL = "participant@example.com";
    public static final boolean PARTICIPANT_SHOW_NAME = true;
    public static final String CONFIRMATION_CODE = "confirmationCode";
    public static final Membership PARTICIPANT_MEMBERSHIP = Membership.property;

    @Resource
    ParticipantDao participantDao;

    @Resource
    TestHelper testHelper;
    private Long testMunicipalityId;
    private Long testInitiativeId;
    private Long otherMunicipalityId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipalityId = testHelper.createTestMunicipality("Municipality");
        testInitiativeId = testHelper.create(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        otherMunicipalityId = testHelper.createTestMunicipality("Other Municipality");
    }

    @Test
    public void adds_new_participants() {
        precondition(testHelper.countAll(QParticipant.participant), is(1L));
        participantDao.confirmParticipation(participantDao.create(participantCreateDto(), CONFIRMATION_CODE), CONFIRMATION_CODE);
        assertThat(testHelper.countAll(QParticipant.participant), is(2L));
    }

    @Test
    public void participant_information_is_saved() {
        precondition(participantDao.findPublicParticipants(testInitiativeId), hasSize(1));

        participantDao.confirmParticipation(participantDao.create(participantCreateDto(), CONFIRMATION_CODE), CONFIRMATION_CODE);
        List<Participant> allParticipants = participantDao.findPublicParticipants(testInitiativeId);
        assertThat(allParticipants, hasSize(2));

        Participant participant = allParticipants.get(0);
        assertThat(participant.getName(), is(PARTICIPANTS_NAME));
        assertThat(participant.getHomeMunicipality().getId(), is(otherMunicipalityId));
        assertThat(participant.getParticipateDate(), is(notNullValue()));
        assertThat(participant.getEmail(), is(PARTICIPANT_EMAIL));
        assertThat(participant.getMembership(), is(PARTICIPANT_MEMBERSHIP));
        assertThat(participant.getId(), is(notNullValue()));
        ReflectionTestUtils.assertNoNullFields(participant);
    }

    @Test
    public void counts_all_supports_according_to_right_of_voting_and_publicity_of_names() {
        Long initiativeId = testHelper.create(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        //createParticipant(initiativeId, true, true); // This is the default author created by testHelper

        createConfirmedParticipant(initiativeId, false);
        createConfirmedParticipant(initiativeId, false);

        createConfirmedParticipant(initiativeId, true);
        createConfirmedParticipant(initiativeId, true);
        createConfirmedParticipant(initiativeId, true);

        ParticipantCount participantCount = participantDao.getParticipantCount(initiativeId);
        assertThat(participantCount.getPublicNames(), is(4L));
        assertThat(participantCount.getPrivateNames(), is(2L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        ParticipantCount participantCount = participantDao.getParticipantCount(testInitiativeId);
        assertThat(participantCount.getPublicNames(), is(1L)); // This is the default author
        assertThat(participantCount.getPrivateNames(), is(0L));
    }

    @Test
    public void getPublicParticipants_returns_public_names() {

        Long initiativeId = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).applyAuthor().withPublicName(false).toInitiativeDraft());

        createConfirmedParticipant(initiativeId, false, "no right no public");
        createConfirmedParticipant(initiativeId, false, "yes right no public");
        createConfirmedParticipant(initiativeId, true, "no right yes public");
        createConfirmedParticipant(initiativeId, true, "yes right yes public");

        List<Participant> participants = participantDao.findPublicParticipants(initiativeId);

        assertThat(participants, hasSize(2));
        assertThat(participants.get(0).getName(), is("yes right yes public"));
        assertThat(participants.get(1).getName(), is("no right yes public"));
    }

    @Test
    public void getAllParticipants_returns_public_and_private_names() {
        Long initiativeId = testHelper.create(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        createConfirmedParticipant(initiativeId, false, "no right no public");
        createConfirmedParticipant(initiativeId, false, "yes right no public");
        createConfirmedParticipant(initiativeId, true, "no right yes public");
        createConfirmedParticipant(initiativeId, true, "yes right yes public");

        List<Participant> participants = participantDao.findAllParticipants(initiativeId);

        assertThat(participants, hasSize(5)); // Four and the creator

    }

    @Test
    public void getPublicParticipants_returns_only_confirmed_participants() {
        precondition(participantDao.findPublicParticipants(testInitiativeId), hasSize(1));
        ParticipantCreateDto newParticipant = participantCreateDto();

        participantDao.create(newParticipant, CONFIRMATION_CODE);

        String confirmedParticipantName = "Some Confirmed Participant";
        newParticipant.setParticipantName(confirmedParticipantName);
        participantDao.confirmParticipation(participantDao.create(newParticipant, CONFIRMATION_CODE), CONFIRMATION_CODE);

        List<Participant> publicParticipants = participantDao.findPublicParticipants(testInitiativeId);

        assertThat(publicParticipants, hasSize(2));
        assertThat(publicParticipants.get(0).getName(), is(confirmedParticipantName));

    }

    @Test
    public void getAllParticipants_returns_only_confirmed_participants() {
        precondition(participantDao.findAllParticipants(testInitiativeId), hasSize(1));
        ParticipantCreateDto newParticipant = participantCreateDto();

        participantDao.create(newParticipant, CONFIRMATION_CODE);

        String confirmedParticipantName = "Some Confirmed Participant";
        newParticipant.setParticipantName(confirmedParticipantName);
        participantDao.confirmParticipation(participantDao.create(newParticipant, CONFIRMATION_CODE), CONFIRMATION_CODE);

        List<Participant> allParticipants = participantDao.findAllParticipants(testInitiativeId);

        assertThat(allParticipants, hasSize(2));
        assertThat(allParticipants.get(0).getName(), is(confirmedParticipantName));
    }

    @Test
    public void getParticipantCount_counts_only_confirmed_participants() {

        precondition(participantDao.getParticipantCount(testInitiativeId).getTotal(), is(1L));

        participantDao.create(participantCreateDto(), CONFIRMATION_CODE);
        participantDao.confirmParticipation(participantDao.create(participantCreateDto(), CONFIRMATION_CODE), CONFIRMATION_CODE);

        assertThat(participantDao.getParticipantCount(testInitiativeId).getTotal(), is(2L));
    }


    @Test
    public void getAllParticipants_adds_municipality_name_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createConfirmedParticipant(testInitiativeId, otherMunicipality, false, "Participant Name");

        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId);

        Participant participant = participants.get(0);
        assertThat(participant.getHomeMunicipality().getNameFi(), is("Some other Municipality"));
        assertThat(participant.getHomeMunicipality().getNameSv(), is("Some other Municipality sv"));
    }

    @Test
    public void getPublicParticipants_adds_municipality_name_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createConfirmedParticipant(testInitiativeId, otherMunicipality, true, "Participant Name");

        List<Participant> participants = participantDao.findPublicParticipants(testInitiativeId);

        Participant participant = participants.get(0);
        assertThat(participant.getHomeMunicipality().getNameFi(), is("Some other Municipality"));
        assertThat(participant.getHomeMunicipality().getNameSv(), is("Some other Municipality sv"));
    }

    @Test
    public void getAllParticipants_adds_participateTime_to_data() {
        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId);
        Participant participant = participants.get(0);
        assertThat(participant.getParticipateDate(), is(notNullValue()));
    }

    @Test
    public void getPublicParticipants_adds_participateTime_to_data() {
        List<Participant> participants = participantDao.findPublicParticipants(testInitiativeId);
        Participant participant = participants.get(0);
        assertThat(participant.getParticipateDate(), is(notNullValue()));
    }

    @Test
    public void confirming_participant_makes_participation_public() {

        String participantConfirmationCode = "someConfirmationCode";
        Long participantId = participantDao.create(participantCreateDto(), participantConfirmationCode);
        participantDao.create(participantCreateDto(), CONFIRMATION_CODE); // Some other unconfirmed participant

        long originalParticipants = participantDao.getParticipantCount(testInitiativeId).getTotal();
        participantDao.confirmParticipation(participantId, participantConfirmationCode);
        assertThat(participantDao.getParticipantCount(testInitiativeId).getTotal(), is(originalParticipants+1));
    }

    @Test
    public void delete_participant() {
        Long participantId = testHelper.createParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
        Long participantsBeforeDelete = testHelper.countAll(QParticipant.participant);
        participantDao.deleteParticipant(testInitiativeId, participantId);

        Long participantsAfterDelete = testHelper.countAll(QParticipant.participant);
        assertThat(participantsAfterDelete, is(participantsBeforeDelete - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_participant_fails_if_initiative_id_and_participant_does_not_match() {
        Long participantId = testHelper.createParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
        Long wrongInitiativeId = testHelper.createCollaborativeReview(testMunicipalityId);
        participantDao.deleteParticipant(wrongInitiativeId, participantId);
    }

    private Long createConfirmedParticipant(Long initiativeId, Long homeMunicipality, boolean publicName, String participantName) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setParticipantName(participantName);
        participantCreateDto.setHomeMunicipality(homeMunicipality);
        participantCreateDto.setShowName(publicName);
        participantCreateDto.setMunicipalMembership(PARTICIPANT_MEMBERSHIP);
        Long participantId = participantDao.create(participantCreateDto, CONFIRMATION_CODE);
        participantDao.confirmParticipation(participantId, CONFIRMATION_CODE);
        return participantId;
    }


    private Long createConfirmedParticipant(long initiativeId, boolean publicName) {
        return createConfirmedParticipant(initiativeId, publicName, "Composers name");
    }

    private Long createConfirmedParticipant(long initiativeId, boolean publicName, String participantName) {
        return createConfirmedParticipant(initiativeId, testMunicipalityId, publicName, participantName);
    }

    private ParticipantCreateDto participantCreateDto() {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(testInitiativeId);
        participantCreateDto.setParticipantName(PARTICIPANTS_NAME);
        participantCreateDto.setHomeMunicipality(otherMunicipalityId);
        participantCreateDto.setEmail(PARTICIPANT_EMAIL);
        participantCreateDto.setShowName(PARTICIPANT_SHOW_NAME);
        participantCreateDto.setMunicipalMembership(PARTICIPANT_MEMBERSHIP);
        return participantCreateDto;
    }


}
