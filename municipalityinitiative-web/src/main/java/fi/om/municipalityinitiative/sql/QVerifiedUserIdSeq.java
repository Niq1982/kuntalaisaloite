package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserIdSeq is a Querydsl query type for QVerifiedUserIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserIdSeq> {

    private static final long serialVersionUID = 1460283649;

    public static final QVerifiedUserIdSeq verifiedUserIdSeq = new QVerifiedUserIdSeq("verified_user_id_seq");

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

    public QVerifiedUserIdSeq(String variable) {
        super(QVerifiedUserIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_user_id_seq");
    }

    public QVerifiedUserIdSeq(Path<? extends QVerifiedUserIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_id_seq");
    }

    public QVerifiedUserIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedUserIdSeq.class, metadata, "municipalityinitiative", "verified_user_id_seq");
    }

}

