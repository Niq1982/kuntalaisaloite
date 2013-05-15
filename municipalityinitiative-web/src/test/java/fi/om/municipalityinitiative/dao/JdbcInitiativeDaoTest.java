package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcInitiativeDaoTest {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    TestHelper testHelper;

    private Municipality testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);
    }

    // Create and get are tested at MunicipalityInitiativeServiceIntegrationTests

    @Test
    public void find_returns_all() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch());
        assertThat(result.size(), is(4));
    }

    @Test
    public void find_does_not_find_if_not_published() {
        testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.ACCEPTED));

        assertThat(initiativeDao.find(initiativeSearch()), hasSize(0));
    }

    @Test
    public void get_returns_all_information() {
        Long authorsMunicipalityId = testHelper.createTestMunicipality("Authors Municipality");

        Long initiativeId = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withAuthorMunicipality(authorsMunicipalityId)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withSent(new DateTime(2010, 1, 1, 0, 0)));

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getCreateTime(), is(notNullValue()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.getSentTime().isPresent(), is(true));
        assertThat(initiative.getState(), is(TestHelper.DEFAULT_STATE));
        assertThat(initiative.getType(), is(InitiativeType.COLLABORATIVE_CITIZEN));
        assertThat(initiative.getParticipantCount(), is(1));

        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void update_initiative_state() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId());

        initiativeDao.updateInitiativeState(original, InitiativeState.PUBLISHED);

        assertThat(initiativeDao.getByIdWithOriginalAuthor(original).getState(), is(InitiativeState.PUBLISHED));
        assertThat(initiativeDao.getByIdWithOriginalAuthor(someOther).getState(), is(InitiativeState.DRAFT));
    }

    @Test
    public void update_initiative_state_sets_stateChange_time() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        DateTime fixedDateTime = new DateTime(2010, 1, 1, 0, 0);
        testHelper.updateField(original, QMunicipalityInitiative.municipalityInitiative.stateTimestamp, fixedDateTime);

        precondition(initiativeDao.getByIdWithOriginalAuthor(original).getStateTime(), is(fixedDateTime.toLocalDate()));

        initiativeDao.updateInitiativeState(original, InitiativeState.PUBLISHED);

        assertThat(initiativeDao.getByIdWithOriginalAuthor(original).getStateTime(), is(not(fixedDateTime.toLocalDate())));

    }

    @Test
    public void update_initiative_type() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId());

        initiativeDao.updateInitiativeType(original, InitiativeType.COLLABORATIVE);

        assertThat(initiativeDao.getByIdWithOriginalAuthor(original).getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(initiativeDao.getByIdWithOriginalAuthor(someOther).getType(), is(InitiativeType.UNDEFINED));
    }

    @Test
    public void mark_initiative_as_sent_adds_timestamp_and_sentComment() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId()); // Even drafts may be marked as sent by dao
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId()); // Even drafts may be marked as sent by dao

        precondition(initiativeDao.getByIdWithOriginalAuthor(original).getSentTime().isPresent(), is(false));

        initiativeDao.markInitiativeAsSent(original);

        Initiative markedAsSent = initiativeDao.getByIdWithOriginalAuthor(original);
        Initiative notMarkedAsSent = initiativeDao.getByIdWithOriginalAuthor(someOther);

        assertThat(markedAsSent.getSentTime().isPresent(), is(true));
        assertThat(notMarkedAsSent.getSentTime().isPresent(), is(false));
    }

    @Test
    public void update_moderator_comment() {
        Long initiative = testHelper.createCollectableReview(testMunicipality.getId());

        String comment = "some moderator comment";
        initiativeDao.updateModeratorComment(initiative, comment);

        assertThat(initiativeDao.getByIdWithOriginalAuthor(initiative).getModeratorComment(), is(comment));
    }

    @Test
    public void moderator_comment_is_never_null_but_empty_string() {
        Initiative singleSent = initiativeDao.getByIdWithOriginalAuthor(testHelper.createSingleSent(testMunicipality.getId()));
        assertThat(singleSent.getModeratorComment(), is(notNullValue()));
        assertThat(singleSent.getModeratorComment(), isEmptyString());
    }

    @Test
    public void find_with_limit() {
        testHelper.createSingleSent(testMunicipality.getId());
        testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch().setShow(InitiativeSearch.Show.all);

        assertThat(initiativeDao.find(search.setLimit(2)), hasSize(2));
        assertThat(initiativeDao.find(search.setLimit(1)), hasSize(1));
    }

    @Test
    public void find_with_offset() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);

        InitiativeSearch search = initiativeSearch().setShow(InitiativeSearch.Show.all);
        precondition(initiativeDao.find(search), hasSize(2));

        assertThat(initiativeDao.find(search.setOffset(1)), hasSize(1));
        assertThat(initiativeDao.find(search.setOffset(2)), hasSize(0));
    }

    @Test
    public void find_orders_by_sent() {

        DateTime oldestSentTime = new DateTime(2010, 1, 1, 0, 0);
        DateTime latestSentTime = new DateTime(2020, 1, 1, 0, 0);

        Long oldestId = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withSent(oldestSentTime));
        Long latestId = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withSent(latestSentTime));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> oldestSentFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.oldestSent));
        precondition(oldestSentFirst, hasSize(2)); // Precondition
        assertThat(oldestSentFirst.get(0).getId(), is(oldestId));

        List<InitiativeListInfo> latestSentFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.latestSent));
        precondition(latestSentFirst, hasSize(2)); // Precondition
        assertThat(latestSentFirst.get(0).getId(), is(latestId));
    }

    @Test
    public void find_orders_by_participants() {

        Long mostParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(10));
        Long leastParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(1));
        Long someParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(5));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> mostParticipantsFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.mostParticipants));
        precondition(mostParticipantsFirst, hasSize(3)); // Precondition
        assertThat(mostParticipantsFirst.get(0).getId(), is(mostParticipants));

        List<InitiativeListInfo> leastParticipantsFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.leastParticipants));
        precondition(leastParticipantsFirst, hasSize(3)); // Precondition
        assertThat(leastParticipantsFirst.get(0).getId(), is(leastParticipants));
    }

    @Test
    public void find_orders_by_counts_non_collectables_as_zero() {

        Long mostParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(10));
        Long leastParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(1));
        Long someParticipants = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(5));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> mostParticipantsFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.mostParticipants));
        precondition(mostParticipantsFirst, hasSize(3)); // Precondition
        assertThat(mostParticipantsFirst.get(0).getId(), is(mostParticipants));

        List<InitiativeListInfo> leastParticipantsFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.leastParticipants));
        precondition(leastParticipantsFirst, hasSize(3)); // Precondition
        assertThat(leastParticipantsFirst.get(0).getId(), is(leastParticipants));
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createSingleSent(testMunicipality.getId());
        Long second = testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch());
        assertThat(second, is(result.get(0).getId()));
        assertThat(first, is(result.get(1).getId()));
    }

    @Test
    public void finds_by_municipality() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");

        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldNotBeFound = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipality(municipalityId);

        List<InitiativeListInfo> result = initiativeDao.find(search);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void sets_type_to_listView_object() {
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.all));
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getType(), is(InitiativeType.COLLABORATIVE));
    }

    @Test
    public void does_not_set_collectable_to_listView_if_not_collectable() {

        testHelper.createSingleSent(testMunicipality.getId());
        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).isCollectable(), is(false));
    }

    @Test
    public void counts_participants_to_listView() {

        testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(17));

        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getParticipantCount(), is(17L));
    }

    @Test
    public void sets_sent_time_to_listView_if_initiative_is_sent() {

        testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime().isPresent(), is(true));
    }

    @Test
    public void sets_sent_time_as_absent_to_listView_if_initiative_is_not_sent() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.all));
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime().isPresent(), is(false));
    }

    @Test
    public void finds_by_name() {

        testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("name that sould not be found")
                .withState(InitiativeState.PUBLISHED));
        Long shouldBeFound = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("name that should be found ääöö")
                .withState(InitiativeState.PUBLISHED));

        InitiativeSearch search = initiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<InitiativeListInfo> result = initiativeDao.find(search);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_sent_finds_published_if_sent() {
        Long collectableSent = testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(new DateTime(2010, 1, 1, 0, 0)));

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(collectableSent));
    }

    @Test
    public void finds_by_sent_does_not_find_published_if_not_sent() {
        testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(null));
        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(0));
    }

    @Test
    public void finds_by_sent_finds_not_collectable_if_sent() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(singleSent));

    }

    @Test
    public void counts_initiatives_by_state() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.createSingleSent(testMunicipality.getId());

        InitiativeCounts initiativeCounts = initiativeDao.getInitiativeCounts(Maybe.<Long>absent());

        assertThat(initiativeCounts.getCollecting(), is(2L));
        assertThat(initiativeCounts.getSent(), is(1L));
        assertThat(initiativeCounts.getAll(), is(3L));
    }

    @Test
    public void counts_initiatives_by_state_if_municipalityId_is_given() {

        testHelper.create(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        InitiativeCounts initiativeCounts = initiativeDao.getInitiativeCounts(Maybe.of(testMunicipality.getId()));

        assertThat(initiativeCounts.getCollecting(), is(1L));

    }

    @Test(expected = NotFoundException.class)
    public void throws_exception_if_initiative_is_not_found() {
        initiativeDao.getByIdWithOriginalAuthor(-1L);
    }

    private static InitiativeSearch initiativeSearch() {
        return new InitiativeSearch().setShow(InitiativeSearch.Show.all);
    }

}
