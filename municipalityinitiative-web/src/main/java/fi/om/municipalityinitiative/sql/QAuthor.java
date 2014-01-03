package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAuthor is a Querydsl query type for QAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthor extends com.mysema.query.sql.RelationalPathBase<QAuthor> {

    private static final long serialVersionUID = 313430235;

    public static final QAuthor author = new QAuthor("author");

    public final StringPath address = createString("address");

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final StringPath managementHash = createString("managementHash");

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final StringPath phone = createString("phone");

    public final com.mysema.query.sql.PrimaryKey<QAuthor> authorPk = createPrimaryKey(participantId);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> authorInitiativeIdFk = createForeignKey(initiativeId, "id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> authorParticipantFk = createForeignKey(participantId, "id");

    public QAuthor(String variable) {
        super(QAuthor.class,  forVariable(variable), "municipalityinitiative", "author");
        addMetadata();
    }

    public QAuthor(Path<? extends QAuthor> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author");
        addMetadata();
    }

    public QAuthor(PathMetadata<?> metadata) {
        super(QAuthor.class,  metadata, "municipalityinitiative", "author");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").ofType(12).withSize(256));
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(managementHash, ColumnMetadata.named("management_hash").ofType(12).withSize(40));
        addMetadata(participantId, ColumnMetadata.named("participant_id").ofType(-5).withSize(19).notNull());
        addMetadata(phone, ColumnMetadata.named("phone").ofType(12).withSize(30));
    }

}

