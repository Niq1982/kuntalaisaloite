package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityInitiative is a Querydsl query type for QMunicipalityInitiative
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiative extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiative> {

    private static final long serialVersionUID = -2033192200;

    public static final QMunicipalityInitiative municipalityInitiative = new QMunicipalityInitiative("municipality_initiative");

    public final NumberPath<Long> authorId = createNumber("author_id", Long.class);

    public final StringPath comment = createString("comment");

    public final StringPath contactAddress = createString("contact_address");

    public final StringPath contactEmail = createString("contact_email");

    public final StringPath contactName = createString("contact_name");

    public final StringPath contactPhone = createString("contact_phone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath managementHash = createString("management_hash");

    public final StringPath moderatorComment = createString("moderator_comment");

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final NumberPath<Long> municipalityId = createNumber("municipality_id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> participantCount = createNumber("participant_count", Integer.class);

    public final StringPath proposal = createString("proposal");

    public final DateTimePath<org.joda.time.DateTime> sent = createDateTime("sent", org.joda.time.DateTime.class);

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeState> state = createEnum("state", fi.om.municipalityinitiative.util.InitiativeState.class);

    public final DateTimePath<org.joda.time.DateTime> stateTimestamp = createDateTime("state_timestamp", org.joda.time.DateTime.class);

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeType> type = createEnum("type", fi.om.municipalityinitiative.util.InitiativeType.class);

    public final com.mysema.query.sql.PrimaryKey<QMunicipalityInitiative> municipalityInitiativePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QAuthor> initiativeAuthorFk = createForeignKey(authorId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipality> municipalityInitiativeMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> _participantMunicipalityInitiativeIdFk = createInvForeignKey(id, "municipality_initiative_id");

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

