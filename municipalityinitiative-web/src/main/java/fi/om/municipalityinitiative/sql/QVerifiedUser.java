package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUser is a Querydsl query type for QVerifiedUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUser extends com.mysema.query.sql.RelationalPathBase<QVerifiedUser> {

    private static final long serialVersionUID = -932147709;

    public static final QVerifiedUser verifiedUser = new QVerifiedUser("verified_user");

    public final StringPath address = createString("address");

    public final StringPath hash = createString("hash");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final com.mysema.query.sql.PrimaryKey<QVerifiedUser> verifiedUserPk = createPrimaryKey(id);

    public QVerifiedUser(String variable) {
        super(QVerifiedUser.class, forVariable(variable), "municipalityinitiative", "verified_user");
    }

    public QVerifiedUser(Path<? extends QVerifiedUser> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user");
    }

    public QVerifiedUser(PathMetadata<?> metadata) {
        super(QVerifiedUser.class, metadata, "municipalityinitiative", "verified_user");
    }

}

