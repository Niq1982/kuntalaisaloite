package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailInitiativeIdSeq is a Querydsl query type for QEmailInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmailInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QEmailInitiativeIdSeq> {

    private static final long serialVersionUID = -754218324;

    public static final QEmailInitiativeIdSeq emailInitiativeIdSeq = new QEmailInitiativeIdSeq("email_initiative_id_seq");

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

    public QEmailInitiativeIdSeq(String variable) {
        super(QEmailInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "email_initiative_id_seq");
    }

    public QEmailInitiativeIdSeq(Path<? extends QEmailInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email_initiative_id_seq");
    }

    public QEmailInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QEmailInitiativeIdSeq.class, metadata, "municipalityinitiative", "email_initiative_id_seq");
    }

}

