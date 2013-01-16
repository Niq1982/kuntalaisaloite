package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.ComposerDao;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;
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
@ContextConfiguration(classes={NEWIntegrationTestConfiguration.class})
public class JdbcComposerDaoTest {

    @Resource
    ComposerDao composerDao;

    @Resource
    NEWTestHelper testHelper;
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
        composerDao.add(composerCreateDto());
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

        SupportCount supportCount = composerDao.countSupports(initiativeId);
        assertThat(supportCount.getRightOfVoting().getPublicNames(), is(1L));
        assertThat(supportCount.getRightOfVoting().getPrivateNames(), is(2L));
        assertThat(supportCount.getNoRightOfVoting().getPublicNames(), is(3L));
        assertThat(supportCount.getNoRightOfVoting().getPrivateNames(), is(4L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        SupportCount supportCount = composerDao.countSupports(testInitiativeId);
        assertThat(supportCount.getRightOfVoting().getPublicNames(), is(1L)); // This is the default author
        assertThat(supportCount.getRightOfVoting().getPrivateNames(), is(0L));
        assertThat(supportCount.getNoRightOfVoting().getPublicNames(), is(0L));
        assertThat(supportCount.getNoRightOfVoting().getPrivateNames(), is(0L));
    }

    private ComposerCreateDto createComposer(long initiativeId, boolean rightOfVoting, boolean publicName) {
        ComposerCreateDto composerCreateDto = new ComposerCreateDto();
        composerCreateDto.municipalityInitiativeId = initiativeId;
        composerCreateDto.name ="Composers name";
        composerCreateDto.municipalityId = testMunicipalityId;
        composerCreateDto.right_of_voting = rightOfVoting;
        composerCreateDto.showName = publicName;
        composerDao.add(composerCreateDto);
        return composerCreateDto;
    }

    private ComposerCreateDto composerCreateDto() {
        ComposerCreateDto composerCreateDto = new ComposerCreateDto();
        composerCreateDto.municipalityInitiativeId = testInitiativeId;
        composerCreateDto.name ="Composers name";
        composerCreateDto.municipalityId = testMunicipalityId;
        composerCreateDto.right_of_voting = true;
        composerCreateDto.showName = true;
        return composerCreateDto;
    }


}
