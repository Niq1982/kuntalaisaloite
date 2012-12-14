package fi.om.municipalityinitiative.dao;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.conf.IntegrationTestConfiguration;
import fi.om.municipalityinitiative.dto.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.util.Locales.asLocalizedString;
import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={IntegrationTestConfiguration.class})
public class InitiativeDaoTest {
    
    @Resource
    private InitiativeDao initiativeDao;

    private Long userId;

    private static LocalDate DOB = new LocalDate().minusYears(20);

    private static DateTime testStartTime = new DateTime();
    
    private static LocalDate today = testStartTime.toLocalDate();

    private static Integer stringChangerIndex = 0;
    
    @Resource
    private TestHelper testHelper;
    
    @Before
    public void init() {
        testHelper.dbCleanup();
        userId = testHelper.createTestUser();
        //NOTE: testStartTime should use db server time so that comparisons to trigger updated fields don't fail
        testStartTime = testHelper.getDbCurrentTime();
        today = testStartTime.toLocalDate();
    }
    
    @Test
    public void Intiative_CRUD_OK() {
        InitiativeManagement afterCreate = intiativeCreateTest();
        wait100(); // to ensure that timestamp would change
        // Initiative aferUpdate = updateTest(aferCreate);
        intiativeUpdateTest(afterCreate);
        //TODO: delete test
    }

    private InitiativeManagement getTestInitiativeDraft() {
        Long id = create(createInitiative(null), userId);
        return initiativeDao.getInitiativeForManagement(id, false);
    }
    
    @Test
    public void Intiative_Remove_Link_With_Empty_Label_And_URI_OK() {
        InitiativeManagement beforeUpdate = getTestInitiativeDraft();

        Link link = beforeUpdate.getLinks().get(0);
        link.setLabel("");
        link.setUri(null);

        initiativeDao.updateLinks(beforeUpdate.getId(), beforeUpdate.getLinks());
        
        InitiativeBase afterUpdate = initiativeDao.getInitiativeForPublic(beforeUpdate.getId());

        assertEquals(beforeUpdate.getLinks().size() - 1, afterUpdate.getLinks().size());
    }

    @Test
    public void Intiative_List_OK() {
        int initiativesCount = 3; // for larger counts optimize test to use sorted lists
        List<InitiativeBase> initiativesGet = Lists.newArrayList();

        Author author = createAuthor(userId);

        for (int i = 0; i < initiativesCount; i++) {
            Long id = create(createInitiative(null), userId);
            // Author is required for own...
            initiativeDao.insertAuthor(id, userId, author);
            InitiativeBase initiative = initiativeDao.getInitiativeForPublic(id);
            initiativesGet.add(initiative);
        }

        List<InitiativeInfo> initiativesList = initiativeDao.findInitiatives(new InitiativeSearch(false, false), userId, 0);
        assertEquals(0, initiativesList.size()); // was empty condition

        initiativesList = initiativeDao.findInitiatives(new InitiativeSearch(true, false), userId, 0);
        assertEquals(0, initiativesList.size()); // there were no public initiatives yet

        initiativesList = initiativeDao.findInitiatives(new InitiativeSearch(false, true), userId, 0);
        assertEquals(initiativesCount, initiativesList.size());

        List<InitiativeInfo> initiativesList2 = initiativeDao.findInitiatives(new InitiativeSearch(true, true), userId, 0);
        assertEquals(initiativesCount, initiativesList2.size());

        for (int i = 0; i < initiativesCount; i++) {
            for (int j = 0; j < initiativesCount; j++) {
                if (initiativesGet.get(i).getId().equals(initiativesList.get(j).getId())) {
                    assertInitiative(initiativesGet.get(i), initiativesList.get(j));
                    assertInitiativeAutoFieldsEqual(initiativesGet.get(i), initiativesList.get(j));
                }
                if (initiativesGet.get(i).getId().equals(initiativesList2.get(j).getId())) {
                    assertInitiative(initiativesGet.get(i), initiativesList2.get(j));
                    assertInitiativeAutoFieldsEqual(initiativesGet.get(i), initiativesList2.get(j));
                }

            }
         }
    }

