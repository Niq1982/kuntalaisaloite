package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;
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
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.ACCEPTED));

        assertThat(initiativeDao.find(initiativeSearch()), hasSize(0));
    }

    @Test
    public void find_does_not_find_if_fixState_not_OK() {
        Long initiative = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED));
        precondition(initiativeDao.find(initiativeSearch()), hasSize(1));

        initiativeDao.updateInitiativeFixState(initiative, FixState.FIX);
        assertThat(initiativeDao.find(initiativeSearch()), hasSize(0));
        initiativeDao.updateInitiativeFixState(initiative, FixState.REVIEW);
        assertThat(initiativeDao.find(initiativeSearch()), hasSize(0));
    }

    @Test
    public void get_returns_all_information() {
        Long authorsMunicipalityId = testHelper.createTestMunicipality("Authors Municipality");

        Long initiativeId = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withSent(new DateTime(2010, 1, 1, 0, 0))
                .applyAuthor().withParticipantMunicipality(authorsMunicipalityId)
                .toInitiativeDraft());

        Initiative initiative = initiativeDao.get(initiativeId);

        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getCreateTime(), is(notNullValue()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.getSentTime().isPresent(), is(true));
        assertThat(initiative.getState(), is(TestHelper.DEFAULT_STATE));
        assertThat(initiative.getType(), is(InitiativeType.COLLABORATIVE_CITIZEN));
        assertThat(initiative.getParticipantCount(), is(1));
        assertThat(initiative.getFixState(), is(FixState.OK));

        ReflectionTestUtils.assertNoNullFields(initiative);
    }

    @Test
    public void update_initiative_state() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId());

        initiativeDao.updateInitiativeState(original, InitiativeState.PUBLISHED);

        assertThat(initiativeDao.get(original).getState(), is(InitiativeState.PUBLISHED));
        assertThat(initiativeDao.get(someOther).getState(), is(InitiativeState.DRAFT));
    }

    @Test
    public void update_initiative_state_sets_stateChange_time() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        DateTime fixedDateTime = new DateTime(2010, 1, 1, 0, 0);
        testHelper.updateField(original, QMunicipalityInitiative.municipalityInitiative.stateTimestamp, fixedDateTime);

        precondition(initiativeDao.get(original).getStateTime(), is(fixedDateTime.toLocalDate()));

        initiativeDao.updateInitiativeState(original, InitiativeState.PUBLISHED);

        assertThat(initiativeDao.get(original).getStateTime(), is(not(fixedDateTime.toLocalDate())));

    }

    @Test
    public void update_initiative_fixState() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        precondition(initiativeDao.get(original).getFixState(), is(FixState.OK));
        initiativeDao.updateInitiativeFixState(original, FixState.FIX);
        assertThat(initiativeDao.get(original).getFixState(), is(FixState.FIX));
    }

    @Test
    public void update_initiative_type() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId());
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId());

        initiativeDao.updateInitiativeType(original, InitiativeType.COLLABORATIVE);

        assertThat(initiativeDao.get(original).getType(), is(InitiativeType.COLLABORATIVE));
        assertThat(initiativeDao.get(someOther).getType(), is(InitiativeType.UNDEFINED));
    }

    @Test
    public void mark_initiative_as_sent_adds_timestamp_and_sentComment() {
        Long original = testHelper.createEmptyDraft(testMunicipality.getId()); // Even drafts may be marked as sent by dao
        Long someOther = testHelper.createEmptyDraft(testMunicipality.getId()); // Even drafts may be marked as sent by dao

        precondition(initiativeDao.get(original).getSentTime().isPresent(), is(false));

        initiativeDao.markInitiativeAsSent(original);

        Initiative markedAsSent = initiativeDao.get(original);
        Initiative notMarkedAsSent = initiativeDao.get(someOther);

        assertThat(markedAsSent.getSentTime().isPresent(), is(true));
        assertThat(notMarkedAsSent.getSentTime().isPresent(), is(false));
    }

    @Test
    public void update_moderator_comment() {
        Long initiative = testHelper.createCollaborativeReview(testMunicipality.getId());

        String comment = "some moderator comment";
        initiativeDao.updateModeratorComment(initiative, comment);

        assertThat(initiativeDao.get(initiative).getModeratorComment(), is(comment));
    }

    @Test
    public void moderator_comment_is_never_null_but_empty_string() {
        Initiative singleSent = initiativeDao.get(testHelper.createSingleSent(testMunicipality.getId()));
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

        Long oldestId = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withSent(oldestSentTime));
        Long latestId = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
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

        Long mostParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(10));
        Long leastParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(1));
        Long someParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
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
    public void find_orders_by_counts_non_collaboratives_as_zero() {

        Long mostParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(10));
        Long leastParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(1));
        Long someParticipants = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
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
    public void does_not_set_collaborative_to_listView_if_not_collaborative() {

        testHelper.createSingleSent(testMunicipality.getId());
        List<InitiativeListInfo> all = initiativeDao.find(initiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).isCollaborative(), is(false));
    }

    @Test
    public void counts_participants_to_listView() {

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
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

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("name that sould not be found")
                .withState(InitiativeState.PUBLISHED));
        Long shouldBeFound = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
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
        Long collaborativeSent = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(new DateTime(2010, 1, 1, 0, 0)));

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(collaborativeSent));
    }

    @Test
    public void finds_by_sent_does_not_find_published_if_not_sent() {
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(null));
        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(0));
    }

    @Test
    public void finds_by_sent_finds_not_collaborative_if_sent() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.sent));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(singleSent));
    }

    @Test
    public void finds_by_draft() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long collaborative = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        Long draft = testHelper.createDraft(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.draft));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(draft));
    }

    @Test
    public void finds_by_draft_will_not_show_initiatives_with_empty_names() {

        Long draft = testHelper.createDraft(testMunicipality.getId());
        Long emptyDraft = testHelper.createEmptyDraft(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.draft));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(draft));

    }

    @Test
    public void finds_by_fix_finds_if_fixState_is_FIX() {

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.PUBLISHED));

        assertThat(initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.draft)), hasSize(0)); // Previous implementation
        assertThat(initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.fix)), hasSize(1)); // Previous implementation

    }

    @Test
    public void finds_by_review_finds_also_if_fixState_is_REVIEW() {

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.REVIEW)
                .withState(InitiativeState.PUBLISHED));

        assertThat(initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.review)), hasSize(1));
    }

    @Test
    public void finds_by_review() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long collaborative = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        Long review = testHelper.createCollaborativeReview(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.review));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(review));
    }

    @Test
    public void finds_by_accepted_shows_accepted_initiatives_with_fixState_OK() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long review = testHelper.createCollaborativeReview(testMunicipality.getId());
        Long acceptedButReturnedForFixing = testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.ACCEPTED));

        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.accepted));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(accepted));
    }

    @Test
    public void finds_by_om_all_returns_everything() {

        for (InitiativeState initiativeState : InitiativeState.values()) {
            testHelper.create(testMunicipality.getId(), initiativeState, InitiativeType.UNDEFINED);
        }
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());

        assertThat(initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.omAll)), hasSize(InitiativeState.values().length + 1));

    }

    @Test
    public void find_by_om_all_does_not_return_initiatives_at_prepare_state() {
        testHelper.createEmptyDraft(testMunicipality.getId());
        assertThat(initiativeDao.find(initiativeSearch().setShow(InitiativeSearch.Show.omAll)), hasSize(0));
    }

    @Test
    public void counts_public_initiatives_by_state() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.createSingleSent(testMunicipality.getId());

        InitiativeCounts initiativeCounts = initiativeDao.getPublicInitiativeCounts(Maybe.<Long>absent());

        assertThat(initiativeCounts.getCollecting(), is(2L));
        assertThat(initiativeCounts.getSent(), is(1L));
        assertThat(initiativeCounts.getAll(), is(3L));
    }

    @Test
    public void does_not_count_public_initiatives_if_fixState_not_ok() {
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.OK));

        InitiativeCounts publicInitiativeCounts = initiativeDao.getPublicInitiativeCounts(Maybe.<Long>absent());
        precondition(publicInitiativeCounts.getAll(), is(1L));
        precondition(publicInitiativeCounts.collecting, is(1L));

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.FIX));
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.REVIEW));

        publicInitiativeCounts = initiativeDao.getPublicInitiativeCounts(Maybe.<Long>absent());
        assertThat(publicInitiativeCounts.getAll(), is(1L));
        assertThat(publicInitiativeCounts.collecting, is(1L));
    }

    @Test
    public void counts_all_initiatives_by_state() {

        // 1
        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));
        //2
        testHelper.create(testMunicipality.getId(), InitiativeState.REVIEW, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.REVIEW, InitiativeType.UNDEFINED);
        //3
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        //4
        testHelper.createSingleSent(testMunicipality.getId());
        testHelper.createSingleSent(testMunicipality.getId());
        testHelper.createSingleSent(testMunicipality.getId());
        testHelper.createSingleSent(testMunicipality.getId());
        //5
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        // 6
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);

        Long aLong = testHelper.countAll(QMunicipalityInitiative.municipalityInitiative);
        InitiativeCounts counts = initiativeDao.getAllInitiativeCounts(Maybe.<Long>absent());
        assertThat(counts.fix, is(1L));
        assertThat(counts.review, is(2L));
        assertThat(counts.accepted, is(3L));
        assertThat(counts.sent, is(4L));
        assertThat(counts.collecting, is(5L));
        assertThat(counts.draft, is(6L));

    }

    @Test
    public void counts_initiatives_by_state_if_municipalityId_is_given() {

        testHelper.createInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        InitiativeCounts initiativeCounts = initiativeDao.getPublicInitiativeCounts(Maybe.of(testMunicipality.getId()));

        assertThat(initiativeCounts.getCollecting(), is(1L));

    }

    @Test(expected = NotFoundException.class)
    public void throws_exception_if_initiative_is_not_found() {
        initiativeDao.get(-1L);
    }

    private static InitiativeSearch initiativeSearch() {
        return new InitiativeSearch().setShow(InitiativeSearch.Show.all);
    }

}
