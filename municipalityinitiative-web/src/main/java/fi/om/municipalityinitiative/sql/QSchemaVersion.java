package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QSchemaVersion is a Querydsl query type for QSchemaVersion
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSchemaVersion extends com.mysema.query.sql.RelationalPathBase<QSchemaVersion> {

    private static final long serialVersionUID = -2126131545;

    public static final QSchemaVersion schemaVersion = new QSchemaVersion("schema_version");

    public final DateTimePath<org.joda.time.DateTime> executed = createDateTime("executed", org.joda.time.DateTime.class);

    public final StringPath script = createString("script");

    public final com.mysema.query.sql.PrimaryKey<QSchemaVersion> schemaVersionScriptPk = createPrimaryKey(script);

    public QSchemaVersion(String variable) {
        super(QSchemaVersion.class,  forVariable(variable), "municipalityinitiative", "schema_version");
        addMetadata();
    }

    public QSchemaVersion(Path<? extends QSchemaVersion> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "schema_version");
        addMetadata();
    }

    public QSchemaVersion(PathMetadata<?> metadata) {
        super(QSchemaVersion.class,  metadata, "municipalityinitiative", "schema_version");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(executed, ColumnMetadata.named("executed").ofType(93).withSize(29).withDigits(6).notNull());
        addMetadata(script, ColumnMetadata.named("script").ofType(12).withSize(64).notNull());
    }

}

