package fi.om.municipalityinitiative.newdao;

import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.ParticipantCount;
import fi.om.municipalityinitiative.newdto.ParticipantCreateDto;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static fi.om.municipalityinitiative.sql.QParticipant.participant;

@Transactional(readOnly = true)
@SQLExceptionTranslated
public class JdbcParticipantDao implements ParticipantDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = false)
    public Long create(ParticipantCreateDto createDto) {
        return queryFactory.insert(participant)
                .set(participant.franchise, createDto.getFranchise())
                .set(participant.municipalityId, createDto.getHomeMunicipality())
                .set(participant.municipalityInitiativeId, createDto.getMunicipalityInitiativeId())
                .set(participant.name, createDto.getParticipantName())
                .set(participant.showName, createDto.getShowName())
                .executeWithKey(participant.id);
    }

    @Override
    public ParticipantCount countSupports(Long municipalityId) {
        List<Object[]> resultRowArray = queryFactory.query()
                .from(participant)
                .where(participant.municipalityInitiativeId.eq(municipalityId))
                .groupBy(participant.franchise)
                .groupBy(participant.showName)
                .list(participant.id.count(), participant.franchise, participant.showName);

        return parseToSupportCount(resultRowArray);

    }

    // XXX: This looks pretty messed up. Does querydsl support some other way of doing this?
    // Row headers are:
    // count(id) | hasRightOfVoting | showName
    private static ParticipantCount parseToSupportCount(List<Object[]> resultRowList) {
        ParticipantCount participantCount = new ParticipantCount();
        for (Object[] row : resultRowList) {
            if ((Boolean) row[1]) {
                if ((Boolean) row[2]) {
                    participantCount.getRightOfVoting().setPublicNames((Long) row[0]);
                }
                else {
                    participantCount.getRightOfVoting().setPrivateNames((Long) row[0]);
                }
            }
            else {
                if ((Boolean) row[2]) {
                    participantCount.getNoRightOfVoting().setPublicNames((Long) row[0]);
                }
                else {
                    participantCount.getNoRightOfVoting().setPrivateNames((Long) row[0]);
                }
            }
        }
        return participantCount;
    }
}
