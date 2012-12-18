package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVoteBatchIdSeq is a Querydsl query type for QSupportVoteBatchIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVoteBatchIdSeq extends com.mysema.query.sql.RelationalPathBase<QSupportVoteBatchIdSeq> {

    private static final long serialVersionUID = -203417325;

    public static final QSupportVoteBatchIdSeq supportVoteBatchIdSeq = new QSupportVoteBatchIdSeq("support_vote_batch_id_seq");

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

    public QSupportVoteBatchIdSeq(String variable) {
        super(QSupportVoteBatchIdSeq.class, forVariable(variable), "municipalityinitiative", "support_vote_batch_id_seq");
    }

    public QSupportVoteBatchIdSeq(Path<? extends QSupportVoteBatchIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "support_vote_batch_id_seq");
    }

    public QSupportVoteBatchIdSeq(PathMetadata<?> metadata) {
        super(QSupportVoteBatchIdSeq.class, metadata, "municipalityinitiative", "support_vote_batch_id_seq");
    }

}