    @Test
    public void find_with_offset_given() {

        int count = 5;

        List<Long> ids = Lists.newArrayList();
        for (int i = 0; i < count; ++i) {
            Long id = createAcceptedInitiative();
            ids.add(id);
        }
        ids = Lists.reverse(ids);

        InitiativeSearch search = new InitiativeSearch(true, true);
        search.setOffsetIndex(1);
        search.setOrderBy(InitiativeSearch.OrderBy.ID);

        List<InitiativeInfo> result = initiativeDao.findInitiatives(search, null, 0);

        assertThat(result.get(0).getId(), is(ids.get(1)));
    }

    @Test
    public void find_with_limit_given() {

        int count = 5;

        List<Long> ids = Lists.newArrayList();
        for (int i = 0; i < count; ++i) {
            Long id = createAcceptedInitiative();
            ids.add(id);
        }
        ids = Lists.reverse(ids);

        InitiativeSearch search = new InitiativeSearch(true, true);
        search.setLimit(3);
        search.setOrderBy(InitiativeSearch.OrderBy.ID);

        List<InitiativeInfo> result = initiativeDao.findInitiatives(search, null, 0);

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getId(), is(ids.get(0)));
        assertThat(result.get(2).getId(), is(ids.get(2)));
    }

    @Test
    public void find_with_restrict_given() {

        int count = 5;

        List<Long> ids = Lists.newArrayList();
        for (int i = 0; i < count; ++i) {
            Long id = createAcceptedInitiative();
            ids.add(id);
        }
        ids = Lists.reverse(ids);

        InitiativeSearch search = new InitiativeSearch(true, true);
        search.setRestrict(1, 3);
        search.setOrderBy(InitiativeSearch.OrderBy.ID);

        List<InitiativeInfo> result = initiativeDao.findInitiatives(search, null, 0);

        assertThat(result.size(), is(3));
        assertThat(result.get(0).getId(), is(ids.get(1)));
        assertThat(result.get(2).getId(), is(ids.get(3)));
    }

    @Test
    public void orderBy() {

        today = testStartTime.toLocalDate();
        Long todayInitiative = createAcceptedInitiative();

        today = today.plusDays(1);
        Long tomorrowInitiative = createAcceptedInitiative();

        today = today.minusDays(7);
        Long aboutAWeekAgo = createAcceptedInitiative();

        InitiativeSearch initiativeSearch = new InitiativeSearch(true, true);

        initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.ID);
        List<InitiativeInfo> listById = initiativeDao.findInitiatives(initiativeSearch, null, 0);
        assertThat(listById.get(0).getId(), is(aboutAWeekAgo));
        assertThat(listById.get(1).getId(), is(tomorrowInitiative));
        assertThat(listById.get(2).getId(), is(todayInitiative));

        initiativeSearch.setOrderBy(InitiativeSearch.OrderBy.START_DATE);
        List<InitiativeInfo> listByStartDate = initiativeDao.findInitiatives(initiativeSearch, null, 0);
        assertThat(listByStartDate.get(0).getId(), is(aboutAWeekAgo));
        assertThat(listByStartDate.get(1).getId(), is(todayInitiative));
        assertThat(listByStartDate.get(2).getId(), is(tomorrowInitiative));

    }

    private Long createAcceptedInitiative() {
        Long id = create(createInitiative(null), userId);
        initiativeDao.updateInitiativeState(id, userId, InitiativeState.ACCEPTED, "");
        return id;
    }

    private Long create(InitiativeManagement initiative, Long userId) {
        Long initiativeId = initiativeDao.create(initiative, userId);

        initiativeDao.updateLinks(initiativeId, initiative.getLinks());

        initiativeDao.updateInvitations(initiativeId, initiative.getInitiatorInvitations(),
                initiative.getRepresentativeInvitations(),
                initiative.getReserveInvitations());

        return initiativeId;
    }



    @Test
    public void Intiative_State_Update_OK() {
        InitiativeBase before = getTestInitiativeDraft();
        assertEquals(InitiativeState.DRAFT, before.getState());

        wait100(); // to ensure that timestamps would change

        InitiativeState newState = InitiativeState.REVIEW;
        initiativeDao.updateInitiativeState(before.getId(), userId, newState, null);
        InitiativeManagement after = initiativeDao.getInitiativeForManagement(before.getId(), false);

        assertEquals(newState, after.getState());
        assertAutoTimestamp(before.getStateDate(), after.getStateDate());

        assertInitiative(before, after); //normal fields
        //other auto fields:
        assertEquals(before.getId(), after.getId());
        assertEquals(userId, after.getModifierId());
        assertAutoTimestamp(before.getModified(), after.getModified());
        assertEquals(0, after.getSupportCount());
    }

    @Test
    public void Author_CRUD_OK() {
        InitiativeBase initiative = getTestInitiativeDraft();

        Author afterCreate = authorCreateTest(initiative.getId());
        wait100(); // to ensure that timestamp would change
        Author afterUpdate = authorUpdateTest(initiative.getId(), afterCreate);
        authorFromInitiativeTest(initiative.getId(), afterUpdate);
        //TODO: delete test
    }

    // FIXME invitations
