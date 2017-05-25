package fi.om.municipalityinitiative.dao;


import com.google.common.collect.Sets;
import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.expr.DateTimeExpression;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.service.AuthorInvitation;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static fi.om.municipalityinitiative.dao.Mappings.assertSingleAffection;

@SQLExceptionTranslated
public class JdbcAuthorDao implements AuthorDao {

    private static final Expression<DateTime> CURRENT_TIME = DateTimeExpression.currentTimestamp(DateTime.class);
    static final Expression<NormalAuthor> normalAuthorMapping =
            new MappingProjection<NormalAuthor>(NormalAuthor.class,
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected NormalAuthor map(Tuple row) {

                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(QAuthor.author.address));
                    contactInfo.setPhone(row.get(QAuthor.author.phone));
                    contactInfo.setEmail(row.get(QParticipant.participant.email));
                    contactInfo.setName(row.get(QParticipant.participant.name));
                    contactInfo.setShowName(Boolean.TRUE.equals(row.get(QParticipant.participant.showName)));

                    NormalAuthor author = new NormalAuthor();
                    author.setId(new NormalAuthorId(row.get(QAuthor.author.participantId)));
                    author.setCreateTime(row.get(QParticipant.participant.participateTime));
                    author.setContactInfo(contactInfo);
                    author.setMunicipality(Optional.of(Mappings.parseMunicipality(row)));

                    return author;

                }
            };
    static final Expression<VerifiedAuthor> verifiedAuthorMapper = new MappingProjection<VerifiedAuthor>(VerifiedAuthor.class,
            QVerifiedAuthor.verifiedAuthor.all(),
            QVerifiedParticipant.verifiedParticipant.all(),
            QVerifiedUser.verifiedUser.all(),
            QMunicipality.municipality.all()) {
        @Override
        protected VerifiedAuthor map(Tuple row) {
            VerifiedAuthor author = new VerifiedAuthor();

            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(row.get(QVerifiedUser.verifiedUser.phone));
            contactInfo.setName(row.get(QVerifiedUser.verifiedUser.name));
            contactInfo.setAddress(row.get(QVerifiedUser.verifiedUser.address));
            contactInfo.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            contactInfo.setShowName(row.get(QVerifiedParticipant.verifiedParticipant.showName)); // currently has not null constraint

            author.setContactInfo(contactInfo);
            author.setId(new VerifiedUserId(row.get(QVerifiedUser.verifiedUser.id)));
            author.setCreateTime(row.get(QVerifiedParticipant.verifiedParticipant.participateTime));

            author.setMunicipality(Mappings.parseOptionalMunicipality(row));

            return author;
        }
    };
    static final Expression<AuthorInvitation> authorInvitationMapping =
            new MappingProjection<AuthorInvitation>(AuthorInvitation.class,
                    QAuthorInvitation.authorInvitation.all()) {

                @Override
                protected AuthorInvitation map(Tuple row) {
                    AuthorInvitation authorInvitation = new AuthorInvitation();

                    authorInvitation.setConfirmationCode(row.get(QAuthorInvitation.authorInvitation.confirmationCode));
                    authorInvitation.setInitiativeId(row.get(QAuthorInvitation.authorInvitation.initiativeId));
                    authorInvitation.setEmail(row.get(QAuthorInvitation.authorInvitation.email));
                    authorInvitation.setInvitationTime(row.get(QAuthorInvitation.authorInvitation.invitationTime));
                    authorInvitation.setName(row.get(QAuthorInvitation.authorInvitation.name));
                    authorInvitation.setRejectTime(Optional.ofNullable(row.get(QAuthorInvitation.authorInvitation.rejectTime)));

                    return authorInvitation;

                }
            };

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public NormalAuthorId createAuthor(Long initiativeId, Long participantId, String managementHash) {

        assertSingleAffection(queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.managementHash, managementHash)
                .set(QAuthor.author.participantId, participantId)
                .set(QAuthor.author.initiativeId, initiativeId)
                .execute());
        return new NormalAuthorId(participantId);
    }

    @Override
    public void updateAuthorInformation(NormalAuthorId authorId, ContactInfo contactInfo) {

        assertSingleAffection(queryFactory.update(QParticipant.participant)
                .set(QParticipant.participant.showName, Boolean.TRUE.equals(contactInfo.isShowName()))
                .set(QParticipant.participant.name, contactInfo.getName())
                .set(QParticipant.participant.email, contactInfo.getEmail())
                .where(QParticipant.participant.id.eq(authorId.toLong()))
                .execute());

        assertSingleAffection(queryFactory.update(QAuthor.author)
                .set(QAuthor.author.address, contactInfo.getAddress())
                .set(QAuthor.author.phone, contactInfo.getPhone())
                .where(QAuthor.author.participantId.eq(authorId.toLong()))
                .execute());
    }

    @Override
    public void addAuthorInvitation(AuthorInvitation authorInvitation) {
        assertSingleAffection(queryFactory.insert(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.confirmationCode, authorInvitation.getConfirmationCode())
                .set(QAuthorInvitation.authorInvitation.email, authorInvitation.getEmail())
                .set(QAuthorInvitation.authorInvitation.name, authorInvitation.getName())
                .set(QAuthorInvitation.authorInvitation.invitationTime, authorInvitation.getInvitationTime())
                .set(QAuthorInvitation.authorInvitation.initiativeId, authorInvitation.getInitiativeId())
                .execute());
    }

    @Override
    public AuthorInvitation getAuthorInvitation(Long initiativeId, String confirmationCode) {
        AuthorInvitation authorInvitation = queryFactory.from(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .singleResult(authorInvitationMapping);
        if (authorInvitation == null) {
            throw new InvitationNotValidException("No invitation: " + initiativeId + ", " + confirmationCode);
        }
        return authorInvitation;
    }

    @Override
    public void rejectAuthorInvitation(Long initiativeId, String confirmationCode) {
        assertSingleAffection(queryFactory.update(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.rejectTime, CURRENT_TIME)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .execute());
    }

    @Override
    public void deleteAuthorInvitation(Long initiativeId, String confirmationCode) {
        long affectedRows = queryFactory.delete(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .where(QAuthorInvitation.authorInvitation.confirmationCode.eq(confirmationCode))
                .execute();
        if (affectedRows != 1) {
            throw new NotFoundException(QAuthorInvitation.authorInvitation.getTableName(), initiativeId + ":" + confirmationCode);
        }
    }

    @Override
    public List<AuthorInvitation> findInvitations(Long initiativeId) {
        return queryFactory.from(QAuthorInvitation.authorInvitation)
                .where(QAuthorInvitation.authorInvitation.initiativeId.eq(initiativeId))
                .orderBy(QAuthorInvitation.authorInvitation.invitationTime.asc())
                .list(authorInvitationMapping);
    }

    @Override
    public List<NormalAuthor> findNormalAuthors(Long initiativeId) {
        return queryFactory.from(QAuthor.author)
                .where(QAuthor.author.initiativeId.eq(initiativeId))
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
                .orderBy(QParticipant.participant.id.asc())
                .list(normalAuthorMapping);
    }

    @Override
    public List<Author> findAllAuthors(Long initiativeId) {

        // TODO: Would be lovely to do this with sql union, but have no clue on how to do this with querydsl. Optional later.
        return new ArrayList<Author>() {{
            addAll(findNormalAuthors(initiativeId));
            addAll(findVerifiedAuthors(initiativeId));
            sort((o1, o2) -> (int) (o1.getId().toLong() - o2.getId().toLong())); // These are ids from different tables...
        }};

    }

    @Override
    public Set<Long> getAuthorsInitiatives(String managementHash) {
        List<Long> list = queryFactory.from(QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityInitiativeIdFk, QMunicipalityInitiative.municipalityInitiative)
                .where(QAuthor.author.managementHash.eq(managementHash))
                .list(QMunicipalityInitiative.municipalityInitiative.id);

        TreeSet<Long> initiativeIds = Sets.newTreeSet();
        initiativeIds.addAll(list);

        return initiativeIds;
    }

    @Override
    public Optional<NormalAuthorId> getAuthorId(String managementHash) {
        Long id = queryFactory.from(QAuthor.author)
                .where(QAuthor.author.managementHash.eq(managementHash))
                .uniqueResult(QAuthor.author.participantId);
        if (id == null) {
            return Optional.empty();
        }
        return Optional.of(new NormalAuthorId(id));
    }

    @Override
    public NormalAuthor getNormalAuthor(NormalAuthorId authorId) {
        return queryFactory.from(QAuthor.author)
                .where(QAuthor.author.participantId.eq(authorId.toLong()))
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
                .uniqueResult(normalAuthorMapping);
    }

    @Override
    public VerifiedAuthor getVerifiedAuthor(Long initiativeId, VerifiedUserId userId) {
        return queryFactory.from(QVerifiedUser.verifiedUser)
                .innerJoin(QVerifiedUser.verifiedUser._verifiedParticipantVerifiedUserFk, QVerifiedParticipant.verifiedParticipant)
                .innerJoin(QVerifiedUser.verifiedUser._verifiedAuthorVerifiedUserFk, QVerifiedAuthor.verifiedAuthor)
                        //.innerJoin(QVerifiedParticipant.verifiedParticipant.verifiedParticipantInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .leftJoin(QVerifiedUser.verifiedUser.verifiedUserMunicipalityFk, QMunicipality.municipality)
                .where(QVerifiedUser.verifiedUser.id.eq(userId.toLong()))
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .uniqueResult(verifiedAuthorMapper);

    }

    @Override
    public void deleteAuthorAndParticipant(Long initiativeId, NormalAuthorId authorId) {

        assertSingleAffection(queryFactory.delete(QAuthor.author)
                .where(QAuthor.author.participantId.eq(authorId.toLong())).execute());
        assertSingleAffection(queryFactory.delete(QParticipant.participant)
                .where(QParticipant.participant.id.eq(authorId.toLong())).execute());

    }

    @Override
    public void deleteAuthorAndParticipant(Long initiativeId, VerifiedUserId authorToDelete) {

        assertSingleAffection(queryFactory.delete(QVerifiedAuthor.verifiedAuthor)
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .where(QVerifiedAuthor.verifiedAuthor.verifiedUserId.eq(authorToDelete.toLong()))
                .execute());

        assertSingleAffection(queryFactory.delete(QVerifiedParticipant.verifiedParticipant)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.verifiedUserId.eq(authorToDelete.toLong()))
                .execute());

    }

    @Override
    public List<String> findAuthorEmails(Long initiativeId) {
        return findAllAuthors(initiativeId)
                .stream()
                .map(a -> a.getContactInfo().getEmail())
                .collect(Collectors.toList());
    }


    @Override
    public void updateManagementHash(NormalAuthorId authorId, String newManagementHash) {
        assertSingleAffection(queryFactory.update(QAuthor.author)
                .set(QAuthor.author.managementHash, newManagementHash)
                .where(QAuthor.author.participantId.eq(authorId.toLong()))
                .execute());
    }

    @Override
    public Map<String, String> getManagementLinksByAuthorEmails(Long initiativeId) {
        return queryFactory.from(QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .where(QParticipant.participant.municipalityInitiativeId.eq(initiativeId))
                .map(QParticipant.participant.email, QAuthor.author.managementHash);
    }

    @Override
    public void addVerifiedAuthor(Long initiativeId, VerifiedUserId userId) {
        assertSingleAffection(queryFactory.insert(QVerifiedAuthor.verifiedAuthor)
                .set(QVerifiedAuthor.verifiedAuthor.initiativeId, initiativeId)
                .set(QVerifiedAuthor.verifiedAuthor.verifiedUserId, userId.toLong())
                .execute());
    }

    @Override
    public List<VerifiedAuthor> findVerifiedAuthors(Long initiativeId) {
        return queryFactory.from(QVerifiedAuthor.verifiedAuthor)
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorVerifiedUserFk, QVerifiedUser.verifiedUser)
                .innerJoin(QVerifiedUser.verifiedUser._verifiedParticipantVerifiedUserFk, QVerifiedParticipant.verifiedParticipant)
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .innerJoin(QVerifiedAuthor.verifiedAuthor.verifiedAuthorInitiativeFk, QMunicipalityInitiative.municipalityInitiative)
                .innerJoin(QMunicipalityInitiative.municipalityInitiative.municipalityInitiativeMunicipalityFk, QMunicipality.municipality)
                .orderBy(QVerifiedParticipant.verifiedParticipant.participateTime.asc())
                .list(verifiedAuthorMapper);
    }
}
