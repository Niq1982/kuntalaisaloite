package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorIdIndex is a Querydsl query type for QAuthorIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorIdIndex extends com.mysema.query.sql.RelationalPathBase<QAuthorIdIndex> {

    private static final long serialVersionUID = 2029293340;

    public static final QAuthorIdIndex authorIdIndex = new QAuthorIdIndex("author_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAuthorIdIndex(String variable) {
        super(QAuthorIdIndex.class, forVariable(variable), "municipalityinitiative", "author_id_index");
    }

    public QAuthorIdIndex(Path<? extends QAuthorIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_id_index");
    }

    public QAuthorIdIndex(PathMetadata<?> metadata) {
        super(QAuthorIdIndex.class, metadata, "municipalityinitiative", "author_id_index");
    }

}

