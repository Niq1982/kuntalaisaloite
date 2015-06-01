package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.SupportCountDao;
import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED));

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), twoDaysAgo);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), yesterday);

        testHelper.createDefaultParticipantWithDate(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true), today);

        supportCountService.updateDenormalizedSupportCountForInitiatives();

        Map<LocalDate, Long> supportVoteCountByDate =  initiativeDao.getSupportVoteCountByDateUntil(initiativeId, yesterday);

        assertThat(supportVoteCountByDate.size(), is(2));
        assertThat(supportVoteCountByDate.get(twoDaysAgo), is(3L));
        assertThat(supportVoteCountByDate.get(yesterday), is(2L));
        assertThat(supportVoteCountByDate.get(today), is(nullValue()));

    }
}
