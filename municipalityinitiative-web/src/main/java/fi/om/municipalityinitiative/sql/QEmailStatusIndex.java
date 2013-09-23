package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailStatusIndex is a Querydsl query type for QEmailStatusIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmailStatusIndex extends com.mysema.query.sql.RelationalPathBase<QEmailStatusIndex> {

    private static final long serialVersionUID = -1073848812;

    public static final QEmailStatusIndex emailStatusIndex = new QEmailStatusIndex("email_status_index");

    public final BooleanPath status = createBoolean("status");

    public QEmailStatusIndex(String variable) {
        super(QEmailStatusIndex.class, forVariable(variable), "municipalityinitiative", "email_status_index");
    }

    public QEmailStatusIndex(Path<? extends QEmailStatusIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email_status_index");
    }

    public QEmailStatusIndex(PathMetadata<?> metadata) {
        super(QEmailStatusIndex.class, metadata, "municipalityinitiative", "email_status_index");
    }

}

