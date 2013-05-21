package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInfoTextIdSeq is a Querydsl query type for QInfoTextIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfoTextIdSeq extends com.mysema.query.sql.RelationalPathBase<QInfoTextIdSeq> {

    private static final long serialVersionUID = 681062841;

    public static final QInfoTextIdSeq infoTextIdSeq = new QInfoTextIdSeq("info_text_id_seq");

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

    public QInfoTextIdSeq(String variable) {
        super(QInfoTextIdSeq.class, forVariable(variable), "municipalityinitiative", "info_text_id_seq");
    }

    public QInfoTextIdSeq(Path<? extends QInfoTextIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "info_text_id_seq");
    }

    public QInfoTextIdSeq(PathMetadata<?> metadata) {
        super(QInfoTextIdSeq.class, metadata, "municipalityinitiative", "info_text_id_seq");
    }

}

