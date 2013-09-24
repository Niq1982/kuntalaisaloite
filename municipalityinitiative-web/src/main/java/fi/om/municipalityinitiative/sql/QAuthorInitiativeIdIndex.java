package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInitiativeIdIndex is a Querydsl query type for QAuthorInitiativeIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInitiativeIdIndex extends com.mysema.query.sql.RelationalPathBase<QAuthorInitiativeIdIndex> {

    private static final long serialVersionUID = -1218524880;

    public static final QAuthorInitiativeIdIndex authorInitiativeIdIndex = new QAuthorInitiativeIdIndex("author_initiative_id_index");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QAuthorInitiativeIdIndex(String variable) {
        super(QAuthorInitiativeIdIndex.class, forVariable(variable), "municipalityinitiative", "author_initiative_id_index");
    }

    public QAuthorInitiativeIdIndex(Path<? extends QAuthorInitiativeIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_initiative_id_index");
    }

    public QAuthorInitiativeIdIndex(PathMetadata<?> metadata) {
        super(QAuthorInitiativeIdIndex.class, metadata, "municipalityinitiative", "author_initiative_id_index");
    }

}