//    @Test
//    public void Invitations_Life_Cycle_OK() {
//        InitiativeBase initiative = getTestInitiativeDraft();
//
//        //List<Invitation> afterCreate = invitationsCreateTest(initiative.getId());
//        invitationsCreateTest(initiative.getId());
//        
//        //"update": remove old and add new invitations:
//        List<Invitation> after2Create = invitationsCreateTest(initiative.getId()); 
//
//        wait100(); // to ensure that timestamp would change
//        
//        List<Invitation> afterUpdate = invitationsUpdateTest(initiative.getId(), after2Create);
//        
//        invitationsGetTest(initiative.getId(), afterUpdate);
//        
//        invitationsDeleteTest(initiative.getId(), afterUpdate);
//    }

//    private List<Invitation> invitationsCreateTest(Long initiativeId) {
//        String chg = getChanger();
//        String EMAIL_INI = "ini.test@solita.fi" + chg;
//        String EMAIL_REP = "rep.test@solita.fi" + chg;
//        String EMAIL_RES = "res.test@solita.fi" + chg;
//        String EMAIL_INIREP = "inirep.test@solita.fi" + chg;
//        String EMAIL_INIRES = "inires.test@solita.fi" + chg;
//        int EMAIL_COUNT = 5;
//        final String SEP = ", \t\r\n"; //separator and different whitespace characters should be removed
//        final String SEP2 = "; \t\r\n"; //alternative separator and different whitespace characters should be removed
//        final String SEP3 = "; \t\r\n,;,\n,   , ;"; //empty items between separators should be omitted
//
//        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId);
//        List<Invitation> invitations = initiative.getInvitations();
//        assertEquals(EMAIL_COUNT, invitations.size());
//
//        assertTrue(findAndTestInvitation(invitations, EMAIL_INI, true, AuthorRole.INITIATOR));
//        assertTrue(findAndTestInvitation(invitations, EMAIL_REP, false, AuthorRole.REPRESENTATIVE));
//        assertTrue(findAndTestInvitation(invitations, EMAIL_RES, false, AuthorRole.RESERVE));
//        assertTrue(findAndTestInvitation(invitations, EMAIL_INIREP, true, AuthorRole.REPRESENTATIVE));
//        assertTrue(findAndTestInvitation(invitations, EMAIL_INIRES, true, AuthorRole.RESERVE));
//        return invitations;
//    }
    
//    private List<Invitation> invitationsUpdateTest(Long initiativeId, List<Invitation> befores) {
//        
//        for (Invitation invitation : befores) {
//            invitation.setInvitationCode(invitation.getId()+"code");
//            initiativeDao.updateInvitationSent(invitation, initiativeId);
//        }
//        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId);
//        List<Invitation> afters = initiative.getInvitations();
//
//        assertInvitations(befores, afters, true);
//        
//        return afters;
//    }

