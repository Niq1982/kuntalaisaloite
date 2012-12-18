package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposerIdSeq is a Querydsl query type for QComposerIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposerIdSeq extends com.mysema.query.sql.RelationalPathBase<QComposerIdSeq> {

    private static final long serialVersionUID = 13244084;

    public static final QComposerIdSeq composerIdSeq = new QComposerIdSeq("composer_id_seq");

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

    public QComposerIdSeq(String variable) {
        super(QComposerIdSeq.class, forVariable(variable), "municipalityinitiative", "composer_id_seq");
    }

    public QComposerIdSeq(Path<? extends QComposerIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer_id_seq");
    }

    public QComposerIdSeq(PathMetadata<?> metadata) {
        super(QComposerIdSeq.class, metadata, "municipalityinitiative", "composer_id_seq");
    }

}

