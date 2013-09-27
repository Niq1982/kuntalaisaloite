package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailIdSeq is a Querydsl query type for QEmailIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmailIdSeq extends com.mysema.query.sql.RelationalPathBase<QEmailIdSeq> {

    private static final long serialVersionUID = -1409988008;

    public static final QEmailIdSeq emailIdSeq = new QEmailIdSeq("email_id_seq");

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

    public QEmailIdSeq(String variable) {
        super(QEmailIdSeq.class, forVariable(variable), "municipalityinitiative", "email_id_seq");
    }

    public QEmailIdSeq(Path<? extends QEmailIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email_id_seq");
    }

    public QEmailIdSeq(PathMetadata<?> metadata) {
        super(QEmailIdSeq.class, metadata, "municipalityinitiative", "email_id_seq");
    }

}