//    private List<Invitation> invitationsGetTest(Long initiativeId, List<Invitation> invitations) {
//        List<Invitation> fetchedInvitations =  Lists.newArrayList();
//        
//        for (Invitation invitation : invitations) {
//            Invitation fetchedInvitation = initiativeDao.getOpenInvitation(initiativeId, invitation.getInvitationCode(), null);
//            fetchedInvitations.add(fetchedInvitation);
//        }
//
//        assertInvitations(invitations, fetchedInvitations, false);
//        return fetchedInvitations;
//    }

//    private void invitationsDeleteTest(Long initiativeId, List<Invitation> invitations) {
//        for (Invitation invitation : invitations) {
//            initiativeDao.removeInvitation(initiativeId, invitation.getInvitationCode());
//        }
//
//        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId);
//        List<Invitation> afters = initiative.getInvitations();
//
//        assertEquals(0, afters.size());
//    }
    
//    private void assertInvitations(List<Invitation> befores, List<Invitation> afters, boolean sentUpdated) {
//        assertEquals(befores.size(), afters.size());
//        
//        for (int i = 0; i < afters.size(); i++) {
//            Invitation before = befores.get(i);
//            Invitation after = afters.get(i);
//            //unchanged automatic
//            assertEquals(before.getId(), after.getId());
//            assertEquals(before.getCreated(), after.getCreated());
//            
//            //unchanged inserted  
//            assertEquals(before.getEmail(), after.getEmail());
//            assertEquals(before.isInitiator(), after.isInitiator());
//            assertEquals(before.isRepresentative(), after.isRepresentative());
//            assertEquals(before.isReserve(), after.isReserve());
//
//            assertEquals(before.getInvitationCode(), after.getInvitationCode());
//            if (sentUpdated) {
//                //updated information 
//                assertTrue(after.getInvitationCode().length() >= 1); //TODO: actual length from settings
//                assertAutoTimestamp(after.getSent());
//            } else {
//                assertEquals(before.getInvitationCode(), after.getInvitationCode());
//                assertEquals(before.getSent(), after.getSent());
//            }
//        }
//    }
    
