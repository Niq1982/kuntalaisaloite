package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAttachmentInitiativeIdSeq is a Querydsl query type for QAttachmentInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachmentInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QAttachmentInitiativeIdSeq> {

    private static final long serialVersionUID = -1083725403;

    public static final QAttachmentInitiativeIdSeq attachmentInitiativeIdSeq = new QAttachmentInitiativeIdSeq("attachment_initiative_id_seq");

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

    public QAttachmentInitiativeIdSeq(String variable) {
        super(QAttachmentInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "attachment_initiative_id_seq");
    }

    public QAttachmentInitiativeIdSeq(Path<? extends QAttachmentInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment_initiative_id_seq");
    }

    public QAttachmentInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QAttachmentInitiativeIdSeq.class, metadata, "municipalityinitiative", "attachment_initiative_id_seq");
    }

}

