package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserPk is a Querydsl query type for QVerifiedUserPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserPk extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserPk> {

    private static final long serialVersionUID = 1854219102;

    public static final QVerifiedUserPk verifiedUserPk = new QVerifiedUserPk("verified_user_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QVerifiedUserPk(String variable) {
        super(QVerifiedUserPk.class, forVariable(variable), "municipalityinitiative", "verified_user_pk");
    }

    public QVerifiedUserPk(Path<? extends QVerifiedUserPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_pk");
    }

    public QVerifiedUserPk(PathMetadata<?> metadata) {
        super(QVerifiedUserPk.class, metadata, "municipalityinitiative", "verified_user_pk");
    }

}

