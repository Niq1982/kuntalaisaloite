package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.NEWIntegrationTestConfiguration;
import fi.om.municipalityinitiative.newdao.ComposerDao;
import fi.om.municipalityinitiative.newdto.ComposerCreateDto;
import fi.om.municipalityinitiative.newdto.SupportCount;
import fi.om.municipalityinitiative.sql.QComposer;
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

    @Before
    public void setup() {
        testHelper.dbCleanup();
    }

    @Test
    public void adds_new_composers() {
        composerDao.add(composerCreateDto());
        assertThat(testHelper.countAll(QComposer.composer), is(1L));
    }

    @Test
    public void counts_all_supports_according_to_right_of_voting() {
        ComposerCreateDto composerCreateDto = composerCreateDto();
        composerCreateDto.right_of_voting = true;
        composerDao.add(composerCreateDto);
        composerDao.add(composerCreateDto);

        composerCreateDto.right_of_voting = false;
        composerDao.add(composerCreateDto);

        SupportCount supportCount = composerDao.countSupports(composerCreateDto.municipalityInitiativeId);
        assertThat(supportCount.no_right_of_voting, is(1L));
        assertThat(supportCount.right_of_voting, is(2L));

    }

    @Test
    public void wont_fail_if_counting_supports_when_no_supports() {
        Long municipalityId = testHelper.createTestMunicipality("Municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId);

        SupportCount supportCount = composerDao.countSupports(initiativeId);
        assertThat(supportCount.no_right_of_voting, is(0L));
        assertThat(supportCount.right_of_voting, is(0L));

    }

    private ComposerCreateDto composerCreateDto() {
        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long municipalityId2 = testHelper.createTestMunicipality("Other municipality");
        Long initiativeId = testHelper.createTestInitiative(municipalityId);

        ComposerCreateDto composerCreateDto = new ComposerCreateDto();
        composerCreateDto.municipalityInitiativeId = initiativeId;
        composerCreateDto.name ="Composers name";
        composerCreateDto.municipalityId = municipalityId2;
        composerCreateDto.right_of_voting = true;
        composerCreateDto.showName = true;
        return composerCreateDto;
    }


}
