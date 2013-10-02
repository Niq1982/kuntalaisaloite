package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailTriedIndex is a Querydsl query type for QEmailTriedIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmailTriedIndex extends com.mysema.query.sql.RelationalPathBase<QEmailTriedIndex> {

    private static final long serialVersionUID = -667128108;

    public static final QEmailTriedIndex emailTriedIndex = new QEmailTriedIndex("email_tried_index");

    public final BooleanPath tried = createBoolean("tried");

    public QEmailTriedIndex(String variable) {
        super(QEmailTriedIndex.class, forVariable(variable), "municipalityinitiative", "email_tried_index");
    }

    public QEmailTriedIndex(Path<? extends QEmailTriedIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email_tried_index");
    }

    public QEmailTriedIndex(PathMetadata<?> metadata) {
        super(QEmailTriedIndex.class, metadata, "municipalityinitiative", "email_tried_index");
    }

}

