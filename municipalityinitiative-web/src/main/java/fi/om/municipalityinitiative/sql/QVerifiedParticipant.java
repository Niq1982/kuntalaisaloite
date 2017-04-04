package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QVerifiedParticipant is a Querydsl query type for QVerifiedParticipant
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipant extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipant> {

    private static final long serialVersionUID = -1693721093;

    public static final QVerifiedParticipant verifiedParticipant = new QVerifiedParticipant("verified_participant");

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final EnumPath<fi.om.municipalityinitiative.util.Membership> membershipType = createEnum("membershipType", fi.om.municipalityinitiative.util.Membership.class);

    public final NumberPath<Long> municipalityId = createNumber("municipalityId", Long.class);

    public final DatePath<org.joda.time.LocalDate> participateTime = createDate("participateTime", org.joda.time.LocalDate.class);

    public final BooleanPath showName = createBoolean("showName");

    public final BooleanPath verified = createBoolean("verified");

    public final NumberPath<Long> verifiedUserId = createNumber("verifiedUserId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QVerifiedParticipant> verifiedParticipantPk = createPrimaryKey(initiativeId, verifiedUserId);

    public final com.mysema.query.sql.ForeignKey<QVerifiedUser> verifiedParticipantVerifiedUserFk = createForeignKey(verifiedUserId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> verifiedParticipantInitiativeFk = createForeignKey(initiativeId, "id");

    public QVerifiedParticipant(String variable) {
        super(QVerifiedParticipant.class,  forVariable(variable), "municipalityinitiative", "verified_participant");
        addMetadata();
    }

    public QVerifiedParticipant(Path<? extends QVerifiedParticipant> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant");
        addMetadata();
    }

    public QVerifiedParticipant(PathMetadata<?> metadata) {
        super(QVerifiedParticipant.class,  metadata, "municipalityinitiative", "verified_participant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(membershipType, ColumnMetadata.named("membership_type").ofType(1111).withSize(2147483647).notNull());
        addMetadata(municipalityId, ColumnMetadata.named("municipality_id").ofType(-5).withSize(19).notNull());
        addMetadata(participateTime, ColumnMetadata.named("participate_time").ofType(91).withSize(13));
        addMetadata(showName, ColumnMetadata.named("show_name").ofType(-7).withSize(1).notNull());
        addMetadata(verified, ColumnMetadata.named("verified").ofType(-7).withSize(1).notNull());
        addMetadata(verifiedUserId, ColumnMetadata.named("verified_user_id").ofType(-5).withSize(19).notNull());
    }

}

