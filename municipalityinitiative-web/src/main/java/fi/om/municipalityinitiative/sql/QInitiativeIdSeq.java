package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeIdSeq is a Querydsl query type for QInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QInitiativeIdSeq> {

    private static final long serialVersionUID = 1087484232;

    public static final QInitiativeIdSeq initiativeIdSeq = new QInitiativeIdSeq("initiative_id_seq");

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

    public QInitiativeIdSeq(String variable) {
        super(QInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "initiative_id_seq");
    }

    public QInitiativeIdSeq(Path<? extends QInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "initiative_id_seq");
    }

    public QInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QInitiativeIdSeq.class, metadata, "municipalityinitiative", "initiative_id_seq");
    }

}

