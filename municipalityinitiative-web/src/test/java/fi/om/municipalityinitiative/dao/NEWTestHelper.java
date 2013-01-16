package fi.om.municipalityinitiative.dao;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QMunicipalityInitiative;
import fi.om.municipalityinitiative.sql.QParticipant;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

public class NEWTestHelper {

    @Resource
    PostgresQueryFactory queryFactory;

    public NEWTestHelper() {
    }

    public NEWTestHelper(PostgresQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Transactional(readOnly=false)
    public void dbCleanup() {
        queryFactory.delete(QParticipant.participant).execute();
        queryFactory.delete(QMunicipalityInitiative.municipalityInitiative).execute();
        queryFactory.delete(QMunicipality.municipality).execute();
    }

    @Transactional
    public Long createTestMunicipality(String name) {
        return queryFactory.insert(QMunicipality.municipality)
                    .set(QMunicipality.municipality.name, name)
                    .set(QMunicipality.municipality.nameSv, name)
                .executeWithKey(QMunicipality.municipality.id);
    }

    @Transactional
    public Long createTestInitiative(Long municipalityId) {
        return createTestInitiative(municipalityId, "name");
    }
    @Transactional
    public Long createTestInitiative(Long municipalityId, String name) {
        SQLInsertClause insert = queryFactory.insert(municipalityInitiative);

        insert.set(municipalityInitiative.contactAddress, "contact_address");
        insert.set(municipalityInitiative.contactEmail, "contact_email");
        insert.set(municipalityInitiative.contactPhone, "contact_phone");
        insert.set(municipalityInitiative.contactName, "contact_name");
        insert.set(municipalityInitiative.name, name);
        insert.set(municipalityInitiative.proposal, "proposal");
        insert.set(municipalityInitiative.municipalityId, municipalityId);
        insert.set(municipalityInitiative.authorId, -1L);
        //insert.setNull(municipalityInitiative.authorId); // TODO

        Long initiativeId = insert.executeWithKey(municipalityInitiative.id);

        Long participantId = queryFactory.insert(QParticipant.participant)
                .set(QParticipant.participant.municipalityId, municipalityId)
                .set(QParticipant.participant.municipalityInitiativeId, initiativeId)
                .set(QParticipant.participant.name, "Antti Author")
                .set(QParticipant.participant.showName, true)
                .set(QParticipant.participant.franchise, true)
                .executeWithKey(QParticipant.participant.id);

        queryFactory.update(municipalityInitiative)
                .set(municipalityInitiative.authorId, participantId)
                .where(municipalityInitiative.id.eq(initiativeId))
                .execute();

        return initiativeId;

    }

    @Transactional
    public Long countAll(RelationalPathBase relationalPathBase) {
        return queryFactory.from(relationalPathBase).count();
    }

}

