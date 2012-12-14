package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativePk is a Querydsl query type for QInitiativePk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativePk extends com.mysema.query.sql.RelationalPathBase<QInitiativePk> {

    private static final long serialVersionUID = -584379013;

    public static final QInitiativePk initiativePk = new QInitiativePk("initiative_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInitiativePk(String variable) {
        super(QInitiativePk.class, forVariable(variable), "initiative", "initiative_pk");
    }

    public QInitiativePk(Path<? extends QInitiativePk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "initiative_pk");
    }

    public QInitiativePk(PathMetadata<?> metadata) {
        super(QInitiativePk.class, metadata, "initiative", "initiative_pk");
    }

}

