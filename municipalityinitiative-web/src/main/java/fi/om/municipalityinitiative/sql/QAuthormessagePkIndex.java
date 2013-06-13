package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthormessagePkIndex is a Querydsl query type for QAuthormessagePkIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthormessagePkIndex extends com.mysema.query.sql.RelationalPathBase<QAuthormessagePkIndex> {

    private static final long serialVersionUID = 658831147;

    public static final QAuthormessagePkIndex authormessagePkIndex = new QAuthormessagePkIndex("authormessage_pk_index");

    public final StringPath confirmationCode = createString("confirmation_code");

    public QAuthormessagePkIndex(String variable) {
        super(QAuthormessagePkIndex.class, forVariable(variable), "municipalityinitiative", "authormessage_pk_index");
    }

    public QAuthormessagePkIndex(Path<? extends QAuthormessagePkIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "authormessage_pk_index");
    }

    public QAuthormessagePkIndex(PathMetadata<?> metadata) {
        super(QAuthormessagePkIndex.class, metadata, "municipalityinitiative", "authormessage_pk_index");
    }

}

