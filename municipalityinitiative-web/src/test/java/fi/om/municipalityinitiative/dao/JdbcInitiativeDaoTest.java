package fi.om.municipalityinitiative.dao;

import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.exceptions.NotCollectableException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class JdbcInitiativeDaoTest {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    TestHelper testHelper;

    private MunicipalityInfo testMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();
        testMunicipality = new MunicipalityInfo();
        testMunicipality.setName("Test municipality");
        testMunicipality.setId(testHelper.createTestMunicipality(testMunicipality.getName()));
    }

    // Create and get are tested at MunicipalityInitiativeServiceIntegrationTests

    @Test
    public void find_returns_all() {

        testHelper.createTestInitiative(testMunicipality.getId(), "First");
        testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<InitiativeListInfo> result = initiativeDao.find(new InitiativeSearch());
        assertThat(result.size(), is(2));
    }

    @Test
    public void find_with_limit() {
        testHelper.createTestInitiative(testMunicipality.getId(), "First");
        testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        InitiativeSearch search = new InitiativeSearch().setShow(InitiativeSearch.Show.all);

        assertThat(initiativeDao.find(search.setLimit(2)), hasSize(2));
        assertThat(initiativeDao.find(search.setLimit(1)), hasSize(1));
    }

    @Test
    public void find_with_offset() {
        testHelper.createTestInitiative(testMunicipality.getId(), "First");
        testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        InitiativeSearch search = new InitiativeSearch().setShow(InitiativeSearch.Show.all);
        precondition(initiativeDao.find(search), hasSize(2));

        assertThat(initiativeDao.find(search.setOffset(1)), hasSize(1));
        assertThat(initiativeDao.find(search.setOffset(2)), hasSize(0));
    }

    @Test
    public void find_orders_by_sent() {
        Long oldestId = testHelper.createTestInitiative(testMunicipality.getId(), "First");
        Long latestId = testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        DateTime oldestSentTime = new DateTime(2010, 1, 1, 0, 0);
        DateTime latestSentTime = new DateTime(2020, 1, 1, 0, 0);
        testHelper.updateField(oldestId, QMunicipalityInitiative.municipalityInitiative.sent, oldestSentTime);
        testHelper.updateField(latestId, QMunicipalityInitiative.municipalityInitiative.sent, latestSentTime);

        InitiativeSearch initiativeSearch = new InitiativeSearch().setShow(InitiativeSearch.Show.all);

        List<InitiativeListInfo> oldestSentFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.oldestSent));
        precondition(oldestSentFirst, hasSize(2)); // Precondition
        assertThat(oldestSentFirst.get(0).getId(), is(oldestId));

        List<InitiativeListInfo> latestSentFirst = initiativeDao.find(initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.latestSent));
        precondition(latestSentFirst, hasSize(2)); // Precondition
        assertThat(latestSentFirst.get(0).getId(), is(latestId));
    }

    @Test // TODO: Write test and implement after participantcount is denormalized
    public void find_orders_by_participants() {

    }

    private static <T> void precondition(T actual, Matcher<? super T> matcher) {
        assertThat("Precondition failed", actual, matcher);
    }

    @Test
    public void find_returns_in_correct_order() {
        Long first = testHelper.createTestInitiative(testMunicipality.getId(), "First");
        Long second = testHelper.createTestInitiative(testMunicipality.getId(), "Second");

        List<InitiativeListInfo> result = initiativeDao.find(new InitiativeSearch());
        assertThat(second, Matchers.is(result.get(0).getId()));
        assertThat(first, Matchers.is(result.get(1).getId()));
    }

    @Test
    public void finds_by_municipality() {

        Long municipalityId = testHelper.createTestMunicipality("Some municipality");

        Long shouldBeFound = testHelper.createTestInitiative(municipalityId);
        Long shouldNotBeFound = testHelper.createTestInitiative(testMunicipality.getId());

        InitiativeSearch search = new InitiativeSearch();
        search.setMunicipality(municipalityId);

        List<InitiativeListInfo> result = initiativeDao.find(search);
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void sets_collectable_to_listView_if_collectable() {
        testHelper.createTestInitiative(testMunicipality.getId(), "Collectable", true, true);

        List<InitiativeListInfo> all = initiativeDao.find(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        assertThat(all, hasSize(1));
        assertThat(all.get(0).isCollectable(), is(true));
    }

    @Test
    public void does_not_set_collectable_to_listView_if_not_collectable() {

        testHelper.createTestInitiative(testMunicipality.getId(), "Not collectable", false, false);
        List<InitiativeListInfo> all = initiativeDao.find(new InitiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).isCollectable(), is(false));
    }

    @Test
    public void counts_participants_to_listView() {
        testHelper.createTestInitiative(testMunicipality.getId(), "Not collectable", false, false);
        List<InitiativeListInfo> all = initiativeDao.find(new InitiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getParticipantCount(), is(1L));
    }

    @Test
    public void sets_sent_time_to_listView_if_initiative_is_sent() {

        testHelper.createTestInitiative(testMunicipality.getId(), "Not collectable", false, false);

        List<InitiativeListInfo> all = initiativeDao.find(new InitiativeSearch());
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime().isPresent(), is(true));
    }

    @Test
    public void sets_sent_time_as_absent_to_listView_if_initiative_is_not_sent() {

        testHelper.createTestInitiative(testMunicipality.getId(), "Collectable", true, true);

        List<InitiativeListInfo> all = initiativeDao.find(new InitiativeSearch().setShow(InitiativeSearch.Show.all));
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getSentTime().isPresent(), is(false));
    }

    @Test
    public void finds_by_name() {

        testHelper.createTestInitiative(testMunicipality.getId(), "name that should not be found");
        Long shouldBeFound = testHelper.createTestInitiative(testMunicipality.getId(), "name that should be found ääöö");

        InitiativeSearch search = new InitiativeSearch();
        search.setSearch("SHOULD be found ääöö");

        List<InitiativeListInfo> result = initiativeDao.find(search);

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), is(shouldBeFound));
    }

    @Test
    public void finds_by_sent_finds_collectable_if_sent() {
        Long collectableSent = testHelper.createTestInitiative(testMunicipality.getId(), "Title", true, true);
        testHelper.updateField(collectableSent, QMunicipalityInitiative.municipalityInitiative.sent, new DateTime());

        List<InitiativeListInfo> result = initiativeDao.find(new InitiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), Matchers.is(collectableSent));
    }

    @Test
    public void finds_by_sent_does_not_find_collectable_if_not_sent() {
        testHelper.createTestInitiative(testMunicipality.getId(), "Title", true, true);
        List<InitiativeListInfo> result = initiativeDao.find(new InitiativeSearch().setShow(InitiativeSearch.Show.sent));
        assertThat(result, hasSize(0));
    }

    @Test
    public void finds_by_sent_finds_not_collectable() {
        Long notCollectable = testHelper.createTestInitiative(testMunicipality.getId(), "Title", false, false);

        List<InitiativeListInfo> result = initiativeDao.find(new InitiativeSearch().setShow(InitiativeSearch.Show.sent));

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), Matchers.is(notCollectable));

    }

    @Test
    public void marks_as_sended_if_collectable_and_not_sended() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, true);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.getSentTime().isPresent(), is(false)); // Precondition

        initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, contactInfo());
        initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.getSentTime().isPresent(), is(true));
    }

    @Test(expected = NotCollectableException.class)
    public void throws_exception_if_not_collectable_and_marking_as_sent() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, false);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.isCollectable(), is(false)); // Precondition

        initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, contactInfo());
    }

    @Test
    public void throws_exception_if_trying_double_send() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, true);

        InitiativeViewInfo initiative = initiativeDao.getById(initiativeId);
        assertThat(initiative.isCollectable(), is(true)); // Precondition
        assertThat(initiative.getSentTime().isPresent(), is(false)); // Precondition

        initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, contactInfo());

        try {
            initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, contactInfo());
            fail("Should have thrown exception");
        } catch (NotCollectableException e) {

        }
    }

    @Test
    public void marking_as_sent_updates_contact_information() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId(), "Some initiative name", false, true);
        ContactInfo original = new ContactInfo();
        original.setAddress("new address");
        original.setPhone("new phone");
        original.setEmail("email@example.com");
        original.setName("new name");
        initiativeDao.markAsSendedAndUpdateContactInfo(initiativeId, original);

        ContactInfo updated = initiativeDao.getContactInfo(initiativeId);
        assertThat(updated.getPhone(), is(original.getPhone()));
        assertThat(updated.getName(), is(original.getName()));
        assertThat(updated.getEmail(), is(original.getEmail()));
        assertThat(updated.getAddress(), is(original.getAddress()));

    }

    private static ContactInfo contactInfo() {
        return new ContactInfo();
    }

    @Test
    public void find_contact_info() {
        Long initiativeId = testHelper.createTestInitiative(testMunicipality.getId());

        ContactInfo contactInfo = initiativeDao.getContactInfo(initiativeId);
        assertThat(contactInfo.getName(), is("contact_name"));
        assertThat(contactInfo.getAddress(), is("contact_address"));
        assertThat(contactInfo.getEmail(), is("contact_email@xxx.yyy"));
        assertThat(contactInfo.getPhone(), is("contact_phone"));
    }

}
