package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.DateTimeExpression;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestHelper {

    public static final String TEST_MANAGEMENT_HASH = "0000000000111111111122222222223333333333";
    public static final String DEFAULT_INITIATIVE_NAME = "Initiative name";
    public static final String DEFAULT_PROPOSAL = "Proposal";
    public static final InitiativeState DEFAULT_STATE = InitiativeState.DRAFT;
    public static final InitiativeType DEFAULT_TYPE = InitiativeType.UNDEFINED;
    public static final String DEFAULT_AUTHOR_NAME = "Antti Author";
    public static final String DEFAULT_AUTHOR_EMAIL = "author_email@example.com";
    public static final String DEFAULT_AUTHOR_ADDRESS = "author address";
    public static final String DEFAULT_AUTHOR_PHONE = "author phone";
    public static final boolean DEFAULT_PUBLIC_NAME = true;
    public static final String DEFAULT_COMMENT = "some default comment";
    private static final DateTime DEFAULT_SENT = null;

    @Resource
    PostgresQueryFactory queryFactory;

    public TestHelper() {
    }

    public TestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QAuthor.author).execute();
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QMunicipality.municipality).execute();
    }

    @Transactional
    public Long createTestMunicipality(String name) {
        return queryFactory.insert(QMunicipality.municipality)
                    .set(QMunicipality.municipality.name, name)
                    .set(QMunicipality.municipality.nameSv, name + " sv")
                    .set(QMunicipality.municipality.email, name.replace(" ", "_")+"@example.com")
                .executeWithKey(QMunicipality.municipality.id);
    }

    @Transactional
    @Deprecated
    public Long createTestInitiative(Long municipalityId) {
        return createTestInitiative(municipalityId, "name");
    }

    @Transactional
    @Deprecated
    public Long createTestInitiative(Long municipalityId, String name) {
        return createTestInitiative(municipalityId, name, true, false);
    }

    @Transactional
    @Deprecated
    public Long createTestInitiative(Long municipalityId, String name, boolean publicName, boolean collectable) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.name, name);
        insert.set(municipalityInitiative.proposal, "proposal");
        insert.set(municipalityInitiative.municipalityId, municipalityId);
        insert.set(municipalityInitiative.authorId, -1L);
        insert.set(municipalityInitiative.comment, "comment");
        insert.set(municipalityInitiative.type, InitiativeType.UNDEFINED);
        //insert.setNull(municipalityInitiative.authorId); // TODO
        if (collectable) {
//            insert.set(municipalityInitiative.managementHash,TEST_MANAGEMENT_HASH);
        }
        else {
            insert.set(municipalityInitiative.sent, DateTimeExpression.currentTimestamp(DateTime.class));
        }

        Long initiativeId = insert.executeWithKey(municipalityInitiative.id);

        Long participantId = queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, municipalityId)
                .set(QParticipant.participant.municipalityInitiativeId, initiativeId)
                .set(QParticipant.participant.name, "Antti Author")
                .set(QParticipant.participant.showName, publicName)
                .set(QParticipant.participant.franchise, true) // Changing these will affect on tests
                .executeWithKey(QParticipant.participant.id);

        Long authorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.address, "author address")
                .set(QAuthor.author.email, "author_email@example.com")
                .set(QAuthor.author.phone, "author phone")
                .set(QAuthor.author.participantId, participantId)
                .set(QAuthor.author.managementHash, TEST_MANAGEMENT_HASH)
                .executeWithKey(QAuthor.author.id);

        queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, authorId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute();

        return initiativeId;

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
                .withSent(new DateTime(2011, 1, 1, 0, 0)));
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
        insert.set(municipalityInitiative.authorId, -1L);
        insert.set(municipalityInitiative.participantCount, initiativeDraft.participantCount);
        insert.set(municipalityInitiative.comment, initiativeDraft.comment);

        insert.set(municipalityInitiative.state, initiativeDraft.state);

        insert.set(municipalityInitiative.type, initiativeDraft.type);

        insert.set(municipalityInitiative.sent, initiativeDraft.sent);

        Long initiativeId = insert.executeWithKey(municipalityInitiative.id);

        Long participantId = queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, initiativeDraft.authorMunicipality)
                .set(QParticipant.participant.municipalityInitiativeId, initiativeId)
                .set(QParticipant.participant.name, initiativeDraft.authorName)
                .set(QParticipant.participant.showName, initiativeDraft.publicName)
                .set(QParticipant.participant.franchise, true) // Changing these will affect on tests
                .executeWithKey(QParticipant.participant.id);

        Long authorId = queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.name, initiativeDraft.authorName)
                .set(QAuthor.author.address, initiativeDraft.authorAddress)
                .set(QAuthor.author.email, initiativeDraft.authorEmail)
                .set(QAuthor.author.phone, initiativeDraft.authorPhone)
                .set(QAuthor.author.participantId, participantId)
                .set(QAuthor.author.managementHash, TEST_MANAGEMENT_HASH)
                .executeWithKey(QAuthor.author.id);

        queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, authorId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute();

        return initiativeId;

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
        public DateTime sent = DEFAULT_SENT;
        public Integer participantCount = 0;
        public String comment = DEFAULT_COMMENT;

        public Long authorMunicipality;

        public InitiativeDraft(Long municipalityId) {
            this.municipalityId = municipalityId;
            this.authorMunicipality = municipalityId;
        }

        public InitiativeDraft withAuthorMunicipality(Long municipalityId) {
            this.authorMunicipality = municipalityId;
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
}