//    private boolean findAndTestInvitation(List<Invitation> invitations, String email, boolean initiator, AuthorRole role) {
//        for (Invitation invitation : invitations) {
//            if (invitation.getEmail().equals(email)) {
//                //automatic
//                assertTrue(invitation.getId() > 0);
//                assertAutoTimestamp(invitation.getCreated());
//                
//                //inserted 
//                assertEquals(email, invitation.getEmail());
//                assertEquals(initiator, invitation.isInitiator());
//                assertEquals(role == AuthorRole.REPRESENTATIVE, invitation.isRepresentative());
//                assertEquals(role == AuthorRole.RESERVE, invitation.isReserve());
//                assertTrue(invitation.isRole(role));
//
//                //empty 
//                assertNull(invitation.getInvitationCode());
//                assertNull(invitation.getSent());
//                return true;
//            }
//        }
//        return false;
//    }
    
    private void authorFromInitiativeTest(Long initiativeId, Author expected) {
        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId, false);
        Author actual;
        if (expected.isInitiator()) {
            actual = initiative.getInitiators().get(0);
        } else if (expected.isRepresentative()) {
            actual = initiative.getRepresentatives().get(0);
        } else if (expected.isReserve()) {
            actual = initiative.getReserves().get(0);
        } else {
            throw new IllegalStateException("Author was not what expected");
        }

        assertEquals(expected.getFirstNames(), actual.getFirstNames());
        assertEquals(expected.getLastName()  , actual.   getLastName());   
        assertEquals(expected.getHomeMunicipality(), actual.getHomeMunicipality());

        assertContacInfoEquals(expected.getContactInfo(), actual.getContactInfo());
    }
    
    private void assertContacInfoEquals(ContactInfo expected, ContactInfo actual) {
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPhone(), actual.getPhone());
    }
    
    private Author authorCreateTest(Long initiativeId) {
        Author before = createAuthor(userId);

        // These are not handled in regular updates -> these should be overriden by defaults
        before.assignConfirmed(testStartTime.plusDays(7));
        before.assignCreated(testStartTime.plusDays(7));

        initiativeDao.insertAuthor(initiativeId, userId, before);
        
        Author after = initiativeDao.getAuthor(initiativeId, userId);

        assertAuthor(before, after);
        assertAuthorAutoFieldsCreated(after);
        return after;
    }

    private Author authorUpdateTest(Long initiativeId, Author before) {
        authorUpdateValues(before);
        initiativeDao.updateAuthor(initiativeId, userId, before);
        Author after = initiativeDao.getAuthor(initiativeId, userId);

        assertAuthor(before, after);
        assertAuthorAutoFieldsEqual(before, after); // doesn't include automatic update fields

        return after;
    }
    
    private InitiativeManagement intiativeCreateTest() {
        InitiativeManagement before = createInitiative(-123l);

        // These are not handled in regular updates -> these should be overriden by defaults
        before.assignModifierId(-123l); // Non-existing
        before.assignModified(testStartTime.plusDays(7));
        before.assignState(InitiativeState.ACCEPTED);
        before.assignStateDate(testStartTime.plusDays(7));
        before.assignSupportCount(50);

        Long id = create(before, userId);
        
        assertNotNull(id);
        
        InitiativeManagement after = initiativeDao.getInitiativeForManagement(id, false);

        assertInitiative(before, after);
        assertInitiativeAutoFieldsModified(null, after, userId, id);
        return after;
    }
    
    private InitiativeBase intiativeUpdateTest(InitiativeManagement before) {
        Long id = before.getId();
        intiativeUpdateValues(before);
        initiativeDao.updateInitiative(before, userId, true, true);
        initiativeDao.updateLinks(before.getId(), before.getLinks());
        
        InitiativeBase after = initiativeDao.getInitiativeForPublic(before.getId());

        assertInitiative(before, after);
        assertInitiativeAutoFieldsModified(before, after, userId, id);

        //updateInitiativeValues updated link #0, removed #1 and created a new, so first id should be same and second different
        assertEquals(before.getLinks().get(0).getId(), after.getLinks().get(0).getId());
        assertNotEquals(before.getLinks().get(1).getId(), after.getLinks().get(1).getId());
        return after;
    }

    
    private void assertInitiative(InitiativeInfo before, InitiativeInfo after) {
        assertEquals(before.isFinancialSupport(), after.isFinancialSupport());
        assertEquals(before.getFinancialSupportURL(), after.getFinancialSupportURL());
        assertEquals(before.getName(), after.getName());
        assertEquals(before.getProposalType(), after.getProposalType());
        assertEquals(before.getPrimaryLanguage(), after.getPrimaryLanguage());
        assertEquals(before.getStartDate(), after.getStartDate());
        assertEquals(before.getEndDate(), after.getEndDate());
        assertEquals(before.isSupportStatementsInWeb(), after.isSupportStatementsInWeb());
        assertEquals(before.isSupportStatementsOnPaper(), after.isSupportStatementsOnPaper());
    }

    
    private void assertInitiative(InitiativeBase before, InitiativeBase after) {
        assertInitiative((InitiativeInfo) before, (InitiativeInfo) after);
        assertEquals(before.getProposal(), after.getProposal());
        assertEquals(before.getRationale(), after.getRationale());

        // Compare links
        List<Link> beforeLinks = before.getLinks();
        List<Link> afterLinks = after.getLinks();
        assertEquals(beforeLinks.size(), afterLinks.size());
        
        for (int i=0; i < beforeLinks.size(); i++) {
            Link expectedLink = beforeLinks.get(i);
            Link actualLink = afterLinks.get(i);
            
            assertEquals(expectedLink.getUri(), actualLink.getUri());
            assertEquals(expectedLink.getLabel(), actualLink.getLabel());
        }
    }

