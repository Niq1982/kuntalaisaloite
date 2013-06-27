package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedAuthor is a Querydsl query type for QVerifiedAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthor extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthor> {

    private static final long serialVersionUID = 1283921347;

    public static final QVerifiedAuthor verifiedAuthor = new QVerifiedAuthor("verified_author");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final NumberPath<Long> verifiedUserId = createNumber("verified_user_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QVerifiedAuthor> verifiedAuthorPk = createPrimaryKey(initiativeId, verifiedUserId);

    public QVerifiedAuthor(String variable) {
        super(QVerifiedAuthor.class, forVariable(variable), "municipalityinitiative", "verified_author");
    }

    public QVerifiedAuthor(Path<? extends QVerifiedAuthor> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author");
    }

    public QVerifiedAuthor(PathMetadata<?> metadata) {
        super(QVerifiedAuthor.class, metadata, "municipalityinitiative", "verified_author");
    }

}

