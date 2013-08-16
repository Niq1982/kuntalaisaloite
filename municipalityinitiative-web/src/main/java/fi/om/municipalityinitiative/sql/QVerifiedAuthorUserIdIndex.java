package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QVerifiedAuthorUserIdIndex is a Querydsl query type for QVerifiedAuthorUserIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthorUserIdIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthorUserIdIndex> {

    private static final long serialVersionUID = -1968082839;

    public static final QVerifiedAuthorUserIdIndex verifiedAuthorUserIdIndex = new QVerifiedAuthorUserIdIndex("verified_author_user_id_index");

    public final NumberPath<Long> verifiedUserId = createNumber("verified_user_id", Long.class);

    public QVerifiedAuthorUserIdIndex(String variable) {
        super(QVerifiedAuthorUserIdIndex.class, forVariable(variable), "municipalityinitiative", "verified_author_user_id_index");
    }

    public QVerifiedAuthorUserIdIndex(Path<? extends QVerifiedAuthorUserIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author_user_id_index");
    }

    public QVerifiedAuthorUserIdIndex(PathMetadata<?> metadata) {
        super(QVerifiedAuthorUserIdIndex.class, metadata, "municipalityinitiative", "verified_author_user_id_index");
    }

}

