package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMunicipality is a Querydsl query type for QMunicipality
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipality extends com.mysema.query.sql.RelationalPathBase<QMunicipality> {

    private static final long serialVersionUID = -614926388;

    public static final QMunicipality municipality = new QMunicipality("municipality");

    public final BooleanPath active = createBoolean("active");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nameSv = createString("nameSv");

    public final com.mysema.query.sql.PrimaryKey<QMunicipality> municipalityPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QVerifiedUser> _verifiedUserMunicipalityFk = createInvForeignKey(id, "municipality_id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> _municipalityInitiativeMunicipalityFk = createInvForeignKey(id, "municipality_id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> _participantMunicipalityFk = createInvForeignKey(id, "municipality_id");

    public QMunicipality(String variable) {
        super(QMunicipality.class,  forVariable(variable), "municipalityinitiative", "municipality");
        addMetadata();
    }

    public QMunicipality(Path<? extends QMunicipality> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality");
        addMetadata();
    }

    public QMunicipality(PathMetadata<?> metadata) {
        super(QMunicipality.class,  metadata, "municipalityinitiative", "municipality");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(active, ColumnMetadata.named("active").ofType(-7).withSize(1));
        addMetadata(email, ColumnMetadata.named("email").ofType(12).withSize(100));
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(30).notNull());
        addMetadata(nameSv, ColumnMetadata.named("name_sv").ofType(12).withSize(30).notNull());
    }

}

