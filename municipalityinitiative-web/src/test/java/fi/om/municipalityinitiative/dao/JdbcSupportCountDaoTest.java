package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcSupportCountDaoTest {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    SupportCountDao supportCountDao;

    @Resource
    TestHelper testHelper;

    private Municipality testMunicipality;



    private LocalDate today = LocalDate.now();
    private LocalDate yesterday = LocalDate.now().minusDays(1);
    private LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    private DateTime today_datetime = DateTime.now();
    private DateTime yesterday_datetime = DateTime.now().minusDays(1);
    private DateTime twoDaysAgo_datetime = DateTime.now().minusDays(2);

    @Before
    public void setup() {
        testHelper.dbCleanup();
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
    }

    @Test
    public void list_all_running_initiative_ids() {
        // Create a published initiative with 10 supports
        Long published_id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withParticipantCount(10).withSupporCountData("[]"));

        // Create a published initiative with 0 supports
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withParticipantCount(0).withSupporCountData("[]"));

        // Create a draft initiative
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.DRAFT));

        //Create a initiative that sent date is in the past
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withSent(new DateTime(2014, 1, 1, 1, 1, 1, 1)).withSupporCountData("[]"));

        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Long> initiativeIds = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(yesterday);
        assertThat(initiativeIds.size(), is(2));

    }

    @Test
    public void get_initiative_id_that_was_sent_today() {
        //Create a initiative that sent date is today
        DateTime today = DateTime.now();
        Long published_id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withParticipantCount(1).withSent(today));

        LocalDate yesterday = LocalDate.now();
        List<Long> initiativeIds = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(yesterday);
        assertThat(initiativeIds.size(), is(1));
        assertThat(initiativeIds.get(0), is(published_id));

    }
    @Test
    public void do_not_get_initiative_id_that_was_sent_yesterday() {
        //Create a initiative that sent date is today

        DateTime yesterday = DateTime.now().minusDays(1);
        Long published_id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withParticipantCount(1)
                .withSent(yesterday)
                .withSupporCountData("[]"));

        List<Long> initiativeIds = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate.now());
        assertThat(initiativeIds.size(), is(0));
    }
    @Test
    public void get_initiative_that_is_sent_tomorrow() {
        //Create a initiative that sent date is today
        DateTime tomorrow = DateTime.now().plusDays(1);
        Long published_id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withParticipantCount(1).withSent(tomorrow));

        List<Long> initiativeIds = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate.now());
        assertThat(initiativeIds.size(), is(1));
        assertThat(initiativeIds.get(0), is(published_id));
    }

    @Test
    public void get_support_count_per_day_as_map() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);


        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED));


        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), today);

        Map<LocalDate, Long> supportVoteCountByDate =  initiativeDao.getSupportVoteCountByDateUntil(initiativeId, yesterday);
        assertThat(supportVoteCountByDate.size(), is(2));
        assertThat(supportVoteCountByDate.get(twoDaysAgo), is(3L));
        assertThat(supportVoteCountByDate.get(yesterday), is(2L));
        assertThat(supportVoteCountByDate.get(today), is(nullValue()));

        supportVoteCountByDate =  initiativeDao.getSupportVoteCountByDateUntil(initiativeId, today);
        assertThat(supportVoteCountByDate.size(), is(3));
        assertThat(supportVoteCountByDate.get(twoDaysAgo), is(3L));
        assertThat(supportVoteCountByDate.get(yesterday), is(2L));
        assertThat(supportVoteCountByDate.get(today), is(1L));

        assertThat( initiativeDao.getSupportVoteCountByDateUntil(initiativeId, twoDaysAgo.minusDays(1)).size(), is(0));
    }

    @Test
    public void save_and_get_denormalized_support_count_json_data() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED));

        String denormalizedData = "some denormalized data";

        supportCountDao.saveDenormalizedSupportCountDataJson(initiativeId, denormalizedData);

        assertThat(supportCountDao.getDenormalizedSupportCountDataJson(initiativeId), is(denormalizedData));
    }



    @Test
    public void rewrite_and_get_denormalized_support_count_data() {

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED));

        Map<LocalDate, Long> supportCounts = new HashMap<>();
        supportCounts.put(new LocalDate(2010, 1, 1), 10L);
        supportCounts.put(new LocalDate(2010, 1, 2), 15L);
        supportCountDao.saveDenormalizedSupportCountData(initiativeId, supportCounts);

        Map<LocalDate, Integer> denormalizedSupportCountData = supportCountDao.getDenormalizedSupportCountData(initiativeId);
        assertThat(denormalizedSupportCountData.size(), is(2));
        assertThat(denormalizedSupportCountData.get(new LocalDate(2010, 1, 1)), is(10));
        assertThat(denormalizedSupportCountData.get(new LocalDate(2010, 1, 2)), is(15));

        supportCounts.put(new LocalDate(2010, 1, 3), 20L);
        supportCountDao.saveDenormalizedSupportCountData(initiativeId, supportCounts);

        denormalizedSupportCountData = supportCountDao.getDenormalizedSupportCountData(initiativeId);
        assertThat(denormalizedSupportCountData.size(), is(3));
        assertThat(denormalizedSupportCountData.get(new LocalDate(2010, 1, 1)), is(10));
        assertThat(denormalizedSupportCountData.get(new LocalDate(2010, 1, 2)), is(15));
        assertThat(denormalizedSupportCountData.get(new LocalDate(2010, 1, 3)), is(20));


    }

    @Test
    public void create_verifiable_inititative(){
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE_CITIZEN));

        testHelper.createVerifiedParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), twoDaysAgo);

        testHelper.createVerifiedParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withShowName(true), twoDaysAgo);

        Map<LocalDate, Long> supportVoteCountByDate =  initiativeDao.getSupportVoteCountByDateUntil(initiativeId, yesterday);

        assertThat(supportVoteCountByDate.get(twoDaysAgo), is(2L));
    }

    @Test
    public void create_an_old_initiative_that_has_support_but_no_denormalized_support_count_saved(){
        DateTime old_date = new DateTime(2014, 1, 1, 1, 1, 1, 1);
        Long published_id = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withParticipantCount(1).withSent(old_date));

        List<Long> initiativeIds = initiativeDao.getInitiativesThatAreSentAtTheGivenDateOrInFutureOrStillRunning(LocalDate.now());
        assertThat(initiativeIds.size(), is(1));
        assertThat(initiativeIds.get(0), is(published_id));
    }

}
