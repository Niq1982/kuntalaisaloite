package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorMessageInitiativeIdSeq is a Querydsl query type for QAuthorMessageInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorMessageInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QAuthorMessageInitiativeIdSeq> {

    private static final long serialVersionUID = 1687225932;

    public static final QAuthorMessageInitiativeIdSeq authorMessageInitiativeIdSeq = new QAuthorMessageInitiativeIdSeq("author_message_initiative_id_seq");

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

    public QAuthorMessageInitiativeIdSeq(String variable) {
        super(QAuthorMessageInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "author_message_initiative_id_seq");
    }

    public QAuthorMessageInitiativeIdSeq(Path<? extends QAuthorMessageInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_message_initiative_id_seq");
    }

    public QAuthorMessageInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QAuthorMessageInitiativeIdSeq.class, metadata, "municipalityinitiative", "author_message_initiative_id_seq");
    }

}

