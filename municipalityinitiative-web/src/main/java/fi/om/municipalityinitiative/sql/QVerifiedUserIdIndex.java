package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserIdIndex is a Querydsl query type for QVerifiedUserIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserIdIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserIdIndex> {

    private static final long serialVersionUID = -1130695436;

    public static final QVerifiedUserIdIndex verifiedUserIdIndex = new QVerifiedUserIdIndex("verified_user_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QVerifiedUserIdIndex(String variable) {
        super(QVerifiedUserIdIndex.class, forVariable(variable), "municipalityinitiative", "verified_user_id_index");
    }

    public QVerifiedUserIdIndex(Path<? extends QVerifiedUserIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_id_index");
    }

    public QVerifiedUserIdIndex(PathMetadata<?> metadata) {
        super(QVerifiedUserIdIndex.class, metadata, "municipalityinitiative", "verified_user_id_index");
    }

}

