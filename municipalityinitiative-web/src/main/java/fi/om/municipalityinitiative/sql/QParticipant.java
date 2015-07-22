package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QParticipant is a Querydsl query type for QParticipant
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipant extends com.mysema.query.sql.RelationalPathBase<QParticipant> {

    private static final long serialVersionUID = -1571974685;

    public static final QParticipant participant = new QParticipant("participant");

    public final StringPath confirmationCode = createString("confirmationCode");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<fi.om.municipalityinitiative.util.Membership> membershipType = createEnum("membershipType", fi.om.municipalityinitiative.util.Membership.class);

    public final NumberPath<Long> municipalityId = createNumber("municipalityId", Long.class);

    public final NumberPath<Long> municipalityInitiativeId = createNumber("municipalityInitiativeId", Long.class);

    public final StringPath name = createString("name");

    public final DatePath<org.joda.time.LocalDate> participateTime = createDate("participateTime", org.joda.time.LocalDate.class);

    public final BooleanPath showName = createBoolean("showName");

    public final com.mysema.query.sql.PrimaryKey<QParticipant> participantPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> participantMunicipalityInitiativeIdFk = createForeignKey(municipalityInitiativeId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipality> participantMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QAuthor> _authorParticipantFk = createInvForeignKey(id, "participant_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedUserNormalInitiatives> _verifiedUserNormalInitiativesParticipantId = createInvForeignKey(id, "participant");

    public QParticipant(String variable) {
        super(QParticipant.class,  forVariable(variable), "municipalityinitiative", "participant");
        addMetadata();
    }

    public QParticipant(Path<? extends QParticipant> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant");
        addMetadata();
    }

    public QParticipant(PathMetadata<?> metadata) {
        super(QParticipant.class,  metadata, "municipalityinitiative", "participant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(confirmationCode, ColumnMetadata.named("confirmation_code").ofType(12).withSize(20));
        addMetadata(email, ColumnMetadata.named("email").ofType(12).withSize(100));
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(membershipType, ColumnMetadata.named("membership_type").ofType(1111).withSize(2147483647).notNull());
        addMetadata(municipalityId, ColumnMetadata.named("municipality_id").ofType(-5).withSize(19).notNull());
        addMetadata(municipalityInitiativeId, ColumnMetadata.named("municipality_initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(100));
        addMetadata(participateTime, ColumnMetadata.named("participate_time").ofType(91).withSize(13));
        addMetadata(showName, ColumnMetadata.named("show_name").ofType(-7).withSize(1));
    }

}

