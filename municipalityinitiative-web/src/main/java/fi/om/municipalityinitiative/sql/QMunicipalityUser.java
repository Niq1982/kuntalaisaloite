package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QMunicipalityUser is a Querydsl query type for QMunicipalityUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityUser extends com.mysema.query.sql.RelationalPathBase<QMunicipalityUser> {

    private static final long serialVersionUID = 325620151;

    public static final QMunicipalityUser municipalityUser = new QMunicipalityUser("municipality_user");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final StringPath loginHash = createString("loginHash");

    public final DateTimePath<org.joda.time.DateTime> loginHashCreateTime = createDateTime("loginHashCreateTime", org.joda.time.DateTime.class);

    public final StringPath managementHash = createString("managementHash");

    public final com.mysema.query.sql.PrimaryKey<QMunicipalityUser> municipalityUserPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> municipalityUserInitiativeId = createForeignKey(initiativeId, "id");

    public QMunicipalityUser(String variable) {
        super(QMunicipalityUser.class,  forVariable(variable), "municipalityinitiative", "municipality_user");
        addMetadata();
    }

    public QMunicipalityUser(Path<? extends QMunicipalityUser> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_user");
        addMetadata();
    }

    public QMunicipalityUser(PathMetadata<?> metadata) {
        super(QMunicipalityUser.class,  metadata, "municipalityinitiative", "municipality_user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(loginHash, ColumnMetadata.named("login_hash").ofType(12).withSize(40));
        addMetadata(loginHashCreateTime, ColumnMetadata.named("login_hash_create_time").ofType(93).withSize(29).withDigits(6));
        addMetadata(managementHash, ColumnMetadata.named("management_hash").ofType(12).withSize(40).notNull());
    }

}

