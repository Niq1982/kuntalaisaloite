package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserHashIndex is a Querydsl query type for QVerifiedUserHashIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserHashIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserHashIndex> {

    private static final long serialVersionUID = -673606239;

    public static final QVerifiedUserHashIndex verifiedUserHashIndex = new QVerifiedUserHashIndex("verified_user_hash_index");

    public final StringPath hash = createString("hash");

    public QVerifiedUserHashIndex(String variable) {
        super(QVerifiedUserHashIndex.class, forVariable(variable), "municipalityinitiative", "verified_user_hash_index");
    }

    public QVerifiedUserHashIndex(Path<? extends QVerifiedUserHashIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_hash_index");
    }

    public QVerifiedUserHashIndex(PathMetadata<?> metadata) {
        super(QVerifiedUserHashIndex.class, metadata, "municipalityinitiative", "verified_user_hash_index");
    }

}

