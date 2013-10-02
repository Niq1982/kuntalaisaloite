package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAttachmentIdSeq is a Querydsl query type for QAttachmentIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachmentIdSeq extends com.mysema.query.sql.RelationalPathBase<QAttachmentIdSeq> {

    private static final long serialVersionUID = 1615076497;

    public static final QAttachmentIdSeq attachmentIdSeq = new QAttachmentIdSeq("attachment_id_seq");

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

    public QAttachmentIdSeq(String variable) {
        super(QAttachmentIdSeq.class, forVariable(variable), "municipalityinitiative", "attachment_id_seq");
    }

    public QAttachmentIdSeq(Path<? extends QAttachmentIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment_id_seq");
    }

    public QAttachmentIdSeq(PathMetadata<?> metadata) {
        super(QAttachmentIdSeq.class, metadata, "municipalityinitiative", "attachment_id_seq");
    }

}

