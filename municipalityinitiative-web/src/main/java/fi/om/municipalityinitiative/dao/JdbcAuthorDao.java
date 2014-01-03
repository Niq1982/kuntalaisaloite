package fi.om.municipalityinitiative.dao;


import com.google.common.collect.Lists;
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
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static fi.om.municipalityinitiative.dao.JdbcInitiativeDao.assertSingleAffection;

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
                    author.setMunicipality(Maybe.of(Mappings.parseMunicipality(row)));

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

            author.setMunicipality(Mappings.parseMaybeMunicipality(row));

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
                    authorInvitation.setRejectTime(Maybe.fromNullable(row.get(QAuthorInvitation.authorInvitation.rejectTime)));

                    return authorInvitation;

                }
            };

    @Resource
    PostgresQueryFactory queryFactory;

    @Override
    public NormalAuthorId createAuthor(Long initiativeId, Long participantId, String managementHash) {

        JdbcInitiativeDao.assertSingleAffection(queryFactory.insert(QAuthor.author)
                .set(QAuthor.author.managementHash, managementHash)
                .set(QAuthor.author.participantId, participantId)
                .set(QAuthor.author.initiativeId, initiativeId)
                .execute());
        return new NormalAuthorId(participantId);
    }

    @Override
    public void updateAuthorInformation(NormalAuthorId authorId, ContactInfo contactInfo) {

        JdbcInitiativeDao.assertSingleAffection(queryFactory.update(QParticipant.participant)
                .set(QParticipant.participant.showName, Boolean.TRUE.equals(contactInfo.isShowName()))
                .set(QParticipant.participant.name, contactInfo.getName())
                .set(QParticipant.participant.email, contactInfo.getEmail())
                .where(QParticipant.participant.id.eq(authorId.toLong()))
                .execute());

        JdbcInitiativeDao.assertSingleAffection(queryFactory.update(QAuthor.author)
                .set(QAuthor.author.address, contactInfo.getAddress())
                .set(QAuthor.author.phone, contactInfo.getPhone())
                .where(QAuthor.author.participantId.eq(authorId.toLong()))
                .execute());
    }

    @Override
    public Long addAuthorInvitation(AuthorInvitation authorInvitation) {
        return queryFactory.insert(QAuthorInvitation.authorInvitation)
                .set(QAuthorInvitation.authorInvitation.confirmationCode, authorInvitation.getConfirmationCode())
                .set(QAuthorInvitation.authorInvitation.email, authorInvitation.getEmail())
                .set(QAuthorInvitation.authorInvitation.name, authorInvitation.getName())
                .set(QAuthorInvitation.authorInvitation.invitationTime, authorInvitation.getInvitationTime())
                .set(QAuthorInvitation.authorInvitation.initiativeId, authorInvitation.getInitiativeId())
                .execute();
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
        JdbcInitiativeDao.assertSingleAffection(queryFactory.update(QAuthorInvitation.authorInvitation)
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
//            return queryFactory.from(municipalityInitiative)
//                    .innerJoin(municipalityInitiative._participantMunicipalityInitiativeIdFk, QParticipant.participant)
//                    .innerJoin(QParticipant.participant._authorParticipantFk, QAuthor.author)
//                    .innerJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
//                    .where(municipalityInitiative.id.eq(initiativeId))
//                    .orderBy(QParticipant.participant.id.asc())
//                    .list(Mappings.normalAuthorMapping);
        return queryFactory.from(QAuthor.author)
                .where(QAuthor.author.initiativeId.eq(initiativeId))
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .innerJoin(QParticipant.participant.participantMunicipalityFk, QMunicipality.municipality)
                .orderBy(QParticipant.participant.id.asc())
                .list(normalAuthorMapping);
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
    public Maybe<NormalAuthorId> getAuthorId(String managementHash) {
        Long id = queryFactory.from(QAuthor.author)
                .where(QAuthor.author.managementHash.eq(managementHash))
                .uniqueResult(QAuthor.author.participantId);
        if (id == null) {
            return Maybe.absent();
        }
        return Maybe.of(new NormalAuthorId(id));
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
    public void deleteAuthorAndParticipant(NormalAuthorId authorId) {

        Long initiativeId = queryFactory.from(QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .where(QAuthor.author.participantId.eq(authorId.toLong()))
                .uniqueResult(QParticipant.participant.municipalityInitiativeId);

        // Lock all authors of the initiative from another transactions
        queryFactory.from(QAuthor.author)
                .where(QAuthor.author.initiativeId.eq(initiativeId))
                .forUpdate().of(QAuthor.author).list(QAuthor.author.participantId);

        assertSingleAffection(queryFactory.delete(QAuthor.author)
                .where(QAuthor.author.participantId.eq(authorId.toLong())).execute());
        assertSingleAffection(queryFactory.delete(QParticipant.participant)
                .where(QParticipant.participant.id.eq(authorId.toLong())).execute());
        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.subtract(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute());

        long authorCount = queryFactory.from(QAuthor.author)
                .innerJoin(QAuthor.author.authorParticipantFk, QParticipant.participant)
                .where(QParticipant.participant.municipalityInitiativeId.eq(initiativeId))
                .count();

        assertNotZero(authorCount, "Deleting last author is forbidden");

    }

    @Override
    public void deleteAuthorAndParticipant(Long initiativeId, VerifiedUserId authorToDelete) {

        // Lock all authors of the initiative from another transactions

        queryFactory.from(QVerifiedAuthor.verifiedAuthor)
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .forUpdate().of(QVerifiedAuthor.verifiedAuthor).list(QVerifiedAuthor.verifiedAuthor.initiativeId);

        assertSingleAffection(queryFactory.delete(QVerifiedAuthor.verifiedAuthor)
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .where(QVerifiedAuthor.verifiedAuthor.verifiedUserId.eq(authorToDelete.toLong()))
                .execute());

        assertSingleAffection(queryFactory.delete(QVerifiedParticipant.verifiedParticipant)
                .where(QVerifiedParticipant.verifiedParticipant.initiativeId.eq(initiativeId))
                .where(QVerifiedParticipant.verifiedParticipant.verifiedUserId.eq(authorToDelete.toLong()))
                .execute());

        assertSingleAffection(queryFactory.update(QMunicipalityInitiative.municipalityInitiative)
                .set(QMunicipalityInitiative.municipalityInitiative.participantCount, QMunicipalityInitiative.municipalityInitiative.participantCount.subtract(1))
                .where(QMunicipalityInitiative.municipalityInitiative.id.eq(initiativeId))
                .execute());

        long authorCount = queryFactory.from(QVerifiedAuthor.verifiedAuthor)
                .where(QVerifiedAuthor.verifiedAuthor.initiativeId.eq(initiativeId))
                .count();

        assertNotZero(authorCount, "Deleting last verified author is forbidden");

    }

    private static void assertNotZero(long authorCount, String messag) {
        if (authorCount == 0) {
            throw new OperationNotAllowedException(messag);
        }
    }

    @Override
    public List<String> findNormalAuthorEmails(Long initiativeId) {
        return authorEmails(findNormalAuthors(initiativeId));
    }


    @Override
    public List<String> findVerifiedAuthorEmails(Long initiativeId) {
        return authorEmails(findVerifiedAuthors(initiativeId));
    }

    private static List<String> authorEmails(List<? extends Author> normalAuthors) {
        List<String> emails = Lists.newArrayList();
        for (Author author : normalAuthors) {
            emails.add(author.getContactInfo().getEmail());
        }
        return emails;
    }

    @Override
    public void updateManagementHash(NormalAuthorId authorId, String newManagementHash) {
        JdbcInitiativeDao.assertSingleAffection(queryFactory.update(QAuthor.author)
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
                .list(verifiedAuthorMapper);
    }
}
