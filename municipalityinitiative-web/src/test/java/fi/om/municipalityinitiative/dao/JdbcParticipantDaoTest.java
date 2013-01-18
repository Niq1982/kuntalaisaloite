package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;
import fi.om.municipalityinitiative.sql.QParticipant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
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
        participantDao.create(composerCreateDto());
        assertThat(testHelper.countAll(QParticipant.participant), is(2L)); // Creator plus this
    }

    @Test
    public void counts_all_supports_according_to_right_of_voting_and_publicity_of_names() {
        boolean rightOfVoting = true;
        boolean publicName = true;

        Long municipalityId = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId);

        //createComposer(initiativeId, true, true); // This is the default author created by testHelper

        createComposer(initiativeId, true, false);
        createComposer(initiativeId, true, false);

        createComposer(initiativeId, false, true);
        createComposer(initiativeId, false, true);
        createComposer(initiativeId, false, true);

        createComposer(initiativeId, false, false);
        createComposer(initiativeId, false, false);
        createComposer(initiativeId, false, false);
        createComposer(initiativeId, false, false);

        ParticipantCount participantCount = participantDao.countSupports(initiativeId);
        assertThat(participantCount.getRightOfVoting().getPublicNames(), is(1L));
        assertThat(participantCount.getRightOfVoting().getPrivateNames(), is(2L));
        assertThat(participantCount.getNoRightOfVoting().getPublicNames(), is(3L));
        assertThat(participantCount.getNoRightOfVoting().getPrivateNames(), is(4L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        ParticipantCount participantCount = participantDao.countSupports(testInitiativeId);
        assertThat(participantCount.getRightOfVoting().getPublicNames(), is(1L)); // This is the default author
        assertThat(participantCount.getRightOfVoting().getPrivateNames(), is(0L));
        assertThat(participantCount.getNoRightOfVoting().getPublicNames(), is(0L));
        assertThat(participantCount.getNoRightOfVoting().getPrivateNames(), is(0L));
    }

    private ParticipantCreateDto createComposer(long initiativeId, boolean rightOfVoting, boolean publicName) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setName("Composers name");
        participantCreateDto.setMunicipalityId(testMunicipalityId);
        participantCreateDto.setFranchise(rightOfVoting);
        participantCreateDto.setShowName(publicName);
        participantDao.create(participantCreateDto);
        return participantCreateDto;
    }

    private ParticipantCreateDto composerCreateDto() {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(testInitiativeId);
        participantCreateDto.setName("Composers name");
        participantCreateDto.setMunicipalityId(testMunicipalityId);
        participantCreateDto.setFranchise(true);
        participantCreateDto.setShowName(true);
        return participantCreateDto;
    }


}
