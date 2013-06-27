package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedAuthorPk is a Querydsl query type for QVerifiedAuthorPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthorPk extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthorPk> {

    private static final long serialVersionUID = 1192803102;

    public static final QVerifiedAuthorPk verifiedAuthorPk = new QVerifiedAuthorPk("verified_author_pk");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final NumberPath<Long> verifiedUserId = createNumber("verified_user_id", Long.class);

    public QVerifiedAuthorPk(String variable) {
        super(QVerifiedAuthorPk.class, forVariable(variable), "municipalityinitiative", "verified_author_pk");
    }

    public QVerifiedAuthorPk(Path<? extends QVerifiedAuthorPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author_pk");
    }

    public QVerifiedAuthorPk(PathMetadata<?> metadata) {
        super(QVerifiedAuthorPk.class, metadata, "municipalityinitiative", "verified_author_pk");
    }

}

