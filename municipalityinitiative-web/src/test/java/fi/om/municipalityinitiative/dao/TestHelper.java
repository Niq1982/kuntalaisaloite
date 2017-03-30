package fi.om.municipalityinitiative.dao;

import com.google.common.net.MediaType;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import fi.om.municipalityinitiative.conf.PropertyNames;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.*;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestHelper {

    public static final String DEFAULT_INITIATIVE_NAME = "Initiative name";
    public static final String DEFAULT_PROPOSAL = "Proposal";
    public static final InitiativeState DEFAULT_STATE = InitiativeState.DRAFT;
    public static final InitiativeType DEFAULT_TYPE = InitiativeType.UNDEFINED;
    public static final String DEFAULT_PARTICIPANT_NAME = "Antti Author";
    public static final String DEFAULT_PARTICIPANT_EMAIL = "author_email@example.com";
    public static final String DEFAULT_AUTHOR_ADDRESS = "author address";
    public static final String DEFAULT_AUTHOR_PHONE = "author phone";
    public static final boolean DEFAULT_PUBLIC_NAME = true;
    public static final String DEFAULT_EXTRA_INFO = "some default extraInfo";
    public static final DateTime DEFAULT_SENT_TIME = null;
    public static final DateTime SENT_TIME = new DateTime(2011, 1, 1, 0, 0);
    public static final DateTime DEFAULT_CREATE_TIME = DateTime.now();
    public static final DateTime DEFAULT_STATE_TIME = DEFAULT_CREATE_TIME;
    public static final String DEFAULT_SENT_COMMENT = "some default sent comment";
    public static final Integer DEFAULT_EXTERNAL_PARTICIPANT_COUNT = 0;
    public static final String LOCATION_DESCRIPTION = "sijainnin kuvaus";
    public static final double LOCATION_LNG = 23.456789;
    public static final double LOCATION_LAT = 23.455678;
    public static final List<Location> LOCATIONS = new ArrayList<Location>() {{ add(new Location(LOCATION_LAT, LOCATION_LNG));}};
    public static final String VALID_VIDEO_URL = "https://www.youtube.com/watch?v=P4W1VSb-dGU";

    public static LoginUserHolder authorLoginUserHolder;
    public static LoginUserHolder unknownLoginUserHolder = new LoginUserHolder(User.anonym());
    public static LoginUserHolder lastLoggedInVerifiedUserHolder;
    public static OmLoginUserHolder omLoginUser = new OmLoginUserHolder(User.omUser(""));

    @Inject
    private Environment environment;

    @Resource
    PostgresQueryFactory queryFactory;

    @Resource
    EncryptionService encryptionService;

    private Long lastInitiativeId;
    private Long lastAuthorId;
    private Long lastVerifiedUserId;
    private String previousTestManagementHash;
    private String previousUserSsnHash;
    private String lastMunicipalityHash;

    public TestHelper() {
    }

    public TestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        dbCleanupAllButMunicipalities();
        queryFactory.delete(QMunicipality.municipality).execute();
//        cacheManager.getCacheManager().clearAll();
    }

    @Transactional(readOnly = false)
    public void dbCleanupAllButMunicipalities() {
        queryFactory.delete(QInitiativeSupportVoteDay.initiativeSupportVoteDay).execute();
        queryFactory.delete(QAuthorMessage.authorMessage).execute();
        queryFactory.delete(QReviewHistory.reviewHistory).execute();
        queryFactory.delete(QAttachment.attachment).execute();
        queryFactory.delete(QAuthorInvitation.authorInvitation).execute();
        queryFactory.delete(QAuthor.author).execute();
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QVerifiedAuthor.verifiedAuthor).execute();
        queryFactory.delete(QVerifiedParticipant.verifiedParticipant).execute();
        queryFactory.delete(QVerifiedUser.verifiedUser).execute();
        queryFactory.delete(QEmail.email).execute();
        queryFactory.delete(QLocation.location).execute();
        queryFactory.delete(QDecisionAttachment.decisionAttachment).execute();
        queryFactory.delete(QMunicipalityUser.municipalityUser).execute();
        queryFactory.delete(QFollowInitiative.followInitiative).execute();
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QInfoText.infoText).execute();
        queryFactory.delete(QAdminUser.adminUser).execute();
        authorLoginUserHolder = null;
        lastInitiativeId = null;
        lastAuthorId = null;
        lastVerifiedUserId = null;
        previousUserSsnHash = null;
        previousTestManagementHash = null;
        lastMunicipalityHash = null;
    }

    @Transactional
    public Long createTestMunicipality(String name) {
        return createTestMunicipality(name, true);
    }

    @Transactional
    public Long createTestMunicipality(String name, boolean isActive) {
        return queryFactory.insert(QMunicipality.municipality)
                .set(QMunicipality.municipality.name, name)
                .set(QMunicipality.municipality.nameSv, name + " sv")
                .set(QMunicipality.municipality.email, toEmail(name))
                .set(QMunicipality.municipality.active, isActive)
                .executeWithKey(QMunicipality.municipality.id);
    }

    public Municipality createMunicipalityDraft(Long fakeId, String name) {
        return new Municipality(fakeId, name+"fi", name+"sv", true);
    }

    public static String toEmail(String name) {
        return name.replace(" ", "_") + "@example.com";
    }


    @Transactional
    public Long createCollaborativeReview(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.REVIEW)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long createCollaborativeAccepted(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.ACCEPTED)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());
    }
    @Transactional
    public Long createCollaborativeAcceptedWithLocationInformation(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.ACCEPTED)
                .withType(InitiativeType.COLLABORATIVE)
                .withLocations(LOCATIONS)
                .withVideoUrl(VALID_VIDEO_URL)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long create(Long municipalityId, InitiativeState state, InitiativeType type) {
        InitiativeDraft initiativeDraft = new InitiativeDraft(municipalityId)
                .withState(state)
                .withType(type)
                .applyAuthor().toInitiativeDraft();
        if (type == InitiativeType.SINGLE && state == InitiativeState.PUBLISHED) {
            initiativeDraft.withSent(DateTime.now());
        }
        return createDefaultInitiative(initiativeDraft);
    }

    @Transactional
    public Long createSingleSent(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.SINGLE)
                .withSent(SENT_TIME)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long createDraft(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.DRAFT)
                .applyAuthor().toInitiativeDraft());
    }
    
    @Transactional
    public Long createEmptyDraft(Long municipalityId) {
        return createDefaultInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.DRAFT)
                .withType(InitiativeType.UNDEFINED)
                .withName(null)
                .withProposal(null)
                .applyAuthor()
                .withParticipantName(null)
                .withAuthorAddress(null)
                .withAuthorPhone(null)
                .toInitiativeDraft());
    }

    @Transactional
    public Long createDefaultInitiative(InitiativeDraft initiativeDraft) {
        return createInitiative(initiativeDraft, false);
    }

    @Transactional
    public Long createVerifiedInitiative(InitiativeDraft initiativeDraft) {
        return createInitiative(initiativeDraft, true);
    }

    private Long createInitiative(InitiativeDraft initiativeDraft, boolean verified) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.name, initiativeDraft.name);
        insert.set(municipalityInitiative.proposal, initiativeDraft.proposal);
        insert.set(municipalityInitiative.municipalityId, initiativeDraft.municipalityId);
        insert.set(municipalityInitiative.participantCount, initiativeDraft.participantCount);
        insert.set(municipalityInitiative.extraInfo, initiativeDraft.extraInfo);
        insert.set(municipalityInitiative.externalparticipantcount, initiativeDraft.externalParticipantCount);

        insert.set(municipalityInitiative.state, initiativeDraft.state);

        if (initiativeDraft.type.isNotVerifiable() && verified) {
            insert.set(municipalityInitiative.type, InitiativeType.COLLABORATIVE_COUNCIL);
        }
        else {
            insert.set(municipalityInitiative.type, initiativeDraft.type);
        }

        insert.set(municipalityInitiative.sent, initiativeDraft.sent);
        insert.set(municipalityInitiative.modified, initiativeDraft.modified);
        insert.set(municipalityInitiative.sentComment, initiativeDraft.sentComment);
        insert.set(municipalityInitiative.fixState, initiativeDraft.fixState);
        insert.set(municipalityInitiative.moderatorComment, initiativeDraft.moderatorComment);
        insert.set(municipalityInitiative.stateTimestamp, initiativeDraft.stateTime);
        insert.set(municipalityInitiative.lastEmailReportTime, initiativeDraft.emailReportDateTime);
        insert.set(municipalityInitiative.lastEmailReportType, initiativeDraft.emailReportType);


        if (initiativeDraft.supporCountData != null) {
            insert.set(municipalityInitiative.supportCountData, initiativeDraft.supporCountData);
        }

        if (initiativeDraft.videoUrl.isPresent()) {
            insert.set(municipalityInitiative.videoUrl, initiativeDraft.videoUrl.getValue());
        }


        if (initiativeDraft.decisionDate.isPresent()) {
            insert.set(municipalityInitiative.municipalityDecisionDate, initiativeDraft.decisionDate.getValue());
        }

        lastInitiativeId = insert.executeWithKey(municipalityInitiative.id);

        if (initiativeDraft.locations.size() > 0  ) {
            createLocations(initiativeDraft.locations, lastInitiativeId);
        }

        if (initiativeDraft.authorDraft.isPresent()) {
            initiativeDraft.authorDraft.get().withInitiativeId(lastInitiativeId);
            if (!verified) {
                createDefaultAuthorAndParticipant(initiativeDraft.authorDraft.get());
            }
            else {
                createVerifiedAuthorAndParticipant(initiativeDraft.authorDraft.get());
            }
        }
        return lastInitiativeId;
    }

    @Transactional(readOnly = false)
    private void createLocations(List<Location> locations, Long initiativeId) {
        for(Location location : locations) {
            queryFactory.insert(QLocation.location)
                    .set(QLocation.location.initiativeId, initiativeId)
                    .set(QLocation.location.locationLat, location.getLat())
                    .set(QLocation.location.locationLng, location.getLng()).execute();
        }
    }

    @Transactional(readOnly = false)
    public Long createDefaultAuthorAndParticipant(AuthorDraft authorDraft) {
        Long lastParticipantId = createDefaultParticipant(authorDraft);
        lastAuthorId = lastParticipantId;
        queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.address, authorDraft.authorAddress)
                .set(QAuthor.author.phone, authorDraft.authorPhone)
                .set(QAuthor.author.participantId, lastParticipantId)
                .set(QAuthor.author.managementHash, generateHash(40))
                .set(QAuthor.author.initiativeId, authorDraft.initiativeId)
                .execute();
        authorLoginUserHolder = new LoginUserHolder(User.normalUser(new NormalAuthorId(lastAuthorId), Collections.singleton(lastInitiativeId)));

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress(authorDraft.authorAddress);
        contactInfo.setPhone(authorDraft.authorPhone);
        contactInfo.setEmail(authorDraft.participantEmail);
        contactInfo.setName(authorDraft.participantName);
        contactInfo.setShowName(true);
        return lastAuthorId;
    }

    @Transactional(readOnly = false)
    public Long createVerifiedAuthorAndParticipant(AuthorDraft authorDraft) {
        SQLInsertClause insertVerifiedUser = queryFactory.insert(QVerifiedUser.verifiedUser)
                .set(QVerifiedUser.verifiedUser.hash, authorDraft.userSsn.isNotPresent() ? createUserSsnHash() : encryptionService.registeredUserHash(authorDraft.userSsn.get()))
                .set(QVerifiedUser.verifiedUser.address, authorDraft.authorAddress)
                .set(QVerifiedUser.verifiedUser.phone, authorDraft.authorPhone)
                .set(QVerifiedUser.verifiedUser.email, authorDraft.participantEmail)
                .set(QVerifiedUser.verifiedUser.name, authorDraft.participantName);

        if (authorDraft.participantMunicipality == null) {
            insertVerifiedUser.setNull(QVerifiedUser.verifiedUser.municipalityId);
        } else {
            insertVerifiedUser.set(QVerifiedUser.verifiedUser.municipalityId, authorDraft.participantMunicipality);
        }

        Long verifiedUserId = insertVerifiedUser.executeWithKey(QVerifiedUser.verifiedUser.id);

        queryFactory.insert(QVerifiedAuthor.verifiedAuthor)
                .set(QVerifiedAuthor.verifiedAuthor.initiativeId, authorDraft.initiativeId)
                .set(QVerifiedAuthor.verifiedAuthor.verifiedUserId, verifiedUserId)
                .execute();

        queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.showName, authorDraft.publicName)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, authorDraft.initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, verifiedUserId)
                .set(QVerifiedParticipant.verifiedParticipant.verified, authorDraft.participantMunicipality != null)
                .execute();

        increaseParticipantCount(authorDraft);

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setAddress(authorDraft.authorAddress);
        contactInfo.setPhone(authorDraft.authorPhone);
        contactInfo.setEmail(authorDraft.participantEmail);
        contactInfo.setName(authorDraft.participantName);
        contactInfo.setShowName(true);

        this.lastVerifiedUserId = verifiedUserId;
        Maybe<Municipality> participantMunicipality;
        if (authorDraft.participantMunicipality == null) {
            participantMunicipality = Maybe.absent();
        } else {
            participantMunicipality = Maybe.of(new Municipality(authorDraft.participantMunicipality, "name_fi", "name_sv", true));
        }
        authorLoginUserHolder = new LoginUserHolder(User.verifiedUser(new VerifiedUserId(verifiedUserId), previousUserSsnHash, contactInfo,
                Collections.singleton(authorDraft.initiativeId),
                Collections.singleton(authorDraft.initiativeId),
                participantMunicipality));

        return lastVerifiedUserId;
    }

    private void increaseParticipantCount(AuthorDraft authorDraft) {
        SQLUpdateClause updateClause = queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.add(1));

        if (authorDraft.publicName) {
            updateClause.set(QMunicipalityInitiative.municipalityInitiative.participantCountPublic, QMunicipalityInitiative.municipalityInitiative.participantCountPublic.add(1));
        }

        updateClause.where(QMunicipalityInitiative.municipalityInitiative.id.eq(authorDraft.initiativeId)).execute();
    }

    @Transactional(readOnly = false)
    public void createVerifiedParticipant(AuthorDraft authorDraft) {

        Long verifiedUserId = queryFactory.insert(QVerifiedUser.verifiedUser)
                .set(QVerifiedUser.verifiedUser.hash, createUserSsnHash())
                .set(QVerifiedUser.verifiedUser.address, authorDraft.authorAddress)
                .set(QVerifiedUser.verifiedUser.phone, authorDraft.authorPhone)
                .set(QVerifiedUser.verifiedUser.email, authorDraft.participantEmail)
                .set(QVerifiedUser.verifiedUser.name, authorDraft.participantName)
                .set(QVerifiedUser.verifiedUser.municipalityId, authorDraft.participantMunicipality)
                .executeWithKey(QVerifiedUser.verifiedUser.id);

        queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.showName, authorDraft.publicName)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, authorDraft.initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, verifiedUserId)
                .set(QVerifiedParticipant.verifiedParticipant.verified, authorDraft.participantMunicipality != null)
                .execute();

        increaseParticipantCount(authorDraft);
        this.lastVerifiedUserId = verifiedUserId;
    }
    @Transactional(readOnly = false)
    public Long createVerifiedParticipantWithDate(AuthorDraft authorDraft, org.joda.time.LocalDate date) {

        Long verifiedUserId = queryFactory.insert(QVerifiedUser.verifiedUser)
                .set(QVerifiedUser.verifiedUser.hash, createUserSsnHash())
                .set(QVerifiedUser.verifiedUser.address, authorDraft.authorAddress)
                .set(QVerifiedUser.verifiedUser.phone, authorDraft.authorPhone)
                .set(QVerifiedUser.verifiedUser.email, authorDraft.participantEmail)
                .set(QVerifiedUser.verifiedUser.name, authorDraft.participantName)
                .set(QVerifiedUser.verifiedUser.municipalityId, authorDraft.participantMunicipality)
                .executeWithKey(QVerifiedUser.verifiedUser.id);

        Long id = queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.showName, authorDraft.publicName)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, authorDraft.initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, verifiedUserId)
                .set(QVerifiedParticipant.verifiedParticipant.verified, authorDraft.participantMunicipality != null)
                .set(QVerifiedParticipant.verifiedParticipant.participateTime, date)
                .execute();

        increaseParticipantCount(authorDraft);
        this.lastVerifiedUserId = verifiedUserId;

        return id;
    }

    @Transactional(readOnly = false)
    public Long createVerifiedParticipantWithVerifiedUserId(AuthorDraft authorDraft) {

        Long id = queryFactory.insert(QVerifiedParticipant.verifiedParticipant)
                .set(QVerifiedParticipant.verifiedParticipant.showName, authorDraft.publicName)
                .set(QVerifiedParticipant.verifiedParticipant.initiativeId, authorDraft.initiativeId)
                .set(QVerifiedParticipant.verifiedParticipant.verifiedUserId, authorDraft.verifiedUserId.getValue())
                .set(QVerifiedParticipant.verifiedParticipant.verified, authorDraft.participantMunicipality != null)
                .execute();

        increaseParticipantCount(authorDraft);

        return id;
    }

    @Transactional(readOnly = false)
    public Long createVerifiedUser(AuthorDraft authorDraft){
        String hash = createUserSsnHash();
        Long verifiedUserId = queryFactory.insert(QVerifiedUser.verifiedUser)
                .set(QVerifiedUser.verifiedUser.hash, hash)
                .set(QVerifiedUser.verifiedUser.address, authorDraft.authorAddress)
                .set(QVerifiedUser.verifiedUser.phone, authorDraft.authorPhone)
                .set(QVerifiedUser.verifiedUser.email, authorDraft.participantEmail)
                .set(QVerifiedUser.verifiedUser.name, authorDraft.participantName)
                .set(QVerifiedUser.verifiedUser.municipalityId, authorDraft.participantMunicipality)
                .executeWithKey(QVerifiedUser.verifiedUser.id);

        this.lastVerifiedUserId = verifiedUserId;

        Maybe<Municipality> participantMunicipality = Maybe.of(new Municipality(authorDraft.participantMunicipality, "name_fi", "name_sv", true));
        lastLoggedInVerifiedUserHolder = new LoginUserHolder(User.verifiedUser(new VerifiedUserId(verifiedUserId), hash, new ContactInfo(), null, null, participantMunicipality));
        return verifiedUserId;
    }

    private String createUserSsnHash() {
        previousUserSsnHash = RandomHashGenerator.shortHash();
        return previousUserSsnHash;
    }

    @Transactional(readOnly = false)
    public Long createDefaultParticipant(AuthorDraft authorDraft) {

        increaseParticipantCount(authorDraft);

        return queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, authorDraft.participantMunicipality)
                .set(QParticipant.participant.municipalityInitiativeId, authorDraft.initiativeId)
                .set(QParticipant.participant.name, authorDraft.participantName)
                .set(QParticipant.participant.showName, authorDraft.publicName)
                .set(QParticipant.participant.email, authorDraft.participantEmail)
                .set(QParticipant.participant.membershipType, authorDraft.municipalityMembership)
                .executeWithKey(QParticipant.participant.id);
    }

    @Transactional(readOnly = false)
    public Long createDefaultParticipantWithDate(AuthorDraft authorDraft, LocalDate date, String confirmationCode) {

        increaseParticipantCount(authorDraft);

        SQLInsertClause set = queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, authorDraft.participantMunicipality)
                .set(QParticipant.participant.municipalityInitiativeId, authorDraft.initiativeId)
                .set(QParticipant.participant.name, authorDraft.participantName)
                .set(QParticipant.participant.showName, authorDraft.publicName)
                .set(QParticipant.participant.email, authorDraft.participantEmail)
                .set(QParticipant.participant.membershipType, authorDraft.municipalityMembership)
                .set(QParticipant.participant.participateTime, date);

        if (confirmationCode != null) {
            set.set(QParticipant.participant.confirmationCode, confirmationCode);
        }
        return set.executeWithKey(QParticipant.participant.id);
    }

    public VerifiedUser getVerifiedUser() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("Paavo Paavolainen");
        return User.verifiedUser(new VerifiedUserId(123L), "ffafdsf", contactInfo, null, null, Maybe.of(new Municipality(1, "Oulu", "Åbo", true)));
    }

    @Transactional(readOnly = false)
    public Long createDefaultParticipantWithDate(AuthorDraft authorDraft, LocalDate date) {
        return createDefaultParticipantWithDate(authorDraft, date, null);
    }

    private String generateHash(int len) {
        previousTestManagementHash = RandomHashGenerator.randomString(len);
        return previousTestManagementHash;
    }

    @Transactional
    public Long countAll(RelationalPathBase from) {
        return queryFactory.from(from).count();
    }

    @Transactional
    public Long countAll(RelationalPathBase from, Predicate where) {
        return queryFactory.from(from).where(where).count();
    }

    @Transactional(readOnly = false)
    public void updateField(Long initiativeId, Path field, Object value) {
        long affectedRows = queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(field, value)
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute();

        assertThat("Unable to update initiative", affectedRows, is(1L));
    }

    @Transactional(readOnly = false)
    public AuthorInvitation createInvitation(Long initiativeId, String contactName, String contactEmail) {
        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setInvitationTime(new DateTime());
        authorInvitation.setEmail(contactEmail);
        authorInvitation.setName(contactName);
        authorInvitation.setConfirmationCode(generateHash(10));
        authorInvitation.setInitiativeId(initiativeId);

        queryFactory.insert(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.initiativeId, authorInvitation.getInitiativeId())
                .set(QAuthorInvitation.authorInvitation.invitationTime, authorInvitation.getInvitationTime())
                .set(QAuthorInvitation.authorInvitation.name, authorInvitation.getName())
                .set(QAuthorInvitation.authorInvitation.email, authorInvitation.getEmail())
                .set(QAuthorInvitation.authorInvitation.confirmationCode, authorInvitation.getConfirmationCode())
                .execute();

        return authorInvitation;
    }

    @Transactional(readOnly = false)
    public Long createInfoText(LanguageCode languageCode,
                               InfoTextCategory category,
                               int orderPosition,
                               String uri_fi,
                               String subject_fi,
                               String draft_subject_fi,
                               String text_fi,
                               String draft_fi,
                               DateTime modified,
                               String modifierName) {
        return createInfoText(languageCode, category, orderPosition, uri_fi, subject_fi, draft_subject_fi, text_fi, draft_fi, modified, modifierName, false);
    }

    @Transactional(readOnly = false)
    public Long createInfoText(LanguageCode languageCode,
                               InfoTextCategory category,
                               int orderPosition,
                               String uri_fi,
                               String subject_fi,
                               String draft_subject_fi,
                               String text_fi,
                               String draft_fi,
                               DateTime modified,
                               String modifierName,
                               boolean footerDisplay) {
        return queryFactory.insert(QInfoText.infoText)
                .set(QInfoText.infoText.languagecode, languageCode)
                .set(QInfoText.infoText.category, category)
                .set(QInfoText.infoText.draft, draft_fi)
                .set(QInfoText.infoText.uri, uri_fi)
                .set(QInfoText.infoText.publishedSubject, subject_fi)
                .set(QInfoText.infoText.draftSubject, draft_subject_fi)
                .set(QInfoText.infoText.published, text_fi)
                .set(QInfoText.infoText.orderposition, orderPosition)
                .set(QInfoText.infoText.modified, modified)
                .set(QInfoText.infoText.modifier, modifierName)
                .set(QInfoText.infoText.footerDisplay, footerDisplay)
                .executeWithKey(QInfoText.infoText.id);
    }

    @Transactional(readOnly = false)
    public void createTestAdminUser(String userName, String password, String name) {

        queryFactory.delete(QAdminUser.adminUser)
                .where(QAdminUser.adminUser.username.eq(userName))
                .execute();

        queryFactory.insert(QAdminUser.adminUser)
                .set(QAdminUser.adminUser.username, userName)
                .set(QAdminUser.adminUser.password, EncryptionService.toSha1(environment.getProperty(PropertyNames.omUserSalt) + password))
                .set(QAdminUser.adminUser.name, name)
                .execute();
    }

    @Transactional(readOnly = false)
    public Long createAuthorMessage(AuthorMessage authorMessage) {
        return queryFactory.insert(QAuthorMessage.authorMessage)
                .set(QAuthorMessage.authorMessage.message, authorMessage.getMessage())
                .set(QAuthorMessage.authorMessage.confirmationCode, authorMessage.getConfirmationCode())
                .set(QAuthorMessage.authorMessage.contactor, authorMessage.getContactName())
                .set(QAuthorMessage.authorMessage.contactorEmail, authorMessage.getContactEmail())
                .set(QAuthorMessage.authorMessage.initiativeId, authorMessage.getInitiativeId())
                .execute();


    }

    @Transactional(readOnly = true)
    public NormalParticipant getUniqueNormalParticipant(Long initiativeId) {
        return queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(initiativeId))
                .leftJoin(participant.participantMunicipalityFk, QMunicipality.municipality)
                .leftJoin(participant._verifiedUserNormalInitiativesParticipantId, QVerifiedUserNormalInitiatives.verifiedUserNormalInitiatives)
                .orderBy(participant.id.desc())
                .uniqueResult(JdbcParticipantDao.normalParticipantMapping);
    }

    @Transactional(readOnly = true)
    public AuthorInvitation getAuthorInvitation(String confirmationCode) {
        return queryFactory.from(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .uniqueResult(JdbcAuthorDao.authorInvitationMapping);
    }

    @Transactional(readOnly = false)
    public Long addAuthorInvitation(AuthorInvitation authorInvitation, boolean rejected) {
        return queryFactory.insert(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.confirmationCode, authorInvitation.getConfirmationCode())
                .set(QAuthorInvitation.authorInvitation.email, authorInvitation.getEmail())
                .set(QAuthorInvitation.authorInvitation.name, authorInvitation.getName())
                .set(QAuthorInvitation.authorInvitation.invitationTime, authorInvitation.getInvitationTime())
                .set(QAuthorInvitation.authorInvitation.initiativeId, authorInvitation.getInitiativeId())
                .set(QAuthorInvitation.authorInvitation.rejectTime, rejected ? new DateTime() : null)
                .execute();
    }

    public static ContactInfo defaultContactInfo() {
        ContactInfo contactInfo = new ContactInfo();

        contactInfo.setEmail(DEFAULT_PARTICIPANT_EMAIL);
        contactInfo.setPhone(DEFAULT_AUTHOR_PHONE);
        contactInfo.setName(DEFAULT_PARTICIPANT_NAME);
        contactInfo.setAddress(DEFAULT_AUTHOR_ADDRESS);
        contactInfo.setShowName(DEFAULT_PUBLIC_NAME);
        return contactInfo;
    }

    @Transactional
    public void simpleCreate(Long municipality, InitiativeState published, InitiativeType type, DateTime now) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.name, "name");
        insert.set(municipalityInitiative.proposal, "proposal");
        insert.set(municipalityInitiative.municipalityId, municipality);
        insert.set(municipalityInitiative.participantCount, 0);
        insert.set(municipalityInitiative.extraInfo, "");
        insert.set(municipalityInitiative.externalparticipantcount, 0);
        insert.set(municipalityInitiative.state, published);
        insert.set(municipalityInitiative.type, type);
        insert.set(municipalityInitiative.fixState, FixState.OK);
        insert.set(municipalityInitiative.stateTimestamp, now);
        insert.execute();
    }

    @Transactional(readOnly = true)
    public EmailDto getSingleQueuedEmail() {
        List<EmailDto> queuedEmails = findQueuedEmails();
        assertThat(queuedEmails, hasSize(1));
        return queuedEmails.get(0);
    }

    @Transactional(readOnly = false)
    public Long createRandomEmail(Long initiativeId, EmailAttachmentType attachmentType) {
        return queryFactory.insert(QEmail.email)
                .set(QEmail.email.attachment, attachmentType)
                .set(QEmail.email.subject, randomString())
                .set(QEmail.email.bodyHtml, randomString())
                .set(QEmail.email.bodyText, randomString())
                .set(QEmail.email.initiativeId, initiativeId)
                .set(QEmail.email.recipients, randomString())
                .set(QEmail.email.sender, randomString())
                .set(QEmail.email.replyTo, randomString())
                .executeWithKey(QEmail.email.id);
    }

    private static String randomString() {
        return String.valueOf(new Random().nextLong());
    }

    @Transactional(readOnly = true)
    public EmailDto getEmail(Long emailId) {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.id.eq(emailId))
                .uniqueResult(JdbcEmailDao.emailMapping);
    }

    @Transactional(readOnly = false)
    public Long createUnconfirmedParticipant(AuthorDraft authorDraft, String confirmCode) {
        Long defaultParticipant = createDefaultParticipant(authorDraft);
        queryFactory.update(QParticipant.participant)
                .where(QParticipant.participant.id.eq(defaultParticipant))
                .set(QParticipant.participant.confirmationCode, confirmCode)
                .execute();
        return defaultParticipant;
    }

    @Transactional(readOnly = true)
    public List<ReviewHistoryRow> getInitiativeReviewHistory(Long initiativeId) {
        return queryFactory.from(QReviewHistory.reviewHistory)
                .where(QReviewHistory.reviewHistory.initiativeId.eq(initiativeId))
                .list(JdbcReviewHistoryDao.reviewHistoryRowWrapper);
    }
    @Transactional(readOnly = true)
    public List<Long> getAllMunicipalities(){
        return queryFactory.from(QMunicipality.municipality)
                .list(QMunicipality.municipality.id);
    }

    @Transactional
    public void clearSentEmails() {
        queryFactory.delete(QEmail.email).execute();
    }

    @Transactional
    public void sendToMunicipality(Long verifiedInitiativeId) {

        String managementHash = RandomHashGenerator.longHash();
        queryFactory.insert(QMunicipalityUser.municipalityUser)
                .set(QMunicipalityUser.municipalityUser.initiativeId, verifiedInitiativeId)
                .set(QMunicipalityUser.municipalityUser.managementHash, managementHash)
                .executeWithKey(QMunicipalityUser.municipalityUser.id);

        lastMunicipalityHash =  managementHash;

    }

    public String getPreviousMunicipalityHash() {
        return lastMunicipalityHash;
    }

    @Transactional
    public String addFollower(Long initiativeId, String s) {
        String randomHash =  RandomHashGenerator.shortHash();
        queryFactory.insert(QFollowInitiative.followInitiative)
                .set(QFollowInitiative.followInitiative.initiativeId, initiativeId)
                .set(QFollowInitiative.followInitiative.email, s)
                .set(QFollowInitiative.followInitiative.unsubscribeHash, randomHash)
                .execute();

        return randomHash;
    }

    @Transactional
    public void addVideo(Long initiativeId, String videoName, String videoUrl) {
        queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.videoUrl, videoUrl)
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute();
    }


    public static class AuthorDraft {

        public Long initiativeId;

        public final Maybe<InitiativeDraft> initiativeDraftMaybe;
        public Long participantMunicipality;
        public Membership municipalityMembership = Membership.none;
        public String participantName = DEFAULT_PARTICIPANT_NAME;
        public String participantEmail = DEFAULT_PARTICIPANT_EMAIL;
        public boolean publicName = DEFAULT_PUBLIC_NAME;
        public String authorAddress = DEFAULT_AUTHOR_ADDRESS;
        public String authorPhone = DEFAULT_AUTHOR_PHONE;
        public Maybe<String> userSsn = Maybe.absent();
        public Maybe<Long> verifiedUserId = Maybe.absent();

        public AuthorDraft(Long initiativeId, Long participantMunicipality) {
            this.initiativeId = initiativeId;
            this.initiativeDraftMaybe = Maybe.absent();
            this.participantMunicipality = participantMunicipality;
        }

        private AuthorDraft(InitiativeDraft initiativeDraft, Long participantMunicipality) {
            this.initiativeDraftMaybe = Maybe.of(initiativeDraft);
            this.participantMunicipality = participantMunicipality;
        }

        public AuthorDraft withMunicipalityMembership(Membership municipalityMembership) {
            this.municipalityMembership = municipalityMembership;
            return this;
        }

        public AuthorDraft withParticipantName(String participantName) {
            this.participantName = participantName;
            return this;
        }

        public AuthorDraft withParticipantEmail(String authorEmail) {
            this.participantEmail = authorEmail;
            return this;
        }

        public AuthorDraft withAuthorAddress(String authorAddress) {
            this.authorAddress = authorAddress;
            return this;
        }


        public AuthorDraft withAuthorPhone(String authorPhone) {
            this.authorPhone = authorPhone;
            return this;
        }

        public AuthorDraft withPublicName(boolean publicName) {
            this.publicName = publicName;
            return this;
        }

        public AuthorDraft withParticipantMunicipality(Long municipalityId) {
            this.participantMunicipality = municipalityId;
            return this;
        }

        public InitiativeDraft toInitiativeDraft() {
            return initiativeDraftMaybe.get();
        }

        public AuthorDraft withInitiativeId(Long lastInitiativeId) {
            initiativeId = lastInitiativeId;
            return this;
        }
        public AuthorDraft withVerifiedUserId(Long verifiedUserId) {
            this.verifiedUserId = Maybe.of(verifiedUserId);
            return this;
        }

    }
    public static class InitiativeDraft {

        public final Long municipalityId;

        public String name = DEFAULT_INITIATIVE_NAME;

        public String proposal = DEFAULT_PROPOSAL;
        public String extraInfo = DEFAULT_EXTRA_INFO;
        public String sentComment = DEFAULT_SENT_COMMENT;
        public InitiativeState state = DEFAULT_STATE;
        public InitiativeType type = DEFAULT_TYPE;
        public DateTime sent = DEFAULT_SENT_TIME;

        public DateTime modified = DEFAULT_CREATE_TIME;
        public DateTime stateTime = DEFAULT_STATE_TIME;
        public Integer participantCount = 0;

        public Maybe<AuthorDraft> authorDraft = Maybe.absent();
        public FixState fixState = FixState.OK;
        public String moderatorComment;
        public Integer externalParticipantCount = DEFAULT_EXTERNAL_PARTICIPANT_COUNT;
        public EmailReportType emailReportType;
        public DateTime emailReportDateTime;
        public String supporCountData;
        public List<Location> locations = new ArrayList<>();
        private Maybe<String> videoUrl = Maybe.absent();
        private Maybe<String> videoName = Maybe.absent();
        private Maybe<DateTime> decisionDate = Maybe.absent();


        public AuthorDraft applyAuthor() {
            this.authorDraft = Maybe.of(new AuthorDraft(this, municipalityId));
            return this.authorDraft.get();
        }

        public AuthorDraft applyAuthor(String userSsn) {
            this.authorDraft = Maybe.of(new AuthorDraft(this, municipalityId));
            this.authorDraft.get().userSsn = Maybe.of(userSsn);

            return this.authorDraft.get();
        }

        public InitiativeDraft(Long municipalityId) {
            this.municipalityId = municipalityId;
        }

        public InitiativeDraft withModified(DateTime created) {
            this.modified = created;
            return this;
        }

        public InitiativeDraft withName(String name) {
            this.name = name;
            return this;
        }

        public InitiativeDraft withProposal(String proposal) {
            this.proposal = proposal;
            return this;
        }

        public InitiativeDraft withState(InitiativeState state) {
            this.state = state;
            return this;
        }

        public InitiativeDraft withFixState(FixState fixState) {
            this.fixState = fixState;
            return this;
        }

        public InitiativeDraft withType(InitiativeType type) {
            this.type = type;
            return this;
        }

        public InitiativeDraft withSent(DateTime dateTime) {
            this.sent = dateTime;
            return this;
        }

        public InitiativeDraft withParticipantCount(Integer participantCount) {
            this.participantCount = participantCount;
            return this;
        }

        public InitiativeDraft withExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
            return this;
        }

        public InitiativeDraft withSentComment(String sentComment) {
            this.sentComment = sentComment;
            return this;
        }

        public InitiativeDraft withModeratorComment(String moderatorComment) {
            this.moderatorComment = moderatorComment;
            return this;
        }

        public InitiativeDraft withExternalParticipantCount(int externalParticipantCount) {
            this.externalParticipantCount = externalParticipantCount;
            return this;
        }

        public InitiativeDraft withStateTime(DateTime stateTime) {
            this.stateTime = stateTime;
            return this;
        }

        public InitiativeDraft witEmailReportSent(EmailReportType emailReportType, DateTime dateTime) {
            this.emailReportType = emailReportType;
            this.emailReportDateTime = dateTime;
            return this;
        }

        public InitiativeDraft withSupporCountData(String s) {
            this.supporCountData = s;
            return this;
        }
        public InitiativeDraft withLocations(List<Location> locations) {
            this.locations = new ArrayList<>(locations);
            return this;
        }

        public InitiativeDraft withVideoUrl(String url) {
            this.videoUrl = Maybe.of(url);
            return this;
        }

        public InitiativeDraft withVideoName(String videoname) {
            this.videoName = Maybe.of(videoname);
            return this;
        }

        public InitiativeDraft withDecisionDate(DateTime dateTime){
            this.decisionDate = Maybe.of(dateTime);
            return this;
        }
    }
    public Long getLastInitiativeId() {
        return lastInitiativeId;
    }

    @Transactional
    public Long getLastParticipantId() {
        return queryFactory.from(QParticipant.participant).orderBy(QParticipant.participant.id.desc()).list(QParticipant.participant.id).get(0);
    }

    @Transactional
    public NormalAuthorId getLastNormalAuthorId() {
        return new NormalAuthorId(queryFactory.from(QAuthor.author).orderBy(QAuthor.author.participantId.desc()).list(QAuthor.author.participantId).get(0));
    }

    @Transactional
    public Initiative getInitiative(Long id) {
        return queryFactory.from(QMunicipalityInitiative.municipalityInitiative)
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(id))
                .innerJoin(municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .uniqueResult(JdbcInitiativeDao.initiativeInfoMapping);
    }

    @Transactional
    public List<EmailDto> findQueuedEmails() {
        List<EmailDto> list = queryFactory.from(QEmail.email)
                .where(QEmail.email.tried.eq(false))
                .orderBy(QEmail.email.id.asc())
                .list(JdbcEmailDao.emailMapping);
        for (EmailDto emailDto : list) {
            ReflectionTestUtils.assertNoNullFields(emailDto);
        }
        return list;
    }

    @Transactional
    public List<EmailDto> findTriedEmails() {
        return queryFactory.from(QEmail.email)
                .where(QEmail.email.tried.eq(true))
                .orderBy(QEmail.email.id.asc())
                .list(JdbcEmailDao.emailMapping);
    }

    @Transactional(readOnly = false)
    public Long addAttachment(Long initiativeId, String description, boolean accepted, String fileType) {
        return addAttachment(initiativeId, description, accepted, fileType, null);
    }

    @Transactional(readOnly = false)
    public Long addAttachment(Long initiativeId, String description, boolean accepted, String fileType, Long forcedAttachmentId) {
        String contentType;

        switch (fileType.toLowerCase()) {
            case "jpg":
                contentType = MediaType.JPEG.toString();
                break;
            case "pdf":
                contentType = MediaType.PDF.toString();
                break;
            default:
                throw new IllegalArgumentException("no test-implementatino for filetype: " + fileType);
        }

        SQLInsertClause insert = queryFactory.insert(QAttachment.attachment)
                .set(QAttachment.attachment.initiativeId, initiativeId)
                .set(QAttachment.attachment.description, description)
                .set(QAttachment.attachment.contentType, contentType)
                .set(QAttachment.attachment.fileType, fileType)
                .set(QAttachment.attachment.accepted, accepted);

        if (forcedAttachmentId != null) {
            insert.set(QAttachment.attachment.id, forcedAttachmentId);
        }

        return insert.executeWithKey(QAttachment.attachment.id);
    }

    public String getPreviousTestManagementHash() {
        return previousTestManagementHash;
    }

    public String getPreviousUserSsnHash() {
        return previousUserSsnHash;
    }

    public Long getLastVerifiedUserId() {
        return lastVerifiedUserId;
    }



    public void assertLocations(List<Location> locations, List<Location> exceptedLocations) {
        Assert.assertThat(locations.size(), Is.is(exceptedLocations.size()));

        sortLocations(locations);
        sortLocations(exceptedLocations);

        for (int i = 0; i<locations.size(); i++) {
            assertLocation(locations.get(i), exceptedLocations.get(i));
        }
    }


    public static void sortLocations(List<Location> locations) {
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location location1, Location location2) {
                return location1.getLat().compareTo(location2.getLat());
            }
        });
    }

    public static void assertLocation(Location location1, Location location2) {
        Assert.assertThat(location1, Is.is(notNullValue()));
        Assert.assertThat(location2, Is.is(notNullValue()));

        Assert.assertThat(convertToSixDecimals(location1.getLat()), Is.is(convertToSixDecimals(location2.getLat())));
        Assert.assertThat(convertToSixDecimals(location2.getLng()), Is.is(convertToSixDecimals(location2.getLng())));
    }

    private static Double convertToSixDecimals(Double decimal) {
        return Math.round(decimal*1000000.0)/1000000.0;
    }
}

