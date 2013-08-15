package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMunicipalityInitiative is a Querydsl query type for QMunicipalityInitiative
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiative extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiative> {

    private static final long serialVersionUID = -2033192200;

    public static final QMunicipalityInitiative municipalityInitiative = new QMunicipalityInitiative("municipality_initiative");

    public final NumberPath<Integer> externalparticipantcount = createNumber("externalparticipantcount", Integer.class);

    public final StringPath extraInfo = createString("extra_info");

    public final EnumPath<fi.om.municipalityinitiative.util.FixState> fixState = createEnum("fix_state", fi.om.municipalityinitiative.util.FixState.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath moderatorComment = createString("moderator_comment");

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final NumberPath<Long> municipalityId = createNumber("municipality_id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> participantCount = createNumber("participant_count", Integer.class);

    public final NumberPath<Integer> participantCountPublic = createNumber("participant_count_public", Integer.class);

    public final StringPath proposal = createString("proposal");

    public final DateTimePath<org.joda.time.DateTime> sent = createDateTime("sent", org.joda.time.DateTime.class);

    public final StringPath sentComment = createString("sent_comment");

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeState> state = createEnum("state", fi.om.municipalityinitiative.util.InitiativeState.class);

    public final DateTimePath<org.joda.time.DateTime> stateTimestamp = createDateTime("state_timestamp", org.joda.time.DateTime.class);

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeType> type = createEnum("type", fi.om.municipalityinitiative.util.InitiativeType.class);

    public final com.mysema.query.sql.PrimaryKey<QMunicipalityInitiative> municipalityInitiativePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipality> municipalityInitiativeMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QAuthorMessage> _authormessageInitiativeidFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QAuthorInvitation> _authorInvitationInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> _participantMunicipalityInitiativeIdFk = createInvForeignKey(id, "municipality_initiative_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedAuthor> _verifiedAuthorInitiativeFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedParticipant> _verifiedParticipantInitiativeFk = createInvForeignKey(id, "initiative_id");

    public QMunicipalityInitiative(String variable) {
        super(QMunicipalityInitiative.class, forVariable(variable), "municipalityinitiative", "municipality_initiative");
    }

    public QMunicipalityInitiative(Path<? extends QMunicipalityInitiative> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative");
    }

    public QMunicipalityInitiative(PathMetadata<?> metadata) {
        super(QMunicipalityInitiative.class, metadata, "municipalityinitiative", "municipality_initiative");
    }

}

