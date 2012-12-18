package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeLinkIdSeq is a Querydsl query type for QInitiativeLinkIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeLinkIdSeq extends com.mysema.query.sql.RelationalPathBase<QInitiativeLinkIdSeq> {

    private static final long serialVersionUID = -1824826514;

    public static final QInitiativeLinkIdSeq initiativeLinkIdSeq = new QInitiativeLinkIdSeq("initiative_link_id_seq");

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

    public QInitiativeLinkIdSeq(String variable) {
        super(QInitiativeLinkIdSeq.class, forVariable(variable), "municipalityinitiative", "initiative_link_id_seq");
    }

    public QInitiativeLinkIdSeq(Path<? extends QInitiativeLinkIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "initiative_link_id_seq");
    }

    public QInitiativeLinkIdSeq(PathMetadata<?> metadata) {
        super(QInitiativeLinkIdSeq.class, metadata, "municipalityinitiative", "initiative_link_id_seq");
    }

}

