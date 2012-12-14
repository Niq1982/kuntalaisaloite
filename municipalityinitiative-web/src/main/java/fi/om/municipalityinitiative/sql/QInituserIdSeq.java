package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInituserIdSeq is a Querydsl query type for QInituserIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInituserIdSeq extends com.mysema.query.sql.RelationalPathBase<QInituserIdSeq> {

    private static final long serialVersionUID = -56258731;

    public static final QInituserIdSeq inituserIdSeq = new QInituserIdSeq("inituser_id_seq");

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

    public QInituserIdSeq(String variable) {
        super(QInituserIdSeq.class, forVariable(variable), "initiative", "inituser_id_seq");
    }

    public QInituserIdSeq(Path<? extends QInituserIdSeq> path) {
        super(path.getType(), path.getMetadata(), "initiative", "inituser_id_seq");
    }

    public QInituserIdSeq(PathMetadata<?> metadata) {
        super(QInituserIdSeq.class, metadata, "initiative", "inituser_id_seq");
    }

}

