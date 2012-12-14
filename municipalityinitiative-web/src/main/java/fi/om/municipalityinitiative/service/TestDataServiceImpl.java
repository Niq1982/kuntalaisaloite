package fi.om.municipalityinitiative.service;

import java.util.List;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.UserDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.InitiativeSettings;
import fi.om.municipalityinitiative.dto.InitiativeState;
import fi.om.municipalityinitiative.dto.User;
import fi.om.municipalityinitiative.sql.QInituser;
import fi.om.municipalityinitiative.util.TestDataTemplates;

@Profile({"dev", "test"})
public class TestDataServiceImpl implements TestDataService {

    @Resource UserDao userDao;
    @Resource InitiativeDao initiativeDao;
    @Resource EncryptionService encryptionService;
    @Resource InitiativeSettings initiativeSettings;

    @Resource PostgresQueryFactory queryFactory; //TODO: should this be moved to a separete TestDataDao?
    private static final QInituser qUser = QInituser.inituser;
    
    @Override
    @Transactional(readOnly=false)
    public void createTestUsersFromTemplates(List<User> userTemplates) {
        for (User user : userTemplates) {
            createTestUserWithHash(user);
        }
    }

    private Long createTestUserWithHash(User user) {
        String ssnHash = encryptionService.registeredUserHash(user.getSsn());
        Long id = getRegisteredUserId(ssnHash);
        if (id == null) {
            id = userDao.register(ssnHash, new DateTime(), user.getFirstNames(), user.getLastName(), user.getDateOfBirth());
        }
        if (user.isVrk() || user.isOm()) { //always update vrk/om roles, to make sure that also pre-existing users will have right roles
            userDao.setUserRoles(id, user.isVrk(), user.isOm());
        }
        return id;
    }

    @Override
    @Transactional(readOnly=false)
    public void createTestInitiativesFromTemplates(List<InitiativeManagement> initiatives, User currentUser, String initiatorEmail, String reserveEmail) {
        if (initiatives.size() == 0) {
            return; // nothing to create
        }
        
//        Long currentUserId = null;
        Long reserveUserId = null;
        if (Strings.isNullOrEmpty(initiatorEmail) || Strings.isNullOrEmpty(reserveEmail)) {
            throw new IllegalArgumentException("Missing email address");
        }
        
        /* currentUserId = */ createTestUserWithHash(currentUser); 
        reserveUserId = createTestUserWithHash(TestDataTemplates.RESERVE_AUTHOR_USER);

        Author currentAuthor = new Author(currentUser);
        currentAuthor.setInitiator(true);
        currentAuthor.setRepresentative(true);
        currentAuthor.setReserve(false);
        currentAuthor.getContactInfo().setEmail(initiatorEmail);
//        currentAuthor.getContactInfo().setAddress(null);
//        currentAuthor.getContactInfo().setPhone(null);
        
        Author reserveAuthor = TestDataTemplates.RESERVE_AUTHOR;    //FIXME: not thread safe for static?
        reserveAuthor.assignUserId(reserveUserId);
        reserveAuthor.getContactInfo().setEmail(reserveEmail);
//        reserveAuthor.getContactInfo().setAddress(null);
//        reserveAuthor.getContactInfo().setPhone(null);
        
        for (InitiativeManagement initiative : initiatives) {
            createTestInitiativeFromTemplate(initiative, currentAuthor, reserveAuthor);
        }
        //TODO: return links to the created initiatives?
    }
    
    private Long createTestInitiativeFromTemplate(InitiativeManagement initiative, Author currentAuthor, Author reserveAuthor) {
        initiative.setStartDate(LocalDate.now());
        initiative.assignEndDate(initiative.getStartDate(), initiativeSettings.getVotingDuration());
        Long id = initiativeDao.create(initiative, currentAuthor.getUserId());

        // currently links and invitations are not in use in templates:
//        initiativeDao.updateLinks(id, initiative.getLinks()); 
//        initiativeDao.updateInvitations(id, initiative.getInitiatorInvitations(), 
//                initiative.getRepresentativeInvitations(), 
//                initiative.getReserveInvitations());
        
        
        initiativeDao.insertAuthor(id, currentAuthor.getUserId(), currentAuthor);
        
        if (initiative.getState() != InitiativeState.DRAFT) {
            initiativeDao.insertAuthor(id, reserveAuthor.getUserId(), reserveAuthor);

            initiativeDao.updateInitiativeState(id, currentAuthor.getUserId(), initiative.getState(), null);
        }
        
        return id;
    }
    
    // ----------------------------------------------------------------

    public Long getRegisteredUserId(String ssnHash) {
        PostgresQuery qry = queryFactory.from(qUser)
                .where(qUser.hash.eq(ssnHash));
        
        Long id = qry.uniqueResult(qUser.id);

        return id;
    }

}
