package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSchemaVersionScriptPk is a Querydsl query type for QSchemaVersionScriptPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSchemaVersionScriptPk extends com.mysema.query.sql.RelationalPathBase<QSchemaVersionScriptPk> {

    private static final long serialVersionUID = 1110153065;

    public static final QSchemaVersionScriptPk schemaVersionScriptPk = new QSchemaVersionScriptPk("schema_version_script_pk");

    public final StringPath script = createString("script");

    public QSchemaVersionScriptPk(String variable) {
        super(QSchemaVersionScriptPk.class, forVariable(variable), "initiative", "schema_version_script_pk");
    }

    public QSchemaVersionScriptPk(Path<? extends QSchemaVersionScriptPk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "schema_version_script_pk");
    }

    public QSchemaVersionScriptPk(PathMetadata<?> metadata) {
        super(QSchemaVersionScriptPk.class, metadata, "initiative", "schema_version_script_pk");
    }

}

