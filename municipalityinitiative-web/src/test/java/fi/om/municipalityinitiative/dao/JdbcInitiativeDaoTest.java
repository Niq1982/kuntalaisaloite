package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.InitiativeCounts;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static fi.om.municipalityinitiative.util.MaybeMatcher.isNotPresent;
import static fi.om.municipalityinitiative.util.MaybeMatcher.isPresent;
import static fi.om.municipalityinitiative.util.TestUtil.precondition;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
@Transactional
public class JdbcInitiativeDaoTest {

    public static final String INITIATIVE_PRPOSAL = "Ehdotamme että muonion tekojärvi ruopataan.";
    public static final String INITIATOR_NAME = "Teemu Teekkari";
    public static final String EXTRA_INFO = "Lisätietoja seuraa perästä";
    public static final int EXTERNAL_PARTICIPANT_COUNT = 12;
    public static final String DECISION_TEXT = "Kunnan päätös";
    public static final String NEW_DECISION_TEXT = "uusi päätös";
    public static final String VIDEO_URL = "www.youtube.com/v=dsklfjadd";
    public static final String VIDEONAME = "VIDEOname";
    public static final String VIDEO_NAME = "Video name";


    @Resource
    InitiativeDao initiativeDao;

    @Resource
    LocationDao locationDao;

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

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch()).list;
        assertThat(result.size(), is(4));
    }

    @Test
    public void find_does_not_find_if_not_published() {
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.ACCEPTED));

        assertThat(initiativeDao.findCached(initiativeSearch()).list, hasSize(0));
    }

    @Test
    public void find_does_not_find_if_fixState_not_OK() {
        Long initiative = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED));
        precondition(initiativeDao.findCached(initiativeSearch()).list, hasSize(1));

        initiativeDao.updateInitiativeFixState(initiative, FixState.FIX);
        assertThat(initiativeDao.findCached(initiativeSearch()).list, hasSize(0));
        initiativeDao.updateInitiativeFixState(initiative, FixState.REVIEW);
        assertThat(initiativeDao.findCached(initiativeSearch()).list, hasSize(0));
    }

    @Test
    public void editInitiative() {
        Long initiativeMunicipalityId = testHelper.createTestMunicipality("Initiative municipality");


        Long initiativeId = initiativeDao.prepareInitiative(initiativeMunicipalityId);

        InitiativeDraftUIEditDto initiativeEdit = new InitiativeDraftUIEditDto();
        initiativeEdit.setName(INITIATOR_NAME);
        initiativeEdit.setProposal(INITIATIVE_PRPOSAL);
        initiativeEdit.setExtraInfo(EXTRA_INFO);
        initiativeEdit.setExternalParticipantCount(EXTERNAL_PARTICIPANT_COUNT);
        initiativeEdit.setLocations(TestHelper.LOCATIONS);


        initiativeDao.editInitiativeDraft(initiativeId, initiativeEdit);
        locationDao.removeLocations(initiativeId);
        locationDao.setLocations(initiativeId, initiativeEdit.getLocations());

        Initiative initiative = initiativeDao.get(initiativeId);


        assertThat(initiative.getName(), is(initiativeEdit.getName()));
        assertThat(initiative.getProposal(), is(initiativeEdit.getProposal()));
        assertThat(initiative.getExternalParticipantCount(), is(initiativeEdit.getExternalParticipantCount()));
        assertThat(initiative.getExtraInfo(), is(initiativeEdit.getExtraInfo()));
        testHelper.assertLocations(locationDao.getLocations(initiativeId), initiativeEdit.getLocations());

    }



    @Test
    public void get_returns_all_information() {
        Long authorsMunicipalityId = testHelper.createTestMunicipality("Authors Municipality");

        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withSent(new DateTime(2010, 1, 1, 0, 0))
                .witEmailReportSent(EmailReportType.IN_ACCEPTED, new DateTime())
                .applyAuthor().withParticipantMunicipality(authorsMunicipalityId)
                .toInitiativeDraft());

        Initiative initiative = initiativeDao.get(initiativeId);

        assertThat(initiative.getMunicipality().getId(), is(testMunicipality.getId()));
        assertThat(initiative.getStateTime(), is(notNullValue()));
        assertThat(initiative.getName(), is(TestHelper.DEFAULT_INITIATIVE_NAME));
        assertThat(initiative.getProposal(), is(TestHelper.DEFAULT_PROPOSAL));
        assertThat(initiative.getSentTime(), isPresent());
        assertThat(initiative.getState(), is(TestHelper.DEFAULT_STATE));
        assertThat(initiative.getType(), is(InitiativeType.COLLABORATIVE_CITIZEN));
        assertThat(initiative.getParticipantCount(), is(1));
        assertThat(initiative.getFixState(), is(FixState.OK));
        assertThat(initiative.getExternalParticipantCount(), is(TestHelper.DEFAULT_EXTERNAL_PARTICIPANT_COUNT));


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

        precondition(initiativeDao.get(original).getSentTime(), isNotPresent());

        initiativeDao.markInitiativeAsSent(original);

        Initiative markedAsSent = initiativeDao.get(original);
        Initiative notMarkedAsSent = initiativeDao.get(someOther);

        assertThat(markedAsSent.getSentTime(), isPresent());
        assertThat(notMarkedAsSent.getSentTime(), isNotPresent());
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

        assertThat(initiativeDao.findCached(search.setLimit(2)).list, hasSize(2));
        assertThat(initiativeDao.findCached(search.setLimit(1)).list, hasSize(1));
    }

    @Test
    public void find_with_offset() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);

        InitiativeSearch search = initiativeSearch().setShow(InitiativeSearch.Show.all);
        precondition(initiativeDao.findCached(search).list, hasSize(2));

        assertThat(initiativeDao.findCached(search.setOffset(1)).list, hasSize(1));
        assertThat(initiativeDao.findCached(search.setOffset(2)).list, hasSize(0));
    }

    @Test
    public void find_orders_by_sent() {

        DateTime oldestSentTime = new DateTime(2010, 1, 1, 0, 0);
        DateTime latestSentTime = new DateTime(2020, 1, 1, 0, 0);

        Long oldestId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withSent(oldestSentTime));
        Long latestId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withSent(latestSentTime));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> oldestSentFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.oldestSent)).list;
        precondition(oldestSentFirst, hasSize(2)); // Precondition
        assertThat(oldestSentFirst.get(0).getId(), is(oldestId));

        List<InitiativeListInfo> latestSentFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.latestSent)).list;
        precondition(latestSentFirst, hasSize(2)); // Precondition
        assertThat(latestSentFirst.get(0).getId(), is(latestId));
    }

    @Test
    public void find_orders_by_participants() {

        Long mostParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withExternalParticipantCount(3)
                .withParticipantCount(3));
        Long leastParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(1));
        Long someParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE)
                .withParticipantCount(5));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> mostParticipantsFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.mostParticipants)).list;
        precondition(mostParticipantsFirst, hasSize(3)); // Precondition
        assertThat(mostParticipantsFirst.get(0).getId(), is(mostParticipants));

        List<InitiativeListInfo> leastParticipantsFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.leastParticipants)).list;
        precondition(leastParticipantsFirst, hasSize(3)); // Precondition
        assertThat(leastParticipantsFirst.get(0).getId(), is(leastParticipants));
    }

    @Test
    public void find_orders_by_counts_non_collaboratives_as_zero() {

        Long mostParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(10));
        Long leastParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(1));
        Long someParticipants = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(5));

        InitiativeSearch initiativeSearch = initiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> mostParticipantsFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.mostParticipants)).list;
        precondition(mostParticipantsFirst, hasSize(3)); // Precondition
        assertThat(mostParticipantsFirst.get(0).getId(), is(mostParticipants));

        List<InitiativeListInfo> leastParticipantsFirst = initiativeDao.findCached(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.leastParticipants)).list;
        precondition(leastParticipantsFirst, hasSize(3)); // Precondition
        assertThat(leastParticipantsFirst.get(0).getId(), is(leastParticipants));
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createSingleSent(testMunicipality.getId());
        Long second = testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch()).list;
        assertThat(second, is(result.get(0).getId()));
        assertThat(first, is(result.get(1).getId()));
    }

    @Test
    public void finds_by_municipality() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");

        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldNotBeFound = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(municipalityId);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_municipality_using_municipality_list() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(municipalityId);
        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldNotBeFound = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(municipalities);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_municipality_using_municipality_list_several_municipalities() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long municipalityId2 = testHelper.createTestMunicipality("Some municipality 2");

        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(municipalityId);
        municipalities.add(municipalityId2);

        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldNotBeFound = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(municipalities);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }
    @Test
    public void finds_several_initiatives_using_municipality_list_several_municipalities() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long municipalityId2 = testHelper.createTestMunicipality("Some municipality 2");

        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(municipalityId);
        municipalities.add(municipalityId2);

        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldBeFound2 = testHelper.createSingleSent(municipalityId2);

        Long shouldNotBeFound = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(municipalities);
        search.setOrderBy(InitiativeSearch.OrderBy.id);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;

        assertThat(result, hasSize(2));
        assertThat(result.get(0).getId(), is(shouldBeFound2));
        assertThat(result.get(1).getId(), is(shouldBeFound));

    }
    @Test
    public void finds_all_initiatives_using_empty_municipality_list() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long municipalityId2 = testHelper.createTestMunicipality("Some municipality 2");

        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(municipalityId);
        municipalities.add(municipalityId2);

        Long shouldBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldBeFound2 = testHelper.createSingleSent(municipalityId2);
        Long shouldBeFound3 = testHelper.createSingleSent(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(new ArrayList<Long>());
        search.setOrderBy(InitiativeSearch.OrderBy.id);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;

        assertThat(result, hasSize(3));

    }

    @Test
    public void finds_no_initiatives_using_several_municipalities_municipality_list() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");
        Long municipalityId2 = testHelper.createTestMunicipality("Some municipality 2");

        Long shouldNotBeFound = testHelper.createSingleSent(municipalityId);
        Long shouldNotBeFound2 = testHelper.createSingleSent(municipalityId2);

        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(testMunicipality.getId());

        InitiativeSearch search = initiativeSearch();
        search.setMunicipalities(municipalities);
        search.setOrderBy(InitiativeSearch.OrderBy.id);

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;

        assertThat(result, hasSize(0));

    }
    @Test
    public void sets_type_to_listView_object() {
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        List<InitiativeListInfo> all = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.all)).list;
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getType(), is(InitiativeType.COLLABORATIVE));
    }

    @Test
    public void does_not_set_collaborative_to_listView_if_not_collaborative() {

        testHelper.createSingleSent(testMunicipality.getId());
        List<InitiativeListInfo> all = initiativeDao.findCached(initiativeSearch()).list;
        assertThat(all, hasSize(1));
        assertThat(all.get(0).isCollaborative(), is(false));
    }

    @Test
    public void counts_participants_to_listView() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withParticipantCount(17)
                .withExternalParticipantCount(10));

        List<InitiativeListInfo> all = initiativeDao.findCached(initiativeSearch()).list;
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getParticipantCount(), is(27L));
    }

    @Test
    public void sets_sent_time_to_listView_if_initiative_is_sent() {

        testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> all = initiativeDao.findCached(initiativeSearch()).list;
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime(), isPresent());
    }

    @Test
    public void sets_sent_time_as_absent_to_listView_if_initiative_is_not_sent() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        List<InitiativeListInfo> all = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.all)).list;
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime(), isNotPresent());
    }

    @Test
    public void finds_by_name() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("name that sould not be found")
                .withState(InitiativeState.PUBLISHED));
        Long shouldBeFound = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("name that should be found ääöö")
                .withState(InitiativeState.PUBLISHED));

        InitiativeSearch search = initiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<InitiativeListInfo> result = initiativeDao.findCached(search).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_sent_finds_published_if_sent() {
        Long collaborativeSent = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(new DateTime(2010, 1, 1, 0, 0)));

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.sent)).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(collaborativeSent));
    }

    @Test
    public void finds_by_sent_does_not_find_published_if_not_sent() {
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withType(InitiativeType.COLLABORATIVE_CITIZEN)
                .withState(InitiativeState.PUBLISHED)
                .withSent(null));
        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.sent)).list;
        assertThat(result, hasSize(0));
    }

    @Test
    public void finds_by_sent_finds_not_collaborative_if_sent() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.sent)).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(singleSent));
    }

    @Test
    public void finds_by_draft() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long collaborative = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        Long draft = testHelper.createDraft(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.draft)).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(draft));
    }

    @Test
    public void finds_by_draft_will_not_show_initiatives_with_empty_names() {

        Long draft = testHelper.createDraft(testMunicipality.getId());
        Long emptyDraft = testHelper.createEmptyDraft(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.draft)).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(draft));

    }

    @Test
    public void finds_by_fix_finds_if_fixState_is_FIX() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.PUBLISHED));

        assertThat(initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.draft)).list, hasSize(0)); // Previous implementation
        assertThat(initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.fix)).list, hasSize(1)); // Previous implementation

    }

    @Test
    public void finds_by_review_finds_also_if_fixState_is_REVIEW() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.REVIEW)
                .withState(InitiativeState.PUBLISHED));

        assertThat(initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.review)).list, hasSize(1));
    }

    @Test
    public void finds_by_review() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long collaborative = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        Long review = testHelper.createCollaborativeReview(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.review)).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(review));
    }

    @Test
    public void finds_by_accepted_shows_accepted_initiatives_with_fixState_OK() {
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());
        Long review = testHelper.createCollaborativeReview(testMunicipality.getId());
        Long acceptedButReturnedForFixing = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.ACCEPTED));

        Long accepted = testHelper.createCollaborativeAccepted(testMunicipality.getId());

        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.accepted)).list;

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(accepted));
    }

    @Test
    public void finds_by_om_all_returns_everything() {

        for (InitiativeState initiativeState : InitiativeState.values()) {
            testHelper.create(testMunicipality.getId(), initiativeState, InitiativeType.UNDEFINED);
        }
        Long singleSent = testHelper.createSingleSent(testMunicipality.getId());

        assertThat(initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.omAll)).list, hasSize(InitiativeState.values().length + 1));

    }

    @Test
    public void find_by_om_all_does_not_return_initiatives_at_prepare_state() {
        testHelper.createEmptyDraft(testMunicipality.getId());
        assertThat(initiativeDao.findCached(initiativeSearch().setShow(InitiativeSearch.Show.omAll)).list, hasSize(0));
    }

    @Test
    public void counts_public_initiatives_by_state() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.createSingleSent(testMunicipality.getId());

        InitiativeCounts initiativeCounts = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.all);

        assertThat(initiativeCounts.getCollecting(), is(2L));
        assertThat(initiativeCounts.getSent(), is(1L));
        assertThat(initiativeCounts.getAll(), is(3L));
    }

    @Test
    public void does_not_count_public_initiatives_if_fixState_not_ok() {
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.OK));

        InitiativeCounts publicInitiativeCounts = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.all);
        precondition(publicInitiativeCounts.getAll(), is(1L));
        precondition(publicInitiativeCounts.collecting, is(1L));

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.FIX));
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withFixState(FixState.REVIEW));

        publicInitiativeCounts = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.all);
        assertThat(publicInitiativeCounts.getAll(), is(1L));
        assertThat(publicInitiativeCounts.collecting, is(1L));
    }

    @Test
    public void counts_all_initiatives_by_state() {

        // 1
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
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

        InitiativeCounts counts = initiativeDao.getAllInitiativeCounts(Maybe.<List<Long>>of(new ArrayList<Long>()), InitiativeSearch.Type.all);
        assertThat(counts.fix, is(1L));
        assertThat(counts.review, is(2L));
        assertThat(counts.accepted, is(3L));
        assertThat(counts.sent, is(4L));
        assertThat(counts.collecting, is(5L));
        assertThat(counts.draft, is(6L));

    }

    @Test
    public void counts_all_initiatives_by_state_and_initiatiative_type_when_collaborative_citizen() {

        // 0
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withFixState(FixState.FIX)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));
        //1
        testHelper.create(testMunicipality.getId(), InitiativeState.REVIEW, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.REVIEW, InitiativeType.COLLABORATIVE_CITIZEN);
        //2
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.UNDEFINED);
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE_CITIZEN);
        //3
        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE_CITIZEN).withSent(DateTime.now()));
        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE_CITIZEN).withSent(DateTime.now()));
        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE_CITIZEN).withSent(DateTime.now()));
        testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED).withType(InitiativeType.COLLABORATIVE_COUNCIL).withSent(DateTime.now()));
        //4
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        // 5
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.DRAFT, InitiativeType.UNDEFINED);

        InitiativeCounts counts = initiativeDao.getAllInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.citizen);
        assertThat(counts.fix, is(0L));
        assertThat(counts.review, is(1L));
        assertThat(counts.accepted, is(2L));
        assertThat(counts.sent, is(3L));
        assertThat(counts.collecting, is(4L));
        assertThat(counts.draft, is(5L));

    }

    @Test
    public void counts_initiatives_by_state_if_municipalityId_is_given() {

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.COLLABORATIVE));

        ArrayList<Long> municipalities = new ArrayList<>();
        municipalities.add(testMunicipality.getId());
        InitiativeCounts initiativeCounts = initiativeDao.getPublicInitiativeCounts(Maybe.<List<Long>>of(municipalities), InitiativeSearch.Type.all);

        assertThat(initiativeCounts.getCollecting(), is(1L));
    }

    @Test(expected = NotFoundException.class)
    public void throws_exception_if_initiative_is_not_found() {
        initiativeDao.get(-1L);
    }

    @Test
    public void find_verified_initiatives_by_verifiedUserId() {
        Long otherInitiative = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());
        Long initiativeToFind = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).applyAuthor().toInitiativeDraft());

        List<InitiativeListInfo> initiatives = initiativeDao.findInitiatives(new VerifiedUserId(testHelper.getLastVerifiedUserId()));
        assertThat(initiatives, hasSize(1));
        assertThat(initiatives.get(0).getId(), is(initiativeToFind));

    }

    @Test
    public void update_denormalized_participant_count() {
        Long initiativeId = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()));
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true));
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true));
        testHelper.createDefaultParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(false));

        initiativeDao.denormalizeParticipantCountForNormalInitiative(initiativeId);

        Initiative initiative = testHelper.getInitiative(initiativeId);
        assertThat(initiative.getParticipantCount(), is(3));
        assertThat(initiative.getParticipantCountPublic(), is(2));
    }

    @Test
    public void update_denormalized_participant_count_for_verified_initiative() {
        Long initiativeId = testHelper.createVerifiedInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(true));
        testHelper.createVerifiedParticipant(new TestHelper.AuthorDraft(initiativeId, testMunicipality.getId()).withPublicName(false));

        initiativeDao.denormalizeParticipantCountForVerifiedInitiative(initiativeId);

        Initiative initiative = testHelper.getInitiative(initiativeId);
        assertThat(initiative.getParticipantCount(), is(3));
        assertThat(initiative.getParticipantCountPublic(), is(2));
    }

    @Test
    public void find_filters_by_type_all() {
        createPublicInitiativesOfAllType();
        List<InitiativeListInfo> list = initiativeDao.findCached(initiativeSearch().setType(InitiativeSearch.Type.all)).list;
        assertThat(list, hasSize(InitiativeType.values().length));
    }

    @Test
    public void find_filters_by_type_normal() {
        createPublicInitiativesOfAllType();
        List<InitiativeListInfo> list = initiativeDao.findCached(initiativeSearch().setType(InitiativeSearch.Type.normal)).list;
        assertThat(list, hasSize(3));
        for (InitiativeListInfo initiativeListInfo : list) {
            assertThat(initiativeListInfo.getType().isVerifiable(), is(false));
        }
    }

    @Test
    public void find_filters_by_type_citizen() {
        createPublicInitiativesOfAllType();
        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setType(InitiativeSearch.Type.citizen)).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getType(), is(InitiativeType.COLLABORATIVE_CITIZEN));
    }

    @Test
    public void find_filters_by_type_council() {
        createPublicInitiativesOfAllType();
        List<InitiativeListInfo> result = initiativeDao.findCached(initiativeSearch().setType(InitiativeSearch.Type.council)).list;
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getType(), is(InitiativeType.COLLABORATIVE_COUNCIL));
    }

    @Test
    public void counts_initiatives_according_to_selected_type_if_all() {
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);

        InitiativeCounts all = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.all);
        assertThat(all.collecting, is(4L));
        assertThat(all.sent, is(1L));
        assertThat(all.getAll(), is(5L));
    }

    private Maybe<List<Long>> emptyMunicipalityList() {
        return Maybe.<List<Long>>of(new ArrayList<Long>());
    }

    @Test
    public void counts_initiatives_according_to_selected_type_if_normal() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);

        InitiativeCounts all = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.normal);
        assertThat(all.collecting, is(2L));
        assertThat(all.sent, is(1L));
        assertThat(all.getAll(), is(3L));
    }

    @Test
    public void counts_initiatives_according_to_selected_type_if_citizen() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);

        InitiativeCounts all = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.citizen);
        assertThat(all.collecting, is(1L));
        assertThat(all.sent, is(0L));
        assertThat(all.getAll(), is(1L));
    }

    @Test
    public void counts_initiatives_according_to_selected_type_if_council() {

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.SINGLE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);
        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_CITIZEN);

        testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, InitiativeType.COLLABORATIVE_COUNCIL);

        InitiativeCounts all = initiativeDao.getPublicInitiativeCounts(emptyMunicipalityList(), InitiativeSearch.Type.council);
        assertThat(all.collecting, is(1L));
        assertThat(all.sent, is(0L));
        assertThat(all.getAll(), is(1L));
    }

    @Test
    public void find_all_by_state_returns_only_with_given_state() {
        DateTime today = DateTime.now();
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withName("accepted")
                        .withState(InitiativeState.ACCEPTED)
                        .withStateTime(today)
        );

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withState(InitiativeState.REVIEW)
                        .withStateTime(today)
        );

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withState(InitiativeState.PUBLISHED)
                        .withStateTime(today)
        );

        List<Initiative> accepted = initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, today.toLocalDate().plusDays(1));
        assertThat(accepted, hasSize(1));
        assertThat(accepted.get(0).getName(), is("accepted"));
    }

    @Test
    public void find_all_by_state_retusn_only_stateChanges_after_given_date() {

        DateTime today = DateTime.now();
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withName("accepted")
                        .withState(InitiativeState.ACCEPTED)
                        .withStateTime(today)
        );

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withState(InitiativeState.REVIEW)
                        .withStateTime(today)
        );

        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                        .withState(InitiativeState.PUBLISHED)
                        .withStateTime(today)
        );

        assertThat(initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, today.toLocalDate().plusDays(1)), hasSize(1));
        assertThat(initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, today.toLocalDate().plusDays(2)), hasSize(1));
        assertThat(initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, today.toLocalDate()), is(empty()));
        assertThat(initiativeDao.findAllByStateChangeBefore(InitiativeState.ACCEPTED, today.minusDays(1).toLocalDate()), is(empty()));

    }

    @Test
    public void find_all_published_not_sent() {
        testHelper.create(testMunicipality.getId(), InitiativeState.ACCEPTED, InitiativeType.COLLABORATIVE);
        testHelper.create(testMunicipality.getId(), InitiativeState.REVIEW, InitiativeType.COLLABORATIVE);
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("Published")
                .withState(InitiativeState.PUBLISHED)
                .withSent(null));
        testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId())
                .withName("Published and sent")
                .withState(InitiativeState.PUBLISHED)
                .withSent(new DateTime()));

        assertThat(initiativeDao.findAllPublishedNotSent(), hasSize(1));
        assertThat(initiativeDao.findAllPublishedNotSent().get(0).getName(), is("Published"));

    }

    @Test
    public void mark_initiative_report_sent_marks_it_as_sent() {
        DateTime now = DateTime.now();
        Long accepted = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.ACCEPTED));

        precondition(testHelper.getInitiative(accepted).getLastEmailReportTime(), is(nullValue()));
        precondition(testHelper.getInitiative(accepted).getLastEmailReportType(), is(nullValue()));

        initiativeDao.markInitiativeReportSent(accepted, EmailReportType.IN_ACCEPTED, now);

        assertThat(testHelper.getInitiative(accepted).getLastEmailReportTime(), is(now));
        assertThat(testHelper.getInitiative(accepted).getLastEmailReportType(), is(EmailReportType.IN_ACCEPTED));

    }

    @Test
    public void create_and_edit_municipality_decision(){

        Long published = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withState(InitiativeState.PUBLISHED));

        initiativeDao.createInitiativeDecision(published, DECISION_TEXT);

        Initiative initiative = initiativeDao.get(published);

        assertThat(initiative.getDecision().getValue(), is(DECISION_TEXT));

        initiativeDao.updateInitiativeDecision(published, NEW_DECISION_TEXT);

        initiative = initiativeDao.get(published);

        assertThat(initiative.getDecision().getValue(), is(NEW_DECISION_TEXT));

        initiativeDao.updateInitiativeDecisionModifiedDate(published);

        initiative = initiativeDao.get(published);

        assertThat(initiative.getDecision().getValue(), is(NEW_DECISION_TEXT));

    }

    @Test
    public void add_video_to_initiative() {
        Long withVideo = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()).withVideoUrl(VIDEO_URL).withVideoName(VIDEONAME));

        Initiative initiative = initiativeDao.get(withVideo);

        assertThat(initiative.getVideoUrl().isPresent(), is(true));
        assertThat(initiative.getVideoUrl().getValue(), is(VIDEO_URL));


    }
    @Test
    @Ignore
    public void can_add_video_to_initiative() {
        Long withVideo = testHelper.createDefaultInitiative(new TestHelper.InitiativeDraft(testMunicipality.getId()));


     //   initiativeDao.addVideoUrl(VIDEO_URL, VIDEONAME, withVideo);

        Initiative initiative = initiativeDao.get(withVideo);
        assertThat(initiative.getVideoUrl().isPresent(), is(true));
        assertThat(initiative.getVideoUrl().getValue(), is(VIDEO_URL));


    }



    private void createPublicInitiativesOfAllType() {
        for (InitiativeType initiativeType : InitiativeType.values()) {
            testHelper.create(testMunicipality.getId(), InitiativeState.PUBLISHED, initiativeType);
        }
    }

    private static InitiativeSearch initiativeSearch() {
        return new InitiativeSearch().setShow(InitiativeSearch.Show.all);
    }


}
