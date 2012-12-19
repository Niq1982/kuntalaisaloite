package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


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
        super(QSchemaVersion.class, forVariable(variable), "municipalityinitiative", "schema_version");
    }

    public QSchemaVersion(Path<? extends QSchemaVersion> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "schema_version");
    }

    public QSchemaVersion(PathMetadata<?> metadata) {
        super(QSchemaVersion.class, metadata, "municipalityinitiative", "schema_version");
    }

}

