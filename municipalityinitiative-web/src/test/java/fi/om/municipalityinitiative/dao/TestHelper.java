package fi.om.municipalityinitiative.dao;

import static fi.om.municipalityinitiative.util.Locales.asLocalizedString;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.DateTimeExpression;

import fi.om.municipalityinitiative.dto.InitiativeInfo;
import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.LocalizedString;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.sql.*;

public class TestHelper {
    
    @Resource
    PostgresQueryFactory queryFactory;
    @Resource
    EncryptionService encryptionService;

    private static final QInitiative qInitiative = QInitiative.initiative;
    
    private static LocalDate DOB = new LocalDate().minusYears(20);

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);

    public TestHelper() {
    }

    public TestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=true)
    public DateTime getDbCurrentTime() {
        PostgresQuery qry = queryFactory.query();
        DateTime dbCurrentTime = qry.singleResult(CURRENT_TIME);
        
        return dbCurrentTime;
    }
    
    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QSupportVote.supportVote).execute();
        queryFactory.delete(QSupportVoteBatch.supportVoteBatch).execute();
        queryFactory.delete(QInitiativeAuthor.initiativeAuthor).execute();
        queryFactory.delete(QInitiative.initiative).execute();
        queryFactory.delete(QInituser.inituser).execute();
    }
    
    @Transactional(readOnly=false)
    public Long createTestUser() {
        UserDaoImpl userDao = new UserDaoImpl(queryFactory);
        return userDao.register("TEST", new DateTime(), "Test", "User", DOB);
    }

    @Transactional(readOnly=false)
    public Long createOMTestUserWithHash(String ssn) {
        UserDaoImpl userDao = new UserDaoImpl(queryFactory);
        String ssnHash = encryptionService.registeredUserHash(ssn);
        Long id = userDao.register(ssnHash, new DateTime(), "OM Test", "User", DOB);
        userDao.setUserRoles(id, false, true);
        return id;
    }

    @Transactional(readOnly=false)
    public Long createVRKTestUserWithHash(String ssn) {
        UserDaoImpl userDao = new UserDaoImpl(queryFactory);
        String ssnHash = encryptionService.registeredUserHash(ssn);
        Long id = userDao.register(ssnHash, new DateTime(), "VRK Test", "User", DOB);
        userDao.setUserRoles(id, true, false);
        return id;
    }

    @Transactional(readOnly=false)
    public void timeMachine(Long initiativeId, ReadablePeriod elapsedTimePeriod) {
        InitiativeDaoImpl initiativeDao = new InitiativeDaoImpl(queryFactory);
        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId, true);
        timeMachine(initiative, elapsedTimePeriod);
        queryFactory
            .update(qInitiative)
            .set(qInitiative.startdate, initiative.getStartDate())
            .set(qInitiative.enddate, initiative.getEndDate())
            .set(qInitiative.verified, initiative.getVerified())
            .set(qInitiative.statedate, initiative.getStateDate())
            .set(qInitiative.supportstatementsremoved, initiative.getSupportStatementsRemoved())
            .where(qInitiative.id.eq(initiativeId))
            .execute();
        //TODO: add timemachine support also to invitations and other child objects if needed
    }
    
    private void timeMachine(InitiativeInfo initiative, ReadablePeriod elapsedTimePeriod) {
        // dates
        initiative.setStartDate(timeMachine(initiative.getStartDate(), elapsedTimePeriod));
        initiative.assignEndDate(timeMachine(initiative.getEndDate(), elapsedTimePeriod));
        initiative.setVerified(timeMachine(initiative.getVerified(), elapsedTimePeriod));
        // timestamps
        initiative.assignStateDate(timeMachine(initiative.getStateDate(), elapsedTimePeriod));
        initiative.assignSupportStatementsRemoved(timeMachine(initiative.getSupportStatementsRemoved(), elapsedTimePeriod));
        //  modified timestamp  <-- currently it is not necessary to change this
    }

    private LocalDate timeMachine(LocalDate originalDate, ReadablePeriod elapsedTimePeriod) {
        return originalDate != null ? originalDate.minus(elapsedTimePeriod): null; 
    }
    private DateTime timeMachine(DateTime originalDate, ReadablePeriod elapsedTimePeriod) {
        return originalDate != null ? originalDate.minus(elapsedTimePeriod): null; 
    }
    
    public static LocalizedString createDefaultMunicipality() {
        return asLocalizedString("Helsinki", "Helsingfors");
    }
    
}
