package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedAuthorVerifiedUserIdSeq is a Querydsl query type for QVerifiedAuthorVerifiedUserIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthorVerifiedUserIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthorVerifiedUserIdSeq> {

    private static final long serialVersionUID = -910404914;

    public static final QVerifiedAuthorVerifiedUserIdSeq verifiedAuthorVerifiedUserIdSeq = new QVerifiedAuthorVerifiedUserIdSeq("verified_author_verified_user_id_seq");

    public final NumberPath<Long> cacheValue = createNumber("cache_value", Long.class);

    public final NumberPath<Long> incrementBy = createNumber("increment_by", Long.class);

    public final BooleanPath isCalled = createBoolean("is_called");

    public final BooleanPath isCycled = createBoolean("is_cycled");

    public final NumberPath<Long> lastValue = createNumber("last_value", Long.class);

    public final NumberPath<Long> logCnt = createNumber("log_cnt", Long.class);

    public final NumberPath<Long> maxValue = createNumber("max_value", Long.class);

    public final NumberPath<Long> minValue = createNumber("min_value", Long.class);

    public final StringPath sequenceName = createString("sequence_name");

    public final NumberPath<Long> startValue = createNumber("start_value", Long.class);

    public QVerifiedAuthorVerifiedUserIdSeq(String variable) {
        super(QVerifiedAuthorVerifiedUserIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_author_verified_user_id_seq");
    }

    public QVerifiedAuthorVerifiedUserIdSeq(Path<? extends QVerifiedAuthorVerifiedUserIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author_verified_user_id_seq");
    }

    public QVerifiedAuthorVerifiedUserIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedAuthorVerifiedUserIdSeq.class, metadata, "municipalityinitiative", "verified_author_verified_user_id_seq");
    }

}

