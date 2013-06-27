package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserHashU is a Querydsl query type for QVerifiedUserHashU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserHashU extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserHashU> {

    private static final long serialVersionUID = 1459301572;

    public static final QVerifiedUserHashU verifiedUserHashU = new QVerifiedUserHashU("verified_user_hash_u");

    public final StringPath hash = createString("hash");

    public QVerifiedUserHashU(String variable) {
        super(QVerifiedUserHashU.class, forVariable(variable), "municipalityinitiative", "verified_user_hash_u");
    }

    public QVerifiedUserHashU(Path<? extends QVerifiedUserHashU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_hash_u");
    }

    public QVerifiedUserHashU(PathMetadata<?> metadata) {
        super(QVerifiedUserHashU.class, metadata, "municipalityinitiative", "verified_user_hash_u");
    }

}

