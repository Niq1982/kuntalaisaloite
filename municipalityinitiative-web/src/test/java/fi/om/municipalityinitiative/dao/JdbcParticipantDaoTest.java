package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.sql.QParticipant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcParticipantDaoTest {

    @Resource
    ParticipantDao participantDao;

    @Resource
    TestHelper testHelper;
    private Long testMunicipalityId;
    private Long testInitiativeId;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipalityId = testHelper.createTestMunicipality("Municipality");
        testInitiativeId = testHelper.createTestInitiative(testMunicipalityId);
    }

    @Test
    public void adds_new_composers() {
        participantDao.create(participantCreateDto());
        assertThat(testHelper.countAll(QParticipant.participant), is(2L)); // Creator plus this
    }

    @Test
    public void counts_all_supports_according_to_right_of_voting_and_publicity_of_names() {
        Long municipalityId = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId);

        //createParticipant(initiativeId, true, true); // This is the default author created by testHelper

        createParticipant(initiativeId, true, false);
        createParticipant(initiativeId, true, false);

        createParticipant(initiativeId, false, true);
        createParticipant(initiativeId, false, true);
        createParticipant(initiativeId, false, true);

        createParticipant(initiativeId, false, false);
        createParticipant(initiativeId, false, false);
        createParticipant(initiativeId, false, false);
        createParticipant(initiativeId, false, false);

        ParticipantCount participantCount = participantDao.getParticipantCount(initiativeId);
        assertThat(participantCount.getFranchise().getPublicNames(), is(1L));
        assertThat(participantCount.getFranchise().getPrivateNames(), is(2L));
        assertThat(participantCount.getNoFranchise().getPublicNames(), is(3L));
        assertThat(participantCount.getNoFranchise().getPrivateNames(), is(4L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        ParticipantCount participantCount = participantDao.getParticipantCount(testInitiativeId);
        assertThat(participantCount.getFranchise().getPublicNames(), is(1L)); // This is the default author
        assertThat(participantCount.getFranchise().getPrivateNames(), is(0L));
        assertThat(participantCount.getNoFranchise().getPublicNames(), is(0L));
        assertThat(participantCount.getNoFranchise().getPrivateNames(), is(0L));
    }

    @Test
    public void getPublicParticipants_returns_public_names() {

        Long municipalityId = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Any title", false, false);

        createParticipant(initiativeId, false, false, "no right no public");
        createParticipant(initiativeId, true, false, "yes right no public");
        createParticipant(initiativeId, false, true, "no right yes public");
        createParticipant(initiativeId, true, true, "yes right yes public");

        List<Participant> participants = participantDao.findPublicParticipants(initiativeId);

        assertThat(participants, hasSize(2));
        assertThat(participants.get(0).getName(), is("yes right yes public"));
        assertThat(participants.get(1).getName(), is("no right yes public"));
    }

    @Test
    public void getAllParticipants_returns_public_and_private_names() {

        Long municipalityId = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Any title", false, false);

        createParticipant(initiativeId, false, false, "no right no public");
        createParticipant(initiativeId, true, false, "yes right no public");
        createParticipant(initiativeId, false, true, "no right yes public");
        createParticipant(initiativeId, true, true, "yes right yes public");

        List<Participant> participants = participantDao.findAllParticipants(initiativeId);

        assertThat(participants, hasSize(5)); // Four and the creator

    }


    @Test
    public void getAllParticipants_adds_municipality_name_and_franchise_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createParticipant(testInitiativeId, otherMunicipality, true, false, "Participant Name");

        List<Participant> participants = participantDao.findAllParticipants(testInitiativeId);

        Participant participant = participants.get(0);
        assertThat(participant.getHomeMunicipality().getFinnishName(), is("Some other Municipality"));
        assertThat(participant.getHomeMunicipality().getSwedishName(), is("Some other Municipality sv"));
        assertThat(participant.isFranchise(), is(true));
    }

    @Test
    public void getPublicParticipants_adds_municipality_name_and_franchise_to_participant_data() {

        Long otherMunicipality = testHelper.createTestMunicipality("Some other Municipality");
        createParticipant(testInitiativeId, otherMunicipality, true, true, "Participant Name");

        List<Participant> participants = participantDao.findPublicParticipants(testInitiativeId);

        Participant participant = participants.get(0);
        assertThat(participant.getHomeMunicipality().getFinnishName(), is("Some other Municipality"));
        assertThat(participant.getHomeMunicipality().getSwedishName(), is("Some other Municipality sv"));
        assertThat(participant.isFranchise(), is(true));
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

    private Long createParticipant(Long initiativeId, Long homeMunicipality, boolean franchise, boolean publicName, String participantName) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setParticipantName(participantName);
        participantCreateDto.setHomeMunicipality(homeMunicipality);
        participantCreateDto.setFranchise(franchise);
        participantCreateDto.setShowName(publicName);
        return participantDao.create(participantCreateDto);
    }


    private Long createParticipant(long initiativeId, boolean franchise, boolean publicName) {
        return createParticipant(initiativeId, franchise, publicName, "Composers name");
    }

    private Long createParticipant(long initiativeId, boolean franchise, boolean publicName, String participantName) {
        return createParticipant(initiativeId, testMunicipalityId, franchise, publicName, participantName);
    }

    private ParticipantCreateDto participantCreateDto() {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(testInitiativeId);
        participantCreateDto.setParticipantName("Participants Name");
        participantCreateDto.setHomeMunicipality(testMunicipalityId);
        participantCreateDto.setFranchise(true);
        participantCreateDto.setShowName(true);
        return participantCreateDto;
    }


}
