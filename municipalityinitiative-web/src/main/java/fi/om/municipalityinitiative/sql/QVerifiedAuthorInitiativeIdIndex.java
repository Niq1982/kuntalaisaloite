package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedAuthorInitiativeIdIndex is a Querydsl query type for QVerifiedAuthorInitiativeIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthorInitiativeIdIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthorInitiativeIdIndex> {

    private static final long serialVersionUID = -984370872;

    public static final QVerifiedAuthorInitiativeIdIndex verifiedAuthorInitiativeIdIndex = new QVerifiedAuthorInitiativeIdIndex("verified_author_initiative_id_index");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QVerifiedAuthorInitiativeIdIndex(String variable) {
        super(QVerifiedAuthorInitiativeIdIndex.class, forVariable(variable), "municipalityinitiative", "verified_author_initiative_id_index");
    }

    public QVerifiedAuthorInitiativeIdIndex(Path<? extends QVerifiedAuthorInitiativeIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author_initiative_id_index");
    }

    public QVerifiedAuthorInitiativeIdIndex(PathMetadata<?> metadata) {
        super(QVerifiedAuthorInitiativeIdIndex.class, metadata, "municipalityinitiative", "verified_author_initiative_id_index");
    }

}

