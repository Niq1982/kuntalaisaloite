package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.SupportCountDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class SupportCountServiceTest {


    @Resource
    InitiativeDao initiativeDao;

    @Resource
    SupportCountService supportCountService;

    @Resource
    SupportCountDao supportCountDao;

    @Resource
    TestHelper testHelper;
    private Municipality testMunicipality;

    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = LocalDate.now().minusDays(1);
    private final LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

    @Before
    public void setup() {

        testHelper.dbCleanup();

        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
    }

    @Test
    public void create_initiatives_and_denormalize_support_count() {
        // Create a published initiative
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withStateTime(twoDaysAgo.toDateTime(new LocalTime("12:00"))));

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), today);

        supportCountService.updateDenormalizedSupportCountForInitiatives();

        Map<LocalDate, Integer> supportVoteCountByDate =  supportCountDao.getDenormalizedSupportCountData(initiativeId);

        assertThat(supportVoteCountByDate.size(), is(2));
        assertThat(supportVoteCountByDate.get(twoDaysAgo), is(3));
        assertThat(supportVoteCountByDate.get(yesterday), is(2));
        assertThat(supportVoteCountByDate.get(today), is(nullValue()));

    }

    @Test
    public void create_initiative_and_denormalize_support_count_moves_supports_before_state_time_to_state_time() {

        // Create a published initiative
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withStateTime(yesterday.toDateTime(new LocalTime("12:00"))));

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), today);

        supportCountService.updateDenormalizedSupportCountForInitiatives();

        Map<LocalDate, Integer> supportVoteCountByDate = supportCountDao.getDenormalizedSupportCountData(initiativeId);

        assertThat(supportVoteCountByDate.size(), is(1));
         assertThat(supportVoteCountByDate.get(twoDaysAgo), is(nullValue()));
         assertThat(supportVoteCountByDate.get(yesterday), is(5));
        assertThat(supportVoteCountByDate.get(today), is(nullValue()));

    }
}
