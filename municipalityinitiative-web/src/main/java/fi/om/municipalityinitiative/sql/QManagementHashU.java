package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QManagementHashU is a Querydsl query type for QManagementHashU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QManagementHashU extends com.mysema.query.sql.RelationalPathBase<QManagementHashU> {

    private static final long serialVersionUID = -1297320652;

    public static final QManagementHashU managementHashU = new QManagementHashU("management_hash_u");

    public final StringPath managementHash = createString("management_hash");

    public QManagementHashU(String variable) {
        super(QManagementHashU.class, forVariable(variable), "municipalityinitiative", "management_hash_u");
    }

    public QManagementHashU(Path<? extends QManagementHashU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "management_hash_u");
    }

    public QManagementHashU(PathMetadata<?> metadata) {
        super(QManagementHashU.class, metadata, "municipalityinitiative", "management_hash_u");
    }

}

