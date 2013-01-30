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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

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
        assertThat(participantCount.getRightOfVoting().getPublicNames(), is(1L));
        assertThat(participantCount.getRightOfVoting().getPrivateNames(), is(2L));
        assertThat(participantCount.getNoRightOfVoting().getPublicNames(), is(3L));
        assertThat(participantCount.getNoRightOfVoting().getPrivateNames(), is(4L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        ParticipantCount participantCount = participantDao.getParticipantCount(testInitiativeId);
        assertThat(participantCount.getRightOfVoting().getPublicNames(), is(1L)); // This is the default author
        assertThat(participantCount.getRightOfVoting().getPrivateNames(), is(0L));
        assertThat(participantCount.getNoRightOfVoting().getPublicNames(), is(0L));
        assertThat(participantCount.getNoRightOfVoting().getPrivateNames(), is(0L));
    }

    @Test
    public void getParticipantNames_returns_public_names() {

        Long municipalityId = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId, "Any title", false, false);

        createParticipant(initiativeId, false, false, "no right no public");
        createParticipant(initiativeId, true, false, "yes right no public");
        createParticipant(initiativeId, false, true, "no right yes public");
        createParticipant(initiativeId, true, true, "yes right yes public");

        List<Participant> participants = participantDao.findPublicParticipants(initiativeId);

        assertThat(participants, hasSize(2));
        assertThat(participants.get(0).getName(), is("no right yes public"));
        assertThat(participants.get(1).getName(), is("yes right yes public"));
    }

    private ParticipantCreateDto createParticipant(long initiativeId, boolean rightOfVoting, boolean publicName) {
        return createParticipant(initiativeId, rightOfVoting, publicName, "Composers name");
    }

    private ParticipantCreateDto createParticipant(long initiativeId, boolean rightOfVoting, boolean publicName, String participantName) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setParticipantName(participantName);
        participantCreateDto.setHomeMunicipality(testMunicipalityId);
        participantCreateDto.setFranchise(rightOfVoting);
        participantCreateDto.setShowName(publicName);
        participantDao.create(participantCreateDto);
        return participantCreateDto;
    }

    private ParticipantCreateDto participantCreateDto() {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(testInitiativeId);
        participantCreateDto.setParticipantName("Composers name");
        participantCreateDto.setHomeMunicipality(testMunicipalityId);
        participantCreateDto.setFranchise(true);
        participantCreateDto.setShowName(true);
        return participantCreateDto;
    }


}
