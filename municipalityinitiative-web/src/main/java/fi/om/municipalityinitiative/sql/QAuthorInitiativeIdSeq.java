package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAuthorInitiativeIdSeq is a Querydsl query type for QAuthorInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QAuthorInitiativeIdSeq> {

    private static final long serialVersionUID = -68297667;

    public static final QAuthorInitiativeIdSeq authorInitiativeIdSeq = new QAuthorInitiativeIdSeq("author_initiative_id_seq");

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

    public QAuthorInitiativeIdSeq(String variable) {
        super(QAuthorInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "author_initiative_id_seq");
    }

    public QAuthorInitiativeIdSeq(Path<? extends QAuthorInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_initiative_id_seq");
    }

    public QAuthorInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QAuthorInitiativeIdSeq.class, metadata, "municipalityinitiative", "author_initiative_id_seq");
    }

}

