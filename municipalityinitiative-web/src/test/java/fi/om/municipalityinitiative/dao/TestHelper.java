package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Path;
import fi.om.municipalityinitiative.conf.PropertyNames;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Inject;

import java.util.Collections;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class TestHelper {

    public static String PREVIOUS_TEST_MANAGEMENT_HASH;
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
    public static final String DEFAULT_SENT_COMMENT = "some default sent comment";

    public static LoginUserHolder authorLoginUserHolder;
    public static LoginUserHolder unknownLoginUserHolder = new LoginUserHolder(User.anonym());
    public static OmLoginUserHolder omLoginUser = new OmLoginUserHolder(User.omUser(""));

    @Inject
    private Environment environment;

    @Resource
    PostgresQueryFactory queryFactory;

    private Long lastInitiativeId;
    private Long lastAuthorId;

    public TestHelper() {
    }

    public TestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QAuthorMessage.authorMessage).execute();
        queryFactory.delete(QAuthorInvitation.authorInvitation).execute();
        queryFactory.delete(QAuthor.author).execute();
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QMunicipality.municipality).execute();
        queryFactory.delete(QInfoText.infoText).execute();
        queryFactory.delete(QAdminUser.adminUser).execute();
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
                .set(QMunicipality.municipality.email, name.replace(" ", "_") + "@example.com")
                .set(QMunicipality.municipality.active, isActive)
                .executeWithKey(QMunicipality.municipality.id);
    }


    @Transactional
    public Long createCollaborativeReview(Long municipalityId) {
        return createInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.REVIEW)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long createCollaborativeAccepted(Long municipalityId) {
        return createInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.ACCEPTED)
                .withType(InitiativeType.COLLABORATIVE)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long create(Long municipalityId, InitiativeState state, InitiativeType type) {
        return createInitiative(new InitiativeDraft(municipalityId)
                .withState(state)
                .withType(type)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long createSingleSent(Long municipalityId) {
        return createInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.SINGLE)
                .withSent(SENT_TIME)
                .applyAuthor().toInitiativeDraft());
    }

    @Transactional
    public Long createDraft(Long municipalityId) {
        return createInitiative(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.DRAFT)
                .applyAuthor().toInitiativeDraft());
    }
    
    @Transactional
    public Long createEmptyDraft(Long municipalityId) {
        return createInitiative(new InitiativeDraft(municipalityId)
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
    public Long createInitiative(InitiativeDraft initiativeDraft) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.name, initiativeDraft.name);
        insert.set(municipalityInitiative.proposal, initiativeDraft.proposal);
        insert.set(municipalityInitiative.municipalityId, initiativeDraft.municipalityId);
        insert.set(municipalityInitiative.participantCount, initiativeDraft.participantCount);
        insert.set(municipalityInitiative.extraInfo, initiativeDraft.extraInfo);

        insert.set(municipalityInitiative.state, initiativeDraft.state);

        insert.set(municipalityInitiative.type, initiativeDraft.type);

        insert.set(municipalityInitiative.sent, initiativeDraft.sent);
        insert.set(municipalityInitiative.modified, initiativeDraft.modified);
        insert.set(municipalityInitiative.sentComment, initiativeDraft.sentComment);
        insert.set(municipalityInitiative.fixState, initiativeDraft.fixState);

        lastInitiativeId = insert.executeWithKey(municipalityInitiative.id);

        if (initiativeDraft.authorDraft.isPresent()) {
            initiativeDraft.authorDraft.get().withInitiativeId(lastInitiativeId);
            createAuthorAndParticipant(initiativeDraft.authorDraft.get());
//            stub(authorLoginUserHolder.getAuthorId()).toReturn(lastAuthorId);
//            stub(authorLoginUserHolder.getUser()).toReturn();
        }
        return lastInitiativeId;

    }

    @Transactional(readOnly = false)
    public Long createAuthorAndParticipant(AuthorDraft authorDraft) {
        Long lastParticipantId = createParticipant(authorDraft);
        lastAuthorId = lastParticipantId;
        queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.address, authorDraft.authorAddress)
                .set(QAuthor.author.phone, authorDraft.authorPhone)
                .set(QAuthor.author.participantId, lastParticipantId)
                .set(QAuthor.author.managementHash, generateHash(40))
                .execute();
        authorLoginUserHolder = new LoginUserHolder(User.normalUser(lastAuthorId, Collections.singleton(lastInitiativeId)));
        return lastAuthorId;
    }


    @Transactional(readOnly = false)
    public Long createParticipant(AuthorDraft authorDraft) {
        return queryFactory.insert(QParticipant.participant)
                        .set(QParticipant.participant.municipalityId, authorDraft.participantMunicipality)
                        .set(QParticipant.participant.municipalityInitiativeId, authorDraft.initiativeId)
                        .set(QParticipant.participant.name, authorDraft.participantName)
                        .set(QParticipant.participant.showName, authorDraft.publicName)
                        .set(QParticipant.participant.email, authorDraft.participantEmail)
                        .set(QParticipant.participant.membershipType, authorDraft.municipalityMembership)
                        .executeWithKey(QParticipant.participant.id);
    }

    private String generateHash(int len) {
        PREVIOUS_TEST_MANAGEMENT_HASH = RandomHashGenerator.randomString(len);
        return PREVIOUS_TEST_MANAGEMENT_HASH;
    }

    @Transactional
    public Long countAll(RelationalPathBase relationalPathBase) {
        return queryFactory.from(relationalPathBase).count();
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
                .executeWithKey(QAuthorInvitation.authorInvitation.id);

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
        public Integer participantCount = 1;

        public Maybe<AuthorDraft> authorDraft = Maybe.absent();
        public FixState fixState = FixState.OK;

        public AuthorDraft applyAuthor() {
            this.authorDraft = Maybe.of(new AuthorDraft(this, municipalityId));
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
    }

    public Long getLastInitiativeId() {
        return lastInitiativeId;
    }

    @Transactional
    public Long getLastParticipantId() {
        return queryFactory.from(QParticipant.participant).orderBy(QParticipant.participant.id.desc()).list(QParticipant.participant.id).get(0);
    }

    @Transactional
    public Long getLastAuthorId() {
        return queryFactory.from(QAuthor.author).orderBy(QAuthor.author.participantId.desc()).list(QAuthor.author.participantId).get(0);
    }


}

