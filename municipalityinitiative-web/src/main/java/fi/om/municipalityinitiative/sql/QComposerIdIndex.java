package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposerIdIndex is a Querydsl query type for QComposerIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposerIdIndex extends com.mysema.query.sql.RelationalPathBase<QComposerIdIndex> {

    private static final long serialVersionUID = -166313497;

    public static final QComposerIdIndex composerIdIndex = new QComposerIdIndex("composer_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QComposerIdIndex(String variable) {
        super(QComposerIdIndex.class, forVariable(variable), "municipalityinitiative", "composer_id_index");
    }

    public QComposerIdIndex(Path<? extends QComposerIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer_id_index");
    }

    public QComposerIdIndex(PathMetadata<?> metadata) {
        super(QComposerIdIndex.class, metadata, "municipalityinitiative", "composer_id_index");
    }

}

