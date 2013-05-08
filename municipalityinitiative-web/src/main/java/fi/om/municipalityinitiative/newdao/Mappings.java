package fi.om.municipalityinitiative.newdao;

import com.google.common.base.Strings;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.sql.QAuthor;
import fi.om.municipalityinitiative.sql.QAuthorInvitation;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.sql.QParticipant;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import static fi.om.municipalityinitiative.sql.QMunicipalityInitiative.municipalityInitiative;

public class Mappings {

    // This is for querydsl for not being able to create a row with DEFERRED not-null-check value being null..
    // Querydsl always assigned some value to it and setting it to null was not an option.
    public static final Long PREPARATION_ID = -1L;

    public static Expression<Author> authorMapping =
            new MappingProjection<Author>(Author.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected Author map(Tuple row) {

                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(QAuthor.author.address));
                    contactInfo.setPhone(row.get(QAuthor.author.phone));
                    contactInfo.setEmail(row.get(QParticipant.participant.email));
                    contactInfo.setName(row.get(QParticipant.participant.name));
                    contactInfo.setShowName(Boolean.TRUE.equals(row.get(QParticipant.participant.showName)));

                    Author author = new Author();
                    author.setId(row.get(QAuthor.author.id));
                    author.setContactInfo(contactInfo);
                    author.setMunicipality(parseMunicipality(row));

                    return author;


                }
            };
    public static Expression<InitiativeListInfo> initiativeListInfoMapping =
            new MappingProjection<InitiativeListInfo>(InitiativeListInfo.class,
                    municipalityInitiative.all(),
                    QMunicipality.municipality.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected InitiativeListInfo map(Tuple row) {
                    InitiativeListInfo info = new InitiativeListInfo();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row));
                    info.setCollectable(InitiativeType.isCollectable(row.get(municipalityInitiative.type)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    info.setType(row.get(municipalityInitiative.type));
                    return info;
                }
            };
    public static Expression<Initiative> initiativeInfoMapping =
            new MappingProjection<Initiative>(Initiative.class,
                    municipalityInitiative.all(),
                    JdbcInitiativeDao.AUTHOR_MUNICIPALITY.all(),
                    JdbcInitiativeDao.INITIATIVE_MUNICIPALITY.all(),
                    QParticipant.participant.all(),
                    QAuthor.author.all()) {
                @Override
                protected Initiative map(Tuple row) {
                    Initiative info = new Initiative();
                    info.setId(row.get(municipalityInitiative.id));
                    info.setCreateTime(row.get(municipalityInitiative.modified).toLocalDate());
                    info.setName(row.get(municipalityInitiative.name));
                    info.setMunicipality(parseMunicipality(row, JdbcInitiativeDao.INITIATIVE_MUNICIPALITY)
                    );
                    info.setType(row.get(municipalityInitiative.type));
                    info.setProposal(row.get(municipalityInitiative.proposal));
                    info.setManagementHash(Maybe.of(row.get(QAuthor.author.managementHash)));
                    info.setSentTime(maybeLocalDate(row.get(municipalityInitiative.sent)));
                    info.setState(row.get(municipalityInitiative.state));
                    info.setStateTime(row.get(municipalityInitiative.stateTimestamp).toLocalDate());
                    info.setExtraInfo(row.get(municipalityInitiative.extraInfo));
                    info.setModeratorComment(Strings.nullToEmpty(row.get(municipalityInitiative.moderatorComment)));
                    info.setParticipantCount(row.get(municipalityInitiative.participantCount));
                    info.setSentComment(row.get(municipalityInitiative.sentComment));

                    Author author = new Author();
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setAddress(row.get(QAuthor.author.address));
                    contactInfo.setPhone(row.get(QAuthor.author.phone));
                    contactInfo.setName(row.get(QAuthor.author.name));
                    contactInfo.setEmail(row.get(QParticipant.participant.email));
                    contactInfo.setShowName(Boolean.TRUE.equals(row.get(QParticipant.participant.showName)));
                    author.setId(row.get(QAuthor.author.id));
                    author.setContactInfo(contactInfo);
                    author.setMunicipality(parseMunicipality(row, JdbcInitiativeDao.AUTHOR_MUNICIPALITY));

                    info.setAuthor(author);

                    return info;
                }
            };
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

    public static Municipality parseMunicipality(Tuple row) {
        return new Municipality(
                row.get(QMunicipality.municipality.id),
                row.get(QMunicipality.municipality.name),
                row.get(QMunicipality.municipality.nameSv));
    }

    public static Municipality parseMunicipality(Tuple row, QMunicipality municipality) {
        return new Municipality(
                row.get(municipality.id),
                row.get(municipality.name),
                row.get(municipality.nameSv));
    }

    public static Maybe<LocalDate> maybeLocalDate(DateTime sentTime) {
        if (sentTime != null) {
            return Maybe.of(sentTime.toLocalDate());
        }
        return Maybe.absent();
    }
}
