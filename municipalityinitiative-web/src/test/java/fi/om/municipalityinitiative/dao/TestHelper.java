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
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestHelper {

    public static final String TEST_MANAGEMENT_HASH = "0000000000111111111122222222223333333333";

    @Resource
    PostgresQueryFactory queryFactory;

    public TestHelper() {
    }

    public TestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QAuthor.author).execute();
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
    public Long createTestInitiative(Long municipalityId) {
        return createTestInitiative(municipalityId, "name");
    }

    @Transactional
    public Long createTestInitiative(Long municipalityId, String name) {
        return createTestInitiative(municipalityId, name, true, false);
    }

    @Transactional
    public Long createTestInitiative(Long municipalityId, String name, boolean publicName, boolean collectable) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.contactAddress, "contact_address");
        insert.set(municipalityInitiative.contactEmail, "contact_email@xxx.yyy");
        insert.set(municipalityInitiative.contactPhone, "contact_phone");
        insert.set(municipalityInitiative.contactName, "contact_name");
        insert.set(municipalityInitiative.name, name);
        insert.set(municipalityInitiative.proposal, "proposal");
        insert.set(municipalityInitiative.municipalityId, municipalityId);
        insert.set(municipalityInitiative.authorId, -1L);
        insert.set(municipalityInitiative.newAuthorId, -1L);
        //insert.setNull(municipalityInitiative.authorId); // TODO
        if (collectable) {
            insert.set(municipalityInitiative.managementHash,TEST_MANAGEMENT_HASH);
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
                .set(QAuthor.author.initiativeId, initiativeId)
                .executeWithKey(QAuthor.author.id);

        queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, participantId)
                .set(municipalityInitiative.newAuthorId, authorId)
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
}

