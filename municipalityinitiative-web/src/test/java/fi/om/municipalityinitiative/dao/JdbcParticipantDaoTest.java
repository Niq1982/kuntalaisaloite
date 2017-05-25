package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static fi.om.municipalityinitiative.util.Membership.community;
import static fi.om.municipalityinitiative.util.Membership.none;
import static fi.om.municipalityinitiative.util.OptionalMatcher.isPresent;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static fi.om.municipalityinitiative.web.Urls.MAX_PARTICIPANT_LIST_LIMIT;
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
    private Long testVerifiedInitiativeId;
    private Long otherMunicipalityId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipalityId = testHelper.createTestMunicipality("Municipality");
        testInitiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE));
        testVerifiedInitiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId).withState(InitiativeState.ACCEPTED).withType(InitiativeType.COLLABORATIVE_CITIZEN));

        otherMunicipalityId = testHelper.createTestMunicipality("Other Municipality");
    }

    @Test
    public void adds_new_participants() {
        precondition(testHelper.countAll(QParticipant.participant), is(0L));
        participantDao.confirmParticipation(participantDao.create(participantCreateDto(), CONFIRMATION_CODE), CONFIRMATION_CODE);
        assertThat(testHelper.countAll(QParticipant.participant), is(1L));
    }

    @Test
    public void participant_information_is_saved() {
        precondition(participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE), hasSize(0));

        participantDao.confirmParticipation(participantDao.create(participantCreateDto(), CONFIRMATION_CODE), CONFIRMATION_CODE);
        List<Participant> allParticipants = participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE);

        assertThat(allParticipants, hasSize(1));

        Participant participant = allParticipants.get(0);
        assertThat(participant.getName(), is(PARTICIPANTS_NAME));
        Optional<Municipality> homeMunicipality = participant.getHomeMunicipality();
        assertThat(homeMunicipality.get().getId(), is(otherMunicipalityId));
        assertThat(participant.getParticipateDate(), is(notNullValue()));
        assertThat(participant.getEmail(), is(PARTICIPANT_EMAIL));
        assertThat(participant.getMembership(), is(PARTICIPANT_MEMBERSHIP));
        assertThat(participant.getId(), is(notNullValue()));
        ReflectionTestUtils.assertNoNullFields(participant);
    }

    @Test
    public void getAllNormalParticipants_returns_public_and_private_names() {
        Long initiativeId = testHelper.createWithAuthor(testMunicipalityId, InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        createConfirmedParticipant(initiativeId, false, "no right no public");
        createConfirmedParticipant(initiativeId, false, "yes right no public");
        createConfirmedParticipant(initiativeId, true, "no right yes public");
        createConfirmedParticipant(initiativeId, true, "yes right yes public");

        List<Participant> participants = participantDao.findAllParticipants(initiativeId, false, 0, MAX_PARTICIPANT_LIST_LIMIT);

        assertThat(participants, hasSize(5)); // Four and the creator

    }

    @Test
    public void getAllParticipants_returns_verified_and_normal_participants() {

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("par1@example.com")
                .withShowName(true)
                .withParticipantName("2 Normal community"));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withMunicipalityMembership(none)
                .withShowName(false)
                .withParticipantEmail("par2@example.com")
                .withParticipantName("1 Normal inhabitant"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("ver1@example.com")
                .withShowName(true)
                .withVerifiedParticipantMunicipalityVerified(false)
                .withParticipantName("4 Non-verified"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withParticipantEmail("ver2@example.com")
                .withShowName(false)
                .withVerifiedParticipantMunicipalityVerified(true)
                .withParticipantName("3 Verified"));


        List<Participant> allParticipants = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);

        assertThat(allParticipants, hasSize(4));

        Participant normalInhabitant = allParticipants.get(0);
        Participant normalCommunity = allParticipants.get(1);
        Participant verified = allParticipants.get(2);
        Participant verifiedNonVerifiedMunicipality = allParticipants.get(3);

        assertThat(normalInhabitant.getEmail(), is("par2@example.com"));
        assertThat(normalInhabitant.isVerified(), is(false));
        assertThat(normalInhabitant.getName(), is("1 Normal inhabitant"));
        assertThat(normalInhabitant.getMembership(), is(none));
        assertThat(normalInhabitant.getParticipateDate(), is(LocalDate.now()));
        assertThat(normalInhabitant.isMunicipalityVerified(), is(false));
        assertThat(normalInhabitant.isShowName(), is(false));
        assertThat(((Municipality) normalInhabitant.getHomeMunicipality().get()).getId(), is(testMunicipalityId));

        assertThat(normalCommunity.getEmail(), is("par1@example.com"));
        assertThat(normalCommunity.isVerified(), is(false));
        assertThat(normalCommunity.isShowName(), is(true));
        assertThat(normalCommunity.getName(), is("2 Normal community"));
        assertThat(normalCommunity.getMembership(), is(community));
        assertThat(normalCommunity.getParticipateDate(), is(LocalDate.now()));
        assertThat(normalCommunity.isMunicipalityVerified(), is(false));
        assertThat(((Municipality) normalCommunity.getHomeMunicipality().get()).getId(), is(otherMunicipalityId));

        assertThat(verified.getEmail(), is("ver2@example.com"));
        assertThat(verified.isVerified(), is(true));
        assertThat(verified.isShowName(), is(false));
        assertThat(verified.getName(), is("3 Verified"));
        assertThat(verified.getMembership(), is(none));
        assertThat(verified.getParticipateDate(), is(LocalDate.now()));
        assertThat(verified.isMunicipalityVerified(), is(true));
        assertThat(((Municipality) verified.getHomeMunicipality().get()).getId(), is(testMunicipalityId));

        assertThat(verifiedNonVerifiedMunicipality.getEmail(), is("ver1@example.com"));
        assertThat(verifiedNonVerifiedMunicipality.isVerified(), is(true));
        assertThat(verifiedNonVerifiedMunicipality.isShowName(), is(true));
        assertThat(verifiedNonVerifiedMunicipality.getName(), is("4 Non-verified"));
        assertThat(verifiedNonVerifiedMunicipality.getMembership(), is(community));
        assertThat(verifiedNonVerifiedMunicipality.getParticipateDate(), is(LocalDate.now()));
        assertThat(verifiedNonVerifiedMunicipality.isMunicipalityVerified(), is(false));
        assertThat(((Municipality) verifiedNonVerifiedMunicipality.getHomeMunicipality().get()).getId(), is(otherMunicipalityId));

    }

    @Test
    public void get_all_participants_can_return_only_public_participants() {


        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("par1@example.com")
                .withShowName(true)
                .withParticipantName("2 Normal community"));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withMunicipalityMembership(none)
                .withShowName(false)
                .withParticipantEmail("par2@example.com")
                .withParticipantName("1 Normal inhabitant"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("ver1@example.com")
                .withShowName(true)
                .withVerifiedParticipantMunicipalityVerified(false)
                .withParticipantName("4 Non-verified"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withParticipantEmail("ver2@example.com")
                .withShowName(false)
                .withVerifiedParticipantMunicipalityVerified(true)
                .withParticipantName("3 Verified"));

        List<Participant> allParticipants = participantDao.findAllParticipants(initiativeId, true, 0, Integer.MAX_VALUE);

        assertThat(allParticipants, hasSize(2));
        assertThat(allParticipants.stream().filter(Participant::isShowName).count(), is(2L));

    }

    @Test
    public void findAllParticipants_returns_if_only_normal_participants() {

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId));
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId));

        List<Participant> allParticipants = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(allParticipants, hasSize(2));

    }

    @Test
    public void findAllParticipants_returns_if_only_verified_participants() {

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId));

        List<Participant> allParticipants = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);

        assertThat(allParticipants, hasSize(2));

    }

    @Test
    public void get_all_participants_can_user_offset_and_limit() {


        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("par1@example.com")
                .withShowName(true)
                .withParticipantName("2 Normal community"));

        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withMunicipalityMembership(none)
                .withShowName(false)
                .withParticipantEmail("par2@example.com")
                .withParticipantName("1 Normal inhabitant"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, otherMunicipalityId)
                .withMunicipalityMembership(Membership.community)
                .withParticipantEmail("ver1@example.com")
                .withShowName(true)
                .withVerifiedParticipantMunicipalityVerified(false)
                .withParticipantName("4 Non-verified"));

        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withParticipantEmail("ver2@example.com")
                .withShowName(false)
                .withVerifiedParticipantMunicipalityVerified(true)
                .withParticipantName("3 Verified"));


        List<Participant> allParticipants = participantDao.findAllParticipants(initiativeId, false, 0, Integer.MAX_VALUE);
        assertThat(allParticipants, hasSize(4));

        List<Participant> firstTwo = participantDao.findAllParticipants(initiativeId, false, 0, 2);
        assertThat(firstTwo, hasSize(2));
        assertThat(firstTwo.get(0).getEmail(), is(allParticipants.get(0).getEmail()));
        assertThat(firstTwo.get(1).getEmail(), is(allParticipants.get(1).getEmail()));

        List<Participant> onlyThird = participantDao.findAllParticipants(initiativeId, false, 2, 1);
        assertThat(onlyThird, hasSize(1));
        assertThat(onlyThird.get(0).getEmail(), is(allParticipants.get(2).getEmail()));

    }


    @Test
    public void find_verified_all_participants_sets_all_data() {

        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .applyAuthor().withParticipantMunicipality(testHelper.createTestMunicipality("SomeOtherMunicipality"))
                .toInitiativeDraft());

        List<VerifiedParticipant> participants = participantDao.findVerifiedAllParticipants(initiativeId, 0, MAX_PARTICIPANT_LIST_LIMIT);
        assertThat(participants, hasSize(1));
        VerifiedParticipant participant = participants.get(0);

        assertThat(participant.getEmail(), is(TestHelper.DEFAULT_PARTICIPANT_EMAIL));
        assertThat(participant.getName(), is(TestHelper.DEFAULT_PARTICIPANT_NAME));
        assertThat(participant.isMunicipalityVerified(), is(true));
        assertThat(participant.getParticipateDate(), is(LocalDate.now()));

    }

    @Test
    public void find_all_verified_participants_returns_all() {

        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId)
                .applyAuthor()
                .withParticipantName("Private Participant")
                .withShowName(false)
                .toInitiativeDraft());

        testHelper.createVerifiedAuthorAndParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId)
                .withParticipantName("Public Participant")
                .withShowName(true));

        List<? extends Participant> participants = participantDao.findVerifiedAllParticipants(initiativeId, 0, MAX_PARTICIPANT_LIST_LIMIT);
        assertThat(participants, hasSize(2));
    }

    @Test
    public void find_all_verified_participants_limits_results() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipalityId));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withParticipantName("1"));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withParticipantName("2").withShowName(false));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipalityId).withParticipantName("3"));
        List<VerifiedParticipant> result = participantDao.findVerifiedAllParticipants(initiativeId, 1, 1);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getName(), is("2"));
    }

    @Test
    public void findAllParticipants_returns_only_confirmed_participants() {
        ParticipantCreateDto newParticipant = participantCreateDto();

        precondition(participantDao.findAllParticipants(testInitiativeId, false, 0, MAX_PARTICIPANT_LIST_LIMIT), hasSize(0));

        Long unconfirmedParticipant = participantDao.create(newParticipant, CONFIRMATION_CODE);

        assertThat(participantDao.findAllParticipants(testInitiativeId, false, 0, MAX_PARTICIPANT_LIST_LIMIT), hasSize(0));

        String confirmedParticipantName = "Some Confirmed Participant";
        newParticipant.setParticipantName(confirmedParticipantName);
        Long confirmedParticipant = participantDao.create(newParticipant, CONFIRMATION_CODE);
        participantDao.confirmParticipation(confirmedParticipant, CONFIRMATION_CODE);

        List<Participant> allParticipants = participantDao.findAllParticipants(testInitiativeId, false, 0, MAX_PARTICIPANT_LIST_LIMIT);

        assertThat(allParticipants, hasSize(1));
        assertThat(allParticipants.get(0).getName(), is(confirmedParticipantName));
    }


    @Test
    public void getAllParticipants_adds_municipality_name_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createConfirmedParticipant(testInitiativeId, otherMunicipality, false, "Participant Name");

        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId,false, 0, MAX_PARTICIPANT_LIST_LIMIT);

        Participant participant = participants.get(0);
        Optional<Municipality> homeMunicipality = participant.getHomeMunicipality();
        assertThat(homeMunicipality, isPresent());

        assertThat(homeMunicipality.get().getNameFi(), is("Some other Municipality"));
        assertThat(homeMunicipality.get().getNameSv(), is("Some other Municipality sv"));
    }

    @Test
    public void findAllParticipants_adds_municipality_name_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createConfirmedParticipant(testInitiativeId, otherMunicipality, true, "1 First");

        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE);

        assertThat(participants, hasSize(1));

        Participant participant = participants.get(0);
        Optional<Municipality> homeMunicipality = participant.getHomeMunicipality();
        assertThat(homeMunicipality, isPresent());
        assertThat(homeMunicipality.get().getNameFi(), is("Some other Municipality"));
        assertThat(homeMunicipality.get().getNameSv(), is("Some other Municipality sv"));
    }

    @Test
    public void findAllParticipants_adds_participateTime_to_data() {
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId, false, 0, MAX_PARTICIPANT_LIST_LIMIT);
        Participant participant = participants.get(0);
        assertThat(participant.getParticipateDate(), is(notNullValue()));
    }

    @Test
    public void confirming_participant_makes_participation_public() {

        int originalParticipantCount = participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE).size();

        String participantConfirmationCode = "someConfirmationCode";
        ParticipantCreateDto createDto = participantCreateDto();
        createDto.setShowName(true);
        Long participantId = participantDao.create(createDto, participantConfirmationCode);

        participantDao.create(participantCreateDto(), CONFIRMATION_CODE); // Some other unconfirmed participant
        assertThat(participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE), hasSize(originalParticipantCount));

        participantDao.confirmParticipation(participantId, participantConfirmationCode);
        assertThat(participantDao.findAllParticipants(testInitiativeId, false, 0, Integer.MAX_VALUE), hasSize(originalParticipantCount + 1));
    }

    @Test
    public void delete_participant() {
        Long participantId = testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
        Long participantsBeforeDelete = testHelper.countAll(QParticipant.participant);
        participantDao.deleteParticipant(testInitiativeId, participantId);

        Long participantsAfterDelete = testHelper.countAll(QParticipant.participant);
        assertThat(participantsAfterDelete, is(participantsBeforeDelete - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_participant_fails_if_initiative_id_and_participant_does_not_match() {
        Long participantId = testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(testInitiativeId, testMunicipalityId));
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

    private ParticipantCreateDto participantCreateDtoWithoutEmail() {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(testInitiativeId);
        participantCreateDto.setParticipantName(PARTICIPANTS_NAME);
        participantCreateDto.setHomeMunicipality(otherMunicipalityId);
        participantCreateDto.setShowName(PARTICIPANT_SHOW_NAME);
        participantCreateDto.setMunicipalMembership(PARTICIPANT_MEMBERSHIP);
        return participantCreateDto;
    }


}
