package fi.om.municipalityinitiative.dao;

import com.google.common.base.Strings;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.sql.*;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;
import static fi.om.municipalityinitiative.sql.QParticipant.participant;
import static fi.om.municipalityinitiative.sql.QVerifiedUser.verifiedUser;

public class Mappings {

    public static Expression<NormalAuthor> normalAuthorMapping =
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
                    author.setMunicipality(Maybe.of(parseMunicipality(row)));

                    return author;

                }
            };
    public static Expression<InitiativeListInfo> initiativeListInfoMapping =
            new MappingProjection<InitiativeListInfo>(InitiativeListInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row));
                    info.setCollaborative(InitiativeType.isCollaborative(row.get(municipalityInitiative.type)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setParticipantCount(nullToZero(row.get(municipalityInitiative.participantCount)) + nullToZero(row.get(municipalityInitiative.externalparticipantcount)));
                    info.setType(row.get(municipalityInitiative.type));
                    info.setState(row.get(municipalityInitiative.state));
                    return info;
                }
            };
    public static Expression<Initiative> initiativeInfoMapping =
            new MappingProjection<Initiative>(Initiative.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all()) {
                @Override
                protected Initiative map(Tuple row) {
                    Initiative info = new Initiative();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row));
                    info.setType(row.get(municipalityInitiative.type));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setState(row.get(municipalityInitiative.state));
                    info.setStateTime(row.get(municipalityInitiative.stateTimestamp).toLocalDate());
                    info.setExtraInfo(row.get(municipalityInitiative.extraInfo));
                    info.setModeratorComment(Strings.nullToEmpty(row.get(municipalityInitiative.moderatorComment)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    info.setSentComment(row.get(municipalityInitiative.sentComment));
                    info.setFixState(row.get(municipalityInitiative.fixState));
                    info.setExternalParticipantCount(nullToZero(row.get(municipalityInitiative.externalparticipantcount)));

                    return info;
                }
            };
    static Expression<ContactInfo> verifiedUserContactInfo
            = new MappingProjection<ContactInfo>(ContactInfo.class, verifiedUser.all()) {
        @Override
        protected ContactInfo map(Tuple row) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(row.get(QVerifiedUser.verifiedUser.phone));
            contactInfo.setName(row.get(QVerifiedUser.verifiedUser.name));
            contactInfo.setAddress(row.get(QVerifiedUser.verifiedUser.address));
            contactInfo.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            return contactInfo;
        }
    };

    static Expression<ContactInfo> verifiedAuthorContactInfoMapper
            =  new MappingProjection<ContactInfo>(ContactInfo.class,
            verifiedUser.phone,
            verifiedUser.name,
            verifiedUser.address,
            verifiedUser.email,
            QVerifiedParticipant.verifiedParticipant.showName) {
        @Override
        protected ContactInfo map(Tuple row) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(row.get(QVerifiedUser.verifiedUser.phone));
            contactInfo.setName(row.get(QVerifiedUser.verifiedUser.name));
            contactInfo.setAddress(row.get(QVerifiedUser.verifiedUser.address));
            contactInfo.setEmail(row.get(QVerifiedUser.verifiedUser.email));
            contactInfo.setShowName(row.get(QVerifiedParticipant.verifiedParticipant.showName)); // currently has not null constraint
            return contactInfo;
        }
    };
    public static Expression<VerifiedAuthor> verifiedAuthorMapper = new MappingProjection<VerifiedAuthor>(VerifiedAuthor.class,
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

            if (row.get(QMunicipality.municipality.name) == null) {
                author.setMunicipality(Maybe.<Municipality>absent());
            }
            else {
                author.setMunicipality(Maybe.of(parseMunicipality(row)));
            }





            return author;
        }
    };

    private static int nullToZero(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    public static Expression<AuthorInvitation> authorInvitationMapping =
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
    public static Expression<Participant> participantMapping =
            new MappingProjection<Participant>(Participant.class,
                    participant.all(), QMunicipality.municipality.all()) {
                @Override
                protected Participant map(Tuple row) {
                    Participant par = new Participant();
                    par.setParticipateDate(row.get(participant.participateTime));
                    par.setName(row.get(participant.name));
                    par.setEmail(row.get(participant.email));
                    par.setMembership(row.get(participant.membershipType));
                    par.setHomeMunicipality(parseMunicipality(row));
                    par.setId(row.get(participant.id));
                    return par;

                }
            };
    static public Expression<AuthorMessage> authorMessageMapping = new MappingProjection<AuthorMessage>(AuthorMessage.class,
            QAuthorMessage.authorMessage.all()) {
        @Override
        protected AuthorMessage map(Tuple row) {

            AuthorMessage authorMessage = new AuthorMessage();
            authorMessage.setInitiativeId(row.get(QAuthorMessage.authorMessage.initiativeId));
            authorMessage.setContactName(row.get(QAuthorMessage.authorMessage.contactor));
            authorMessage.setContactEmail(row.get(QAuthorMessage.authorMessage.contactorEmail));
            authorMessage.setMessage(row.get(QAuthorMessage.authorMessage.message));
            authorMessage.setConfirmationCode(row.get(QAuthorMessage.authorMessage.confirmationCode));
            return authorMessage;
        }
    };

    public static Municipality parseMunicipality(Tuple row) {
        return new Municipality(
                row.get(QMunicipality.municipality.id),
                row.get(QMunicipality.municipality.name),
                row.get(QMunicipality.municipality.nameSv),
                row.get(QMunicipality.municipality.active));
    }

    public static Maybe<LocalDate> maybeLocalDate(DateTime sentTime) {
        if (sentTime != null) {
            return Maybe.of(sentTime.toLocalDate());
        }
        return Maybe.absent();
    }
}