//    private void assertInitiativeAutoFieldsEqual(InitiativeManagement before, InitiativeManagement after) {
//        assertInitiative((InitiativeInfo) before, (InitiativeInfo) after); 
//        assertEquals(before.getModifierId(), after.getModifierId());
//    }

    private void assertInitiativeAutoFieldsEqual(InitiativeInfo before, InitiativeInfo after) {
        // Automatic fields should remain same when not updated
        assertEquals(before.getId(), after.getId());
        assertEquals(before.getModified(), after.getModified());
        assertEquals(before.getState(), after.getState());
        assertEquals(before.getStateDate(), after.getStateDate());
        assertEquals(before.getSupportCount(), after.getSupportCount());
    }
    
    private void assertInitiativeAutoFieldsModified(InitiativeManagement old, InitiativeManagement i, Long userId, Long id) {
        assertInitiativeAutoFieldsModified((InitiativeBase) old, (InitiativeBase) i, userId, id);
        assertEquals(userId, i.getModifierId());
    }
    
    private void assertInitiativeAutoFieldsModified(InitiativeBase old, InitiativeBase i, Long userId, Long id) {
        // These are not inserted or updated manually -> assert defaults
        assertEquals(id, i.getId());
        
        if (old == null) {
            assertAutoTimestamp(i.getModified());
            assertEquals(InitiativeState.DRAFT, i.getState());
            assertAutoTimestamp(i.getStateDate());
        }
        else {
            assertAutoTimestamp(old.getModified(), i.getModified());
            assertEquals(old.getState(), i.getState());
            assertEquals(old.getStateDate(), i.getStateDate());
        }
        assertEquals(0, i.getSupportCount());
    }

    public static InitiativeManagement createInitiative(Long id) {
        String chg = getChanger();
        InitiativeManagement initiative = new InitiativeManagement();
        initiative.assignId(id);
        initiative.setFinancialSupport(true);
        initiative.setFinancialSupportURL(new InitURI("http://www.solita.fi"+chg));
        initiative.setName(asLocalizedString("Nimi"+chg, null));
        initiative.setProposal(asLocalizedString("Ehdotus"+chg, null));
        initiative.setProposalType(ProposalType.LAW);
        initiative.setRationale(asLocalizedString("Perustelut"+chg, null));
        initiative.setPrimaryLanguage(LanguageCode.FI);
        initiative.setStartDate(today);
        initiative.assignEndDate(today.plusMonths(6));
        initiative.setSupportStatementsInWeb(true);
        initiative.setSupportStatementsOnPaper(true);

        initiative.setLinks(Lists.newArrayList(intiativeLinkCreateValues(), intiativeLinkCreateValues()));
        
        return initiative;
    }

    private void intiativeUpdateValues(InitiativeBase i) {
        String chg = getChanger();
        i.setFinancialSupport(!i.isFinancialSupport());
        i.setFinancialSupportURL(new InitURI(i.getFinancialSupportURL()+chg));
        i.setName(updateFiSvMap(i.getName(), chg));
        i.setProposal(updateFiSvMap(i.getProposal(), chg));
        i.setProposalType(ProposalType.PREPARATION);
        i.setRationale(updateFiSvMap(i.getRationale(), chg));
        i.setPrimaryLanguage(LanguageCode.FI);
        i.setStartDate(i.getStartDate().plusDays(1));
        i.assignEndDate(i.getEndDate().plusDays(1));
        i.setSupportStatementsInWeb(!i.isSupportStatementsInWeb());
        i.setSupportStatementsOnPaper(!i.isSupportStatementsOnPaper());

        List<Link> oldLinks = i.getLinks(); //there should be two links inserted in a previous create
        //update link #0, remove #1 and create a new
        List<Link> newLinks = Lists.newArrayList(intiativeLinkUpdateValues(oldLinks.get(0)), intiativeLinkCreateValues());
        i.setLinks(newLinks);
    }
    
    private Link intiativeLinkUpdateValues(Link l) {
        String chg = getChanger();
        l.setLabel(l.getLabel() + chg);
        l.setUri(new InitURI(l.getUri()+chg));
        return l;
    }
    
    public static Link intiativeLinkCreateValues() {
        String chg = getChanger();
        Link link = new Link();
        link.setLabel("Solita"+chg);
        link.setUri(new InitURI("http://www.solita.fi"+chg));
        return link;
    }

    
    
    private void assertAuthor(Author before, Author after) {
        assertEquals(before.getUserId(), after.getUserId());
        assertEquals(before.getFirstNames(), after.getFirstNames());
        assertEquals(before.getLastName(), after.getLastName());
        assertEquals(before.getHomeMunicipality(), after.getHomeMunicipality());
        
        assertEquals(before.isInitiator(), after.isInitiator());
        assertEquals(before.isRepresentative(), after.isRepresentative());
        assertEquals(before.isReserve(), after.isReserve());

        assertContacInfoEquals(before.getContactInfo(), after.getContactInfo());
    }

    private void assertAuthorAutoFieldsEqual(Author before, Author after) {
        // Automatic fields should remain same when not updated
        assertEquals(before.getConfirmed(), after.getConfirmed());
        assertEquals(before.getCreated(), after.getCreated());
    }
    
    private void assertAuthorAutoFieldsCreated(Author after) {
        assertAutoTimestamp(after.getConfirmed());
        assertAutoTimestamp(after.getCreated());
    }

    public static Author createAuthor(Long userId, boolean initiator, boolean representative, boolean reserve) {
        String chg = getChanger();
        Author author = new Author(userId, "Etunimi"+chg, "Sukunimi"+chg, DOB, TestHelper.createDefaultMunicipality());
        author.setInitiator(initiator);
        author.setRepresentative(representative);
        author.setReserve(reserve);
        
        author.assignAddress("Kotikatu 5"+chg);
        author.assignEmail("email"+chg+"@domain.fi");
        author.assignPhone("123456"+chg);
        return author;
    }
    
    public static Author createAuthor(Long userId) {
        return createAuthor(userId, true, true, false);
    }
    

    private void authorUpdateValues(Author a) {
        String chg = getChanger();
        //Author author = new Author(userId, "Etunimi"+chg, "Sukunimi"+chg, "Kotikunta"+chg);
        a.setInitiator(!a.isInitiator());
        a.setRepresentative(!a.isRepresentative());
        a.setReserve(!a.isReserve());

        ContactInfo c = a.getContactInfo();
        c.setAddress(c.getAddress()+chg);
        c.setEmail(c.getEmail()+chg);
        c.setPhone(c.getPhone()+chg);
    }
    
    private static String getChanger() {
        //increments changer string to ensure that each version of test data is different
        stringChangerIndex++;
        return stringChangerIndex.toString();
    }
    
    private LocalizedString updateFiSvMap(LocalizedString original, String changer) {
        return asLocalizedString(original.getFi()+changer, original.getSv()+changer);
    }

    private void assertAutoTimestamp(DateTime beforeModification, DateTime afterModification) {
        assertAutoTimestamp(afterModification);
        assertBefore(beforeModification, afterModification);
    }
    
    private void assertAutoTimestamp(DateTime afterModification) {
        assertBeforeOrEqual(testStartTime, afterModification);
        assertBeforeOrEqualNow(afterModification);
    }
    
    private void assertBeforeOrEqual(DateTime before, DateTime after) {
        assertTrue(before.isEqual(after) || before.isBefore(after));
    }
    private void assertBefore(DateTime before, DateTime after) {
        assertTrue(before.isBefore(after));
    }
    private void assertBeforeOrEqualNow(DateTime value) {
        assertTrue("Not true: "+value+"<="+new DateTime(), value.isBeforeNow() || value.isEqualNow());
    }
    private void assertNotEquals(Long before, Long after) {
        assertTrue(!after.equals(before));
    }

    private synchronized void wait100() {
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
