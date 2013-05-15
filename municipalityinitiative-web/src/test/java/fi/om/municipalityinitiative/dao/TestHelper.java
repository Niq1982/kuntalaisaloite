package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Path;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.service.AccessDeniedException;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.*;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class TestHelper {

    public static String PREVIOUS_TEST_MANAGEMENT_HASH;
    public static final String DEFAULT_INITIATIVE_NAME = "Initiative name";
    public static final String DEFAULT_PROPOSAL = "Proposal";
    public static final InitiativeState DEFAULT_STATE = InitiativeState.DRAFT;
    public static final InitiativeType DEFAULT_TYPE = InitiativeType.UNDEFINED;
    public static final String DEFAULT_AUTHOR_NAME = "Antti Author";
    public static final String DEFAULT_AUTHOR_EMAIL = "author_email@example.com";
    public static final String DEFAULT_AUTHOR_ADDRESS = "author address";
    public static final String DEFAULT_AUTHOR_PHONE = "author phone";
    public static final boolean DEFAULT_PUBLIC_NAME = true;
    public static final String DEFAULT_EXTRA_INFO = "some default extraInfo";
    public static final DateTime DEFAULT_SENT_TIME = null;
    public static final DateTime SENT_TIME = new DateTime(2011, 1, 1, 0, 0);
    public static final DateTime DEFAULT_CREATE_TIME = DateTime.now();
    public static final String DEFAULT_SENT_COMMENT = "some default sent comment";

    public final static LoginUserHolder authorLoginUserHolder = mock(LoginUserHolder.class);
    public final static LoginUserHolder unknownLoginUserHolder = mock(LoginUserHolder.class);

    static {
        doThrow(new AccessDeniedException("Access denied")).when(unknownLoginUserHolder).assertManagementRightsForInitiative(anyLong());
    }

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
        queryFactory.delete(QAuthorInvitation.authorInvitation).execute();
        queryFactory.delete(QAuthor.author).execute();
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QMunicipality.municipality).execute();
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
    public Long createCollectableReview(Long municipalityId) {
        return create(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.REVIEW)
                .withType(InitiativeType.COLLABORATIVE));
    }

    @Transactional
    public Long createCollectableAccepted(Long municipalityId) {
        return create(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.ACCEPTED)
                .withType(InitiativeType.COLLABORATIVE));
    }

    @Transactional
    public Long create(Long municipalityId, InitiativeState state, InitiativeType type) {
        return create(new InitiativeDraft(municipalityId)
                .withState(state)
                .withType(type));
    }

    @Transactional
    public Long createSingleSent(Long municipalityId) {
        return create(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.PUBLISHED)
                .withType(InitiativeType.SINGLE)
                .withSent(SENT_TIME));
    }

    @Transactional
    public Long createDraft(Long municipalityId) {
        return create(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.DRAFT));
    }
    
    @Transactional
    public Long createEmptyDraft(Long municipalityId) {
        return create(new InitiativeDraft(municipalityId)
                .withState(InitiativeState.DRAFT)
                .withType(InitiativeType.UNDEFINED)
                .withName(null)
                .withProposal(null)
                .withAuthorName(null)
                .withAuthorPhone(null)
                .withAuthorAddress(null));
    }

    @Transactional
    public Long create(InitiativeDraft initiativeDraft) {
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

        lastInitiativeId = insert.executeWithKey(municipalityInitiative.id);

        Long lastParticipantId = queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, initiativeDraft.authorMunicipality)
                .set(QParticipant.participant.municipalityInitiativeId, lastInitiativeId)
                .set(QParticipant.participant.name, initiativeDraft.authorName)
                .set(QParticipant.participant.showName, initiativeDraft.publicName)
                .set(QParticipant.participant.email, initiativeDraft.authorEmail)
                .set(QParticipant.participant.membershipType, initiativeDraft.municipalityMembership)
                .executeWithKey(QParticipant.participant.id);

        lastAuthorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.address, initiativeDraft.authorAddress)
                .set(QAuthor.author.phone, initiativeDraft.authorPhone)
                .set(QAuthor.author.participantId, lastParticipantId)
                .set(QAuthor.author.managementHash, generateHash(40))
                .executeWithKey(QAuthor.author.id);
        stub(authorLoginUserHolder.getAuthorId()).toReturn(lastAuthorId);

        return lastInitiativeId;

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

    public static class InitiativeDraft {

        public final Long municipalityId;

        public String name = DEFAULT_INITIATIVE_NAME;
        public String proposal = DEFAULT_PROPOSAL;
        public InitiativeState state = DEFAULT_STATE;
        public InitiativeType type = DEFAULT_TYPE;
        public String authorName = DEFAULT_AUTHOR_NAME;
        public String authorEmail = DEFAULT_AUTHOR_EMAIL;
        public String authorAddress = DEFAULT_AUTHOR_ADDRESS;
        public String authorPhone = DEFAULT_AUTHOR_PHONE;
        public boolean publicName = DEFAULT_PUBLIC_NAME;
        public DateTime sent = DEFAULT_SENT_TIME;
        public DateTime modified = DEFAULT_CREATE_TIME;
        public Integer participantCount = 1;
        public String extraInfo = DEFAULT_EXTRA_INFO;
        public String sentComment = DEFAULT_SENT_COMMENT;

        public Long authorMunicipality;
        public Membership municipalityMembership = Membership.none;

        public InitiativeDraft(Long municipalityId) {
            this.municipalityId = municipalityId;
            this.authorMunicipality = municipalityId;
        }

        public InitiativeDraft withMunicipalityMembership(Membership municipalityMembership) {
            this.municipalityMembership = municipalityMembership;
            return this;
        }

        public InitiativeDraft withAuthorMunicipality(Long municipalityId) {
            this.authorMunicipality = municipalityId;
            return this;
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

        public InitiativeDraft withType(InitiativeType type) {
            this.type = type;
            return this;
        }

        public InitiativeDraft withAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public InitiativeDraft withAuthorEmail(String authorEmail) {
            this.authorEmail = authorEmail;
            return this;
        }

        public InitiativeDraft withAuthorAddress(String authorAddress) {
            this.authorAddress = authorAddress;
            return this;
        }

        public InitiativeDraft withAuthorPhone(String authorPhone) {
            this.authorPhone = authorPhone;
            return this;
        }

        public InitiativeDraft withPublicName(boolean publicName) {
            this.publicName = publicName;
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
        return queryFactory.from(QAuthor.author).orderBy(QAuthor.author.id.desc()).list(QAuthor.author.id).get(0);
    }
}

